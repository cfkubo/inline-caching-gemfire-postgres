// package com.example.ui;

// import com.example.model.Product;
// import com.vaadin.flow.component.grid.Grid;
// import com.vaadin.flow.component.orderedlayout.VerticalLayout;
// import com.vaadin.flow.router.Route;
// import com.vaadin.flow.spring.annotation.SpringComponent;
// import com.vaadin.flow.spring.annotation.UIScope;
// import org.springframework.web.client.RestTemplate;

// import java.util.Arrays;
// import java.util.List;

// @Route("listproducts") // This defines the Vaadin route, e.g., http://localhost:8080/products
// @SpringComponent
// @UIScope
// public class ProductListView extends VerticalLayout {

//     private final RestTemplate restTemplate;

//     public ProductListView(RestTemplate restTemplate) {
//         this.restTemplate = restTemplate;
//         setupUI();
//     }

//     private void setupUI() {
//         Grid<Product> productGrid = new Grid<>(Product.class);
//         productGrid.addColumn(Product::getProd_id).setHeader("ID");
//         productGrid.addColumn(Product::getTitle).setHeader("Title");
//         productGrid.addColumn(Product::getActor).setHeader("Actor");
//         productGrid.addColumn(Product::getPrice).setHeader("Price");
//         productGrid.addColumn(Product::getCategory).setHeader("Category");
//         productGrid.addColumn(Product::getSpecial).setHeader("Special");
//         productGrid.addColumn(Product::getCommon_prod_id).setHeader("Common Prod ID");


//         // Fetch products from the REST API and set them to the grid
//         List<Product> products = fetchProducts();
//         productGrid.setItems(products);
//         productGrid.setSizeFull(); // Make the grid take up the full available space
//         add(productGrid);
//         setSizeFull(); // Make the layout take up the full available space
//         // setPadding(false); // Remove default padding for a cleaner look
//         // setMargin(false); // Remove default margin for a cleaner look
//     }

//     private List<Product> fetchProducts() {
//         // Call the REST API to get the list of products
//         Product[] productsArray = restTemplate.getForObject("http://localhost:9989/api/listproducts", Product[].class);
//         return Arrays.asList(productsArray != null ? productsArray : new Product[0]);
//     }
// }

package com.example.ui;

import com.example.model.Product;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Route("listproducts")
@SpringComponent
@UIScope
public class ProductListView extends VerticalLayout {

    private final RestTemplate restTemplate;
    private final Grid<Product> productGrid = new Grid<>(Product.class);

    public ProductListView(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        setupUI();
    }

    private void setupUI() {
        productGrid.addColumn(Product::getProd_id).setHeader("ID");
        productGrid.addColumn(Product::getTitle).setHeader("Title");
        productGrid.addColumn(Product::getActor).setHeader("Actor");
        productGrid.addColumn(Product::getPrice).setHeader("Price");
        productGrid.addColumn(Product::getCategory).setHeader("Category");
        productGrid.addColumn(Product::getSpecial).setHeader("Special");
        productGrid.addColumn(Product::getCommon_prod_id).setHeader("Common Prod ID");
        productGrid.setSizeFull();

        Button loadButton = new Button("Load Products", event -> loadProducts());
        add(loadButton, productGrid);
        setSizeFull();
    }

    private void loadProducts() {
        try {
            List<Product> products = fetchProducts();
            if (products.isEmpty()) {
                Notification.show("No products found in cache.");
            }
            productGrid.setItems(products);
        } catch (Exception e) {
            Notification.show("Failed to load products: " + e.getMessage());
        }
    }

    private List<Product> fetchProducts() {
        Product[] productsArray = restTemplate.getForObject("http://localhost:9989/api/listproducts", Product[].class);
        return Arrays.asList(productsArray != null ? productsArray : new Product[0]);
    }
}