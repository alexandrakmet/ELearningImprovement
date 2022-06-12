package com.elearning.system.services;

import com.elearning.system.repositories.dao.implementation.CategoryDaoImpl;
import com.elearning.system.repositories.entities.Category;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class CategoryService {
    @Autowired
    private CategoryDaoImpl categoryDao;

    public Category getCategoryById(int id) {
        return categoryDao.getById(id);
    }

    public List<Category> getAllCategories() {
        return categoryDao.getAll();
    }
}
