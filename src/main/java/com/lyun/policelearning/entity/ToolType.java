package com.lyun.policelearning.entity;

import lombok.Data;

import java.util.List;

@Data
public class ToolType {
    private int id;
    private String name;
    private List<ToolType> children;
    private int pid;
}
