package com.onlinejudge.cryn.rest.backend;

import com.github.pagehelper.PageInfo;
import com.onlinejudge.cryn.common.RestResponseEnum;
import com.onlinejudge.cryn.entity.Blog;
import com.onlinejudge.cryn.entity.BlogCategory;
import com.onlinejudge.cryn.entity.User;
import com.onlinejudge.cryn.response.RestResponseVO;
import com.onlinejudge.cryn.service.BlogCategoryService;
import com.onlinejudge.cryn.service.BlogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller("backendBlogController")
@RequestMapping("/backend/blog")
@Slf4j
public class BlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private BlogCategoryService blogCategoryService;

    /**
     * 跳转到博客列表页面
     *
     * @return
     */
    @RequestMapping("/blogListPage")
    public String userListPage(HttpServletRequest request) {
        request.setAttribute("blogManageActive",true);
        request.setAttribute("blogActive",true);
        return "backend/blog/blog-list";
    }

    /**
     * 跳转到编辑页面
     *
     * @return
     */
    @RequestMapping("/blogEditPage")
    public String blogEditPage(HttpServletRequest request,Integer blogId) {
        Blog blog = new Blog();
        if (blogId != null) {
            blog = blogService.getById(blogId).getData();

        }
        RestResponseVO<List<BlogCategory>> blogCategoryResponse = blogCategoryService.listAll();
        List<BlogCategory> blogCategoryList = blogCategoryResponse.getData();

        //setData
        request.setAttribute("blogCategoryList", blogCategoryList);
        request.setAttribute("blog",blog);
        request.setAttribute("blogManageActive",true);
        request.setAttribute("blogEditActive",true);
        return "backend/blog/blog-edit";
    }




    /**
     * 获取博客到页面
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param keyword
     * @param bcId
     * @return
     */
    @RequestMapping("/list2Page")
    @ResponseBody
    public RestResponseVO<PageInfo> listBlog(@RequestParam(defaultValue = "1")Integer pageNum,
                                             @RequestParam(defaultValue = "10")Integer pageSize,
                                             @RequestParam(defaultValue = "-1") Integer sort,
                                             @RequestParam(defaultValue = "") String keyword,
                                             @RequestParam(defaultValue = "-1") Integer bcId){

        return blogService.listBlogVO2Page(pageNum,pageSize,sort,keyword,bcId);
    }

    /**
     * save
     * @param blog
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    public RestResponseVO save(Blog blog,
                               @AuthenticationPrincipal UserDetails userDetails){
        User user = (User) userDetails;
        if (user == null) {
            return RestResponseVO.createByErrorEnum(RestResponseEnum.UNAUTHORIZED);
        }
        blog.setUserId(user.getId());

        if(blog.getId() != null){
            return blogService.updateById(blog);
        }else {
            return blogService.insert(blog);
        }
    }

    /**
     * 删除
     * @param blogId
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public RestResponseVO delete(Integer blogId){
        return blogService.delById(blogId);
    }











}
