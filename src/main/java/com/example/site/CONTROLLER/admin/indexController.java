package com.example.site.CONTROLLER.admin;

import com.example.site.CONSTANT.LogActions;
import com.example.site.CONSTANT.webConst;
import com.example.site.CONTROLLER.baseController;
import com.example.site.DTO.statisticDto;
import com.example.site.EXCEPTION.BusinessException;
import com.example.site.POJO.comment;
import com.example.site.POJO.content;
import com.example.site.POJO.log;
import com.example.site.POJO.user;
import com.example.site.SERVICE.log.logService;
import com.example.site.SERVICE.site.siteService;
import com.example.site.SERVICE.user.userService;
import com.example.site.utils.APIResponse;
import com.example.site.utils.GsonUtils;
import com.example.site.utils.TaleUtils;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Api("后台首页")
@Controller("adminIndexController")
@RequestMapping("/admin")
public class indexController extends baseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(indexController.class);

    @Autowired
    private siteService siteService;

    @Autowired
    private logService logService;

    @Autowired
    private userService userService;

    @ApiOperation("进入首页")
    @GetMapping("/index")
    public String index(HttpServletRequest request){
        LOGGER.info("Enter admin index method");
        List<comment> comments = siteService.getComments(5);
        List<content> contents = siteService.getNewArticles(5);
        statisticDto statistics = siteService.getStatistics();
        // 取最新的20条日志
        PageInfo<log> logs = logService.getLogs(1, 5);
        List<log> list = logs.getList();
        request.setAttribute("comments", comments);
        request.setAttribute("articles", contents);
        request.setAttribute("statistics", statistics);
        request.setAttribute("logs", list);
        LOGGER.info("Exit admin index method");
        return "admin/index";
    }

    @GetMapping("/profile")
    public String toMySetting() {
        return "admin/profile";
    }

    @PostMapping("/profile")
    @ResponseBody
    public APIResponse saveProfile(@RequestParam String screenName, @RequestParam String email, HttpServletRequest request, HttpSession session) {
        user user = this.user(request);
        if (StringUtils.isNotBlank(screenName) && StringUtils.isNotBlank(email)) {
            user temp = new user();
            temp.setUid(user.getUid());
            temp.setScreenName(screenName);
            temp.setEmail(email);
            userService.updateUserInfo(temp);
            logService.addLog(LogActions.UP_INFO.getAction(), GsonUtils.toJsonString(temp), request.getRemoteAddr(), user.getUid());

            //更新session中的数据
            user original= (user) session.getAttribute(webConst.LOGIN_SESSION_KEY);
            original.setScreenName(screenName);
            original.setEmail(email);
            session.setAttribute(webConst.LOGIN_SESSION_KEY,original);
        }
        return APIResponse.success();
    }

    @PostMapping(value = "/password")
    @ResponseBody
    public APIResponse upPwd(@RequestParam String oldPassword, @RequestParam String password, HttpServletRequest request,HttpSession session) {
        user user = this.user(request);
        if (StringUtils.isBlank(oldPassword) || StringUtils.isBlank(password)) {
            return APIResponse.fail("请确认信息输入完整");
        }

        if (!user.getPwd().equals(TaleUtils.MD5encode(user.getUsername() + oldPassword))) {
            return APIResponse.fail("旧密码错误");
        }
        if (password.length() < 6 || password.length() > 14) {
            return APIResponse.fail("请输入6-14位密码");
        }

        try {
            user temp = new user();
            temp.setUid(user.getUid());
            String pwd = TaleUtils.MD5encode(user.getUsername() + password);
            temp.setPwd(pwd);
            userService.updateUserInfo(temp);
            logService.addLog(LogActions.UP_PWD.getAction(), null, request.getRemoteAddr(), user.getUid());

            //更新session中的数据
            user original= (user)session.getAttribute(webConst.LOGIN_SESSION_KEY);
            original.setPwd(pwd);
            session.setAttribute(webConst.LOGIN_SESSION_KEY,original);
            return APIResponse.success();
        } catch (Exception e){
            String msg = "密码修改失败";
            if (e instanceof BusinessException) {
                msg = e.getMessage();
            } else {
                LOGGER.error(msg, e);
            }
            return APIResponse.fail(msg);
        }
    }
}
