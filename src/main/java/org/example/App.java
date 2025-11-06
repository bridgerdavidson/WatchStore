package org.example;

import org.example.console.MainMenu;
import org.example.console.ProductCRUDMenu;
import org.example.console.ProductCategoryCRUDMenu;
import org.example.dao.ProductCategoryDAO;
import org.example.dao.ProductDAO;
import org.example.service.ProductCategoryService;
import org.example.service.ProductService;

public class App 
{
    public static void main( String[] args )
    {
        MainMenu mainMenu = new MainMenu();
        mainMenu.run();
    }
}
