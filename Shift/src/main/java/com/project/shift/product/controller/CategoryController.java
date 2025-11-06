package com.project.shift.product.controller;

import com.project.shift.product.dto.CategoryDTO;
import com.project.shift.product.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @GetMapping("/list")
    public List<CategoryDTO> getCategoryList() {
        return categoryService.getCategoryList();
    }
}
