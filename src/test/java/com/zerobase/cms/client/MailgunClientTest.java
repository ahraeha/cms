package com.zerobase.cms.client;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MailgunClientTest {

  @Autowired
  private MailgunClient mailgunClient;

  @Test
  public void EmailTest() {
//    mailgunClient.sendEmail(null);
//
//    String repsonse = emailSendService.sendEmail();
//    System.out.println(repsonse);
  }
}