package com.juity.blog.CONTROLLER.admin;


import com.juity.blog.CONSTANT.LogActions;
import com.juity.blog.CONSTANT.webConst;
import com.juity.blog.CONTROLLER.baseController;
import com.juity.blog.POJO.option;
import com.juity.blog.SERVICE.log.logService;
import com.juity.blog.SERVICE.option.optionService;
import com.juity.blog.utils.APIResponse;
import com.juity.blog.utils.GsonUtils;
import com.juity.blog.utils.IPKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/setting")
public class settingController extends baseController {
    @Autowired
    private optionService optionService;
    @Autowired
    private logService logService;

    @GetMapping("")
    public String toSetting(HttpServletRequest request){
        List<option> optionsList = optionService.getOptions();
        Map<String, String> options = new HashMap<>();
        optionsList.forEach((option) -> {
            options.put(option.getName(), option.getValue());
        });
        request.setAttribute("options", options);
        return "admin/setting";
    }

    @PostMapping("")
    @ResponseBody
    public APIResponse save(HttpServletRequest request){
        try {
            Map<String, String[]> parameterMap = request.getParameterMap();
            Map<String, String> queries = new HashMap<>();
            parameterMap.forEach((key, value) -> queries.put(key, join(value)));
            optionService.saveOptions(queries);
            String ip = IPKit.getIpAddrByRequest(request);
            logService.addLog(LogActions.SYS_SETTING.getAction(), GsonUtils.toJsonString(queries), ip, this.user(request).getUid());

            //刷新设置
            List<option> options = optionService.getOptions();
            if (!CollectionUtils.isEmpty(options)) {
                HashMap<String, String> query = new HashMap<>();
                options.forEach(option -> query.put(option.getName(), option.getValue()));
                webConst.initConfig = query;
            }
            return APIResponse.success();
        } catch (Exception e) {
            return APIResponse.fail(e.getMessage());
        }
    }

}
