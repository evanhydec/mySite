package com.example.site.POJO;

public class content {
    private int cid;
    private String title;
    private String titlePic;
    private String slug;
    private int created;
    private int modified;
    private String content;
    private int authorId;
    private String type;
    private String status;
    private String tags;
    private String categories;
    private int hits;
    private int commentsNum;
    private int allowComment;
    private int allowPing;
    private int allowFeed;

    @Override
    public String toString() {
        return "content{" +
                "cid=" + cid +
                ", title='" + title + '\'' +
                ", titlePic='" + titlePic + '\'' +
                ", slug='" + slug + '\'' +
                ", created=" + created +
                ", modified=" + modified +
                ", content='" + content + '\'' +
                ", authorId=" + authorId +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", tags='" + tags + '\'' +
                ", categories='" + categories + '\'' +
                ", hits=" + hits +
                ", commentsNum=" + commentsNum +
                ", allowComment=" + allowComment +
                ", allowPing=" + allowPing +
                ", allowFeed=" + allowFeed +
                '}';
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitlePic() {
        return titlePic;
    }

    public void setTitlePic(String titlePic) {
        this.titlePic = titlePic;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public int getModified() {
        return modified;
    }

    public void setModified(int modified) {
        this.modified = modified;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public int getCommentsNum() {
        return commentsNum;
    }

    public void setCommentsNum(int commentsNum) {
        this.commentsNum = commentsNum;
    }

    public int getAllowComment() {
        return allowComment;
    }

    public void setAllowComment(int allowComment) {
        this.allowComment = allowComment;
    }

    public int getAllowPing() {
        return allowPing;
    }

    public void setAllowPing(int allowPing) {
        this.allowPing = allowPing;
    }

    public int getAllowFeed() {
        return allowFeed;
    }

    public void setAllowFeed(int allowFeed) {
        this.allowFeed = allowFeed;
    }
}
