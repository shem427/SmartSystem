package cn.com.nex.monitor.webapp.common.constant;

import java.util.HashMap;
import java.util.Map;

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

    public static final Map<String, Integer> UNIT_TYPE_MAP = new HashMap<>();

    static {
        UNIT_TYPE_MAP.put("一级行政区", 1);
        UNIT_TYPE_MAP.put("二级行政区", 2);
        UNIT_TYPE_MAP.put("三级行政区", 3);
        UNIT_TYPE_MAP.put("其它行政区", 4);
        UNIT_TYPE_MAP.put("医院", 51);
        UNIT_TYPE_MAP.put("幼儿园", 52);
        UNIT_TYPE_MAP.put("一级子单位 - 医院", 61);
        UNIT_TYPE_MAP.put("一级子单位 - 幼儿园", 62);
        UNIT_TYPE_MAP.put("二级子单位 - 医院", 71);
        UNIT_TYPE_MAP.put("二级子单位 - 幼儿园", 72);
        UNIT_TYPE_MAP.put("三级子单位 - 医院", 81);
        UNIT_TYPE_MAP.put("三级子单位 - 幼儿园", 82);
        UNIT_TYPE_MAP.put("紫外线灯", 9);
    }
}
