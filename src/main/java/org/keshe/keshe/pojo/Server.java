package org.keshe.keshe.pojo;

import lombok.Data;

import java.io.BufferedReader;
import java.io.OutputStream;

@Data
public class Server {
    private Process p;
    private long pid;
    private BufferedReader reader;
    private OutputStream outputstream;
    private String name;
    private String cmd;
    private String nickname;
    private int ifon;
    private int ifconnect;
}
