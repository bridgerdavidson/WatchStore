package org.example.dao;

import org.example.models.ProductCategoryModel;
import org.example.models.ProductModel;
import org.example.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ProductCategoryDAO {
    public List<ProductCategoryModel> findAll() {
        List<ProductCategoryModel> products = new ArrayList<>();
        String sql = "SELECT category_id, category_name FROM product_categories";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                products.add(toProductCategoryModel(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
    public ProductCategoryModel findById(int id) {
        String sql = "SELECT category_id, category_name FROM product_categories WHERE category_id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return toProductCategoryModel(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ProductCategoryModel> findByName(String query) {
        List<ProductCategoryModel> products = new ArrayList<>();
        String sql = "SELECT category_id, category_name FROM product_categories WHERE LOWER(category_name) LIKE ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // append wildcards to the query to allow for partial matches
            String pattern = "%" + query.toLowerCase() + "%";
            stmt.setString(1, pattern);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                products.add(toProductCategoryModel(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
    public ProductCategoryModel create(ProductCategoryModel product) {
        String sql = "INSERT INTO product_categories (category_name) VALUES (?)";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, product.getCategoryName());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) return null;
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return new ProductCategoryModel(keys.getInt(1), product.getCategoryName());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public ProductCategoryModel update(ProductCategoryModel product) {
        String sql = "UPDATE product_categories SET category_name=? WHERE category_id=?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, product.getCategoryName());
            stmt.setInt(2, product.getId());
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0 ? product : null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM product_categories WHERE category_id=?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private ProductCategoryModel toProductCategoryModel(ResultSet resultSet) throws SQLException {
        return new ProductCategoryModel(
                resultSet.getInt("category_id"),
                resultSet.getString("category_name")
        );
    }
}
