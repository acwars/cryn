package com.onlinejudge.cryn.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.onlinejudge.cryn.common.*;
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
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.JDBCDataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

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

    @Autowired
    private DataModel dataModel;

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
        problem.setRating(0);
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
    public RestResponseVO<PageInfo> listProblemVOToPage(Integer userId,Integer flag,Integer sort, String keyword, Integer rating, String tagIds, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize, true);
        List<Integer> tagIdsList = null;
        if (StringUtils.isNoneBlank(tagIds)) {
            tagIdsList = new ArrayList<>();
            for (String tagId : tagIds.split(",")) {
                tagIdsList.add(Integer.parseInt(tagId));
            }
        }

        List<ProblemVO> problemList = problemMapper.listAll2VO(flag,sort, keyword, rating, tagIdsList);
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
    public RestResponseVO<List<ProblemDetailVO>> listSuggestProblem(Integer userId, Integer row) {
        if (userId == null) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }

        List<ProblemDetailVO> problemList = null;
        try {
            //计算相似度，相似度算法有很多种，采用基于余弦相似度
            UserSimilarity similarity = new AdjustedCosineSimilarity(dataModel);
            //计算最近邻域，邻居有两种算法，基于固定数量的邻居和基于相似度的邻居，这里使用基于相似度的邻居
            //ThresholdUserNeighborhood 对每个用户基于一定的限制，相似度限制内的所有用户为邻居
            double threshold = 0.0;
            UserNeighborhood userNeighborhood = new ThresholdUserNeighborhood(threshold, similarity, dataModel);
            //构建推荐器，基于用户的协同过滤推荐
            Recommender recommender = new GenericUserBasedRecommender(dataModel, userNeighborhood, similarity);
            long start = System.currentTimeMillis();
            //推荐题目
            List<RecommendedItem> recommendedItemList = recommender.recommend(userId, row);
            List<Long> ProblemIds = new ArrayList<Long>();
            for (RecommendedItem recommendedItem : recommendedItemList) {
                System.out.println(recommendedItem);
                ProblemIds.add(recommendedItem.getItemID());
            }
            System.out.println("推荐出来的题目id集合"+ProblemIds);

            //根据题目id查询题目
            if(ProblemIds!=null &&ProblemIds.size()>0) {
                problemList = problemMapper.findAllByProblemIds(ProblemIds);
            }else{
                problemList = new ArrayList<>();
            }
            System.out.println("推荐数量:"+problemList.size() +"耗时："+(System.currentTimeMillis()-start));
        } catch (TasteException e) {
            e.printStackTrace();
        }

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
