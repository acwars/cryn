package com.onlinejudge.cryn.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.onlinejudge.cryn.common.*;
import com.onlinejudge.cryn.dao.*;
import com.onlinejudge.cryn.entity.*;
import com.onlinejudge.cryn.request.UserRequest;
import com.onlinejudge.cryn.response.ProblemResultRecentVO;
import com.onlinejudge.cryn.response.RankVO;
import com.onlinejudge.cryn.response.RestResponseVO;
import com.onlinejudge.cryn.response.UserDetailVO;
import com.onlinejudge.cryn.service.UserService;
import com.onlinejudge.cryn.utils.BeanUtil;
import com.onlinejudge.cryn.utils.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RegisterMapper registerMapper;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private ProblemMapper problemMapper;

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private ProblemResultMapper problemResultMapper;


    @Value("${spring.mail.username}")
    private String mailUsername;

    private final String DEFAULT_PASSWORD = "cryn";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public RestResponseVO addSignCount(Integer userId) {
        if (userId == null) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }
        int effect = userMapper.addSignCount(userId);
        return effect > 0 ? RestResponseVO.createBySuccess() : RestResponseVO.createByError();
    }

    @Override
    public RestResponseVO<UserDetailVO> getById(Integer userId) {
        if (userId == null) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }
        User user = userMapper.getById(userId);
        UserDetailVO userDetailVO = new UserDetailVO();
        BeanUtil.copyProperties(user, userDetailVO);
        List<Role> roleList = userDetailVO.getRoleList();
        if (roleList != null && roleList.size() > 0) {
            for (int i = 0; i < roleList.size(); i++) {
                if (i != roleList.size() - 1) {
                    userDetailVO.setRoleIds(roleList.get(i).getId() + ",");
                } else {
                    userDetailVO.setRoleIds(roleList.get(i).getId() + "");
                }
            }
        }
        userDetailVO.setCompetitionCount(registerMapper.countByUserId(userId));
        return RestResponseVO.createBySuccess(userDetailVO);
    }

    @Override
    @Transactional
    public RestResponseVO insert(UserRequest request) {
        if (request == null) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }
        RestResponseVO checkResponse = this.checkFieldByAdd(request);
        if (!checkResponse.isSuccess()) {
            return checkResponse;
        }
        User user = new User();
        BeanUtil.copyProperties(request, user);
        if (StringUtils.isBlank(user.getPassword())) {
            user.setPassword(DEFAULT_PASSWORD);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        int effect = userMapper.insertSelective(user);
        if (effect > 0) {
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId(request.getRoleId());
            effect = userRoleMapper.insertSelective(userRole);
        }
        return effect > 0 ? RestResponseVO.createBySuccessMessage(StringConst.ADD_SUCCESS)
                : RestResponseVO.createByErrorMessage(StringConst.ADD_FAIL);
    }


    @Override
    public RestResponseVO delById(Integer id) {
        if (id == null) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }
        int effect = userMapper.updateUserFlagById(id, UserFlagEnum.DELETED.getId());
        return effect > 0 ? RestResponseVO.createBySuccessMessage(StringConst.DEL_SUCCESS)
                : RestResponseVO.createByErrorMessage(StringConst.DEL_FAIL);
    }


    @Override
    public RestResponseVO lockById(Integer id) {
        if (id == null) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }
        int effect = userMapper.updateUserFlagById(id, UserFlagEnum.LOCK.getId());
        return effect > 0 ? RestResponseVO.createBySuccessMessage(StringConst.UPDATE_SUCCESS)
                : RestResponseVO.createByErrorMessage(StringConst.UPDATE_FAIL);
    }


    @Override
    public RestResponseVO activeById(Integer id) {
        if (id == null) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }
        int effect = userMapper.updateUserFlagById(id, UserFlagEnum.ACTIVE.getId());
        return effect > 0 ? RestResponseVO.createBySuccessMessage(StringConst.UPDATE_SUCCESS)
                : RestResponseVO.createByErrorMessage(StringConst.UPDATE_FAIL);
    }


    @Override
    @Transactional
    public RestResponseVO updateById(UserRequest request) {
        if (request == null || request.getId() == null) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }
        RestResponseVO checkResponse = this.checkFieldByUpdate(request);
        if (!checkResponse.isSuccess()) {
            return checkResponse;
        }
        User user = new User();
        BeanUtil.copyPropertiesIgnoreNull(request, user);
        if (StringUtils.isNoneBlank(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(null);
        }
        int effect = userMapper.updateByPrimaryKeySelective(user);
        if (effect > 0) {
            if (request.getRoleId() != null) {
                userRoleMapper.deleteByUserId(user.getId());
                UserRole userRole = new UserRole();
                userRole.setRoleId(request.getRoleId());
                userRole.setUserId(user.getId());
                effect = userRoleMapper.insertSelective(userRole);
            }
        }
        return effect > 0 ? RestResponseVO.createBySuccessMessage(StringConst.UPDATE_SUCCESS)
                : RestResponseVO.createByErrorMessage(StringConst.UPDATE_FAIL);
    }

    @Override
    public RestResponseVO sendRegisterEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }

        int effect = userMapper.countByEmail(email);
        if (effect > 0) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.EMAIL_REPEATED_ERROR);
        }
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            //send email
            String token = UUIDUtil.createByAPI36();
            redisTemplate.opsForValue().set(TokenConst.TokenKeyPrefix.REGISTER + email,
                    token, TokenConst.TokenExpireTime.REGISTER, TimeUnit.SECONDS);
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(mailUsername);
            helper.setTo(email);
            helper.setSubject("???Online Judge???????????????");
            String href = "http://localhost:8081/user/registerSubmitPage?email=" + email + "&token=" + token;
            String html = StringConst.getRegisterEmailContent(href);
            helper.setText(html, true);
            javaMailSender.send(message);
            return RestResponseVO.createBySuccess();
        } catch (Exception e) {
            logger.error("??????????????????,{}", e.getMessage());
            return RestResponseVO.createByErrorEnum(RestResponseEnum.EMAIL_SEND_ERROR);
        }
    }


    @Override
    public RestResponseVO sendForgetEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }
        int effect = userMapper.countByEmail(email);
        if (effect == 0) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.EMAIL_NOT_FOUND_ERROR);
        }
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            //send email
            String token = UUIDUtil.createByAPI36();
            redisTemplate.opsForValue().set(TokenConst.TokenKeyPrefix.REST_PASSWORD + email,
                    token, TokenConst.TokenExpireTime.REST_PASSWORD, TimeUnit.SECONDS);
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(mailUsername);
            helper.setTo(email);
            helper.setSubject("???Online Judge???????????????");
            String href = "http://localhost:8081/user/forgetSubmitPage?email=" + email + "&token=" + token;
            String html = StringConst.getForgetPasswordEmailContent(href);
            helper.setText(html, true);
            javaMailSender.send(message);
            return RestResponseVO.createBySuccess();
        } catch (Exception e) {
            logger.error("??????????????????,{}", e.getMessage());
            return RestResponseVO.createByErrorEnum(RestResponseEnum.EMAIL_SEND_ERROR);
        }
    }

    @Override
    public RestResponseVO forgetRestPassword(String token, String email, String password) {
        if (!StringUtils.isNoneBlank(token, email, password)) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }
        //???token???????????????????????????remove token
        String tokenFormRedis = (String) redisTemplate.opsForValue().get(TokenConst.TokenKeyPrefix.REST_PASSWORD + email);
        if (!token.equals(tokenFormRedis)) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.TOKEN_ERROR);
        }
        String encodePassword = passwordEncoder.encode(password);
        int effect = userMapper.resetPasswordByEmail(email, encodePassword);
        if (effect > 0) {
            redisTemplate.delete(TokenConst.TokenKeyPrefix.REST_PASSWORD + email);
            return RestResponseVO.createBySuccessMessage(StringConst.UPDATE_SUCCESS);
        } else {
            return RestResponseVO.createByErrorMessage(StringConst.UPDATE_FAIL);
        }
    }

    @Override
    @Transactional
    public RestResponseVO register(String token, UserRequest request) {
        if (request == null || StringUtils.isBlank(token)) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }
        //??????token??????????????????remove token
        String tokenFormRedis = (String) redisTemplate.opsForValue().get(TokenConst.TokenKeyPrefix.REGISTER + request.getEmail());
        if (!token.equals(tokenFormRedis)) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.TOKEN_ERROR);
        }
        int rows = userMapper.countByUsername(request.getUsername());
        if (rows > 0) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.USERNAME_REPEATED_ERROR);
        }
        request.setName(UUIDUtil.createByTime() + "");
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        User user = new User();
        BeanUtil.copyProperties(request, user);
        int effect = userMapper.insertSelective(user);
        if (effect > 0) {
            redisTemplate.delete(TokenConst.TokenKeyPrefix.REGISTER + user.getEmail());
            UserRole userRole = new UserRole();
            userRole.setRoleId(RoleEnum.USER.getId());
            userRole.setUserId(user.getId());
            effect = userRoleMapper.insertSelective(userRole);
        }
        return effect > 0 ? RestResponseVO.createBySuccessMessage(StringConst.ADD_SUCCESS)
                : RestResponseVO.createByErrorMessage(StringConst.ADD_FAIL);
    }

    @Override
    public RestResponseVO<PageInfo> listRankUser2Page(Integer pageNum, Integer pageSize, String keyword) {
        PageHelper.startPage(pageNum, pageSize, true);
        List<RankVO> userList = userMapper.listRankUser(keyword);
        PageInfo<RankVO> pageInfo = new PageInfo<>(userList);
        return RestResponseVO.createBySuccess(pageInfo);
    }


    /**
     * todo
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public RestResponseVO checkLoginByAdmin(String username, String password) {
        if (!StringUtils.isNoneBlank(username, password)) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }
        User user = userMapper.getByUserName(username);
        if (user == null) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.USERNAME_OR_PASSWORD_ERROR);
        }
        boolean matches = passwordEncoder.matches(password, user.getPassword());
        if (!matches) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.USERNAME_OR_PASSWORD_ERROR);
        }
        List<Role> roleList = user.getRoleList();
        boolean hasRole = false;
        if (!CollectionUtils.isEmpty(roleList)) {
            for (Role role : roleList) {
                if (RoleEnum.ADMIN.getName().equals(role.getName())) {
                    hasRole = true;
                    break;
                }
            }
        }
        if (!hasRole) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.FORBIDDEN);
        }
        return RestResponseVO.createBySuccess();
    }


    @Override
    public RestResponseVO<PageInfo> listUser2Page(Integer pageNum, Integer pageSize, String keyword) {
        PageHelper.startPage(pageNum, pageSize, true);
        List<User> userList = userMapper.listUser2Page(keyword);
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        return RestResponseVO.createBySuccess(pageInfo);
    }

    @Override
    public RestResponseVO<List<Problem>> listAllSolveProblemByUserId(Integer userId) {
        if (userId == null) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }
        return RestResponseVO.createBySuccess(problemMapper.listAllSolveProblemByUserId(userId));
    }

    @Override
    public RestResponseVO<List<Blog>> listRecentBlog(Integer userId, Integer recentSize) {
        if(userId == null || recentSize == null){
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }
        return RestResponseVO.createBySuccess(blogMapper.listRecentBlog(userId,recentSize));
    }

    @Override
    public RestResponseVO<List<ProblemResultRecentVO>> listRecentProblem(Integer userId, Integer recentSize) {
        if(userId == null || recentSize == null){
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }
       return  RestResponseVO.createBySuccess(problemResultMapper.listRecentByUserId(userId, recentSize));
    }



    @Override
    public RestResponseVO listProblemRecord(Integer userId, Integer flag) {
        if(userId == null || flag == null){
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }
        return RestResponseVO.createBySuccess(problemResultMapper.listProblemRecord(userId,flag));
    }

    @Override
    public RestResponseVO updateSecurity(Integer id, String email, String oldPassword, String password) {
        if(id == null || StringUtils.isBlank(email) || StringUtils.isBlank(oldPassword) || StringUtils.isBlank(password) ){
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }
        User userFromDB = userMapper.getById(id);
        if (userFromDB == null) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.UNAUTHORIZED);
        }
        if (!passwordEncoder.matches(oldPassword,userFromDB.getPassword())) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.ORIGINAL_PASSWORD_ERROR);
        }
        password = passwordEncoder.encode(password);
        User updateUser = new User();
        updateUser.setId(id);
        updateUser.setEmail(email);
        updateUser.setPassword(password);
        int effect = userMapper.updateByPrimaryKeySelective(updateUser);
        return effect > 0 ? RestResponseVO.createBySuccessMessage(StringConst.UPDATE_SUCCESS)
                : RestResponseVO.createByErrorMessage(StringConst.UPDATE_FAIL);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.getByUserNameOrEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User Not Found");
        }
        return user;
    }


    private RestResponseVO checkFieldByAdd(UserRequest request) {
        int effect = 0;
        if (StringUtils.isNoneBlank(request.getUsername())) {
            effect = userMapper.countByUsername(request.getUsername());
            if (effect > 0) {
                return RestResponseVO.createByErrorEnum(RestResponseEnum.USERNAME_REPEATED_ERROR);
            }
        }
        if (StringUtils.isNoneBlank(request.getName())) {
            effect = userMapper.countByName(request.getName());
            if (effect > 0) {
                return RestResponseVO.createByErrorEnum(RestResponseEnum.NAME_REPEATED_ERROR);
            }
        }
        if (StringUtils.isNoneBlank(request.getEmail())) {
            effect = userMapper.countByEmail(request.getEmail());
            if (effect > 0) {
                return RestResponseVO.createByErrorEnum(RestResponseEnum.EMAIL_REPEATED_ERROR);
            }
        }
        if (StringUtils.isNoneBlank(request.getPhone())) {
            effect = userMapper.countByPhone(request.getPhone());
            if (effect > 0) {
                return RestResponseVO.createByErrorEnum(RestResponseEnum.PHONE_REPEATED_ERROR);
            }
        }
        return RestResponseVO.createBySuccess();
    }


    private RestResponseVO checkFieldByUpdate(UserRequest request) {

        User userFromDB = null;
        if (StringUtils.isNoneBlank(request.getUsername())) {
            userFromDB = userMapper.getByUserName(request.getUsername());
            if (userFromDB != null && !userFromDB.getId().equals(request.getId())) {
                return RestResponseVO.createByErrorEnum(RestResponseEnum.USERNAME_REPEATED_ERROR);
            }
        }
        if (StringUtils.isNoneBlank(request.getName())) {
            userFromDB = userMapper.getByName(request.getName());
            if (userFromDB != null && !userFromDB.getId().equals(request.getId())) {
                return RestResponseVO.createByErrorEnum(RestResponseEnum.NAME_REPEATED_ERROR);
            }
        }
        if (StringUtils.isNoneBlank(request.getEmail())) {
            userFromDB = userMapper.getEmail(request.getEmail());
            if (userFromDB != null && !userFromDB.getId().equals(request.getId())) {
                return RestResponseVO.createByErrorEnum(RestResponseEnum.EMAIL_REPEATED_ERROR);
            }
        }
        if (StringUtils.isNoneBlank(request.getPhone())) {
            userFromDB = userMapper.getByPhone(request.getPhone());
            if (userFromDB != null && !userFromDB.getId().equals(request.getId())) {
                return RestResponseVO.createByErrorEnum(RestResponseEnum.PHONE_REPEATED_ERROR);
            }
        }
        return RestResponseVO.createBySuccess();
    }

}
