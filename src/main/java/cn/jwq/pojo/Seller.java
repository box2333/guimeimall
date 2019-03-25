package cn.jwq.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author jia
 * @Description
 * @date 2019-03-23-21:20
 */
@ToString
@Setter
@Getter
public class Seller {
    private Integer id;
    private String sellerName;
    private String sellerUser;
    private String sellerPasswordsellerName;
    private String sellerSex;
    private Date sellerBirthday;
    private String sellerIdCard;
    private String sellerEmail;
    private String sellerTel;
    private String sellerAddress;
}
