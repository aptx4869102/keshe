package org.keshe.keshe.pojo;

import lombok.Data;

@Data
public class FileInfo {
    private String name;
    private String isDirectory;
    private long size;
    private String path;
}
