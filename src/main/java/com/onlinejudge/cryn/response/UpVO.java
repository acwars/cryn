package com.onlinejudge.cryn.response;

public class UpVO {

    private Boolean status = false;

    private Integer count;

    public UpVO(Boolean status ,Integer count){
        this.status = status;
        this.count = count;
    }

    public UpVO(){

    }
    public Boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "UpVO{" +
                "status=" + status +
                ", count=" + count +
                '}';
    }

}
