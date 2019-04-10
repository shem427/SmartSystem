package cn.com.nex.monitor.webapp.common.util;

import cn.com.nex.monitor.webapp.user.bean.UserBean;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class MonitorUtil {
    private static final Logger LOG = LoggerFactory.getLogger(MonitorUtil.class);
    /**
     * 散列算法
     */
    private static final String ALGORITHM = "SHA-256";

    private MonitorUtil() {
    }

    /**
     * 密码散列。
     * @param password 密码
     * @return 散列后的密码
     */
    public static String hashEncode(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(ALGORITHM);
            messageDigest.update(password.getBytes());

            return bytesToHexString(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 从spring-security上下文中获取当前用户信息。
     * @return 当前用户信息
     */
    public static UserBean getUserFromSecurity() {
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        UserBean user = (UserBean) auth.getPrincipal();

        return user;
    }

    /**
     * md5加密
     * @param plain 平文
     * @param ck ck
     * @return 密文
     */
    public static String md5(String plain, String ck) {
        //加密后的字符串
        String encodeStr= DigestUtils.md5Hex(plain + ck);
        return encodeStr;
    }

    /**
     * 根据地址获取经纬度
     * @param address 地址
     * @param key 腾讯地图key
     * @param ck 腾讯地图ck
     * @return 经纬度
     */
    public static double[] getLatLngByAddress(String address, String key, String ck) {
        String fullUrl = "https://apis.map.qq.com/ws/geocoder/v1/?address=" + address + "&key=" + key + "&sig=";
        String url = "/ws/geocoder/v1/?address=" + address + "&key=" + key;
        String sig = md5(url, ck);

        JSONObject json = httpGet(fullUrl + sig) ;

        int status = (int) json.get("status");
        if (status != 0) {
            return null;
        }
        JSONObject result = (JSONObject) json.get("result");
        JSONObject location = (JSONObject) result.get("location");
        double lng = (double) location.get("lng");
        double lat = (double) location.get("lat");

        return new double[] {lat, lng};
    }

    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return sdf.format(date);
    }

    private static JSONObject httpGet(String url) {
        // get请求返回结果
        JSONObject jsonResult = null;
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            // 发送get请求
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);

            // 请求发送成功，并得到响应
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // 读取服务器返回过来的json字符串数据
                String strResult = EntityUtils.toString(response.getEntity());
                // 把json字符串转换成json对象
                jsonResult = JSONObject.fromObject(strResult);
                url = URLDecoder.decode(url, "UTF-8");
            } else {
                LOG.error("get请求提交失败:" + url);
            }
        } catch (IOException e) {
            LOG.error("get请求提交失败:" + url, e);
        }
        return jsonResult;
    }

    /**
     * byte数组转换为字符串
     * @param bytes byte数组
     * @return 字符串
     */
    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
