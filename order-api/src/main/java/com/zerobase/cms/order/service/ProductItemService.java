package com.zerobase.cms.order.service;

import com.zerobase.cms.order.domain.model.Product;
import com.zerobase.cms.order.domain.model.ProductItem;
import com.zerobase.cms.order.domain.product.AddProductItemForm;
import com.zerobase.cms.order.domain.repository.ProductItemRepository;
import com.zerobase.cms.order.domain.repository.ProductRepository;
import com.zerobase.cms.order.exception.CustomException;
import com.zerobase.cms.order.exception.ErrorCode;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductItemService {

  private final ProductRepository productRepository;
  private final ProductItemRepository productItemRepository;

  @Transactional
  public Product addProductItem(Long sellerId, AddProductItemForm form) {
    Product product = productRepository.findBySellerIdAndId(sellerId, form.getProductId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT));

    if (product.getProductItems().stream()
        .anyMatch(item -> item.getName().equals(form.getName()))) {
      throw new CustomException(ErrorCode.SAME_ITEM_NAME);
    }

    ProductItem productItem = ProductItem.of(sellerId, form);
    product.getProductItems().add(productItem);
    return product;
  }
}