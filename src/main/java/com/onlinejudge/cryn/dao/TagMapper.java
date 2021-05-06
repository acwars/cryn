package com.onlinejudge.cryn.dao;

import com.onlinejudge.cryn.entity.Tag;
import com.onlinejudge.cryn.response.TagVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TagMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Tag record);

    int insertSelective(Tag record);

    Tag selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Tag record);

    int updateByPrimaryKey(Tag record);

    List<TagVO> listParentVOAll();

    List<TagVO> list2Page(@Param("keyword") String keyword);

    List<Tag> listAll();

    List<Tag> listChildByParentId(Integer parentId);

}