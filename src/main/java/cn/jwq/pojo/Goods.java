package cn.jwq.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author jia
 * @Description
 * @date 2019-03-23-21:19
 */
@ToString
@Setter
@Getter
public class Goods {
    private Integer id;
    private String goodsName;
    private Integer goodsSmalId;
    private Double goodsMoney;
    private Integer goodsNumber;
    private String goodsImage;
    private Double goodsCarriage;
    private Integer goodsType;
    private Integer goodsDiscId;
}
