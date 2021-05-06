package com.onlinejudge.cryn.service;

import com.onlinejudge.cryn.entity.Tag;
import com.onlinejudge.cryn.response.RestResponseVO;
import com.onlinejudge.cryn.response.TagVO;

import java.util.List;

/**
 * 题目tag
 */

public interface TagService {

     RestResponseVO<List<TagVO>> listParentVOAll();

     RestResponseVO insert(Tag tag);

     RestResponseVO delById(Integer id);

     RestResponseVO updateById(Tag tag);

    RestResponseVO getById(Integer tagId);

    RestResponseVO list2Page(Integer pageNum, Integer pageSize, String keyword);

    RestResponseVO<List<Tag>> listAll();
}
