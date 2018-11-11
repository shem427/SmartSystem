package cn.com.nex.monitor.webapp.common;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Message Service类
 */
@Component
public class MessageService {
    @Resource
    private MessageSource messageSource;

    public String getMessage(String code){
        return getMessage(code, null);
    }

    public String getMessage(String code, Object[] args) {
        return getMessage(code, args, code);
    }

    public String getMessage(String code, Object[] args, String defaultValue) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code, args, defaultValue, locale);
    }

    public String getLogEntry(String...args) {
        if (args == null || args.length == 0) {
            return getMessage(MonitorConstant.LOG_ENTRY_CODE);
        }
        List<String> paramList = getParamList(args);
        return getMessage(MonitorConstant.LOG_ENTRY_CODE, paramList.toArray());
    }

    public String getLogExit(String...args) {
        if (args == null || args.length == 0) {
            return getMessage(MonitorConstant.LOG_EXIT_CODE);
        }
        List<String> paramList = getParamList(args);
        return getMessage(MonitorConstant.LOG_EXIT_CODE, paramList.toArray());
    }

    private List<String> getParamList(String...args) {
        List<String> paramList = new ArrayList<>();
        for (String arg : args) {
            paramList.add(getMessage(arg));
        }
        return paramList;
    }
}
