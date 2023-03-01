package com.ecommerce.site.shop.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * @author Nguyen Thanh Phuong
 */
public class EmailUtils {

    public static @NotNull String getSiteUrl(@NotNull HttpServletRequest request) {
        String siteUrl = request.getRequestURL().toString();

        return siteUrl.replace(request.getServletPath(), "");
    }

    public static @NotNull JavaMailSenderImpl prepareMailSender(@NotNull EmailSettingBagUtils emailSettingBag) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(emailSettingBag.getHost());
        mailSender.setPort(emailSettingBag.getMailPort());
        mailSender.setUsername(emailSettingBag.getMailUsername());
        mailSender.setPassword(emailSettingBag.getMailPassword());

        Properties mailProperties = mailSender.getJavaMailProperties();
        mailProperties.setProperty("mail.smtp.auth", emailSettingBag.getSmtpAuth());
        mailProperties.setProperty("mail.smtp.starttls.enable", emailSettingBag.getSmtpSecured());

        return mailSender;
    }

}
