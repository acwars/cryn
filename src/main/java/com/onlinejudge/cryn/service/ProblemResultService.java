package com.onlinejudge.cryn.service;

import com.github.pagehelper.PageInfo;
import com.onlinejudge.cryn.entity.ProblemResult;
import com.onlinejudge.cryn.response.ProblemResultDetailVO;
import com.onlinejudge.cryn.response.ProblemResultSubmitVO;
import com.onlinejudge.cryn.response.RestResponseVO;

public interface ProblemResultService {

    RestResponseVO<ProblemResult> getById(Integer problemResultId);

    RestResponseVO delById(Integer problemResultId);

    RestResponseVO insert(ProblemResult problemResult);

    RestResponseVO listProblemResult2Page(Integer problemId, String name, String type, Integer status, Integer pageNum,Integer pageSize);

    RestResponseVO<ProblemResultSubmitVO> getByRunNum2SubmitVO(String runNum);

    RestResponseVO<ProblemResultDetailVO> getById2DetailVO(Integer problemResultId);

    RestResponseVO<PageInfo> listProblemResultCompetitionVO2Page(Integer pageNum, Integer pageSize, Integer userId, Integer compId);
}
