package com.onlinejudge.cryn.service;

import com.onlinejudge.cryn.response.RestResponseVO;

public interface TestcaseService {

     RestResponseVO listTestcaseByProblemId(Integer problemId);

}
