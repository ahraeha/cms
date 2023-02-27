package com.zerobase.cms.application;

import com.zerobase.cms.client.MailgunClient;
import com.zerobase.cms.client.mailgun.SendMailForm;
import com.zerobase.cms.domain.SignUpForm;
import com.zerobase.cms.domain.model.Customer;
import com.zerobase.cms.exception.CustomException;
import com.zerobase.cms.exception.ErrorCode;
import com.zerobase.cms.service.SignUpCustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SignUpApplication {

  private final MailgunClient mailgunClient;
  private final SignUpCustomerService signUpCustomerService;

  public void customerVerify(String email, String code) {
    signUpCustomerService.verifyEmail(email, code);
  }

  public String customerSignUp(SignUpForm form) {
    if (signUpCustomerService.isEmailExist(form.getEmail())){
      //exception
      throw new CustomException(ErrorCode.ALREADY_REGISTER_USER);
    } else {
      Customer c = signUpCustomerService.singUp(form);

      String code = getRandomCode();

      SendMailForm sendMailForm = SendMailForm.builder()
          .from("tester@ahnmytester.com")
          .to(form.getEmail())
          .subject("Verification Email")
          .text(getVerificationEmailBody(c.getEmail(), c.getName(), code))
          .build();

      log.info("Send email result : " + mailgunClient.sendEmail(sendMailForm).getBody());
      mailgunClient.sendEmail(sendMailForm);

      signUpCustomerService.changeCustomerValidateEmail(c.getId(), code);
      return "회원 가입에 성공하였습니다.";
    }

  }

  private String getRandomCode() {
    return RandomStringUtils.random(10, true, true);
  }
  private String getVerificationEmailBody(String email, String name, String code) {

    StringBuilder builder = new StringBuilder();
    return builder.append("hello").append(name).append("! Please Click Link for Verification.\n\n")
        .append("http://localhost:8080/signup/verify/customer?email=")
        .append(email)
        .append("&code=")
        .append(code).toString();
  }


}
