package com.zerobase.cms.service;

import com.zerobase.cms.domain.SignUpForm;
import com.zerobase.cms.domain.model.Customer;
import com.zerobase.cms.domain.repository.CustomerRepository;
import com.zerobase.cms.exception.CustomException;
import com.zerobase.cms.exception.ErrorCode;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignUpCustomerService {

  private final CustomerRepository customerRepository;

  public Customer singUp(SignUpForm form) {
    return customerRepository.save(Customer.from(form));
  }

  public boolean isEmailExist(String email) {
    return customerRepository.findByEmail(email.toLowerCase(Locale.ROOT)).isPresent();
  }

  @Transactional
  public void verifyEmail(String email, String code) {
    Customer customer = customerRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER) );
    if (customer.isVerify()){
      throw new CustomException(ErrorCode.ALREADY_VERIFY);
    } else if (!customer.getVerificationCode().equals(code)) {
      throw new CustomException(ErrorCode.WRONG_VERIFICATION);
    } else if (customer.getVerifyExpiredAt().isBefore(LocalDateTime.now())) {
      throw new CustomException(ErrorCode.EXPIRE_CODE);
    }
    customer.setVerify(true);
  }

  @Transactional
  public LocalDateTime changeCustomerValidateEmail(Long customerId, String verificationCode) {
    Optional<Customer> customerOptional = customerRepository.findById(customerId);

    if (customerOptional.isPresent()){
      Customer customer = customerOptional.get();
      customer.setVerificationCode(verificationCode);
      customer.setVerifyExpiredAt(LocalDateTime.now().plusDays(1));
      return customer.getVerifyExpiredAt();
    } else {
      throw new CustomException(ErrorCode.NOT_FOUND_USER);
    }

  }
}
