package com.dacoders.buksue_libraryapp.CollectionModel;

import com.dacoders.buksue_libraryapp.CollectionDataAccessObject.Collection_DAO;

import java.util.List;

public class Category {
    String categoryName;

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }


    public Category() {
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
