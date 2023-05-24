package com.juity.blog.CONTROLLER.admin;

import com.juity.blog.CONSTANT.ErrorConstant;
import com.juity.blog.CONSTANT.Types;
import com.juity.blog.CONSTANT.webConst;
import com.juity.blog.DTO.attachDto;
import com.juity.blog.EXCEPTION.BusinessException;
import com.juity.blog.POJO.attach;
import com.juity.blog.POJO.user;
import com.juity.blog.utils.APIResponse;
import com.juity.blog.utils.Commons;
import com.juity.blog.utils.TaleUtils;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@Api("附件上传管理")
@Controller
@RequestMapping("/admin/attach")
public class attachController {
    public static final String CLASSPATH = TaleUtils.getUplodFilePath();
    private static final Logger LOGGER = LoggerFactory.getLogger(attachController.class);
    @Autowired
    private com.juity.blog.SERVICE.attach.attachService attachService;
    @Autowired
    private com.juity.blog.utils.qiniu qiniu;


    @ApiOperation("附件首页")
    @GetMapping("")
    public String index(
            @ApiParam(name = "page", value = "页数", required = false)
            @RequestParam(name = "page", required = false, defaultValue = "1")
            int page,
            @ApiParam(name = "limit", value = "条数", required = false)
            @RequestParam(name = "limit", required = false, defaultValue = "12")
            int limit,
            HttpServletRequest request
    ) {
        PageInfo<attachDto> attaches = attachService.getAttaches(page, limit);
        request.setAttribute("attaches", attaches);
        request.setAttribute(Types.ATTACH_URL.getType(), Commons.site_option(Types.ATTACH_URL.getType(), Commons.site_url()));
        request.setAttribute("max_file_size", webConst.MAX_FILE_SIZE / 1024);
        return "admin/attach";
    }

    @ApiOperation("markdown文件上传")
    @PostMapping("/uploadfile")
    public void fileUpLoadToTencentCloud(
            HttpServletRequest request,
            HttpServletResponse response,
            @ApiParam(name = "editormd-image-file", value = "文件数组", required = true)
            @RequestParam(name = "editormd-image-file", required = true)
            MultipartFile file
    ) {
        //文件上传
        try {
            request.setCharacterEncoding("utf-8");
            response.setHeader("Content-Type", "text/html");

            String fileName = TaleUtils.getFileKey(file.getOriginalFilename()).replaceFirst("/", "");

            qiniu.upload(file, fileName);
            attach attAch = new attach();
            HttpSession session = request.getSession();
            user sessionUser = (user) session.getAttribute(webConst.LOGIN_SESSION_KEY);
            attAch.setAuthorId(sessionUser.getUid());
            attAch.setType(TaleUtils.isImage(file.getInputStream()) ? Types.IMAGE.getType() : Types.FILE.getType());
            attAch.setName(fileName);
            String baseUrl = qiniu.QINIU_UPLOAD_SITE.endsWith("/") ? qiniu.QINIU_UPLOAD_SITE : qiniu.QINIU_UPLOAD_SITE + "/";
            attAch.setKey(baseUrl + fileName);
            attachService.addAttach(attAch);
            response.getWriter().write("{\"success\": 1, \"message\":\"上传成功\",\"url\":\"" + attAch.getKey() + "\"}");
        } catch (IOException e) {
            e.printStackTrace();
            try {
                response.getWriter().write("{\"success\":0}");
            } catch (IOException e1) {
                throw BusinessException.withErrorCode(ErrorConstant.Att.UPLOAD_FILE_FAIL)
                        .withErrorMessageArguments(e.getMessage());
            }
            throw BusinessException.withErrorCode(ErrorConstant.Att.UPLOAD_FILE_FAIL)
                    .withErrorMessageArguments(e.getMessage());
        }
    }

    @ApiOperation("多文件上传")
    @PostMapping(value = "upload")
    @ResponseBody
    public APIResponse<Void> uploadfilesUploadToCloud(
            HttpServletRequest request,
            HttpServletResponse response,
            @ApiParam(name = "file", value = "文件数组", required = true)
            @RequestParam(name = "file", required = true)
            MultipartFile[] files
    ) {
        //文件上传
        try {
            request.setCharacterEncoding("utf-8");
            response.setHeader("Content-Type", "text/html");

            for (MultipartFile file : files) {

                String fileName = TaleUtils.getFileKey(file.getOriginalFilename()).replaceFirst("/", "");

                qiniu.upload(file, fileName);
                attach attAch = new attach();
                HttpSession session = request.getSession();
                user sessionUser = (user) session.getAttribute(webConst.LOGIN_SESSION_KEY);
                attAch.setAuthorId(sessionUser.getUid());
                attAch.setType(TaleUtils.isImage(file.getInputStream()) ? Types.IMAGE.getType() : Types.FILE.getType());
                attAch.setName(fileName);
                String baseUrl = qiniu.QINIU_UPLOAD_SITE.endsWith("/") ? qiniu.QINIU_UPLOAD_SITE : qiniu.QINIU_UPLOAD_SITE + "/";
                attAch.setKey(baseUrl + fileName);
                attachService.addAttach(attAch);
            }
            return APIResponse.success();
        } catch (IOException e) {
            e.printStackTrace();
            throw BusinessException.withErrorCode(ErrorConstant.Att.UPLOAD_FILE_FAIL)
                    .withErrorMessageArguments(e.getMessage());
        }
    }

    @ApiOperation("删除文件")
    @PostMapping(value = "/delete")
    @ResponseBody
    public APIResponse deleteFileInfo(
            @ApiParam(name = "id", value = "文件主键", required = true)
            @RequestParam(name = "id", required = true)
            Integer id,
            HttpServletRequest request
    ) {
        try {
            attach attAch = attachService.getAttachById(id);
            if (null == attAch)
                throw BusinessException.withErrorCode(ErrorConstant.Att.DELETE_ATT_FAIL + ": 文件不存在");
            attachService.deleteAttach(id);
            qiniu.delete(attAch.getName());
            return APIResponse.success();
        } catch (Exception e) {
            e.printStackTrace();
            throw BusinessException.withErrorCode(e.getMessage());
        }
    }


}