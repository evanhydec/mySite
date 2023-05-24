package com.juity.blog.POJO;

public class option {
    private String name;
    private String value;

    public option(String name, String value) {
        this.name = name;
        this.value = value;
    }

    private String description;

    @Override
    public String toString() {
        return "option{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
