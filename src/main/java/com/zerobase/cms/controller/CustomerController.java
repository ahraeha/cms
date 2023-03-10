package com.zerobase.cms.controller;

import com.zerobase.cms.domain.customer.ChangeBalanceForm;
import com.zerobase.cms.domain.customer.CustomerDto;
import com.zerobase.cms.domain.model.Customer;
import com.zerobase.cms.exception.CustomException;
import com.zerobase.cms.exception.ErrorCode;
import com.zerobase.cms.service.customer.CustomerBalanceService;
import com.zerobase.cms.service.customer.CustomerService;
import com.zerobase.config.JwtAuthenticationProvider;
import com.zerobase.domain.common.UserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

  private final JwtAuthenticationProvider provider;
  private final CustomerService customerService;
  private final CustomerBalanceService customerBalanceService;

  @GetMapping("/getInfo")
  public ResponseEntity<CustomerDto> getInfo(@RequestHeader(name = "X-AUTH-TOKEN") String token) {
    UserVo vo = provider.getUserVo(token);
    Customer c = customerService.findByIdAndEmail(vo.getId(), vo.getEmail()).orElseThrow(
        () -> new CustomException(ErrorCode.NOT_FOUND_USER));

    return ResponseEntity.ok(CustomerDto.from(c));
  }

  @PostMapping("/balance")
  public ResponseEntity<Integer> changeBalance(@RequestHeader(name = "X-AUTH-TOKEN") String token, @RequestBody
      ChangeBalanceForm form) {
    UserVo vo = provider.getUserVo(token);

    return ResponseEntity.ok(customerBalanceService.changeBalance(vo.getId(), form).getCurrentMoney());
  }
}
