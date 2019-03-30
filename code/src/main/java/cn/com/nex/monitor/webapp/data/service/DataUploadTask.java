package cn.com.nex.monitor.webapp.data.service;

import cn.com.nex.monitor.webapp.data.bean.DataBean;
import cn.com.nex.monitor.webapp.data.dao.DataDao;
import org.springframework.transaction.annotation.Transactional;

public class DataUploadTask extends Thread {
    private DataBean data;
    private DataDao dataDao;

    public DataUploadTask(DataBean data, DataDao dao) {
        this.data = data;
        this.dataDao = dao;
    }

    @Override
    @Transactional
    public void run() {
        dataDao.uploadData(data);
    }
}
