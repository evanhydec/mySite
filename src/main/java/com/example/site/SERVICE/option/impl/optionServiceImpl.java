package com.example.site.SERVICE.option.impl;

import com.example.site.CONSTANT.ErrorConstant;
import com.example.site.DAO.optionDao;
import com.example.site.EXCEPTION.BusinessException;
import com.example.site.POJO.option;
import com.example.site.SERVICE.option.optionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class optionServiceImpl implements optionService {

    @Autowired
    private optionDao optionDao;

    @Override
    @Cacheable(value = "optionCache", key = "'optionByName_' + #p0")
    public option getOptionByName(String name) {
        if(StringUtils.isBlank(name))
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        return optionDao.getOptionByName(name);
    }

    @Override
    public List<option> getOptions() {
        return optionDao.getOptions();
    }

    @Override
    public void saveOptions(Map<String, String> options) {
        if (null != options && !options.isEmpty()) {
            options.forEach(this::updateOption);
        }
    }

    @Override
    public void updateOption(String name,String value) {
        if (StringUtils.isBlank(name))
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        optionDao.updateOption(new option(name,value));
    }
}
