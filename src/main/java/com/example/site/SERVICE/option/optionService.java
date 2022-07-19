package com.example.site.SERVICE.option;

import com.example.site.POJO.option;

import java.util.List;
import java.util.Map;

public interface optionService {
    option getOptionByName(String name);
    List<option> getOptions();
    void saveOptions(Map<String, String> options);
    void updateOption(String name,String value);
}
