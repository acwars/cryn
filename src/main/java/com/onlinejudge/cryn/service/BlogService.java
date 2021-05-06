package com.onlinejudge.cryn.service;

import com.github.pagehelper.PageInfo;
import com.onlinejudge.cryn.entity.Blog;
import com.onlinejudge.cryn.response.BlogDetailVO;
import com.onlinejudge.cryn.response.BlogVO;
import com.onlinejudge.cryn.response.RestResponseVO;

import java.util.List;

public interface BlogService {

    RestResponseVO<Blog> getById(Integer blogId);

    RestResponseVO insert(Blog blog);

    RestResponseVO delById(Integer id);

    RestResponseVO updateById(Blog blog);

    RestResponseVO<PageInfo> listBlogVO2Page(Integer pageNum, Integer pageSize,Integer sort, String keyword, Integer bcId);

    RestResponseVO<BlogDetailVO> getBlogDetailVOById(Integer blogId,Integer userId);

    RestResponseVO updateViewCount(Integer blogId);

    RestResponseVO<List<BlogVO>> listHotBlogVO(Integer pageSize);

    RestResponseVO<List<BlogVO>> listLastCommentBlogVO(Integer pageSize);

    RestResponseVO<List<BlogVO>> listNoticeBlogVO(Integer pageSize);

}
