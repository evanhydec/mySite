package com.juity.blog.INTERCEPTOR;

import com.juity.blog.CONSTANT.Types;
import com.juity.blog.CONSTANT.webConst;
import com.juity.blog.POJO.option;
import com.juity.blog.POJO.user;
import com.juity.blog.SERVICE.option.optionService;
import com.juity.blog.SERVICE.user.userService;
import com.juity.blog.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class baseInterceptor implements HandlerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(baseInterceptor.class);
    private static final String USER_AGENT = "user-agent";

    @Autowired
    private optionService optionService;
    @Autowired
    private userService userService;

    private Commons commons = new Commons();
    private AdminCommons adminCommons = new AdminCommons();

    private MapCache cache = MapCache.single();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        LOGGER.info("UserAgent: {}", request.getHeader(USER_AGENT));
        LOGGER.info("用户访问地址: {}, 来路地址: {}", uri, IPKit.getIpAddrByRequest(request));


        //请求拦截
        user user = TaleUtils.getLoginUser(request);
        if (null == user) {
            Integer uid = TaleUtils.getCookieUid(request);
            if (null != uid) {
                //这里还是有安全隐患,cookie是可以伪造的
                user = userService.getUserInfoById(uid);
                request.getSession().setAttribute(webConst.LOGIN_SESSION_KEY, user);
            }
        }
        if (uri.startsWith("/admin") && !uri.startsWith("/admin/login") && null == user
                && !uri.startsWith("/admin/css") && !uri.startsWith("/admin/images")
                && !uri.startsWith("/admin/js") && !uri.startsWith("/admin/plugins")
                && !uri.startsWith("/admin/editormd")) {
            response.sendRedirect(request.getContextPath() + "/admin/login");
            return false;
        }
        //get请求的token
        if (request.getMethod().equals("GET")) {
            String csrf_token = UUID.UU64();
            // 默认存储30分钟
            cache.hset(Types.CSRF_TOKEN.getType(), csrf_token, uri, 30 * 60);
            request.setAttribute("_csrf_token", csrf_token);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        option ov = optionService.getOptionByName("site_record");
        httpServletRequest.setAttribute("commons", commons);
        httpServletRequest.setAttribute("option", ov);
        httpServletRequest.setAttribute("adminCommons", adminCommons);
        initSiteConfig(httpServletRequest);
    }

    private void initSiteConfig(HttpServletRequest request) {
        if (webConst.initConfig.isEmpty()){
            List<option> options = optionService.getOptions();
            Map<String, String> query = new HashMap<>();
            options.forEach(option -> {
                query.put(option.getName(), option.getValue());
            });
            webConst.initConfig = query;
        }
    }
}
