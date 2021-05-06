package com.onlinejudge.cryn.dao;

import com.onlinejudge.cryn.entity.BlogComment;
import com.onlinejudge.cryn.response.BlogCommentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BlogCommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BlogComment record);

    int insertSelective(BlogComment record);

    BlogComment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BlogComment record);

    int updateByPrimaryKey(BlogComment record);

    List<BlogCommentVO> listBlogCommentVO(@Param("sort") Integer sort, @Param("blogId") Integer blogId);

    int deleteByBlogId(Integer blogId);
}