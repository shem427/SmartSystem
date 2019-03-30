package cn.com.nex.monitor.webapp.data.service;

import cn.com.nex.monitor.webapp.data.bean.DataBean;
import cn.com.nex.monitor.webapp.data.dao.DataDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class DataService {
    private static final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(10);

    @Autowired
    private DataDao dao;

    public void uploadData(DataBean data) {
        DataUploadTask task = new DataUploadTask(data, dao);
        THREAD_POOL.execute(task);
    }
}
