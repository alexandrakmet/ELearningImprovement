package com.elearning.system.repositories.dao;

import com.elearning.system.repositories.entities.Image;

public interface ImageDao extends GenericDao<Image> {
    String TABLE_NAME = "image";

    int getIdBySrc(String src);
}
