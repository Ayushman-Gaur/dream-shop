package com.ayush.dream_shop.controller;

import com.ayush.dream_shop.dto.ProductDto;
import com.ayush.dream_shop.exceptions.AlreadyExistsException;
import com.ayush.dream_shop.exceptions.ResourceNotfoundException;
import com.ayush.dream_shop.model.Product;
import com.ayush.dream_shop.request.AddProductRequest;
import com.ayush.dream_shop.request.ProductUpdateRequest;
import com.ayush.dream_shop.response.ApiResponse;
import com.ayush.dream_shop.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {

    // Requestbody and used when one has to enter or get object or complex data
    // requestparam is used when there is any parameter in url like filter of age and name
    // pathvaraible is used when there is a value embeded in the url then it is used

    // Injecting dependencies
    private final IProductService productService;

    @GetMapping("/products/all")
    public ResponseEntity<ApiResponse> getAllProducts(){
        List<Product> products = productService.getAllProducts();
        List<ProductDto> convertedProducts= productService.getConvertedProducts(products);
        return ResponseEntity.ok(new ApiResponse("Success",convertedProducts));
    }

    @GetMapping("product/{productId}/product")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId){
        try {
            Product product= productService.getProductById(productId);
            ProductDto productDto =  productService.convertToDto(product);
            return ResponseEntity.ok(new ApiResponse("Success",productDto));
        } catch (ResourceNotfoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @PostMapping("/product/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest product){
        try {
            Product theProduct = productService.addProduct(product);
            ProductDto productDto =  productService.convertToDto(theProduct);
            return ResponseEntity.ok(new ApiResponse("Adding product Success",theProduct));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @PutMapping("/product/{productId}/update")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductUpdateRequest request ,@PathVariable Long productId){
        try {
            Product theProduct = productService.updateProduct(request,productId);
            ProductDto productDto =  productService.convertToDto(theProduct);
            return ResponseEntity.ok(new ApiResponse("Updated Success!",theProduct));
        } catch (ResourceNotfoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @DeleteMapping("/product/{productId}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId){
        try {
            productService.deleteProductById(productId);
            return ResponseEntity.ok(new ApiResponse("Delete product Success",productId));
        } catch (ResourceNotfoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }

    }
    @GetMapping("/products/by/brand-and-name")
    public ResponseEntity<ApiResponse> getProductByBrandAndName(@RequestParam String brandName, @RequestParam String productName){
        try {
            List<Product>products=productService.getProductsByBrandAndName(brandName,productName);

            if(products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No product Found",null));
            }
            List<ProductDto> convertedProducts= productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Success",convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @GetMapping("/products/by/category-and-brand")
    public ResponseEntity<ApiResponse> getProductByCategoryAndBrand(@RequestParam String category ,@RequestParam String brand){
        try {
            List<Product> products=productService.getProductsByCategoryAndBrand(category, brand);

            if(products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No product Found",null));
            }
            List<ProductDto> convertedProducts= productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Success",convertedProducts));
        } catch (ResourceNotfoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("error",e.getMessage()));
        }

    }

    @GetMapping("/products/{name}/products")
    public ResponseEntity<ApiResponse> getProductByName(@PathVariable String name){
        try {
            List<Product> products = productService.getProductsByName(name);

            if(products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No Product Found",null));
            }
            List<ProductDto> convertedProducts= productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Success",convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("error",e.getMessage()));
        }
    }

    @GetMapping("/product/by-brand")
    public ResponseEntity<ApiResponse> getProductByBrand(@RequestParam String brand){
        try {
            List<Product> products= productService.getProductsByBrand(brand);

            if(products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No Product Found",null));
            }
            List<ProductDto> convertedProducts= productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Success",convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("error",e.getMessage()));
        }
    }

    @GetMapping("/product/{category}/all/products")
    public ResponseEntity<ApiResponse> getProductsByCategory(@PathVariable  String category){
        try {
            List<Product> products= productService.getProductsByCategory(category);

            if(products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No Product Found",null));
            }
            List<ProductDto> convertedProducts= productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Success",convertedProducts));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("error",e.getMessage()));
        }
    }

    @GetMapping("/product/count/by-brand/and-name")
    public ResponseEntity<ApiResponse> countProductByBrandAndName(@RequestParam String brand,@RequestParam String name){
        try {
            var productCount= productService.countProductsByBrandAndName(brand,name);
            return ResponseEntity.ok(new ApiResponse("Product count!",productCount));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("error",e.getMessage()));
        }
    }

}
