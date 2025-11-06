package org.example.models;

public class ProductCategoryModel {
    private int id;
    private String categoryName;

    public ProductCategoryModel(int id, String name) {
        this.id = id;
        this.categoryName = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String name) {
        this.categoryName = name;
    }
}
