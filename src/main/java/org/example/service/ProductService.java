package org.example.service;

import org.example.dao.ProductDAO;
import org.example.models.ProductModel;

import java.util.ArrayList;
import java.util.List;

public class ProductService {
    private ProductDAO productDAO;

    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public List<ProductModel> getAllProducts() {
        List<ProductModel> products = new ArrayList<>(productDAO.findAll());
        return products;
    }

    public ProductModel addProduct(String watchBrand, String seriesName, String modelNumber, double price, String description, int categoryId) {
        ProductModel model = new ProductModel(0, watchBrand, seriesName, modelNumber, price, description, categoryId);
        return productDAO.create(model);
    }

    public List<ProductModel> findByBrandOrSeries(String query) {
        return new ArrayList<>(productDAO.findByBrandOrSeries(query));
    }

    public boolean editProduct(int id, String watchBrand, String seriesName, String modelNumber, double price, String description, int categoryId) {
        ProductModel entity = productDAO.findById(id);
        if (entity == null) return false;
        ProductModel updated = new ProductModel(id, watchBrand, seriesName, modelNumber, price, description, categoryId);
        return productDAO.update(updated) != null;
    }

    public boolean deleteProduct(String id) {
        int idInt = Integer.parseInt(id);
        ProductModel entity = productDAO.findById(idInt);
        if (entity == null) return false;
        return productDAO.delete(idInt);
    }
    public List<ProductModel> getProductsByCategoryId(int categoryId) {
        return productDAO.getProductsByCategoryId(categoryId);
    }

    public ProductModel getProduct(int id) {
        return productDAO.findById(id);
    }
}
