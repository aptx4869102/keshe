package org.keshe.keshe.pojo;



import jakarta.validation.constraints.NotNull;

import lombok.Data;



import java.time.LocalDateTime;

@Data
public class User {
    @NotNull
    private int id;

    //@NotEmpty
    //@Pattern(regexp = "^\\S{5,11}$")
    //private String qq;

    private String username;

    //@JsonIgnore
    private String password;

    private LocalDateTime createTime;//创建时间
    private LocalDateTime updateTime;//更新时间
}
