package com.elearning.system.repositories.dao;

import com.elearning.system.repositories.entities.Category;

public interface CategoryDao extends GenericDao<Category> {
    String TABLE_NAME = "category";
}
