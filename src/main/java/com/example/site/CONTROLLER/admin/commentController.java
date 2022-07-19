package com.example.site.CONTROLLER.admin;

import com.example.site.CONSTANT.ErrorConstant;
import com.example.site.CONTROLLER.baseController;
import com.example.site.DTO.cond.commentCond;
import com.example.site.EXCEPTION.BusinessException;
import com.example.site.POJO.comment;
import com.example.site.POJO.user;
import com.example.site.SERVICE.comment.commentService;
import com.example.site.utils.APIResponse;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Api("评论管理")
@RequestMapping("/admin/comments")
@Controller
public class commentController extends baseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(commentController.class);


    @Autowired
    private commentService commentService;

    @ApiOperation("进入评论列表页")
    @GetMapping(value = "")
    public String index(
            @ApiParam(name = "page", value = "页数", required = false)
            @RequestParam(name = "page", required = false, defaultValue = "1")
                    int page,
            @ApiParam(name = "limit", value = "每页条数", required = false)
            @RequestParam(name = "limit", required = false, defaultValue = "15")
                    int limit,
            HttpServletRequest request
    ){
        user user = this.user(request);

        PageInfo<comment> comments = commentService.getCommentsByCond(new commentCond(), page, limit);
        request.setAttribute("comments", comments);
        return "admin/comment_list";
    }

    @ApiOperation("删除一条评论")
    @PostMapping(value = "/delete")
    @ResponseBody
    public APIResponse deleteComment(
            @ApiParam(name = "coid", value = "评论编号", required = true)
            @RequestParam(name = "coid", required = true)
                    Integer coid
    ){

        try {
            comment comment = commentService.getCommentById(coid);
            if (null == comment)
                throw BusinessException.withErrorCode(ErrorConstant.Comment.COMMENT_NOT_EXIST);

            commentService.deleteComment(coid);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            return APIResponse.fail(e.getMessage());
        }
        return APIResponse.success();
    }

    @ApiOperation("更改评论状态")
    @PostMapping(value = "/status")
    @ResponseBody
    public APIResponse changeStatus(
            @ApiParam(name = "coid", value = "评论主键", required = true)
            @RequestParam(name = "coid", required = true)
                    Integer coid,
            @ApiParam(name = "status", value = "状态", required = true)
            @RequestParam(name = "status", required = true)
                    String status
    ){
        try {
            comment comment = commentService.getCommentById(coid);
            if (null != comment){
                commentService.updateCommentStatus(coid, status);
            }else{
                return APIResponse.fail("删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            return APIResponse.fail(e.getMessage());
        }
        return APIResponse.success();
    }
}
