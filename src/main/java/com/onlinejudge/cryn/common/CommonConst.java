package com.onlinejudge.cryn.common;

public class CommonConst {

    public interface CompetitionStatus {
        /**
         * 未开始
         */
        Integer NOT_STARTED = 1;
        /**
         * 进行中
         */
        Integer PROCESSING = 2;
        /**
         * 已结束
         */
        Integer CLOSE = 3;

    }


    public interface ProblemUserStatus {
        /**
         * 尝试中
         */
        Integer TRYING = 1;
        /**
         * 通过
         */
        Integer PASSED = 2;
    }

}
