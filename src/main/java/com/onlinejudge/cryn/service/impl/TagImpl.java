package com.onlinejudge.cryn.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.onlinejudge.cryn.common.RestResponseEnum;
import com.onlinejudge.cryn.common.StringConst;
import com.onlinejudge.cryn.dao.TagMapper;
import com.onlinejudge.cryn.entity.Tag;
import com.onlinejudge.cryn.response.RestResponseVO;
import com.onlinejudge.cryn.response.TagVO;
import com.onlinejudge.cryn.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagImpl implements TagService {
    @Autowired
    private TagMapper tagMapper;

    @Override
    public RestResponseVO<List<TagVO>> listParentVOAll() {
        List<TagVO> problemCategoryList = tagMapper.listParentVOAll();
        return RestResponseVO.createBySuccess(problemCategoryList);
    }

    @Override
    public RestResponseVO insert(Tag tag) {
        if (tag == null) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }
        int effect = tagMapper.insertSelective(tag);
        return effect > 0 ? RestResponseVO.createBySuccessMessage(StringConst.ADD_SUCCESS)
                : RestResponseVO.createByErrorMessage(StringConst.ADD_FAIL);
    }

    @Override
    public RestResponseVO delById(Integer id) {
        if (id == null) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }
        int effect = tagMapper.deleteByPrimaryKey(id);
        return effect > 0 ? RestResponseVO.createBySuccessMessage(StringConst.DEL_SUCCESS)
                : RestResponseVO.createByErrorMessage(StringConst.DEL_FAIL);
    }

    @Override
    public RestResponseVO updateById(Tag tag) {
        if (tag == null) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }
        int effect = tagMapper.updateByPrimaryKeySelective(tag);
        return effect > 0 ? RestResponseVO.createBySuccessMessage(StringConst.UPDATE_SUCCESS)
                : RestResponseVO.createByErrorMessage(StringConst.UPDATE_FAIL);
    }

    @Override
    public RestResponseVO getById(Integer tagId) {
        if (tagId == null) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }
        return RestResponseVO.createBySuccess(tagMapper.selectByPrimaryKey(tagId));
    }

    @Override
    public RestResponseVO list2Page(Integer pageNum, Integer pageSize, String keyword) {
        PageHelper.startPage(pageNum, pageSize, true);
        List<TagVO> tagList = tagMapper.list2Page(keyword);
        PageInfo<TagVO> pageInfo = new PageInfo<>(tagList);
        pageInfo.getList().stream().forEach( tagVO -> {
            if (tagVO.getId() != null) {
                Tag parentTag = tagMapper.selectByPrimaryKey(tagVO.getParentId());
                tagVO.setParentTag(parentTag);
            }
        });
        return RestResponseVO.createBySuccess(pageInfo);
    }

    @Override
    public RestResponseVO<List<Tag>> listAll() {
        return RestResponseVO.createBySuccess(tagMapper.listAll());
    }
}
