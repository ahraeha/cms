package com.zerobase.cms.application;

import com.zerobase.cms.domain.SignInForm;
import com.zerobase.cms.domain.model.Customer;
import com.zerobase.cms.exception.CustomException;
import com.zerobase.cms.exception.ErrorCode;
import com.zerobase.cms.service.CustomerService;
import com.zerobase.config.JwtAuthenticationProvider;
import com.zerobase.domain.common.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignInApplication {

  private final CustomerService customerService;
  private final JwtAuthenticationProvider provider;

  public String customerLoginToken(SignInForm form) {
    //1.로그인 가능 여부
    Customer c = customerService.findValidCustomer(form.getEmail(),
        form.getPassword()).orElseThrow(() -> new CustomException(ErrorCode.LOGIN_CHECK_FAIL));
    //2. 토큰을 발행
    //3. 토른을 response한다.
    return provider.createToken(c.getEmail(), c.getId(), UserType.CUSTOMER);
  }
}
