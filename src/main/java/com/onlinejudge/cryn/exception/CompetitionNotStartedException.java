package com.onlinejudge.cryn.exception;

public class CompetitionNotStartedException extends ApplicationException{

    private static final long serialVersionUID = -997462919114053391L;

    public CompetitionNotStartedException(int status, String msg) {
        super(status, msg);
    }
}
