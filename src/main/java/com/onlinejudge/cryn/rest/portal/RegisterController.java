package com.onlinejudge.cryn.rest.portal;

import com.github.pagehelper.PageInfo;
import com.onlinejudge.cryn.response.RestResponseVO;
import com.onlinejudge.cryn.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    /**
     * 用户比赛排名
     * @param pageNum
     * @param pageSize
     * @param compId
     * @return
     */
    @RequestMapping("/listRegisterByCompId2Page")
    @ResponseBody
    public RestResponseVO<PageInfo> listRegisterByCompId2Page(@RequestParam(defaultValue = "1") Integer pageNum,
                                                              @RequestParam(defaultValue = "10") Integer pageSize,
                                                              Integer compId) {
        return registerService.listRegisterByCompId2Page(compId,pageNum,pageSize);
    }

}
