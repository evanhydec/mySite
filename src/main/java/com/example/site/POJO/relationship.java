package com.example.site.POJO;

public class relationship {
    //文章主键编号
    private int cid;
    //项目编号
    private int mid;

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

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }
}
