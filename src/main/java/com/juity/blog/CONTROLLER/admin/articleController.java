package com.juity.blog.CONTROLLER.admin;

import com.juity.blog.CONSTANT.LogActions;
import com.juity.blog.CONSTANT.Types;
import com.juity.blog.CONTROLLER.baseController;
import com.juity.blog.DTO.cond.contentCond;
import com.juity.blog.DTO.cond.metaCond;
import com.juity.blog.EXCEPTION.BusinessException;
import com.juity.blog.POJO.content;
import com.juity.blog.POJO.meta;
import com.juity.blog.utils.APIResponse;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api("文章管理")
@Controller
@RequestMapping("/admin/article")
@Transactional(rollbackFor = BusinessException.class)
public class articleController extends baseController {
    @Autowired
    private com.juity.blog.SERVICE.content.contentService contentService;
    @Autowired
    private com.juity.blog.SERVICE.log.logService logService;
    @Autowired
    private com.juity.blog.SERVICE.meta.metaService metaService;

    @ApiOperation("文章页")
    @GetMapping(value = "")
    public String index(
            HttpServletRequest request,
            @ApiParam(name = "page", value = "页数", required = false)
            @RequestParam(name = "page", required = false, defaultValue = "1")
                    int page,
            @ApiParam(name = "limit", value = "每页数量", required = false)
            @RequestParam(name = "limit", required = false, defaultValue = "15")
                    int limit
    ) {
        PageInfo<content> articles = contentService.getArticlesByCond(new contentCond(), page, limit);
        request.setAttribute("articles", articles);
        return "admin/article_list";
    }


    @ApiOperation("发布文章")
    @GetMapping("/publish")
    public String toPublic(HttpServletRequest request) {
        metaCond metaCond = new metaCond();
        metaCond.setType(Types.CATEGORY.getType());
        List<meta> metas = metaService.getMetas(metaCond);
        request.setAttribute("categories", metas);
        return "admin/article_edit";
    }

    @ApiOperation("保存文章")
    @PostMapping("/publish")
    @ResponseBody
    public APIResponse SaveArticle(
            HttpServletRequest request,
            @ApiParam(name = "title", value = "标题", required = true)
            @RequestParam(name = "title", required = true)
                    String title,
            @ApiParam(name = "titlePic", value = "标题图片", required = false)
            @RequestParam(name = "titlePic", required = false)
                    String titlePic,
            @ApiParam(name = "slug", value = "内容缩略名", required = false)
            @RequestParam(name = "slug", required = false)
                    String slug,
            @ApiParam(name = "content", value = "内容", required = true)
            @RequestParam(name = "content", required = true)
                    String content,
            @ApiParam(name = "type", value = "文章类型", required = true)
            @RequestParam(name = "type", required = true)
                    String type,
            @ApiParam(name = "status", value = "文章状态", required = true)
            @RequestParam(name = "status", required = true)
                    String status,
            @ApiParam(name = "tags", value = "标签", required = false)
            @RequestParam(name = "tags", required = false)
                    String tags,
            @ApiParam(name = "categories", value = "分类", required = false)
            @RequestParam(name = "categories", required = false, defaultValue = "默认分类")
                    String categories,
            @ApiParam(name = "allowComment", value = "是否允许评论", required = true)
            @RequestParam(name = "allowComment", required = true)
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
        contentDomain.setAllowComment(allowComment?1:0);
        contentService.addArticle(contentDomain);
        return APIResponse.success();
    }

    @ApiOperation("编辑草稿")
    @PostMapping("/modify")
    @ResponseBody
    public APIResponse SaveDraft(
            @ApiParam(name = "cid", value = "文章主键", required = true)
            @RequestParam(name = "cid", required = true)
                    Integer cid,
            @ApiParam(name = "title", value = "标题", required = true)
            @RequestParam(name = "title", required = true)
                    String title,
            @ApiParam(name = "titlePic", value = "标题图片", required = false)
            @RequestParam(name = "titlePic", required = false)
                    String titlePic,
            @ApiParam(name = "slug", value = "内容缩略名", required = false)
            @RequestParam(name = "slug", required = false)
                    String slug,
            @ApiParam(name = "content", value = "内容", required = true)
            @RequestParam(name = "content", required = true)
                    String content,
            @ApiParam(name = "type", value = "文章类型", required = true)
            @RequestParam(name = "type", required = true)
                    String type,
            @ApiParam(name = "status", value = "文章状态", required = true)
            @RequestParam(name = "status", required = true)
                    String status,
            @ApiParam(name = "tags", value = "标签", required = false)
            @RequestParam(name = "tags", required = false)
                    String tags,
            @ApiParam(name = "categories", value = "分类", required = false)
            @RequestParam(name = "categories", required = false, defaultValue = "默认分类")
                    String categories,
            @ApiParam(name = "allowComment", value = "是否允许评论", required = true)
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
        contentDomain.setAllowComment(allowComment?1:0);
        contentService.updateArticle(contentDomain);
        return APIResponse.success();
    }

    @ApiOperation("删除文章")
    @PostMapping("/delete")
    @ResponseBody
    public APIResponse delete(
            @ApiParam(name = "cid", value = "文章主键", required = true)
            @RequestParam(name = "cid", required = true)
                    Integer cid,
            HttpServletRequest request
    ) {
        contentService.delArticleById(cid);
        logService.addLog(LogActions.DEL_ARTICLE.getAction(), cid + "", request.getRemoteAddr(), this.user(request).getUid());
        return APIResponse.success();
    }

    @ApiOperation("重新编辑")
    @GetMapping("/{cid}")
    public String edit(
            @ApiParam(name = "cid", value = "文章编号", required = true)
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
