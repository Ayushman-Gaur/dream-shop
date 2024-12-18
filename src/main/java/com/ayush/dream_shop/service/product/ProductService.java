package com.ayush.dream_shop.service.product;

import com.ayush.dream_shop.dto.ImageDto;
import com.ayush.dream_shop.dto.ProductDto;
import com.ayush.dream_shop.exceptions.AlreadyExistsException;
import com.ayush.dream_shop.exceptions.ProductNotFoundException;
import com.ayush.dream_shop.exceptions.ResourceNotfoundException;
import com.ayush.dream_shop.model.Category;
import com.ayush.dream_shop.model.Image;
import com.ayush.dream_shop.model.Product;
import com.ayush.dream_shop.repository.CategoryRepository;
import com.ayush.dream_shop.repository.ImageRepository;
import com.ayush.dream_shop.repository.ProductRepository;
import com.ayush.dream_shop.request.AddProductRequest;
import com.ayush.dream_shop.request.ProductUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    // Declaring dependencies on the repository layer
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;

    //this is the addProduct function which will accept the call and send it toward the controller
    @Override
    public Product addProduct(AddProductRequest request) {
        // check if the category is found in the DB
        // If Yes, set it as the new product category
        // If No, the save it as a new category
        // The set as the new product category.


        if(productExists(request.getName(), request.getBrand())){
            throw new AlreadyExistsException(request.getBrand()+" "+request.getName()+" already exists");
        }

        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        request.setCategory(category);
        return productRepository.save(createProduct(request, category));
    }


    private boolean productExists(String name, String brand){
        return productRepository.existsByNameAndBrand(name,brand);
    }






    //now this function is the helper function of the addProduct function
    private Product createProduct(AddProductRequest request , Category category){
        //this is for creating a new product and all the request is called from the constructer in product.java file
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(()-> new ResourceNotfoundException("Product Not Found"));
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id).ifPresentOrElse(productRepository::delete, ()->{throw new ProductNotFoundException("Product not found");});
    }


    //this function is for updating the existence product
    @Override

    public Product updateProduct(ProductUpdateRequest request, Long productId) {
//        Here the request is the new information object where new if given by the client
        return productRepository.findById(productId)
//                Here the optional mapping is used if the object is there it will perform the task
                .map(existingProduct-> updateExistingProduct(existingProduct,request))
                .map(productRepository :: save)
                .orElseThrow(()-> new ProductNotFoundException("Product not found"));

    }

    // this is the helper function of the updateProduct
    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request){
// this is function where the property of existing product is being changed to requested product
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());


        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;

    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category,brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand,brand);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand , name);
    }

    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products){
        return products.stream().map(this::convertToDto).toList();
    }


    @Override
    public ProductDto convertToDto(Product product){
        ProductDto productDto= modelMapper.map(product,ProductDto.class);
        List<Image> images =imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos= images.stream()
                .map(image -> modelMapper.map(image,ImageDto.class))
                .toList();
        productDto.setImages(imageDtos);
        return productDto;
    }
}
