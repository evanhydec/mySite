package com.juity.blog.POJO;

public class relationship {
    //文章主键编号
    private Integer cid;
    //项目编号
    private Integer mid;

    public relationship() {
    }

    public relationship(int cid, int mid) {
        this.cid = cid;
        this.mid = mid;
    }

    @Override
    public String toString() {
        return "relationship{" +
                "cid=" + cid +
                ", mid=" + mid +
                '}';
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }
}
