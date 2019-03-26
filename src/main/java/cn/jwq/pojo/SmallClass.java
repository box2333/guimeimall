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
public class SmallClass {
    private Integer id;
    private String smallName;
    private Integer smallBigId;
    private String smallText;
    /**
     * 非数据库字段属性
     */
    private BigClass bigClass;
}
