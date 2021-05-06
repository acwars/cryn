package com.onlinejudge.cryn.service;

import com.github.pagehelper.PageInfo;
import com.onlinejudge.cryn.entity.Blog;
import com.onlinejudge.cryn.entity.Problem;
import com.onlinejudge.cryn.request.UserRequest;
import com.onlinejudge.cryn.response.ProblemResultRecentVO;
import com.onlinejudge.cryn.response.RestResponseVO;
import com.onlinejudge.cryn.response.UserDetailVO;

import java.util.List;

public interface UserService {

    RestResponseVO addSignCount(Integer userId);

    RestResponseVO<UserDetailVO> getById(Integer userId);

    RestResponseVO insert(UserRequest request);

    RestResponseVO delById(Integer id);

    RestResponseVO lockById(Integer id);

    RestResponseVO activeById(Integer id);

    RestResponseVO updateById(UserRequest request);

    RestResponseVO sendRegisterEmail(String email);

    RestResponseVO sendForgetEmail(String email);

    RestResponseVO forgetRestPassword(String token,String email,String password);

    RestResponseVO register(String token, UserRequest request);

    RestResponseVO<PageInfo> listRankUser2Page(Integer pageNum, Integer pageSize, String keyword);

    RestResponseVO checkLoginByAdmin(String username,String password);

    RestResponseVO<PageInfo> listUser2Page(Integer pageNum,Integer pageSize,String keyword);

    RestResponseVO<List<Problem>> listAllSolveProblemByUserId(Integer userId);

    RestResponseVO<List<Blog>> listRecentBlog(Integer userId, Integer recentSize);

    RestResponseVO<List<ProblemResultRecentVO>> listRecentProblem(Integer userId, Integer recentSize);

    RestResponseVO listProblemRecord(Integer userId, Integer flag);

    RestResponseVO updateSecurity(Integer id, String email, String oldPassword, String password);
}
