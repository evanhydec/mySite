package com.example.site.CONTROLLER;


import com.example.site.CONSTANT.Types;
import com.example.site.CONSTANT.webConst;
import com.example.site.DTO.cond.contentCond;
import com.example.site.POJO.comment;
import com.example.site.POJO.content;
import com.example.site.SERVICE.comment.commentService;
import com.example.site.SERVICE.content.contentService;
import com.example.site.SERVICE.site.siteService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api("网站首页和关于")
@Controller
public class homeController extends baseController {
    @Autowired
    private contentService contentService;
    @Autowired
    private commentService commentService;


    @ApiOperation("blog介绍")
    @GetMapping(value = {"/about", "/about/index"})
    public String getAbout(HttpServletRequest request) {
        this.blogBaseData(request, null);//获取友链
        request.setAttribute("active", "about");
        return "site/about";
    }

    @ApiOperation("blog首页")
    @GetMapping(value = {"/blog", "/blog/index"})
    public String blogIndex(
            HttpServletRequest request,
            @ApiParam(name = "limit", value = "页数", required = false)
            @RequestParam(name = "limit", required = false, defaultValue = "11")
            int limit
    ) {
        return this.blogIndex(request, 1, limit);
    }

    @ApiOperation("blog首页-分页")
    @GetMapping("/blog/page/{p}")
    public String blogIndex(
            HttpServletRequest request,
            @PathVariable("p")
            Integer p,
            @RequestParam(value = "limit", required = false, defaultValue = "11")
            int limit
    ) {
        p = p < 0 || p > webConst.MAX_PAGE ? 1 : p;
        contentCond contentCond = new contentCond();
        contentCond.setType(Types.ARTICLE.getType());
        PageInfo<content> articles = contentService.getArticlesByCond(contentCond, p, limit);
        request.setAttribute("articles", articles);//文章列表
        request.setAttribute("type", "articles");
        request.setAttribute("active", "blog");
        return "site/blog";
    }


    @ApiOperation("文章内容页")
    @GetMapping(value = "/blog/article/{cid}")
    public String post(
            @ApiParam(name = "cid", value = "文章主键", required = true)
            @PathVariable("cid")
            Integer cid,
            HttpServletRequest request
    ) {
        content article = contentService.getArticleById(cid);
        request.setAttribute("article", article);
        contentCond contentCond = new contentCond();
        contentCond.setType(Types.ARTICLE.getType());
        this.updateArticleHit(article.getCid(), article.getHits());
        List<comment> commentsPaginator = commentService.getCommentsById(cid);
        request.setAttribute("comments", commentsPaginator);
        request.setAttribute("active", "blog");
        return "site/blog-details";

    }

    private void updateArticleHit(Integer cid, Integer chits) {
        Integer hits = cache.hget("article", "hits");
        if (chits == null) {
            chits = 0;
        }
        hits = null == hits ? 1 : hits + 1;
        if (hits >= webConst.HIT_EXCEED) {
            content temp = new content();
            temp.setCid(cid);
            temp.setHits(chits + hits);
            contentService.updateContentByCid(temp);
            cache.hset("article", "hits", 1);
        } else {
            cache.hset("article", "hits", hits);
        }
    }


}
