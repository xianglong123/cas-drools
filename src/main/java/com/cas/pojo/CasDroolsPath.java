package com.cas.pojo;

import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @description:
 * @author: xianglong
 * @create: 2024-01-04 20:33
 **/
@TableName("cas_drools_path")
public class CasDroolsPath {

    private String id;
    private String path;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
