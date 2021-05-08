package com.onlinejudge.cryn.rest.portal;

import com.github.pagehelper.PageInfo;
import com.onlinejudge.cryn.common.ExceptionStatusConst;
import com.onlinejudge.cryn.entity.User;
import com.onlinejudge.cryn.exception.ProblemNotFoundException;
import com.onlinejudge.cryn.response.ProblemDetailVO;
import com.onlinejudge.cryn.response.RestResponseVO;
import com.onlinejudge.cryn.response.TagVO;
import com.onlinejudge.cryn.service.ProblemService;
import com.onlinejudge.cryn.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/problem")
public class ProblemController {

    @Autowired
    private ProblemService problemService;

    @Autowired
    private TagService tagService;

    private final Integer SUGGEST_PROBLEM_ROW = 5;

    /**
     * 跳转到题目List页面
     *
     * @param request
     * @return
     */
    @RequestMapping("/problemListPage")
    public String problemListPage(HttpServletRequest request,
                                  @RequestParam(defaultValue = "") String keyword) {
        //题目标签
        List<TagVO> tagList = tagService.listParentVOAll().getData();

        //set data
        request.setAttribute("tagList", tagList);
        request.setAttribute("keyword", keyword);
        request.setAttribute("active2", true);
        return "portal/problem/problem-list";
    }


    /**
     * 题目列表
     *
     * @param userDetails
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param keyword
     * @param rating
     * @param tagIds
     * @return
     */
    @RequestMapping("/listProblem2Page")
    @ResponseBody
    public RestResponseVO<PageInfo> listProblem2Page(@AuthenticationPrincipal UserDetails userDetails,
                                                     @RequestParam(defaultValue = "1") Integer pageNum,
                                                     @RequestParam(defaultValue = "20") Integer pageSize,
                                                     @RequestParam(defaultValue = "-1", required = false) Integer sort,
                                                     @RequestParam(defaultValue = "", required = false) String keyword,
                                                     @RequestParam(defaultValue = "-1", required = false) Integer rating,
                                                     @RequestParam(defaultValue = "",required = false) String tagIds) {
        Integer userId = null;
        if (userDetails != null) {
            userId = ((User) userDetails).getId();
        }
        //公开的题目
        Integer flag = 0;
        return problemService.listProblemVOToPage(userId,flag, sort, keyword, rating, tagIds, pageNum, pageSize);
    }


    /**
     * 题目详情页面
     *
     * @param request
     * @param problemId
     * @return
     */
    @RequestMapping("/problemDetailPage")
    public String problemDetailPage(HttpServletRequest request, Integer problemId,Integer compId) {
        ProblemDetailVO detailVO = problemService.getDetailVOById(problemId).getData();
        if (detailVO == null) {
            throw new ProblemNotFoundException(ExceptionStatusConst.PROBLEM_NOT_FOUND_EXP, "未找到该题号的题目");
        }
        //set data
        request.setAttribute("problem", detailVO);
        request.setAttribute("compId", compId);
        request.setAttribute("active2", true);
        return "portal/problem/problem-detail";
    }

    /**
     * 推荐题目
     *
     * @param userDetails
     * @return
     */
    @RequestMapping("/suggestProblemList")
    @ResponseBody
    public RestResponseVO<List<ProblemDetailVO>> suggestProblemList(@AuthenticationPrincipal UserDetails userDetails) {
        Integer userId = null;
        if (userDetails != null) {
            userId = ((User) userDetails).getId();
        }
        return problemService.listSuggestProblem(userId, SUGGEST_PROBLEM_ROW);
    }

    /**
     * 随机选择一道题目
     *
     * @return
     */
    @RequestMapping("/randomProblem")
    public String randomProblem(HttpServletRequest request) {
        RestResponseVO<Integer> serverResponse = problemService.randomProblemId();
        if (serverResponse.isSuccess()) {
            return "redirect:/problem/problemDetailPage?problemId=" + serverResponse.getData();
        } else {
            //fixme
            return "500";
        }
    }

}
