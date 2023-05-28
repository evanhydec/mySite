package com.juity.blog.CONTROLLER.admin;

import com.juity.blog.CONSTANT.LogActions;
import com.juity.blog.CONSTANT.Types;
import com.juity.blog.CONTROLLER.baseController;
import com.juity.blog.DTO.cond.contentCond;
import com.juity.blog.DTO.cond.metaCond;
import com.juity.blog.EXCEPTION.BusinessException;
import com.juity.blog.POJO.content;
import com.juity.blog.POJO.meta;
import com.juity.blog.SERVICE.content.contentService;
import com.juity.blog.SERVICE.log.logService;
import com.juity.blog.SERVICE.meta.metaService;
import com.juity.blog.utils.APIResponse;
import com.github.pagehelper.PageInfo;
import com.juity.blog.utils.IPKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/admin/article")
@Transactional(rollbackFor = BusinessException.class)
public class articleController extends baseController {
    @Autowired
    private contentService contentService;
    @Autowired
    private logService logService;
    @Autowired
    private metaService metaService;

    @GetMapping(value = "")
    public String index(
            HttpServletRequest request,
            @RequestParam(name = "page", required = false, defaultValue = "1")
            int page,
            @RequestParam(name = "limit", required = false, defaultValue = "15")
            int limit
    ) {
        PageInfo<content> articles = contentService.getArticlesByCond(new contentCond(), page, limit);
        request.setAttribute("articles", articles);
        return "admin/article_list";
    }


    @GetMapping("/publish")
    public String toPublic(HttpServletRequest request) {
        metaCond metaCond = new metaCond();
        metaCond.setType(Types.CATEGORY.getType());
        List<meta> metas = metaService.getMetas(metaCond);
        request.setAttribute("categories", metas);
        return "admin/article_edit";
    }

    @PostMapping("/publish")
    @ResponseBody
    public APIResponse SaveArticle(
            @RequestParam(name = "title")
            String title,
            @RequestParam(name = "titlePic", required = false)
            String titlePic,
            @RequestParam(name = "slug", required = false)
            String slug,
            @RequestParam(name = "content")
            String content,
            @RequestParam(name = "type")
            String type,
            @RequestParam(name = "status")
            String status,
            @RequestParam(name = "tags", required = false)
            String tags,
            @RequestParam(name = "categories", required = false, defaultValue = "默认分类")
            String categories,
            @RequestParam(name = "allowComment")
            Boolean allowComment
    ) {
        content contentDomain = new content();
        contentDomain.setTitle(title);
        contentDomain.setTitlePic(titlePic);
        contentDomain.setSlug(slug);
        contentDomain.setContent(content);
        contentDomain.setType(type);
        contentDomain.setStatus(status);
        contentDomain.setTags(type.equals(Types.ARTICLE.getType()) ? tags : null);
        contentDomain.setCategories(type.equals(Types.ARTICLE.getType()) ? categories : null);
        contentDomain.setAllowComment(allowComment ? 1 : 0);
        contentService.addArticle(contentDomain);
        return APIResponse.success();
    }

    @PostMapping("/modify")
    @ResponseBody
    public APIResponse modify(
            @RequestParam(name = "cid")
            Integer cid,
            @RequestParam(name = "title")
            String title,
            @RequestParam(name = "titlePic", required = false)
            String titlePic,
            @RequestParam(name = "slug", required = false)
            String slug,
            @RequestParam(name = "content")
            String content,
            @RequestParam(name = "type")
            String type,
            @RequestParam(name = "status")
            String status,
            @RequestParam(name = "tags", required = false)
            String tags,
            @RequestParam(name = "categories", required = false, defaultValue = "默认分类")
            String categories,
            @RequestParam(name = "allowComment")
            Boolean allowComment
    ) {
        content contentDomain = new content();
        contentDomain.setCid(cid);
        contentDomain.setTitle(title);
        contentDomain.setTitlePic(titlePic);
        contentDomain.setSlug(slug);
        contentDomain.setContent(content);
        contentDomain.setType(type);
        contentDomain.setStatus(status);
        contentDomain.setTags(tags);
        contentDomain.setCategories(categories);
        contentDomain.setAllowComment(allowComment ? 1 : 0);
        contentService.updateArticle(contentDomain);
        return APIResponse.success();
    }

    @PostMapping("/delete")
    @ResponseBody
    public APIResponse delete(
            @RequestParam(name = "cid", required = true)
            Integer cid,
            HttpServletRequest request
    ) {
        String ip = IPKit.getIpAddrByRequest(request);
        contentService.delArticleById(cid);
        logService.addLog(LogActions.DEL_ARTICLE.getAction(), cid + "", ip, this.user(request).getUid());
        return APIResponse.success();
    }

    @GetMapping("/{cid}")
    public String edit(
            @PathVariable
            Integer cid,
            HttpServletRequest request
    ) {
        content content = contentService.getArticleById(cid);
        request.setAttribute("contents", content);
        metaCond metaCond = new metaCond();
        metaCond.setType(Types.CATEGORY.getType());
        List<meta> categories = metaService.getMetas(metaCond);
        request.setAttribute("categories", categories);
        request.setAttribute("active", "article");
        return "admin/article_edit";
    }
}
