package com.school.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailService {
    @Value("${spring.mail.username}")
    private String from;

    private final Logger logger= LoggerFactory.getLogger(this.getClass());
    @Autowired
    private JavaMailSender mailSender;

    /**
     * 发送简单文本
     * @param to   // 发给谁
     * @param subject // 发给谁
     * @param content //发送内容
     */
    public void sendSimpleMail(String to,String subject,String content){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);  //谁发送的
        message.setTo(to); // 发给谁
        message.setSubject(subject);  // 发给谁
        message.setText(content); //内容
        mailSender.send(message);
    }

    /**
     * 发送HTML邮件的方法
     * @param to
     * @param subjecr
     * @param content
     */
    public void sendHtmlMail(String to ,String subjecr,String content){
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            helper.setTo(to);
            helper.setFrom(from);
            helper.setSubject(subjecr);
            helper.setText(content,true);
            mailSender.send(message);
            logger.info("发送静态邮件成功");
        } catch (MessagingException e) {
            logger.error("发送静态邮件失败：",e);
        }

    }



}
