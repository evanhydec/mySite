package com.juity.blog.CONTROLLER;


import com.juity.blog.CONSTANT.ErrorConstant;
import com.juity.blog.CONSTANT.Types;
import com.juity.blog.CONSTANT.webConst;
import com.juity.blog.DTO.cond.contentCond;
import com.juity.blog.EXCEPTION.BusinessException;
import com.juity.blog.POJO.comment;
import com.juity.blog.POJO.content;
import com.juity.blog.SERVICE.comment.commentService;
import com.juity.blog.SERVICE.content.contentService;
import com.juity.blog.utils.APIResponse;
import com.juity.blog.utils.IPKit;
import com.juity.blog.utils.PatternKit;
import com.juity.blog.utils.TaleUtils;
import com.github.pagehelper.PageInfo;
import com.vdurmont.emoji.EmojiParser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;

@Controller
public class homeController extends baseController {
    @Autowired
    private contentService contentService;
    @Autowired
    private commentService commentService;


    @GetMapping(value = {"/about", "/about/index"})
    public String getAbout(HttpServletRequest request) {
        //获取友链
        this.blogBaseData(request);

        request.setAttribute("active", "about");
        return "site/about";
    }

    @GetMapping(value = {"/blog", "/blog/index"})
    public String blogIndex(
            HttpServletRequest request,
            @RequestParam(name = "limit", required = false, defaultValue = "11")
            int limit
    ) {
        return this.blogIndex(request, 1, limit);
    }

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


    @GetMapping(value = "/blog/article/{cid}")
    public String detail(
            @PathVariable("cid")
            Integer cid,
            HttpServletRequest request
    ) {
        content article = contentService.getArticleById(cid);
        request.setAttribute("article", article);
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


    @PostMapping(value = "/blog/comment")
    @ResponseBody
    public APIResponse comment(
            HttpServletRequest request, HttpServletResponse response,
            @RequestParam(name = "cid") Integer cid,
            @RequestParam(name = "coid", required = false) Integer coid,
            @RequestParam(name = "author", required = false) String author,
            @RequestParam(name = "mail", required = false) String mail,
            @RequestParam(name = "url", required = false) String url,
            @RequestParam(name = "text") String text,
            @RequestParam(name = "_csrf_token") String _csrf_token
    ) {
        String ref = request.getHeader("Referer");
        //防止攻击
        if (StringUtils.isBlank(ref) || StringUtils.isBlank(_csrf_token)) {
            return APIResponse.fail("访问失败");
        }

        String token = cache.hget(Types.CSRF_TOKEN.getType(), _csrf_token);
        if (StringUtils.isBlank(token)) {
            return APIResponse.fail("访问失败");
        }

        if (null == cid || StringUtils.isBlank(text)) {
            return APIResponse.fail("请输入完整后评论");
        }

        if (StringUtils.isNotBlank(author) && author.length() > 50) {
            return APIResponse.fail("姓名过长");
        }

        if (StringUtils.isNotBlank(mail) && !TaleUtils.isEmail(mail)) {
            return APIResponse.fail("请输入正确的邮箱格式");
        }

        if (StringUtils.isNotBlank(url) && !PatternKit.isURL(url)) {
            return APIResponse.fail("请输入正确的URL格式");
        }

        if (text.length() > 200) {
            return APIResponse.fail("请输入200个字符以内的评论");
        }

        String val = IPKit.getIpAddrByRequest(request) + ":" + cid;
        Integer count = cache.hget(Types.COMMENTS_FREQUENCY.getType(), val);
        if (null != count && count > 0) {
            return APIResponse.fail("您发表评论太快了，请过会再试");
        }

        author = TaleUtils.cleanXSS(author);
        text = TaleUtils.cleanXSS(text);

        author = EmojiParser.parseToAliases(author);
        text = EmojiParser.parseToAliases(text);

        comment comments = new comment();
        comments.setAuthor(author);
        comments.setCid(cid);
        comments.setIp(request.getRemoteAddr());
        comments.setUrl(url);
        comments.setContent(text);
        comments.setMail(mail);
        comments.setParent(coid);
        try {
            commentService.addComment(comments);
            cookie("tale_remember_author", URLEncoder.encode(author, "UTF-8"), 7 * 24 * 60 * 60, response);
            cookie("tale_remember_mail", URLEncoder.encode(mail, "UTF-8"), 7 * 24 * 60 * 60, response);
            if (StringUtils.isNotBlank(url)) {
                cookie("tale_remember_url", URLEncoder.encode(url, "UTF-8"), 7 * 24 * 60 * 60, response);
            }
            // 设置对每个文章1分钟可以评论一次
            cache.hset(Types.COMMENTS_FREQUENCY.getType(), val, 1, 10);
            return APIResponse.success();
        } catch (Exception e) {
            throw BusinessException.withErrorCode(ErrorConstant.Comment.ADD_NEW_COMMENT_FAIL);
        }
    }

    private void cookie(String name, String value, int maxAge, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setSecure(false);
        response.addCookie(cookie);
    }

}
