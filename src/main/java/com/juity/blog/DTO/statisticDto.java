package com.juity.blog.DTO;

public class statisticDto {
    private Long articles;
    private Long comments;
    private Long links;
    private Long attaches;

    public Long getArticles() {
        return articles;
    }

    public void setArticles(Long articles) {
        this.articles = articles;
    }

    public Long getComments() {
        return comments;
    }

    public void setComments(Long comments) {
        this.comments = comments;
    }

    public Long getLinks() {
        return links;
    }

    public void setLinks(Long links) {
        this.links = links;
    }

    public Long getAttaches() {
        return attaches;
    }

    public void setAttaches(Long attaches) {
        this.attaches = attaches;
    }
}
