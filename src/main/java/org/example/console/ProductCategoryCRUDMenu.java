package org.example.console;

import org.example.dao.ProductDAO;
import org.example.models.ProductCategoryModel;
import org.example.service.ProductCategoryService;
import org.example.service.ProductService;
import org.example.util.AnsiColors;

import java.util.List;

public class ProductCategoryCRUDMenu {
    private final ProductCategoryService productCategoryService;

    public ProductCategoryCRUDMenu(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    public void run() {
        while (true) {
            System.out.println();
            ConsoleIO.println("Choose an option:", AnsiColors.WHITE);
            ConsoleIO.println("1. List watch categories", AnsiColors.CYAN);
            ConsoleIO.println("2. Find products by selected category", AnsiColors.GREEN);
            ConsoleIO.println("3. Search for a watch category", AnsiColors.CYAN);
            ConsoleIO.println("4. Add watch category", AnsiColors.GREEN);
            ConsoleIO.println("5. Edit watch category", AnsiColors.CYAN);
            ConsoleIO.println("6. Delete watch category", AnsiColors.GREEN);
            ConsoleIO.println("7. Exit watch store", AnsiColors.CYAN);

            System.out.println(AnsiColors.YELLOW);
            int choice = ConsoleIO.readInt("Choice: ", 1, 7);
            switch (choice) {
                case 1: listProductCategories(); break;
                case 2: findProductsByCategory(); break;
                case 3: searchProductCategories(); break;
                case 4: addProductCategory(); break;
                case 5: editProductCategory(); break;
                case 6: deleteProductCategory(); break;
                case 7: return;
            }
        }
    }
    private void listProductCategories() {
        int colorCount = 0;
        List<ProductCategoryModel> products = productCategoryService.getAllProductCategories();
        ConsoleIO.println("-- All Categories --", AnsiColors.WHITE);
        for (ProductCategoryModel p : products) {
            ConsoleIO.printfn(colorCount % 2 == 0? AnsiColors.PURPLE : AnsiColors.BLUE, "ID:%d  | Category:%s", p.getId(), p.getCategoryName());
            colorCount++;
        }
    }

    private void findProductsByCategory() {
        listProductCategories();
        ProductDAO productDAO = new ProductDAO();
        ProductService productService = new ProductService(productDAO);
        new ProductCRUDMenu(productService, productCategoryService).run();
    }

    private void searchProductCategories() {
        int id = ConsoleIO.readInt("Enter the id of the category to search for: ");

        ProductCategoryModel products = productCategoryService.findById(id);
        ConsoleIO.println("-- Search Results --", AnsiColors.CYAN);
            ConsoleIO.printfn(AnsiColors.PURPLE,
                    "ID:%d  | Category:%s",
                    products.getId(),
                    products.getCategoryName());

    }

    private void addProductCategory() {
        ConsoleIO.println("You chose to add a new product category", AnsiColors.GREEN);
        String name = ConsoleIO.readLine("Category: ");

        ProductCategoryModel newProduct = productCategoryService.addProductCategory(name);
        if (newProduct != null) {
            ConsoleIO.println("Product category added: " + name, AnsiColors.GREEN);
        } else {
            ConsoleIO.println("Product category not added.", AnsiColors.RED);
        }
    }

    private void editProductCategory() {
        ConsoleIO.println("You chose to edit a product category", AnsiColors.WHITE);
        listProductCategories();
        System.out.print(AnsiColors.YELLOW);
        int id = ConsoleIO.readInt("Enter ID to edit: ");
        System.out.print(AnsiColors.RESET);
        ProductCategoryModel existing = null;
        for (ProductCategoryModel product : productCategoryService.getAllProductCategories()) {
            if (product.getId() == id) existing = product;
        }
        if (existing == null) {
            ConsoleIO.println("Product category not found.", AnsiColors.RED);
            return;
        }
        System.out.print(AnsiColors.YELLOW);
        String name = ConsoleIO.readLine("Category [" + existing.getCategoryName() + "]: Leave blank for no change:");
        // Default to old values if left blank
        boolean success = productCategoryService.editProductCategory(
                id,
                name.isEmpty() ? existing.getCategoryName() : name
        );
        if (success) {
            ConsoleIO.println("Product category updated.", AnsiColors.GREEN);
        } else {
            ConsoleIO.println("Could not update product category.", AnsiColors.RED);
        }
    }

    private void deleteProductCategory() {
        ConsoleIO.println("You chose to delete a product category", AnsiColors.WHITE);
        listProductCategories();
        System.out.print(AnsiColors.YELLOW);
        String id = ConsoleIO.readLine("Enter ID to delete: ");
        boolean success = productCategoryService.deleteProductCategory(id);
        if (success) {
            ConsoleIO.println("Product category deleted.", AnsiColors.GREEN);
        } else {
            ConsoleIO.println("Product category not found.", AnsiColors.RED);
        }
    }
}
