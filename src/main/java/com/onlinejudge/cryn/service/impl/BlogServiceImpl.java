package com.onlinejudge.cryn.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.onlinejudge.cryn.common.RestResponseEnum;
import com.onlinejudge.cryn.common.StringConst;
import com.onlinejudge.cryn.dao.BlogCommentMapper;
import com.onlinejudge.cryn.dao.BlogMapper;
import com.onlinejudge.cryn.dao.UpMapper;
import com.onlinejudge.cryn.entity.Blog;
import com.onlinejudge.cryn.entity.Up;
import com.onlinejudge.cryn.response.BlogDetailVO;
import com.onlinejudge.cryn.response.BlogVO;
import com.onlinejudge.cryn.response.RestResponseVO;
import com.onlinejudge.cryn.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private UpMapper upMapper;

    @Autowired
    private BlogCommentMapper blogCommentMapper;



    @Override
    public RestResponseVO<Blog> getById(Integer blogId) {
        if (blogId == null) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }
        Blog blog = blogMapper.selectByPrimaryKey(blogId);
        return RestResponseVO.createBySuccess(blog);
    }

    @Override
    public RestResponseVO insert(Blog blog) {
        if (blog == null) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }
        int effect = blogMapper.insertSelective(blog);
        return effect > 0 ? RestResponseVO.createBySuccessMessage(StringConst.ADD_SUCCESS)
                : RestResponseVO.createByErrorMessage(StringConst.ADD_FAIL);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponseVO delById(Integer id) {
        if (id == null) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }
        int effect = blogMapper.deleteByPrimaryKey(id);
        if (effect > 0) {
            upMapper.deleteByBlogId(id);
            blogCommentMapper.deleteByBlogId(id);
            return RestResponseVO.createBySuccessMessage(StringConst.DEL_SUCCESS);
        }
        return RestResponseVO.createByErrorMessage(StringConst.DEL_FAIL);
    }

    @Override
    public RestResponseVO updateById(Blog blog) {
        if (blog == null) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }
        int effect = blogMapper.updateByPrimaryKeySelective(blog);
        return effect > 0 ? RestResponseVO.createBySuccessMessage(StringConst.UPDATE_SUCCESS)
                : RestResponseVO.createByErrorMessage(StringConst.UPDATE_FAIL);
    }

    @Override
    public RestResponseVO<PageInfo> listBlogVO2Page(Integer pageNum, Integer pageSize,Integer sort, String keyword, Integer bcId) {
        PageHelper.startPage(pageNum, pageSize, true);
        List<BlogVO> list = blogMapper.list2BlogVO(sort,keyword, bcId);
        PageInfo<BlogVO> pageInfo = new PageInfo<>(list);
        return RestResponseVO.createBySuccess(pageInfo);
    }

    @Override
    public RestResponseVO<BlogDetailVO> getBlogDetailVOById(Integer blogId, Integer userId) {
        if (blogId == null) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }
        BlogDetailVO blogDetailVO = blogMapper.getBlogDetailVO(blogId);
        if (userId != null) {
            Up up = upMapper.getByBlogIdUserId(blogId, userId);
            if (up != null) {
                blogDetailVO.setUserUpStatus(up.getStatus());
            }
        }
        return RestResponseVO.createBySuccess(blogDetailVO);
    }

    @Override
    public RestResponseVO updateViewCount(Integer blogId) {
        if (blogId == null) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }
        int effect = blogMapper.updateViewCountIncrease(blogId);
        return effect > 0 ? RestResponseVO.createBySuccessMessage(StringConst.UPDATE_SUCCESS)
                : RestResponseVO.createByErrorMessage(StringConst.UPDATE_FAIL);
    }

    @Override
    public RestResponseVO listHotBlogVO(Integer pageSize) {
        List<BlogVO> blogVOList = blogMapper.listHotBlogVO(pageSize);
        return RestResponseVO.createBySuccess(blogVOList);
    }

    @Override
    public RestResponseVO<List<BlogVO>> listLastCommentBlogVO(Integer pageSize) {
        List<BlogVO> blogVOList = blogMapper.listLastCommentBlogVO(pageSize);
        return RestResponseVO.createBySuccess(blogVOList);
    }

    @Override
    public RestResponseVO<List<BlogVO>> listNoticeBlogVO(Integer pageSize) {
        List<BlogVO> blogVOList = blogMapper.listNoticeBlogVO(pageSize);
        return RestResponseVO.createBySuccess(blogVOList);
    }
}
