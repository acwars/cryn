package com.onlinejudge.cryn.common;

public final class TokenConst {

    public interface TokenExpireTime {

        Integer REGISTER = 3600 * 24;

        Integer REST_PASSWORD = 3600 * 24;
    }

    public interface TokenKeyPrefix {
        /**
         * 注册
         */
        String REGISTER = "TOKEN_REGISTER_";

        /**
         * 重置密码
         */
        String REST_PASSWORD = "TOKEN_REST_PASSWORD_";

    }

    public interface SessionKey {
        /**
         * 图形验证码
         */
        String IMAGE_CODE = "SESSION_KEY_IMAGE_CODE";


    }

}
