package cn.jwq.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author jia
 * @Description
 * @date 2019-03-23-21:04
 */
@Getter
@Setter
@ToString
public class Admin {
    private Integer adminId;
    private String adminName;
    private String adminPassword;
    private Integer status;
}
