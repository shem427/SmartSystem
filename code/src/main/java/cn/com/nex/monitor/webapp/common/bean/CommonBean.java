package cn.com.nex.monitor.webapp.common.bean;

import lombok.Data;

/**
 * Bean共通父类。
 */
@Data
public class CommonBean {
    private Status status = Status.SUCCESS;
    private String message;

    public enum Status {
        SUCCESS,
        WARNING,
        ERROR
    }
}

