package com.example.site.CONTROLLER.admin;

import com.example.site.CONSTANT.Types;
import com.example.site.CONSTANT.webConst;
import com.example.site.DTO.attachDto;
import com.example.site.SERVICE.attach.attachService;
import com.example.site.utils.Commons;
import com.example.site.utils.TaleUtils;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;


@Api("附件上传管理")
@Controller
@RequestMapping("/admin/attach")
public class attachController {
    public static final String CLASSPATH = TaleUtils.getUplodFilePath();
    private static final Logger LOGGER = LoggerFactory.getLogger(attachController.class);
    @Autowired
    private attachService attachService;

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
    ){
        PageInfo<attachDto> attaches = attachService.getAttaches(page, limit);
        request.setAttribute("attaches", attaches);
        request.setAttribute(Types.ATTACH_URL.getType(), Commons.site_option(Types.ATTACH_URL.getType(), Commons.site_url()));
        request.setAttribute("max_file_size", webConst.MAX_FILE_SIZE / 1024);
        return "admin/attach";
    }




}
