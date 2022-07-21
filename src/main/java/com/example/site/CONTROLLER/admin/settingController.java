package com.example.site.CONTROLLER.admin;


import com.example.site.CONSTANT.LogActions;
import com.example.site.CONSTANT.webConst;
import com.example.site.CONTROLLER.baseController;
import com.example.site.POJO.option;
import com.example.site.SERVICE.log.logService;
import com.example.site.SERVICE.option.optionService;
import com.example.site.utils.APIResponse;
import com.example.site.utils.GsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

@Api("系统设置")
@Controller
@RequestMapping("/admin/setting")
public class settingController extends baseController {
    @Autowired
    private optionService optionService;
    @Autowired
    private logService logService;

    @ApiOperation("进入设置页")
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

    @ApiOperation("保存设置")
    @PostMapping("")
    @ResponseBody
    public APIResponse save(HttpServletRequest request){
        try {
            Map<String, String[]> parameterMap = request.getParameterMap();
            Map<String, String> querys = new HashMap<>();
            parameterMap.forEach((key, value) -> {
                querys.put(key, join(value));
            });
            optionService.saveOptions(querys);
            //刷新设置
            List<option> options = optionService.getOptions();
            if(! CollectionUtils.isEmpty(options)){
                HashMap<String, String> map = new HashMap<>();
                for (option option : options) {
                    map.put(option.getName(),option.getValue());
                }
                webConst.initConfig = map;
            }
            logService.addLog(LogActions.SYS_SETTING.getAction(), GsonUtils.toJsonString(querys), request.getRemoteAddr(), this.user(request).getUid());
            return APIResponse.success();
        } catch (Exception e) {
            String msg = "保存设置失败";
            return APIResponse.fail(e.getMessage());
        }
    }

}
