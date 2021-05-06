package com.onlinejudge.cryn.exception;

public class CompetitionNotRegisterException extends ApplicationException{

    private static final long serialVersionUID = -6054403920194096087L;

    public CompetitionNotRegisterException(int status, String msg) {
        super(status, msg);
    }
}
