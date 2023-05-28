package com.juity.blog.CONTROLLER.admin;


import com.juity.blog.CONSTANT.Types;
import com.juity.blog.CONSTANT.webConst;
import com.juity.blog.DTO.metaDto;
import com.juity.blog.EXCEPTION.BusinessException;
import com.juity.blog.SERVICE.meta.metaService;
import com.juity.blog.utils.APIResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/admin/category")
public class categoryController {
    private static final Logger LOGGER = LoggerFactory.getLogger(categoryController.class);

    @Autowired
    private metaService metaService;

    @GetMapping("")
    public String index(HttpServletRequest request){
        List<metaDto> categories = metaService.getMetaList(Types.CATEGORY.getType(), webConst.MAX_POSTS);
        List<metaDto> tags = metaService.getMetaList(Types.TAG.getType(), webConst.MAX_POSTS);
        request.setAttribute("categories",categories);
        request.setAttribute("tags",tags);
        return "admin/category";
    }

    @PostMapping(value = "/save")
    @ResponseBody
    public APIResponse addCategory(
            @RequestParam(name = "cname")
            String cname,
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

    @PostMapping(value = "/delete")
    @ResponseBody
    public APIResponse delete(
            @RequestParam(name = "mid")
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
