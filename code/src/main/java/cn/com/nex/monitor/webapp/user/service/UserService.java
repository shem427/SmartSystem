package cn.com.nex.monitor.webapp.user.service;

import cn.com.nex.monitor.webapp.common.SearchParam;
import cn.com.nex.monitor.webapp.common.TableData;
import cn.com.nex.monitor.webapp.user.bean.UserBean;
import cn.com.nex.monitor.webapp.user.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

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
}
