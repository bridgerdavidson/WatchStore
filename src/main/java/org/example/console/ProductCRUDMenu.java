package org.example.console;

import org.example.models.ProductCategoryModel;
import org.example.models.ProductModel;
import org.example.service.ProductCategoryService;
import org.example.service.ProductService;
import org.example.util.AnsiColors;

import java.io.Console;
import java.util.List;

public class ProductCRUDMenu {
    private final ProductService productService;
    private final ProductCategoryService productCategoryService;

    public ProductCRUDMenu(ProductService productService, ProductCategoryService categoryService) {
        this.productService = productService;
        this.productCategoryService = categoryService;
    }

    public void run() {
        System.out.print(AnsiColors.YELLOW);
        String strId = ConsoleIO.readLine("Enter desired category id: ");
        int id = Integer.parseInt(strId);
        ProductCategoryModel selectedCategory = getProductCategory(id);

        while (true) {
            System.out.println(AnsiColors.RESET);
            ConsoleIO.print("Selected category: ", AnsiColors.WHITE);
            System.out.println(selectedCategory.getCategoryName());
            ConsoleIO.println("Choose an option:", AnsiColors.WHITE);
            ConsoleIO.println("1. List products from selected category", AnsiColors.CYAN);
            ConsoleIO.println("2. List all products", AnsiColors.GREEN);
            ConsoleIO.println("3. Search for a product", AnsiColors.CYAN);
            ConsoleIO.println("4. Add product", AnsiColors.GREEN);
            ConsoleIO.println("5. Edit product", AnsiColors.CYAN);
            ConsoleIO.println("6. Delete product", AnsiColors.GREEN);
            ConsoleIO.println("7. <-- Back to categories", AnsiColors.CYAN);
            System.out.println();

            System.out.print(AnsiColors.YELLOW);
            int choice = ConsoleIO.readInt("Choice: ", 1, 7);
            System.out.println();
            switch (choice) {
                case 1: getProductsByCategoryId(selectedCategory); break;
                case 2: listProducts(); break;
                case 3: searchProduct(); break;
                case 4: addProduct(); break;
                case 5: editProduct(); break;
                case 6: deleteProduct(); break;
                case 7: return;
            }
        }
    }
    private void listProducts() {
        int colorCount = 0;
        List<ProductModel> products = productService.getAllProducts();
        ConsoleIO.println("-- All Products --", AnsiColors.CYAN);
        for (ProductModel p : products) {
            ConsoleIO.printfn(colorCount % 2 == 0? AnsiColors.PURPLE : AnsiColors.BLUE,
                    "ID:%d  | Brand:%s | Series:%s | Model:%s | Price:%s | Category Id:%d",
                    p.getId(),
                    p.getBrand(),
                    p.getSeriesName(),
                    p.getModelNumber(),
                    p.getPrice(),
                    p.getCategoryId()
            );
            colorCount++;
        }
    }

    private void searchProduct() {
        int colorCount = 0;
        String brand = ConsoleIO.readLine("Enter brand or series to search for: ");

        List<ProductModel> products = productService.findByBrandOrSeries(brand);
        ConsoleIO.println("-- Search Results --", AnsiColors.CYAN);
        for (ProductModel p : products) {
            ConsoleIO.printfn(colorCount % 2 == 0? AnsiColors.PURPLE : AnsiColors.BLUE,
                    "ID:%d  | Brand:%s | Series:%s | Model:%s | Price:%s | Category Id:%d%n\n",
                    p.getId(),
                    p.getBrand(),
                    p.getSeriesName(),
                    p.getModelNumber(),
                    p.getPrice(),
                    p.getCategoryId()
            );
            colorCount++;
        }
    }

    private void addProduct() {
        ConsoleIO.println("You chose to add a new product", AnsiColors.WHITE);
        System.out.print(AnsiColors.YELLOW);
        String brand = ConsoleIO.readLine("Brand: ");
        String seriesName = ConsoleIO.readLine("Series Name: ");
        String modelNumber = ConsoleIO.readLine("Model Number: ");
        Double price = ConsoleIO.readDouble("Price: ");
        String description = ConsoleIO.readLine("Description: ");
        int categoryId = ConsoleIO.readInt("Category Id: ");

        ProductModel newProduct = productService.addProduct(brand, seriesName, modelNumber, price, description, categoryId);
        if (newProduct != null) {
            ConsoleIO.println("Product added: " + newProduct.getBrand() + " " + newProduct.getSeriesName(), AnsiColors.GREEN);
        } else {
            ConsoleIO.println("Email already exists. User not added.", AnsiColors.RED);
        }
    }

    private void editProduct() {
        ConsoleIO.println("You chose to edit a product", AnsiColors.WHITE);
        listProducts();
        System.out.print(AnsiColors.YELLOW);
        int id = ConsoleIO.readInt("Enter ID to edit: ");
        ProductModel existing = null;
        for (ProductModel product : productService.getAllProducts()) {
            if (product.getId() == id) existing = product;
        }
        if (existing == null) {
            ConsoleIO.println("Product not found.", AnsiColors.RED);
            return;
        }
        System.out.print(AnsiColors.YELLOW);
        String brand = ConsoleIO.readLine("Brand [" + existing.getBrand() + "]: Leave blank for no change:");
        String seriesName = ConsoleIO.readLine("Series Name [" + existing.getSeriesName() + "]: Leave blank for no change:");
        String modelNumber = ConsoleIO.readLine("Model Number [" + existing.getModelNumber() + "]: Leave blank for no change:");
        String priceStr = ConsoleIO.readLine("Price [" + existing.getPrice() + "]: Leave blank for no change:");
        Double price = priceStr.isBlank() ? null : Double.parseDouble(priceStr); // or BigDecimal
        String description = ConsoleIO.readLine("Description [" + existing.getDescription() + "]: Leave blank for no change:");
        String catStr = ConsoleIO.readLine("Category Id [" + existing.getCategoryId() + "]: Leave blank for no change: ");
        Integer categoryId = catStr.isBlank() ? null : Integer.parseInt(catStr);
        // Default to old values if left blank
        boolean success = productService.editProduct(
                id,
                brand.isEmpty() ? existing.getBrand() : brand,
                seriesName.isEmpty() ? existing.getSeriesName() : seriesName,
                modelNumber.isEmpty() ? existing.getModelNumber() : modelNumber,
                price == null ? existing.getPrice() : price,
                description.isEmpty() ? existing.getDescription() : description,
                categoryId == null ? existing.getCategoryId() : categoryId
        );
        if (success) {
            ConsoleIO.println("Product updated.", AnsiColors.GREEN);
        } else {
            ConsoleIO.println("Could not update product.", AnsiColors.RED);
        }
    }

    private void getProductsByCategoryId(ProductCategoryModel selectedProductCategory) {
        int colorCount = 0;
        int categoryId = selectedProductCategory.getId();
        List<ProductModel> products = productService.getProductsByCategoryId(categoryId);
        ConsoleIO.println("-- Products by " + selectedProductCategory.getCategoryName() + " --", AnsiColors.GREEN);
        for (ProductModel p : products) {
            ConsoleIO.printfn(colorCount % 2 == 0? AnsiColors.PURPLE : AnsiColors.BLUE,
                    "ID:%d  | Brand:%s | Series:%s | Model:%s | Price:%s | Category Id:%d%n\n",
                    p.getId(),
                    p.getBrand(),
                    p.getSeriesName(),
                    p.getModelNumber(),
                    p.getPrice(),
                    p.getCategoryId()
            );
            colorCount++;
        }
    }

    private void deleteProduct() {
        ConsoleIO.println("You chose to delete a product", AnsiColors.WHITE);
        listProducts();
        System.out.print(AnsiColors.YELLOW);
        String id = ConsoleIO.readLine("Enter ID to delete: ");
        boolean success = productService.deleteProduct(id);
        if (success) {
            ConsoleIO.println("Product deleted.", AnsiColors.GREEN);
        } else {
            ConsoleIO.println("Product not found.", AnsiColors.RED);
        }
    }
    private ProductCategoryModel getProductCategory(int id) {
        while (true)
        {
            if(productCategoryService.getProductCategory(id) != null) {
                return productCategoryService.getProductCategory(id);
            }
            else {
                ConsoleIO.println("Category not found.", AnsiColors.RED);
                id = ConsoleIO.readInt("Enter desired category id: ", 1, 1000);
            }
        }
    }
}
