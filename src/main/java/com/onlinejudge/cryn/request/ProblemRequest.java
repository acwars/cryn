package com.onlinejudge.cryn.request;

import com.onlinejudge.cryn.response.TestcaseVO;
import lombok.Data;

import java.util.List;

@Data
public class ProblemRequest {

    private Integer id;

    private String name;

    private String content;

    private String htmlContent;

    private String inputDesc;

    private String outputDesc;

    private String testcaseInput;

    private String testcaseOutput;

    private Integer rating;

    private Long time;

    private Long memory;

    private Integer flag;

    private String tags;

    private List<TestcaseVO> testcaseList;

    private boolean settingUpdated = false;


}
