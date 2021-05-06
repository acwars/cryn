package com.onlinejudge.cryn.common;


public enum RoleEnum {

    /**
     * 普通用户
     */
    USER(2,"ROLE_USER","USER"),

    /**
     * 　管理员
     */
    ADMIN(1,"ROLE_ADMIN","ADMIN");

    private Integer id;

    private String name;

    private String configName;

    RoleEnum(Integer id,String name,String configName) {
        this.id = id;
        this.name = name;
        this.configName = configName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }
}
