package com.juity.blog.CONTROLLER.admin;


import com.juity.blog.CONSTANT.ErrorConstant;
import com.juity.blog.CONSTANT.Types;
import com.juity.blog.CONTROLLER.baseController;
import com.juity.blog.DTO.cond.metaCond;
import com.juity.blog.EXCEPTION.BusinessException;
import com.juity.blog.POJO.meta;
import com.juity.blog.SERVICE.meta.metaService;
import com.juity.blog.utils.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/admin/links")
public class linksController extends baseController {

    @Autowired
    private metaService metaService;

    @GetMapping("")
    public String index(HttpServletRequest request) {
        blogBaseData(request);
        return "admin/links";
    }

    @PostMapping("/save")
    @ResponseBody
    public APIResponse save(
            @RequestParam(name = "title")
                    String title,
            @RequestParam(name = "url")
                    String url,
            @RequestParam(name = "logo", required = false)
                    String logo,
            @RequestParam(name = "mid", required = false)
                    Integer mid,
            @RequestParam(name = "sort", required = false, defaultValue = "0")
                    int sort
    ){
        try {
            meta meta = new meta();
            meta.setName(title);
            meta.setSlug(url);
            meta.setDescription(logo);
            meta.setSort(sort);
            meta.setType(Types.LINK.getType());
            if (null != mid){
                meta.setMid(mid);
                metaService.updateMeta(meta);
            }else {
                metaService.addMeta(meta);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw BusinessException.withErrorCode(ErrorConstant.Meta.ADD_META_FAIL);
        }
        return APIResponse.success();
    }

    @PostMapping(value = "/delete")
    @ResponseBody
    public APIResponse delete(
            @RequestParam(name = "mid")
                    int mid
    ) {
        try {
            metaService.delMetaById(mid);
        } catch (Exception e) {
            e.printStackTrace();
            throw BusinessException.withErrorCode(ErrorConstant.Meta.ADD_META_FAIL);
        }
        return APIResponse.success();
    }


}
