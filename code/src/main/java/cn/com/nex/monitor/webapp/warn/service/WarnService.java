package cn.com.nex.monitor.webapp.warn.service;

import cn.com.nex.monitor.webapp.common.bean.SearchParam;
import cn.com.nex.monitor.webapp.common.bean.TableData;
import cn.com.nex.monitor.webapp.warn.bean.UnitWarnBean;
import cn.com.nex.monitor.webapp.warn.dao.WarnDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class WarnService {
    @Autowired
    private WarnDao warnDao;

    public TableData<UnitWarnBean> getWarns(SearchParam param, String userId, Date beginDate, Date endDate) {
        int total = warnDao.countWarnsByUser(userId, beginDate, endDate);
        List<UnitWarnBean> beanList = warnDao.getWarnsByUser(param, userId, beginDate, endDate);

        TableData<UnitWarnBean> ret = new TableData<>();
        ret.setTotal(total);
        ret.setRows(beanList);

        return ret;
    }
}
