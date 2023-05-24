package com.juity.blog.DTO;

import com.juity.blog.POJO.content;

import java.util.List;

public class archiveDto {
    private String date;
    private String count;
    private List<content> articles;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<content> getArticles() {
        return articles;
    }

    public void setArticles(List<content> articles) {
        this.articles = articles;
    }
}
