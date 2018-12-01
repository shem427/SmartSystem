package cn.com.nex.monitor.webapp.common;

/**
 * 常量定义类
 */
public class MonitorConstant {

    private MonitorConstant() {
    }

    /**
     * DB中ROLE的分隔符
     */
    public static final String ROLE_SEPERATOR = ",";

    public static final String LOG_ENTRY_CODE = "mr.server.log.start";
    public static final String LOG_EXIT_CODE = "mr.server.log.end";
    public static final String LOG_ERROR = "mr.server.error";

    public static final String CONFIG_DEFAULT_PASSWORD = "mr.default.password";

    public static final String CAN_NOT_DELETE_SELF = "mr.cannot.delete.self";
}
