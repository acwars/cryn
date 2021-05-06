package com.onlinejudge.cryn.service;

import com.github.pagehelper.PageInfo;
import com.onlinejudge.cryn.entity.BlogComment;
import com.onlinejudge.cryn.response.RestResponseVO;

public interface BlogCommentService {
    RestResponseVO getById(Integer blogCommentId);

    RestResponseVO insert(BlogComment blogComment);

    RestResponseVO delById(Integer id);

    RestResponseVO updateById(BlogComment blogComment);

    RestResponseVO<PageInfo> listByBlogId2Page(Integer userId,Integer sort, Integer pageNum, Integer pageSize, Integer blogId);
}
