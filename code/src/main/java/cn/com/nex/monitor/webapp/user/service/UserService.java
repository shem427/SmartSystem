package cn.com.nex.monitor.webapp.user.service;

import cn.com.nex.monitor.webapp.common.MonitorPasswordEncoder;
import cn.com.nex.monitor.webapp.common.bean.SearchParam;
import cn.com.nex.monitor.webapp.common.bean.TableData;
import cn.com.nex.monitor.webapp.user.bean.UserBean;
import cn.com.nex.monitor.webapp.user.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    MonitorPasswordEncoder passwordEncoder;

    public UserBean getUserById(String userId) {
        return userDao.get(userId);
    }

    /**
     * 根据指定条件检索符合条件的人员。
     * @param param 共通检索条件
     * @param userIdLike 用户ID模糊条件
     * @param nameLike 姓名模糊条件
     * @return 符合条件人员
     */
    public TableData<UserBean> searchUser(SearchParam param, String userIdLike, String nameLike) {
        TableData<UserBean> usersData = new TableData<>();
        List<UserBean> userList = userDao.searchUser(param, userIdLike, nameLike);
        int count = userDao.count(userIdLike, nameLike);

        usersData.setTotal(count);
        usersData.setRows(userList);

        return usersData;
    }

    public List<UserBean> searchUser(String userIdLike, String nameLike) {
        return userDao.searchUser(null, userIdLike, nameLike);
    }

    @Transactional
    public int deleteUser(List<String> userIds) {
        int count = 0;
        if (userIds == null || userIds.isEmpty()) {
            return count;
        }
        for (String id : userIds) {
            count += userDao.deleteUser(id);
        }
        return count;
    }

    @Transactional
    public int addUser(UserBean user) {
        return userDao.addUser(user);
    }

    @Transactional
    public int updateUser(UserBean user) {
        return userDao.updateUser(user);
    }

    @Transactional
    public int resetPassword(List<String> userIds, String defaultPassword) {
        int count = 0;
        if (userIds == null) {
            return 0;
        }
        String pwd = passwordEncoder.encode(defaultPassword);
        for (String userId : userIds) {
            count += userDao.resetPassword(userId, pwd);
        }
        return count;
    }
}
