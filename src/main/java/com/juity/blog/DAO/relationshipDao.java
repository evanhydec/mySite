package com.juity.blog.DAO;

import com.juity.blog.POJO.relationship;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface relationshipDao {
    int addRelationship(relationship relationship);
    int delRelationshipById(@Param("cid")Integer cid,@Param("mid")Integer mid);
    int delRelationshipByCid(@Param("cid")Integer cid);
    int delRelationshipByMid(@Param("mid")Integer mid);
    int updateRelationship(relationship relationship);
    List<relationship> getRelationshipByCid(@Param("cid")Integer cid);
    List<relationship> getRelationshipByMid(@Param("mid")Integer mid);
    Long getCountById(@Param("cid")Integer cid,@Param("mid")Integer mid);
}
