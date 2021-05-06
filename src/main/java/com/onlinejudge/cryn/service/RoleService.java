package com.onlinejudge.cryn.service;

import com.github.pagehelper.PageInfo;
import com.onlinejudge.cryn.entity.Role;
import com.onlinejudge.cryn.response.RestResponseVO;

public interface RoleService {

    RestResponseVO getById(Integer roleId);

    RestResponseVO insert(Role role);

    RestResponseVO delById(Integer id);

    RestResponseVO updateById(Role role);

    RestResponseVO<PageInfo> listRole2Page(Integer pageNum, Integer pageSize,String keyword);

    RestResponseVO listRole();

}
