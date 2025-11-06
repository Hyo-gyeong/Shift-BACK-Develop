package com.project.shift.product.dao;

import com.project.shift.product.dto.CategoryDTO;
import com.project.shift.product.entity.Category;
import com.project.shift.product.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CategoryDAO implements ICategoryDAO {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<CategoryDTO> getCategoryList() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(CategoryDTO::new)
                .collect(Collectors.toList());
    }
}
