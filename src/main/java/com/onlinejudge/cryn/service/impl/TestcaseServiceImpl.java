package com.onlinejudge.cryn.service.impl;

import com.onlinejudge.cryn.common.RestResponseEnum;
import com.onlinejudge.cryn.response.RestResponseVO;
import com.onlinejudge.cryn.service.TestcaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TestcaseServiceImpl implements TestcaseService {


    /**
     * todo 超链接
     * @param problemId
     * @return
     */
    @Override
    public RestResponseVO listTestcaseByProblemId(Integer problemId) {
        if (problemId == null) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.INVALID_REQUEST);
        }
        return null;
    }



}
