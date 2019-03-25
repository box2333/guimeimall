package cn.jwq.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author jia
 * @Description
 * @date 2019-03-23-21:20
 */
@ToString
@Setter
@Getter
public class Shoppingcar {
    private Integer id;
    private Integer scCusId;
    private Integer scGoodsId;
    private Integer scNumber;
}
