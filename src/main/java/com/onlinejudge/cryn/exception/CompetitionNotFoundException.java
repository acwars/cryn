package com.onlinejudge.cryn.exception;

public class CompetitionNotFoundException extends ApplicationException{

    private static final long serialVersionUID = -997462919114053391L;

    public CompetitionNotFoundException(int status, String msg) {
        super(status, msg);
    }
}
