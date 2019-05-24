package cn.com.nex.monitor.webapp.common.constant;

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
    public static final String LOG_ERROR_REASON = "mr.server.error.reason";

    public static final String CAN_NOT_DELETE_SELF = "mr.cannot.delete.self";

    public static final String UNIT_STATUS_JOB_START = "mr.unit.status.job.start";
    public static final String UNIT_STATUS_JOB_END = "mr.unit.status.job.end";
    public static final String UNIT_STATUS_JOB_ERROR = "mr.unit.status.job.error";

    public static final String UNIT_TYPE_PREFIX = "mr.unit.type.";
    public static final int[] UNIT_TYPE_VALUES = { 1, 2, 3, 4, 51, 52, 61, 62, 71, 72, 81, 82, 9 };
}
