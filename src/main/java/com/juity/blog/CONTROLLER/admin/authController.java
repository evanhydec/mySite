package com.juity.blog.CONTROLLER.admin;

import com.juity.blog.CONSTANT.LogActions;
import com.juity.blog.CONSTANT.webConst;
import com.juity.blog.CONTROLLER.baseController;
import com.juity.blog.EXCEPTION.BusinessException;
import com.juity.blog.POJO.user;
import com.juity.blog.SERVICE.log.logService;
import com.juity.blog.SERVICE.user.userService;
import com.juity.blog.utils.APIResponse;
import com.juity.blog.utils.IPKit;
import com.juity.blog.utils.TaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping("/admin")
public class authController extends baseController {

    private static final Logger LOGGER = LogManager.getLogger(authController.class);

    @Autowired
    private userService userService;

    @Autowired
    private logService logService;

    @GetMapping(value = {"","/login"})
    public String login(){
        return "admin/login";
    }

    @PostMapping(value = "/login")
    @ResponseBody
    public APIResponse toLogin(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(name = "username")
                    String username,
            @RequestParam(name = "password")
                    String password,
            @RequestParam(name = "remeber_me", required = false)
                    String rememberMe
    ){

        String ip = IPKit.getIpAddrByRequest(request);
        Integer error_count = cache.hget("login_error_count",ip);

        try {

            user userInfo = userService.login(username, password);
            request.getSession().setAttribute(webConst.LOGIN_SESSION_KEY, userInfo);
            if (StringUtils.isNotBlank(rememberMe)) {
                TaleUtils.setCookie(response, userInfo.getUid());
            }
            logService.addLog(LogActions.LOGIN.getAction(), null, ip, userInfo.getUid());

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            error_count = null == error_count ? 1 : error_count + 1;
            if (error_count > 3) {
                return APIResponse.fail("您输入密码已经错误超过3次，请10分钟后尝试");
            }

            cache.hset("login_error_count", ip,error_count, 10 * 60);
            String msg = "登录失败";
            if (e instanceof BusinessException) {
                msg = e.getMessage();
            } else {
                LOGGER.error(msg, e);
            }
            return APIResponse.fail(msg);
        }
        return APIResponse.success();
    }

    @RequestMapping("/logout")
    public void logout(HttpSession session, HttpServletResponse response, HttpServletRequest request) {
        session.removeAttribute(webConst.LOGIN_SESSION_KEY);
        Cookie cookie = new Cookie(webConst.USER_IN_COOKIE, "");
        cookie.setValue(null);
        cookie.setMaxAge(0);// 立即销毁cookie
        cookie.setPath("/");
        response.addCookie(cookie);
        try {
            response.sendRedirect("/admin/login");
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("注销失败", e);
        }
    }
}
