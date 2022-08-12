package com.lyun.policelearning.entity;


import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Dept extends TreeEntity {
    private String id;
    private String name;
    private Long parent;

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setParent(Long parent)
    {
        this.parent = parent;
    }

    public Long getParent()
    {
        return parent;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("name", getName())
                .append("parent", getParent())
                .toString();
    }
}
