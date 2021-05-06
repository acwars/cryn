package com.onlinejudge.cryn.service;

import com.onlinejudge.cryn.response.RestResponseVO;
import com.onlinejudge.cryn.response.TestcaseVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.TreeSet;

public interface FileService {

    RestResponseVO uploadImageByMD(MultipartFile multipartFile,String guid,String username);

    RestResponseVO<String> uploadImage(MultipartFile multipartFile,String username);

    RestResponseVO<byte[]> get(String path);

    void getTestcaseInput(HttpServletResponse response,Integer problemId, Integer num);

    void getTestcaseOutput(HttpServletResponse response, Integer problemId, Integer num);

    RestResponseVO<String> deleteTestcase(Integer problemId);

    RestResponseVO<String> saveTestcase(Integer problemId,Integer num,String testcaseInput, String testcaseOutput);

    RestResponseVO<TreeSet<TestcaseVO>> listTestcaseVO(Integer problemId);

    RestResponseVO deleteTestcase(Integer problemId, Integer num);
}
