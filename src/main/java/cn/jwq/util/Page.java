package cn.jwq.util;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author jia
 * @Description
 * @date 2019-03-23-23:34
 */
@ToString
@Setter
@Getter
public class Page {
    private int start;
    private int count;
    private int total;
    public Page(int start) {
        this.start = start;
        this.count = 10;
    }
    public int getTotalPage(){
        int totalPage;
        // 假设总数是50，是能够被5整除的，那么就有10页
        if (0 == total % count) {
            totalPage = total /count;
            // 假设总数是51，不能够被5整除的，那么就有11页
        }
        else {
            totalPage = total / count + 1;
        }
        if(0==totalPage) {
            totalPage = 1;
        }
        return totalPage;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}

