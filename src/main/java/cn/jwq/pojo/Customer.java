package cn.jwq.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author jia
 * @Description
 * @date 2019-03-23-21:15
 */
@ToString
@Setter
@Getter
public class Customer {
    private Integer id;
    private String cusName;
    private String cusLoginName;
    private String cusPassword;
    private String cusEmail;
    private String cusSex;
    private String cusPhoto;
    private String cusPhone;
    private String cusHobby;
    private String cusCode;
    private Date cusBirthday;
}
