package org.example.console;

import org.example.dao.ProductCategoryDAO;
import org.example.dao.ProductDAO;
import org.example.service.ProductCategoryService;
import org.example.service.ProductService;
import org.example.util.AnsiColors;

public class MainMenu {
    public void run(){
        ProductDAO dao = new ProductDAO();
        ProductService service = new ProductService(dao);
        ProductCategoryDAO categoryDAO = new ProductCategoryDAO();
        ProductCategoryService categoryService = new ProductCategoryService(categoryDAO);

        ConsoleIO.println("Welcome to the watch store!", AnsiColors.CYAN);

        new ProductCategoryCRUDMenu(categoryService).run();
    }

}
