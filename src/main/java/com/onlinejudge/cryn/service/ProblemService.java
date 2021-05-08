package com.onlinejudge.cryn.service;

import com.github.pagehelper.PageInfo;
import com.onlinejudge.cryn.entity.Problem;
import com.onlinejudge.cryn.request.ProblemRequest;
import com.onlinejudge.cryn.response.ProblemDetailVO;
import com.onlinejudge.cryn.response.RestResponseVO;

import java.util.List;

public interface ProblemService {

    RestResponseVO<Problem> getById(Integer problemId);

    RestResponseVO insert(ProblemRequest problemRequest);

    RestResponseVO delById(Integer id);

    RestResponseVO<Problem> updateById(ProblemRequest problemRequest);

    RestResponseVO<PageInfo> listProblemVOToPage(Integer userId,Integer flag,Integer sort,String keyword, Integer rating, String tagIds, Integer pageNum, Integer pageSize);

    RestResponseVO<List<ProblemDetailVO>> listSuggestProblem(Integer userId, Integer row);

    RestResponseVO<Integer> randomProblemId();

    RestResponseVO<ProblemDetailVO> getDetailVOById(Integer problemId);

}
