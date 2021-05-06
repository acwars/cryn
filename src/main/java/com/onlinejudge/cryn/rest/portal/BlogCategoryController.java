package com.onlinejudge.cryn.rest.portal;

import com.onlinejudge.cryn.entity.BlogCategory;
import com.onlinejudge.cryn.response.RestResponseVO;
import com.onlinejudge.cryn.response.Select2VO;
import com.onlinejudge.cryn.service.BlogCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/blogCategory")
public class BlogCategoryController {

    @Autowired
    private BlogCategoryService blogCategoryService;


    /**
     * 返回所有博客分类给select2　
     * @return
     */
    @ResponseBody
    @RequestMapping("/listSelect2")
    public List<Select2VO> listSelect2() {
        RestResponseVO<List<BlogCategory>> responseVO = blogCategoryService.listAll();
        List<BlogCategory> data = responseVO.getData();
        List<Select2VO> select2VOList = new ArrayList<>();
        data.stream().forEach(blogCategory -> {
            Select2VO select2VO = new Select2VO();
            select2VO.setId(blogCategory.getId());
            select2VO.setText(blogCategory.getName());
            select2VOList.add(select2VO);
        });
        return select2VOList;
    }





}


