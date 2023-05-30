package com.juity.blog.POJO;

import com.juity.blog.ANNOTATION.MongoCond;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.lang.reflect.Field;

public class MongoWrapper extends Query {
    private Query innerQuery;
    private Update innerUpdate;
    private final Object cond;
    public MongoWrapper(Object cond) {
        this.cond = cond;
    }

    public MongoWrapper addCond(String field, Object value, int type) {
        if (value == null) {
            return this;
        }
        switch (type) {
            case 1:
                // 等值查询
                innerQuery.addCriteria(Criteria.where(field).is(value));
                break;
            case 2:
                // 模糊匹配
                innerQuery.addCriteria(Criteria.where(field).regex(value.toString()));
                break;
            default:
                break;
        }
        return this;
    }

    public Query buildQuery() throws IllegalAccessException {
        innerQuery = new Query();
        Field[] fields = cond.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(MongoCond.class)) {
                if (!field.isAnnotationPresent(org.springframework.data.mongodb.core.mapping.Field.class)) {
                    throw new RuntimeException("there is no Field annotation for current field");
                }
                String mongoField = field.getAnnotation(org.springframework.data.mongodb.core.mapping.Field.class).value();
                field.setAccessible(true);
                addCond(mongoField, field.get(cond), field.getDeclaredAnnotation(MongoCond.class).value());
            }
        }
        return innerQuery;
    }

    public Update buildUpdate() throws IllegalAccessException {
        innerUpdate = new Update();
        Field[] fields = cond.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(org.springframework.data.mongodb.core.mapping.Field.class)) {
                String mongoField = field.getAnnotation(org.springframework.data.mongodb.core.mapping.Field.class).value();
                field.setAccessible(true);
                if (field.get(cond) != null) {
                    innerUpdate.set(mongoField, field.get(cond));
                }
            }
        }
        return innerUpdate;
    }

    public enum typeEnum {
        EQUAL(1, "等值查询"),
        MATCH(2, "模糊匹配");
        private Integer code;
        private String name;

        typeEnum(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
