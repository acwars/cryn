package com.onlinejudge.cryn.service;

import com.onlinejudge.cryn.entity.Sign;
import com.onlinejudge.cryn.response.RestResponseVO;

public interface SignService {

    RestResponseVO insert(Sign sign);

    RestResponseVO delById(Integer id);

    RestResponseVO updateById(Sign sign);
}
