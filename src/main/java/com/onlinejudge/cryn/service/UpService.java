package com.onlinejudge.cryn.service;

import com.onlinejudge.cryn.entity.Up;
import com.onlinejudge.cryn.response.RestResponseVO;
import com.onlinejudge.cryn.response.UpVO;

public interface UpService {
    RestResponseVO insert(Up up);

    RestResponseVO delById(Integer id);

    RestResponseVO updateById(Up up);

    RestResponseVO<UpVO> blogUp(Integer blogId, Integer userId);

    RestResponseVO<UpVO> blogCommentUp(Integer blogCommentId,Integer userId);

}
