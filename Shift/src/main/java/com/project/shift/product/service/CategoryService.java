package com.project.shift.product.service;

import com.project.shift.product.dao.ICategoryDAO;
import com.project.shift.product.dto.CategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    private ICategoryDAO categoryDAO;

    @Override
    public List<CategoryDTO> getCategoryList() {
        return categoryDAO.getCategoryList();
    }
}
