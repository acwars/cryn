package com.onlinejudge.cryn.dao;

import com.onlinejudge.cryn.entity.CompetitionProblem;
import com.onlinejudge.cryn.response.CompetitionProblemVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CompetitionProblemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CompetitionProblem record);

    int insertSelective(CompetitionProblem record);

    CompetitionProblem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CompetitionProblem record);

    int updateByPrimaryKey(CompetitionProblem record);

    List<CompetitionProblemVO> listVOByCompetitionId(Integer competitionId);

    Integer getScoreByCompIdProblemId(@Param("compId") Integer compId, @Param("problemId")  Integer problemId);

    CompetitionProblem getByCompIdProblemId(@Param("compId")Integer compId, @Param("problemId")Integer problemId);

    int deleteByCompId(Integer compId);


}