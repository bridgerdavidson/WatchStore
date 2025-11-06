package org.example.service;

import org.example.dao.ProductCategoryDAO;
import org.example.models.ProductCategoryModel;

import java.util.ArrayList;
import java.util.List;

public class ProductCategoryService {
    private ProductCategoryDAO productCategoryDAO;

    public ProductCategoryService(ProductCategoryDAO productCategoryDAO) {
        this.productCategoryDAO = productCategoryDAO;
    }

    public List<ProductCategoryModel> getAllProductCategories() {
        List<ProductCategoryModel> products = new ArrayList<>(productCategoryDAO.findAll());
        return products;
    }

    public ProductCategoryModel findById(int id) {
        return productCategoryDAO.findById(id);
    }

    public ProductCategoryModel addProductCategory(String categoryName) {
        ProductCategoryModel model = new ProductCategoryModel(0, categoryName);
        return productCategoryDAO.create(model);
    }

    public List<ProductCategoryModel> findByName(String query) {
        return new ArrayList<>(productCategoryDAO.findByName(query));
    }

    public boolean editProductCategory(int id, String categoryName) {
        ProductCategoryModel entity = productCategoryDAO.findById(id);
        if (entity == null) return false;
        ProductCategoryModel updated = new ProductCategoryModel(id, categoryName);
        return productCategoryDAO.update(updated) != null;
    }

    public boolean deleteProductCategory(String id) {
        int idInt = Integer.parseInt(id);
        ProductCategoryModel entity = productCategoryDAO.findById(idInt);
        if (entity == null) return false;
        return productCategoryDAO.delete(idInt);
    }

    public ProductCategoryModel getProductCategory(int id) {
        return productCategoryDAO.findById(id);
    }
}
