package cn.jwq.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author jia
 * @Description
 * @date 2019-03-23-21:14
 */
@ToString
@Setter
@Getter
public class Announcement {
    private Integer id;
    private String aTitle;
    private String aText;
    private String aDate;
}
