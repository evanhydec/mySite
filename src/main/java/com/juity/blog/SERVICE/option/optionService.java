package com.juity.blog.SERVICE.option;

import com.juity.blog.POJO.option;

import java.util.List;
import java.util.Map;

public interface optionService {
    option getOptionByName(String name);
    List<option> getOptions();
    void saveOptions(Map<String, String> options);
    void updateOption(String name,String value);
}
