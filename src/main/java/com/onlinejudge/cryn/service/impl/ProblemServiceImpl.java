package com.onlinejudge.cryn.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.onlinejudge.cryn.common.CommonConst;
import com.onlinejudge.cryn.common.JudgeStatusEnum;
import com.onlinejudge.cryn.common.RestResponseEnum;
import com.onlinejudge.cryn.common.StringConst;
import com.onlinejudge.cryn.dao.ProblemMapper;
import com.onlinejudge.cryn.dao.ProblemResultMapper;
import com.onlinejudge.cryn.dao.ProblemTagMapper;
import com.onlinejudge.cryn.dao.TestcaseResultMapper;
import com.onlinejudge.cryn.entity.Problem;
import com.onlinejudge.cryn.entity.ProblemTag;
import com.onlinejudge.cryn.request.ProblemRequest;
import com.onlinejudge.cryn.response.ProblemDetailVO;
import com.onlinejudge.cryn.response.ProblemVO;
import com.onlinejudge.cryn.response.RestResponseVO;
import com.onlinejudge.cryn.response.TestcaseVO;
import com.onlinejudge.cryn.service.FileService;
import com.onlinejudge.cryn.service.ProblemService;
import com.onlinejudge.cryn.utils.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ProblemServiceImpl implements ProblemService {

    @Autowired
    private ProblemMapper problemMapper;

    @Autowired
    private ProblemResultMapper problemResultMapper;

    @Autowired
    private TestcaseResultMapper testcaseResultMapper;
    @Autowired
    private FileService fileService;

    @Autowired
    private ProblemTagMapper problemTagMapper;


    private final Long defaultTime = 1000L;

    private final Long defaultMemory = 262144L;


    @Override
    public RestResponseVO getById(Integer problemId) {
        if (problemId == null) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }
        Problem problem = problemMapper.selectByPrimaryKey(problemId);
        return RestResponseVO.createBySuccess(problem);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponseVO delById(Integer id) {
        if (id == null) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }
        int effect = problemMapper.deleteByPrimaryKey(id);
        if (effect > 0) {

            problemTagMapper.deleteByProblemId(id);

            testcaseResultMapper.deleteByProblemId(id);
            problemResultMapper.deleteByProblemId(id);

            fileService.deleteTestcase(id).isSuccess();

            return RestResponseVO.createBySuccessMessage(StringConst.DEL_SUCCESS);
        } else {
            return RestResponseVO.createByErrorMessage(StringConst.DEL_FAIL);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponseVO insert(ProblemRequest problemRequest) {
        if (problemRequest == null) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }
        Problem problem = new Problem();
        BeanUtil.copyPropertiesIgnoreNull(problemRequest,problem);
        int effect = problemMapper.insertSelective(problem);
        if (effect > 0) {
            boolean result = false;
            result = addTestcaseList(problem.getId(),problemRequest.getTestcaseList());
            if (result) {
                String tags = problemRequest.getTags();
                result = insertTags(problem.getId(), tags);
                return result ? RestResponseVO.createBySuccessMessage(StringConst.ADD_SUCCESS, problem)
                        : RestResponseVO.createByErrorMessage(StringConst.ADD_FAIL);
            }
            return RestResponseVO.createByErrorMessage(StringConst.ADD_FAIL);
        }
        return RestResponseVO.createByErrorMessage(StringConst.ADD_FAIL);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponseVO<Problem> updateById(ProblemRequest problemRequest) {
        if (problemRequest == null) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }
        Problem problem = new Problem();
        BeanUtil.copyPropertiesIgnoreNull(problemRequest, problem);
        int effect = problemMapper.updateByPrimaryKeySelective(problem);
        if (effect > 0) {
            if (problemRequest.isSettingUpdated()) {
                return RestResponseVO.createBySuccessMessage(StringConst.UPDATE_SUCCESS);
            }

            boolean result = false;
            result = addTestcaseList(problemRequest.getId(),problemRequest.getTestcaseList());
            if (result) {
                String tags = problemRequest.getTags();
                result = insertTags(problem.getId(), tags);
                return result ? RestResponseVO.createBySuccessMessage(StringConst.UPDATE_SUCCESS, problem)
                        : RestResponseVO.createByErrorMessage(StringConst.UPDATE_FAIL);
            }
            return RestResponseVO.createByErrorMessage(StringConst.UPDATE_FAIL);
        }
        return RestResponseVO.createByErrorMessage(StringConst.UPDATE_FAIL);
    }


    @Override
    public RestResponseVO<PageInfo> listProblemVOToPage(Integer userId,Integer flag,Integer sort, String keyword, Integer level, String tagIds, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize, true);
        List<Integer> tagIdsList = null;
        if (StringUtils.isNoneBlank(tagIds)) {
            tagIdsList = new ArrayList<>();
            for (String tagId : tagIds.split(",")) {
                tagIdsList.add(Integer.parseInt(tagId));
            }
        }

        List<ProblemVO> problemList = problemMapper.listAll2VO(flag,sort, keyword, level, tagIdsList);
        if (userId != null) {
            for (ProblemVO problemVO : problemList) {
                int totalCount = problemResultMapper.countUserIdProblemId(userId, problemVO.getId());
                if (totalCount > 0) {
                    int acCount = problemResultMapper.countUserIdProblemIdByStatus(userId, problemVO.getId(), JudgeStatusEnum.ACCEPTED.getStatus());
                    if (acCount > 0) {
                        problemVO.setUserStatus(CommonConst.ProblemUserStatus.PASSED);
                    } else {
                        problemVO.setUserStatus(CommonConst.ProblemUserStatus.TRYING);
                    }
                }
            }
        }
        PageInfo<ProblemVO> pageInfo = new PageInfo<>(problemList);
        return RestResponseVO.createBySuccess(pageInfo);
    }

    @Override
    public RestResponseVO<List<ProblemDetailVO>> listSuggestProblem(Integer problemId, Integer row) {
        if (problemId == null) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }
        List<ProblemDetailVO> problemList = problemMapper.listSuggestProblem(problemId, row);
        return RestResponseVO.createBySuccess(problemList);
    }

    @Override
    public RestResponseVO<Integer> randomProblemId() {
        Integer randomProblemId = problemMapper.countRandomProblemId();
        if (randomProblemId != null) {
            return RestResponseVO.createBySuccess(randomProblemId);
        } else {
            return RestResponseVO.createByError();
        }

    }

    @Override
    public RestResponseVO<ProblemDetailVO> getDetailVOById(Integer problemId) {
        if (problemId == null) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }
        ProblemDetailVO problemDetailVO = problemMapper.getDetailVOById(problemId);
        return RestResponseVO.createBySuccess(problemDetailVO);
    }



    private boolean insertTags(Integer problemId, String tags) {
        if (StringUtils.isNoneBlank(tags) && problemId != null) {
            String[] tagsIdsList = tags.split(",");
            if (ArrayUtils.isNotEmpty(tagsIdsList)) {
                problemTagMapper.deleteByProblemId(problemId);
                int effect = 0;
                for (String id : tagsIdsList) {
                    ProblemTag problemTag = new ProblemTag();
                    problemTag.setProblemId(problemId);
                    problemTag.setTagId(Integer.parseInt(id));
                    effect = problemTagMapper.insertSelective(problemTag);
                }
                return effect > 0;
            }
            return false;
        }
        return false;
    }

    private boolean addTestcaseList(Integer problemId,List<TestcaseVO> testcaseVOList){
        if (CollectionUtils.isNotEmpty(testcaseVOList)) {
            if (fileService.deleteTestcase(problemId).isSuccess()) {
                for (TestcaseVO testcaseVO : testcaseVOList) {
                    fileService.saveTestcase(problemId, testcaseVO.getNum(),
                            testcaseVO.getInput(), testcaseVO.getOutput());
                }
            }
            return true;
        }
        return false;
    }

}
