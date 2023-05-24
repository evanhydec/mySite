package com.juity.blog.CONTROLLER;

import com.juity.blog.CONSTANT.Types;
import com.juity.blog.CONSTANT.webConst;
import com.juity.blog.DTO.cond.contentCond;
import com.juity.blog.DTO.metaDto;
import com.juity.blog.POJO.user;
import com.juity.blog.utils.MapCache;
import com.juity.blog.utils.TaleUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public abstract class baseController {

    @Autowired
    private com.juity.blog.SERVICE.meta.metaService metaService;

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
