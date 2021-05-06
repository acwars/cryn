package com.onlinejudge.cryn.common;

public final class StringConst {

    public static final String ADD_SUCCESS = "添加成功";

    public static final String ADD_FAIL = "添加失败";

    public static final String DEL_SUCCESS = "删除成功";

    public static final String DEL_FAIL = "删除失败";

    public static final String UPDATE_SUCCESS = "更新成功";

    public static final String UPDATE_FAIL = "更新失败";

    public static String getRegisterEmailContent(String href) {

        return "<div style=\"POSITION: relative; MARGIN: 0px auto; WIDTH: 600px; OVERFLOW: hidden\">" +
                "    <div>" +
                "        <div style=\"DISPLAY: inline-block; FLOAT: right\">Online Judge-ACM/ICPC在线学习社区 <br><a href=\"http://localhost/\" rel=\"noopener\" target=\"_blank\">http://localhost:8081 </a>" +
                "        </div>" +
                "    </div>" +
                "    <hr>" +
                "    <div style=\"position:rerelative;z-index:2\">" +
                "        <h1>Online Judge - 账号激活 </h1>" +
                "        <p><b></b> 同学： </p>" +
                "        <p>欢迎你注册，一起探求算法的奥秘！</p>" +
                "        <p>打开下面的地址即可完成注册： </p>" +
                "        <p>" +
                "            <a href=" + href + " rel=\"noopener\" target=\"_blank\">" +href+
                "            </a>" +
                "        </p>" +
                "        <p>祝你编程愉快！ </p>" +
                "        <p>&nbsp;</p>" +
                "        <p>&nbsp;</p>" +
                "        <p>&nbsp;</p>" +
                "        <p>&nbsp;</p>" +
                "        <p>&nbsp;</p>" +
                "        <hr>" +
                "        <small>这篇邮件是由Online Judge自动发送，请不要回复这封邮件。</small>" +
                "    </div>" +
                "</div>";

    }

    public static String getForgetPasswordEmailContent(String href) {

        return "<div style=\"POSITION: relative; MARGIN: 0px auto; WIDTH: 600px; OVERFLOW: hidden\">" +
                "    <div>" +
                "        <div style=\"DISPLAY: inline-block; FLOAT: right\">Online Judge-ACM/ICPC在线学习社区 <br><a href=\"http://localhost/\" rel=\"noopener\" target=\"_blank\">http://localhost:8081 </a>" +
                "        </div>" +
                "    </div>" +
                "    <hr>" +
                "    <div style=\"position:rerelative;z-index:2\">" +
                "        <h1>Online Judge - 重设密码 </h1>" +
                "        <p><b></b> 同学： </p>" +
                "        <p>忘记了密码？不要紧</p>" +
                "        <p>打开下面的地址即可重设密码： </p>" +
                "        <p>" +
                "            <a href=" + href + " rel=\"noopener\" target=\"_blank\">" + href +
                "            </a>" +
                "        </p>" +
                "        <p>如果你没有申请过忘记密码，请忽略这封邮件。</p>" +
                "        <p>祝你编程愉快！ </p>" +
                "        <p>&nbsp;</p>" +
                "        <p>&nbsp;</p>" +
                "        <p>&nbsp;</p>" +
                "        <p>&nbsp;</p>" +
                "        <p>&nbsp;</p>" +
                "        <hr>" +
                "        <small>这篇邮件是由Online Judge自动发送，请不要回复这封邮件</small>" +
                "    </div>" +
                "</div>";

    }

}
