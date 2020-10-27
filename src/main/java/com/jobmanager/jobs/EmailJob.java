package com.jobmanager.jobs;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.CollectionUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by sousaJ on 27/10/2020
 * in package - com.jobmanager.jobs
 **/
@Slf4j
public class EmailJob implements Job {

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("job triggered to send emails");
        JobDataMap map = jobExecutionContext.getMergedJobDataMap();

    }

    private void sendEmail(JobDataMap map) {
        String subject = map.getString("subject");
        String messageBody = map.getString("messageBody");

        List<String> to = (List<String>) map.get("to");
        List<String> cc = (List<String>) map.get("cc");
        List<String> bcc = (List<String>) map.get("bcc");

        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, false);

            for (String recipient : to) {

                mimeMessageHelper.setFrom("jm.alves.sousa@gmail.com", "Jose quartz tutorial");
                mimeMessageHelper.setTo(recipient);
                mimeMessageHelper.setSubject(subject);
                mimeMessageHelper.setText(messageBody);

                if (!CollectionUtils.isEmpty(cc)) {
                    mimeMessageHelper.setCc(cc.toArray(String[]::new));
                }

                if (!CollectionUtils.isEmpty(bcc)) {
                    mimeMessageHelper.setBcc(bcc.toArray(String[]::new));
                }

                javaMailSender.send(message);
            }

        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("An error occurred: {}", e.getLocalizedMessage());
        }
    }
}
