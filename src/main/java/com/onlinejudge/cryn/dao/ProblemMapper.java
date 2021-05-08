package com.onlinejudge.cryn.dao;

import com.onlinejudge.cryn.entity.Problem;
import com.onlinejudge.cryn.response.ProblemDetailVO;
import com.onlinejudge.cryn.response.ProblemVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProblemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Problem record);

    int insertSelective(Problem record);

    Problem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Problem record);

    int updateByPrimaryKey(Problem record);

    /**
     * 推荐题目
     * @param userId
     * @param row
     * @return
     */
    List<ProblemDetailVO> listSuggestProblem(Integer userId, @Param("row") int row);

    List<ProblemDetailVO> findAllByProblemIds(@Param("ProblemIds") List<Long> ProblemIds);


    Integer countRandomProblemId();

    List<ProblemVO> listAll2VO(@Param("flag")Integer flag,@Param("sort") Integer sort, @Param("keyword") String keyword,
                               @Param("rating") Integer rating, @Param("tagIdsList") List<Integer> tagIdsList);


    ProblemDetailVO getDetailVOById(Integer problemId);

    List<Problem> listAllSolveProblemByUserId(Integer userId);


}