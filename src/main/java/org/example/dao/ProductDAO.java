package org.example.dao;

import org.example.models.ProductModel;
import java.sql.*;
import org.example.util.DbUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ProductDAO {
    public List<ProductModel> findAll() {
        List<ProductModel> products = new ArrayList<>();
        String sql = "SELECT watch_id, watch_brand, series_name, model_number, price, description, product_categories_category_id FROM products";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                products.add(toProductModel(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
    public ProductModel findById(int id) {
        String sql = "SELECT watch_id, watch_brand, series_name, model_number, price, description, product_categories_category_id FROM products WHERE watch_id = ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return toProductModel(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ProductModel> findByBrandOrSeries(String query) {
        List<ProductModel> products = new ArrayList<>();
        String sql = "SELECT watch_id, watch_brand, series_name, model_number, price, description, product_categories_category_id FROM products WHERE LOWER(watch_brand) LIKE ? OR LOWER(series_name) LIKE ?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // append wildcards to the query to allow for partial matches
            String pattern = "%" + query.toLowerCase() + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                products.add(toProductModel(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
    public ProductModel create(ProductModel product) {
        String sql = "INSERT INTO products (watch_brand, series_name, model_number, price, description, vendors_vendor_id, product_categories_category_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, product.getBrand());
            stmt.setString(2, product.getSeriesName());
            stmt.setString(3, product.getModelNumber());
            stmt.setDouble(4, product.getPrice());
            stmt.setString(5, product.getDescription());
            stmt.setInt(6, ThreadLocalRandom.current().nextInt(1, 1001));
            stmt.setInt(7, product.getCategoryId());


            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) return null;
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return new ProductModel(keys.getInt(1), product.getBrand(), product.getSeriesName(), product.getModelNumber(), product.getPrice(), product.getDescription(), product.getCategoryId());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public ProductModel update(ProductModel product) {
        String sql = "UPDATE products SET watch_brand=?, series_name=?, model_number=?, price=?, description=?, product_categories_category_id=? WHERE watch_id=?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, product.getBrand());
            stmt.setString(2, product.getSeriesName());
            stmt.setString(3, product.getModelNumber());
            stmt.setDouble(4, product.getPrice());
            stmt.setString(5, product.getDescription());
            stmt.setInt(6, product.getCategoryId());
            stmt.setInt(7, product.getId());
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0 ? product : null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM products WHERE watch_id=?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<ProductModel> getProductsByCategoryId(int categoryId) {
        List<ProductModel> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE product_categories_category_id=?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, categoryId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                products.add(toProductModel(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    private ProductModel toProductModel(ResultSet resultSet) throws SQLException {
        return new ProductModel(
                resultSet.getInt("watch_id"),
                resultSet.getString("watch_brand"),
                resultSet.getString("series_name"),
                resultSet.getString("model_number"),
                resultSet.getDouble("price"),
                resultSet.getString("description"),
                resultSet.getInt("product_categories_category_id")
        );
    }
}
