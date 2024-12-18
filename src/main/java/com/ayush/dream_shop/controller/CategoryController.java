package com.ayush.dream_shop.controller;

import com.ayush.dream_shop.exceptions.AlreadyExistsException;
import com.ayush.dream_shop.exceptions.ResourceNotfoundException;
import com.ayush.dream_shop.model.Category;
import com.ayush.dream_shop.response.ApiResponse;
import com.ayush.dream_shop.service.category.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/categories")
public class CategoryController {
    private final ICategoryService categoryService;


    @GetMapping("/category/all")
     public ResponseEntity<ApiResponse> getAllCategory(){
         try {
             List<Category> categories = categoryService.getAllCategories();
             return ResponseEntity.ok(new ApiResponse("Found",categories));
         } catch (Exception e) {
             return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error:",INTERNAL_SERVER_ERROR));
         }
     }


     @PostMapping("/category/add")
     public ResponseEntity<ApiResponse> addCategories(@RequestBody Category name){
         try {
             Category theCategory =categoryService.addCategory(name);
             return ResponseEntity.ok(new ApiResponse("Success",theCategory));
         } catch (AlreadyExistsException e) {
             return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
         }
     }

    @GetMapping("/category/{id}/category")
     public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id){
         try {
             Category theCategory = categoryService.getCategoryById(id);
             return ResponseEntity.ok(new ApiResponse("Found", theCategory));
         } catch (ResourceNotfoundException e) {
             return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
         }
     }
    @GetMapping("/category/{name}/category")
     public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name){
         try {
             Category theCategory= categoryService.getCategoryByName(name);
             return ResponseEntity.ok(new ApiResponse("Found",theCategory));
         } catch (ResourceNotfoundException e) {
             return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
         }
     }

     @DeleteMapping("/category/{id}/delete")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long id){
         try {
             categoryService.deleteCategoryById(id);
             return ResponseEntity.ok(new ApiResponse("Delete Success",null));
         } catch (ResourceNotfoundException e) {
             return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
         }
     }


     @PutMapping("/category/{id}/update")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable Long id, @RequestBody Category category){
         try {
             Category updatedCategory=categoryService.updateCategory(category,id);
             return ResponseEntity.ok(new ApiResponse("Updated Success",updatedCategory));
         } catch (ResourceNotfoundException e) {
             return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
         }
     }

}
