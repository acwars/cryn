package com.onlinejudge.cryn.service;

import com.github.pagehelper.PageInfo;
import com.onlinejudge.cryn.entity.Register;
import com.onlinejudge.cryn.response.RestResponseVO;

public interface RegisterService {

    RestResponseVO insert(Register register);

    RestResponseVO delById(Integer id);

    RestResponseVO updateById(Register register);

    RestResponseVO registerCompetition(Integer userId,Integer compId,String password);

    RestResponseVO isRegisterCompetition(Integer userId, Integer compId);


    RestResponseVO<PageInfo> listRegisterByCompId2Page(Integer compId, Integer pageNum, Integer pageSize);



}
