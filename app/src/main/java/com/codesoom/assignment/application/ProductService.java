package com.codesoom.assignment.application;

import com.codesoom.assignment.ProductNotFoundException;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.domain.ProductRepository;
import java.util.List;
import javax.persistence.Table;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {
  private final ProductRepository productRepository;

  public ProductService(ProductRepository productRepository){
    this.productRepository = productRepository;
  }


    public List<Product> getProducts(){
    return productRepository.findAll();
  }

  public Product getProduct(Long id){
    return productRepository.findById(id)
        .orElseThrow(() -> new ProductNotFoundException(id));  }

}
