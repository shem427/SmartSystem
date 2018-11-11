package cn.com.nex.monitor.webapp.common;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.thymeleaf.util.StringUtils;

/**
 * Table检索条件Class。
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SearchParam {
    private int offset;
    private int limit;
    private String sortOrder;
    private String sortField;

    public String toSQL() {
        StringBuilder sbSQL = new StringBuilder();
        if (!StringUtils.isEmpty(sortField)) {
            sbSQL.append(" ORDER BY ").append(sortField);
            sbSQL.append(" ").append(sortOrder);
        }
        sbSQL.append(" LIMIT ").append(offset);
        sbSQL.append(",").append(limit);

        return sbSQL.toString();
    }
}
