package com.ayush.dream_shop.service.product;

import com.ayush.dream_shop.dto.ProductDto;
import com.ayush.dream_shop.model.Product;
import com.ayush.dream_shop.request.AddProductRequest;
import com.ayush.dream_shop.request.ProductUpdateRequest;

import java.util.List;

public interface  IProductService {
    Product addProduct(AddProductRequest product);

    Product getProductById(Long id);
    void deleteProductById(Long id);
    Product updateProduct(ProductUpdateRequest product , Long productId);
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByCategoryAndBrand(String category , String brand);
    List<Product> getProductsByName(String name);
    List<Product> getProductsByBrandAndName(String brand , String name);
    Long countProductsByBrandAndName(String brand , String name);

    List<ProductDto> getConvertedProducts(List<Product> products);

    ProductDto convertToDto(Product product);
}
