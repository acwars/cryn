package com.onlinejudge.cryn.service.impl;

import com.onlinejudge.cryn.common.RestResponseEnum;
import com.onlinejudge.cryn.common.StringConst;
import com.onlinejudge.cryn.dao.CompetitionProblemMapper;
import com.onlinejudge.cryn.entity.CompetitionProblem;
import com.onlinejudge.cryn.response.CompetitionProblemVO;
import com.onlinejudge.cryn.response.RestResponseVO;
import com.onlinejudge.cryn.service.CompetitionProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompetitionProblemServiceImpl implements CompetitionProblemService {

    @Autowired
    private CompetitionProblemMapper competitionProblemMapper;
    @Override
    public RestResponseVO getById(Integer id) {
        if (id == null) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }
        CompetitionProblem competitionProblem = competitionProblemMapper.selectByPrimaryKey(id);
        return RestResponseVO.createBySuccess(competitionProblem);
    }

    @Override
    public RestResponseVO insert(CompetitionProblem competitionProblem) {
        if (competitionProblem == null) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }

        CompetitionProblem competitionProblemFromDB = competitionProblemMapper.getByCompIdProblemId(competitionProblem.getCompId(), competitionProblem.getProblemId());
        if (competitionProblemFromDB != null) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.COMPETITION_PROBLEM_REPEATED_ERROR);
        }
        int effect = competitionProblemMapper.insertSelective(competitionProblem);
        return effect > 0 ? RestResponseVO.createBySuccessMessage(StringConst.ADD_SUCCESS)
                : RestResponseVO.createByErrorMessage(StringConst.ADD_FAIL);
    }

    @Override
    public RestResponseVO delById(Integer id) {
        if (id == null) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }
        int effect = competitionProblemMapper.deleteByPrimaryKey(id);
        return effect > 0 ? RestResponseVO.createBySuccessMessage(StringConst.DEL_SUCCESS)
                : RestResponseVO.createByErrorMessage(StringConst.DEL_FAIL);
    }

    @Override
    public RestResponseVO updateById(CompetitionProblem competitionProblem) {
        if (competitionProblem == null) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }
        int effect = competitionProblemMapper.updateByPrimaryKeySelective(competitionProblem);
        return effect > 0 ? RestResponseVO.createBySuccessMessage(StringConst.UPDATE_SUCCESS)
                : RestResponseVO.createByErrorMessage(StringConst.UPDATE_FAIL);
    }

    @Override
    public RestResponseVO<List<CompetitionProblemVO>> listVOByCompetitionId(Integer competitionId) {
        if (competitionId == null) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }

        List<CompetitionProblemVO> competitionProblemVOList = competitionProblemMapper.listVOByCompetitionId(competitionId);
        return RestResponseVO.createBySuccess(competitionProblemVOList);
    }

    @Override
    public RestResponseVO<Integer> getScoreByCompIdProblemId(Integer compId, Integer problemId) {
        if (compId == null || problemId == null) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }
        Integer score = competitionProblemMapper.getScoreByCompIdProblemId(compId, problemId);
        return RestResponseVO.createBySuccess(score);
    }
}
