package cn.com.nex.monitor.webapp.common.util;

import cn.com.nex.monitor.webapp.common.bean.CommonBean;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public final class JxlsUtil {
    private JxlsUtil() {
    }

    public static <T extends CommonBean> void exportExcel(InputStream template, OutputStream out, List<T> datas)
            throws IOException {
        Context context = new Context();
        context.putVar("datas", datas);
        JxlsHelper.getInstance().processTemplate(template, out, context);
    }
}
