package com.example.site.CONTROLLER;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Api("首页")
@Controller
public class viewController {
    @ApiOperation("跳转到首页")
    @RequestMapping("")
    public String toAdmin(){
        return "admin/login";
    }
}
