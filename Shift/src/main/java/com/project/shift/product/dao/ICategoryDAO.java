package com.project.shift.product.dao;

import com.project.shift.product.dto.CategoryDTO;
import java.util.List;

public interface ICategoryDAO {
    List<CategoryDTO> getCategoryList();
}
