package com.zerobase.cms.config.filter;

import com.zerobase.cms.service.customer.CustomerService;
import com.zerobase.cms.service.seller.SellerService;
import com.zerobase.config.JwtAuthenticationProvider;
import com.zerobase.domain.common.UserVo;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@WebFilter(urlPatterns = "/seller/*")
@RequiredArgsConstructor
public class SellerFilter implements Filter {

  private final JwtAuthenticationProvider jwtAuthenticationProvider;
  private final SellerService sellerService;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) request;
    String token =req.getHeader("X-AUTH-TOKEN");
    if (!jwtAuthenticationProvider.validateToken(token)){
      throw new ServletException("Invalid Access");
    }
    UserVo vo = jwtAuthenticationProvider.getUserVo(token);
    sellerService.findByIdAndEmail(vo.getId(), vo.getEmail()).orElseThrow(
        () -> new ServletException("Invalid access")
    );
    chain.doFilter(request, response);
  }
}
