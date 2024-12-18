package com.ayush.dream_shop.service.category;

import com.ayush.dream_shop.exceptions.ResourceNotfoundException;
import com.ayush.dream_shop.exceptions.AlreadyExistsException;
import com.ayush.dream_shop.model.Category;
import com.ayush.dream_shop.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{
//    this below line is the dependency of the categoryRepository which we are going to use
    private final CategoryRepository categoryRepository;
//    this function is for finding category by its id
    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(()-> new ResourceNotfoundException("Category not found"));
    }
//  this function is for getting category by its name
    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }
//  this method is for getting all the categories
    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
//  this method is for adding all the categories might be using a helper function for it
    @Override
    public Category addCategory(Category category) {
        return Optional.of(category).filter(c-> !categoryRepository.existsByName(c.getName()))
                .map(categoryRepository::save)
                .orElseThrow(()-> new AlreadyExistsException(category.getName()+"already exists"));
    }
//  this is the method for update categories
@Override
    public Category updateCategory(Category category , Long id) {
        return Optional.ofNullable(getCategoryById(id))
                .map(oldCategory -> {
                    oldCategory.setName(category.getName());
                    return categoryRepository.save(oldCategory);
                }).orElseThrow(()-> new ResourceNotfoundException("Category not found"));
    }
//  this is for deleting the category by id

@Override
    public void deleteCategoryById(Long id) {
        categoryRepository.findById(id).ifPresentOrElse(categoryRepository::delete,()->{
            throw new ResourceNotfoundException("Category not found");
        });
    }
}
