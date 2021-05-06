package com.onlinejudge.cryn.exception;

/**
 * 用户未找到
 */
public class ProblemNotFoundException extends ApplicationException {

    private static final long serialVersionUID = 3919904865407492637L;

    public ProblemNotFoundException(int status, String msg) {
        super(status,msg);
    }
}
