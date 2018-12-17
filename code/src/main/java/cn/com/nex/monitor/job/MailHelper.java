package cn.com.nex.monitor.job;

import cn.com.nex.monitor.webapp.dashboard.bean.DashboardUnitBean;
import cn.com.nex.monitor.webapp.unit.bean.UnitBean;
import com.sun.mail.util.MailSSLSocketFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.GeneralSecurityException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component
public class MailHelper {

    @Value("${cn.com.nex.monitor.mail.mailDebug}")
    private String mailDebug;
    @Value("${cn.com.nex.monitor.mail.smtpAuth}")
    private String smtpAuth;
    @Value("${cn.com.nex.monitor.mail.mailHost}")
    private String mailHost;
    @Value("${cn.com.nex.monitor.mail.mailProtocal}")
    private String mailProtocal;
    @Value("${cn.com.nex.monitor.mail.authUser}")
    private String authUser;
    @Value("${cn.com.nex.monitor.mail.authPassword}")
    private String authPassword;
    @Value("${cn.com.nex.monitor.mail.needSSL}")
    private boolean needSSL;
    @Value("${cn.com.nex.monitor.mail.from}")
    private String from;
    @Value("${cn.com.nex.monitor.mail.subject}")
    private String subject;
    @Value("${cn.com.nex.monitor.mail.body}")
    private String body;

    public void send(DashboardUnitBean unit, List<String> addrList) throws Exception {
        //使用JavaMail发送邮件的5个步骤
        //1、创建session
        Session session = Session.getInstance(initProp());
        //开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
        session.setDebug(true);
        //2、通过session得到transport对象
        Transport ts = session.getTransport();
        //3、使用邮箱的用户名和密码连上邮件服务器。
        ts.connect(mailHost, authUser, authPassword);
        //4、创建邮件
        Message message = createMail(session, unit, addrList);
        //5、发送邮件
        ts.sendMessage(message, message.getAllRecipients());
        ts.close();
    }

    private Properties initProp() throws GeneralSecurityException {
        Properties props = new Properties();
        // 开启debug调试
        props.setProperty("mail.debug", mailDebug);
        // 发送服务器需要身份验证
        props.setProperty("mail.smtp.auth", smtpAuth);
        // 设置邮件服务器主机名
        props.setProperty("mail.host", mailHost);
        // 发送邮件协议名称
        props.setProperty("mail.transport.protocol", mailProtocal);

        if (needSSL) {
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.ssl.socketFactory", sf);
        }

        return props;
    }

    private Message createMail(Session session, DashboardUnitBean unit, List<String> addrList)
            throws MessagingException {
        //创建邮件对象
        MimeMessage message = new MimeMessage(session);
        //指明邮件的发件人
        message.setFrom(new InternetAddress(from));
        //指明邮件的收件人
        InternetAddress[] toList = new InternetAddress[addrList.size()];
        int i = 0;
        for (String addr : addrList) {
            toList[i] = new InternetAddress(addr);
            i++;
        }
        message.setRecipients(Message.RecipientType.TO, toList);
        //邮件的标题
        String subjectText = MessageFormat.format(subject, unit.getUnitName());
        message.setSubject(subjectText);
        //邮件的文本内容
        String bodyText = MessageFormat.format(body, unit.getUnitName(), unit.getStatusDisplay());
        message.setContent(bodyText, "text/html;charset=UTF-8");
        //返回创建好的邮件对象
        return message;
    }
}
