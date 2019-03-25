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
public class Orders {
    private Integer id;
    private Integer ordersGoodsIdid;
    private Integer ordersCusId;
    private Date ordersDate;
    private String ordersAddress;
    private Double ordersMoney;
    private Integer ordersStatus;
}
