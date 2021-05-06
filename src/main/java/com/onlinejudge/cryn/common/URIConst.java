package com.onlinejudge.cryn.common;

public final class URIConst {

    public static final String[] VALIDATE_CODE_ARRAY_URI = new String[]{
            "/user/sendRegisterEmail", "/user/loginProcess",
            "/user/sendForgetEmail","/backend/user/loginProcess"
    };

    public static final String[] NOT_ALLOWED_URI = new String[]{
            "/backend/**"
    };
    public static final String[] ALLOWED_URI = new String[]{
            "/backend/user/loginPage",
    };
}
