package com.onlinejudge.cryn.service;

import com.onlinejudge.cryn.entity.CompetitionProblem;
import com.onlinejudge.cryn.response.CompetitionProblemVO;
import com.onlinejudge.cryn.response.RestResponseVO;

import java.util.List;

public interface CompetitionProblemService {

    RestResponseVO getById(Integer id);

    RestResponseVO insert(CompetitionProblem competitionProblem);

    RestResponseVO delById(Integer id);

    RestResponseVO updateById(CompetitionProblem competitionProblem);

    RestResponseVO<List<CompetitionProblemVO>> listVOByCompetitionId(Integer competitionId);

    RestResponseVO<Integer> getScoreByCompIdProblemId(Integer compId,Integer problemId);

}
