package com.example.site.CONTROLLER.admin;


import com.example.site.CONSTANT.Types;
import com.example.site.CONSTANT.webConst;
import com.example.site.DTO.metaDto;
import com.example.site.EXCEPTION.BusinessException;
import com.example.site.SERVICE.meta.metaService;
import com.example.site.utils.APIResponse;
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

@Api("分类")
@Controller
@RequestMapping("/admin/category")
public class categoryController {
    private static final Logger LOGGER = LoggerFactory.getLogger(categoryController.class);

    @Autowired
    private metaService metaService;

    @ApiOperation("首页")
    @GetMapping("")
    public String index(HttpServletRequest request){
        List<metaDto> categories = metaService.getMetaList(Types.CATEGORY.getType(), null, webConst.MAX_POSTS);
        List<metaDto> tags = metaService.getMetaList(Types.TAG.getType(), null, webConst.MAX_POSTS);
        request.setAttribute("categories",categories);
        request.setAttribute("tags",tags);
        return "admin/category";
    }

    @ApiOperation("保存分类")
    @PostMapping(value = "/save")
    @ResponseBody
    public APIResponse addCategory(
            @ApiParam(name = "cname", value = "分类名", required = true)
            @RequestParam(name = "cname", required = true)
                    String cname,
            @ApiParam(name = "mid", value = "meta编号", required = false)
            @RequestParam(name = "mid", required = false)
                    Integer mid
    ){
        try {
            metaService.saveMeta(Types.CATEGORY.getType(),cname,mid);

        } catch (Exception e) {
            e.printStackTrace();
            String msg = "分类保存失败";
            if (e instanceof BusinessException){
                BusinessException ex = (BusinessException) e;
                msg = ex.getErrorCode();
            }
            LOGGER.error(msg, e);

            return APIResponse.fail(msg);
        }
        return APIResponse.success();
    }

    @ApiOperation("删除分类")
    @PostMapping(value = "/delete")
    @ResponseBody
    public APIResponse delete(
            @ApiParam(name = "mid", value = "主键", required = true)
            @RequestParam(name = "mid", required = true)
                    Integer mid
    ){
        try {
            metaService.delMetaById(mid);

        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            return APIResponse.fail(e.getMessage());
        }
        return  APIResponse.success();
    }
}
