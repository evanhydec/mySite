package com.example.site.CONTROLLER.admin;


import com.example.site.CONSTANT.ErrorConstant;
import com.example.site.CONSTANT.Types;
import com.example.site.CONTROLLER.baseController;
import com.example.site.DTO.cond.metaCond;
import com.example.site.EXCEPTION.BusinessException;
import com.example.site.POJO.meta;
import com.example.site.SERVICE.meta.metaService;
import com.example.site.utils.APIResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api("友链管理")
@Controller
@RequestMapping("/admin/links")
public class linksController extends baseController {

    @Autowired
    private metaService metaService;

    @GetMapping("")
    @ApiOperation("友链首页")
    public String index(HttpServletRequest request) {
        metaCond metaCond = new metaCond();
        metaCond.setType(Types.LINK.getType());
        List<meta> metas = metaService.getMetas(metaCond);
        request.setAttribute("links", metas);
        return "admin/links";
    }

    @ApiOperation("保存友链")
    @PostMapping("/save")
    @ResponseBody
    public APIResponse save(
            @ApiParam(name = "title", value = "标签", required = true)
            @RequestParam(name = "title", required = true)
                    String title,
            @ApiParam(name = "url", value = "链接", required = true)
            @RequestParam(name = "url", required = true)
                    String url,
            @ApiParam(name = "logo", value = "logo", required = false)
            @RequestParam(name = "logo", required = false)
                    String logo,
            @ApiParam(name = "mid", value = "meta编号", required = false)
            @RequestParam(name = "mid", required = false)
                    Integer mid,
            @ApiParam(name = "sort", value = "sort", required = false)
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

    @ApiOperation("删除友链")
    @PostMapping(value = "/delete")
    @ResponseBody
    public APIResponse delete(
            @ApiParam(name = "mid", value = "meta主键", required = true)
            @RequestParam(name = "mid", required = true)
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
