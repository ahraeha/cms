package com.zerobase.cms.domain.customer;

import com.zerobase.cms.domain.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;

@Getter
@Setter
@AllArgsConstructor
public class CustomerDto {

  private Long id;
  private String email;

  public static CustomerDto from(Customer customer){
    return new CustomerDto(customer.getId(), customer.getEmail());

  }
}
