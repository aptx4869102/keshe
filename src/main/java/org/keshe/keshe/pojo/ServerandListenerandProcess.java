package org.keshe.keshe.pojo;

import lombok.Data;
import net.mamoe.mirai.event.Listener;

@Data
public class ServerandListenerandProcess {
    private Server server;
    private Listener listener;
    private Process process;
}
