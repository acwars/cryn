package com.onlinejudge.cryn.rest.backend;

import com.onlinejudge.cryn.entity.CompetitionProblem;
import com.onlinejudge.cryn.response.RestResponseVO;
import com.onlinejudge.cryn.service.CompetitionProblemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("backendCompetitionProblemController")
@RequestMapping("/backend/competitionProblem")
@Slf4j
public class CompetitionProblemController {

    @Autowired
    private CompetitionProblemService competitionProblemService;

    /**
     * 获取比赛题目
     *
     * @param compId
     * @return
     */
    @RequestMapping("/listByCompId")
    @ResponseBody
    public RestResponseVO listByCompId(@RequestParam(defaultValue = "-1") Integer compId) {
        return competitionProblemService.listVOByCompetitionId(compId);
    }

    /**
     * 增加-更新
     *
     * @param competitionProblem
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    public RestResponseVO save(CompetitionProblem competitionProblem) {

        if (competitionProblem.getId() != null) {
            return competitionProblemService.updateById(competitionProblem);
        } else {
            return competitionProblemService.insert(competitionProblem);
        }


    }

    /**
     * 删除
     * @param competitionProblemId
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public RestResponseVO delete(Integer competitionProblemId) {
        return competitionProblemService.delById(competitionProblemId);
    }


}
