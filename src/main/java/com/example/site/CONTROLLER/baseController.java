package com.example.site.CONTROLLER;

import com.example.site.CONSTANT.Types;
import com.example.site.CONSTANT.webConst;
import com.example.site.DTO.cond.contentCond;
import com.example.site.DTO.metaDto;
import com.example.site.POJO.user;
import com.example.site.SERVICE.content.contentService;
import com.example.site.SERVICE.meta.metaService;
import com.example.site.SERVICE.site.siteService;
import com.example.site.utils.MapCache;
import com.example.site.utils.TaleUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public abstract class baseController {

    @Autowired
    private metaService metaService;

    protected MapCache cache = MapCache.single();

    //给request添加title
    public baseController title(HttpServletRequest request, String title) {
        request.setAttribute("title", title);
        return this;
    }

    //获得meta信息
    public baseController blogBaseData(HttpServletRequest request, contentCond contentCond){
        List<metaDto> links = metaService.getMetaList(Types.LINK.getType(), null,webConst.MAX_POSTS);
        request.setAttribute("links", links);
        return this;
    }

    //获取当前user
    public user user(HttpServletRequest request) {
        return TaleUtils.getLoginUser(request);
    }

    //拼接arr数组为一串String
    public String join(String[] arr) {
        StringBuilder ret = new StringBuilder();
        String[] var3 = arr;
        int len = arr.length;

        for (int i = 0; i < len; ++i) {
            ret.append(',').append(var3[i]);
        }
        return ret.length() > 0 ? ret.substring(1) : ret.toString();
    }

}
