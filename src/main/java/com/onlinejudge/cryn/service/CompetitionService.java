package com.onlinejudge.cryn.service;

import com.github.pagehelper.PageInfo;
import com.onlinejudge.cryn.entity.Competition;
import com.onlinejudge.cryn.response.CompetitionDetailVO;
import com.onlinejudge.cryn.response.RestResponseVO;

import java.util.List;

public interface CompetitionService {

    RestResponseVO<Competition> getById(Integer competitionId);

    RestResponseVO insert(Competition competition);

    RestResponseVO delById(Integer competitionId);

    RestResponseVO updateById(Competition competition);

    RestResponseVO<CompetitionDetailVO> getCompetitionDetailVOById(Integer userId,Integer compId);

    RestResponseVO<PageInfo> listCompetitionVO2Page(Integer pageSize,Integer pageNum,String keyword);

    RestResponseVO<List<CompetitionDetailVO>> listLastCompetitionDetailVO(Integer pageSize);

}
