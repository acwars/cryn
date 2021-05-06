package com.onlinejudge.cryn.service.impl;

import com.onlinejudge.cryn.common.RestResponseEnum;
import com.onlinejudge.cryn.common.StringConst;
import com.onlinejudge.cryn.dao.UpMapper;
import com.onlinejudge.cryn.entity.Up;
import com.onlinejudge.cryn.response.RestResponseVO;
import com.onlinejudge.cryn.response.UpVO;
import com.onlinejudge.cryn.service.UpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpServiceImpl implements UpService {

    @Autowired
    private UpMapper upMapper;


    private static final Integer TYPE_BLOG = 0;

    private static final Integer TYPE_BLOG_COMMENT = 1;

    private static final Boolean LIKED = true;

    private static final Boolean NOT_LIKE = false;

    @Override
    public RestResponseVO insert(Up up) {
        if (up == null) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }
        int effect = upMapper.insertSelective(up);
        return effect > 0 ? RestResponseVO.createBySuccessMessage(StringConst.ADD_SUCCESS)
                : RestResponseVO.createByErrorMessage(StringConst.ADD_FAIL);
    }

    @Override
    public RestResponseVO delById(Integer id) {
        if (id == null) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }
        int effect = upMapper.deleteByPrimaryKey(id);
        return effect > 0 ? RestResponseVO.createBySuccessMessage(StringConst.DEL_SUCCESS)
                : RestResponseVO.createByErrorMessage(StringConst.DEL_FAIL);
    }

    @Override
    public RestResponseVO updateById(Up up) {
        if (up == null) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }
        int effect = upMapper.updateByPrimaryKeySelective(up);
        return effect > 0 ? RestResponseVO.createBySuccessMessage(StringConst.UPDATE_SUCCESS)
                : RestResponseVO.createByErrorMessage(StringConst.UPDATE_FAIL);
    }

    @Override
    public RestResponseVO<UpVO> blogUp(Integer blogId, Integer userId) {
        if (blogId == null || userId == null) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }
        int count = upMapper.countBlogById(blogId);
        boolean status;
        Up up = upMapper.getByBlogIdUserId(blogId, userId);
        int effect = 0;
        if (up != null) {
            if (up.getStatus().equals(LIKED)) {
                count--;
            } else {
                count++;
            }
            up.setStatus(!up.getStatus());
            status = up.getStatus();
            effect = upMapper.updateByPrimaryKeySelective(up);
        } else {
            count++;
            up = new Up();
            up.setType(TYPE_BLOG);
            up.setTypeId(blogId);
            up.setUserId(userId);
            up.setStatus(LIKED);
            status = up.getStatus();
            effect = upMapper.insertSelective(up);
        }
        UpVO upVO = new UpVO(status, count);
        return effect > 0 ? RestResponseVO.createBySuccess(upVO)
                : RestResponseVO.createByErrorMessage(StringConst.UPDATE_FAIL);
    }


    @Override
    public RestResponseVO<UpVO> blogCommentUp(Integer blogCommentId, Integer userId) {
        if (blogCommentId == null || userId == null) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }
        int count = upMapper.countBlogCommentById(blogCommentId);
        boolean status;
        Up up = upMapper.getByBlogCommentIdUserId(blogCommentId, userId);
        int effect = 0;
        if (up != null) {
            if (up.getStatus().equals(LIKED)) {
                count--;
            } else {
                count++;
            }
            up.setStatus(!up.getStatus());
            status = up.getStatus();
            effect = upMapper.updateByPrimaryKeySelective(up);
        } else {
            count++;
            up = new Up();
            up.setType(TYPE_BLOG_COMMENT);
            up.setTypeId(blogCommentId);
            up.setUserId(userId);
            up.setStatus(LIKED);
            status = up.getStatus();
            effect = upMapper.insertSelective(up);
        }
        UpVO upVO = new UpVO(status, count);
        return effect > 0 ? RestResponseVO.createBySuccess(upVO)
                : RestResponseVO.createByErrorMessage(StringConst.UPDATE_FAIL);
    }
}
