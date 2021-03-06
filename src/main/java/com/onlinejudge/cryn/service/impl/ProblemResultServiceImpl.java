package com.onlinejudge.cryn.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.onlinejudge.cryn.common.JudgeStatusEnum;
import com.onlinejudge.cryn.common.RestResponseEnum;
import com.onlinejudge.cryn.common.StringConst;
import com.onlinejudge.cryn.dao.ProblemMapper;
import com.onlinejudge.cryn.dao.ProblemResultMapper;
import com.onlinejudge.cryn.dao.TestcaseResultMapper;
import com.onlinejudge.cryn.entity.ProblemResult;
import com.onlinejudge.cryn.entity.TestcaseResult;
import com.onlinejudge.cryn.response.*;
import com.onlinejudge.cryn.service.CompetitionProblemService;
import com.onlinejudge.cryn.service.ProblemResultService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProblemResultServiceImpl implements ProblemResultService {
    @Autowired
    private ProblemResultMapper problemResultMapper;

    @Autowired
    private ProblemMapper problemMapper;

    @Autowired
    private TestcaseResultMapper testcaseResultMapper;

    @Autowired
    private CompetitionProblemService competitionProblemService;


    @Override
    public RestResponseVO<ProblemResult> getById(Integer problemResultId) {
        if (problemResultId == null) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }
        ProblemResult problemResult = problemResultMapper.selectByPrimaryKey(problemResultId);
        return problemResult != null ? RestResponseVO.createBySuccess(problemResult)
                : RestResponseVO.createByError();
    }

    @Override
    public RestResponseVO delById(Integer problemResultId) {
        if (problemResultId == null) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }
        int effect = problemResultMapper.deleteByPrimaryKey(problemResultId);
        return effect > 0 ? RestResponseVO.createBySuccessMessage(StringConst.DEL_SUCCESS)
                : RestResponseVO.createByErrorMessage(StringConst.DEL_FAIL);
    }

    @Override
    public RestResponseVO insert(ProblemResult problemResult) {
        if (problemResult == null) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }
        ProblemDetailVO problem = problemMapper.getDetailVOById(problemResult.getProblemId());
        problemResult.setProblemRating(problem.getRating());
        int effect = problemResultMapper.insertSelective(problemResult);
        int effect2 = problemResultMapper.insert2RecommendsProblemsData(problemResult);
        return (effect > 0 && effect2 > 0) ? RestResponseVO.createBySuccessMessage(StringConst.DEL_SUCCESS)
                : RestResponseVO.createByErrorMessage(StringConst.DEL_FAIL);
    }

    @Override
    public RestResponseVO<PageInfo> listProblemResult2Page(Integer problemId, String name, String type, Integer status, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize, true);
        List<ProblemResultVO> problemResultList = problemResultMapper.listProblemResult(problemId, name, type, status);
        for (ProblemResultVO problemResultVO : problemResultList) {
            List<TestcaseResult> testcaseResultList = testcaseResultMapper.listByProblemResultId(problemResultVO.getId());
            if (testcaseResultList != null) {
                int acCount = 0;
                for (TestcaseResult testcaseResult : testcaseResultList) {
                    if (JudgeStatusEnum.ACCEPTED.getStatus().equals(testcaseResult.getStatus())) {
                        acCount++;
                    }
                }
                int score = (int) ((acCount * 1.0 / testcaseResultList.size()) * 100);
                problemResultVO.setScore(score);
            }

        }
        PageInfo<ProblemResultVO> pageInfo = new PageInfo<ProblemResultVO>(problemResultList);
        return RestResponseVO.createBySuccess(pageInfo);
    }

    @Override
    public RestResponseVO<ProblemResultSubmitVO> getByRunNum2SubmitVO(String runNum) {
        if (StringUtils.isBlank(runNum)) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }
        ProblemResultSubmitVO resultSubmitVO = problemResultMapper.getByRunNum2SubmitVO(runNum);
        return RestResponseVO.createBySuccess(resultSubmitVO);
    }

    @Override
    public RestResponseVO<ProblemResultDetailVO> getById2DetailVO(Integer problemResultId) {
        if (problemResultId == null) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }
        ProblemResultDetailVO detailVO = problemResultMapper.getById2DetailVO(problemResultId);
        if (detailVO != null) {
            List<TestcaseResult> testcaseResultList = detailVO.getTestcaseResultList();
            if (testcaseResultList != null) {
                for (TestcaseResult testcaseResult : testcaseResultList) {
                    if (JudgeStatusEnum.ACCEPTED.getStatus().equals(testcaseResult.getStatus())) {
                        detailVO.setAcCount(detailVO.getAcCount() + 1);
                    }
                }
                int score = (int) ((detailVO.getAcCount() * 1.0 / testcaseResultList.size()) * 100);
                detailVO.setScore(score);
            }
        }

        return RestResponseVO.createBySuccess(detailVO);
    }

    @Override
    public RestResponseVO<PageInfo> listProblemResultCompetitionVO2Page(Integer pageNum, Integer pageSize, Integer userId, Integer compId) {
        if (pageNum == null || pageSize == null || userId == null || compId == null) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }
        PageHelper.startPage(pageNum, pageSize, true);

        List<ProblemResultCompetitionVO> list = problemResultMapper.listProblemResultCompetitionVO2Page(compId, userId);
        for (ProblemResultCompetitionVO dataVO : list) {
            List<TestcaseResult> testcaseResultList = testcaseResultMapper.listByProblemResultId(dataVO.getId());
            for (TestcaseResult testcaseResult : testcaseResultList) {
                if (JudgeStatusEnum.ACCEPTED.getStatus().equals(testcaseResult.getStatus())) {
                    dataVO.setAcCount(dataVO.getAcCount() + 1);
                }
            }
            Integer score = competitionProblemService.getScoreByCompIdProblemId(compId, dataVO.getProblemId()).getData();
            if (score != null) {
                score = (int) ((dataVO.getAcCount() * 1.0 / testcaseResultList.size()) * score);
                dataVO.setScore(dataVO.getScore() + score);
            }

        }

        PageInfo<ProblemResultCompetitionVO> pageInfo = new PageInfo<>(list);

        return RestResponseVO.createBySuccess(pageInfo);
    }


    /**
     * todo
     * @param testcaseResultList
     * @return
     */
    private int getProblemResultBaseScore(List<TestcaseResult> testcaseResultList) {
        return testcaseResultList.size() == 5 ? 20 : testcaseResultList.size() == 10 ? 10 : 0;
    }

}
