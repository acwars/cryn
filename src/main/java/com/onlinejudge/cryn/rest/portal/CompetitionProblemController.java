package com.onlinejudge.cryn.rest.portal;

import com.onlinejudge.cryn.response.CompetitionProblemVO;
import com.onlinejudge.cryn.response.RestResponseVO;
import com.onlinejudge.cryn.service.CompetitionProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequestMapping("/competitionProblem")
@Controller
public class CompetitionProblemController {

    @Autowired
    private CompetitionProblemService competitionProblemService;

    @RequestMapping("/listVOByCompId")
    @ResponseBody
    public RestResponseVO<List<CompetitionProblemVO>> listVOByCompId(Integer compId){
        return competitionProblemService.listVOByCompetitionId(compId);
    }


}
