package com.onlinejudge.cryn.exception;

/**
 * 用户未找到
 */
public class UserNotFoundException extends ApplicationException {

    private static final long serialVersionUID = -6543240172880562733L;

    public UserNotFoundException(int status, String msg) {
        super(status,msg);
    }
}
