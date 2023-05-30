import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ElectronicStoreApp extends Application {
    private ElectronicStore model;
    private ElectronicStoreView view;

    public ElectronicStoreApp() {model=ElectronicStore.createStore();} // create an instance of ElectronicStore

    public void start(Stage primaryStage) {
        view = new ElectronicStoreView(model); // create an instance of the view

        // Set up the view with data from the model
        List<String> storeStockItems = new ArrayList<>();
        for (Product p : model.getStock()) {
            if (p != null) {
                storeStockItems.add(p.toString());
            }
        }
        view.getStoreStockList().getItems().setAll(storeStockItems);

        // most popular items
        List<String> mostPopItems = storeStockItems.subList(0, 3);
        view.getMostPopItemsList().getItems().setAll(mostPopItems);

        // TextFields with default values
        view.getsNumberField().setText("0");
        view.getrField().setText("0.00");
        view.getsAmountField().setText("N/A");

        // disable buttons initially
        view.getButtonPane().getAddButton().setDisable(true);
        view.getButtonPane().getRemoveButton().setDisable(true);
        view.getButtonPane().getCompleteButton().setDisable(true);

        // if user select an item in the Store Stock ListView, enable the 'Add to Cart' button
        view.getStoreStockList().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                view.getButtonPane().getAddButton().setDisable(false);
            } else {
                view.getButtonPane().getAddButton().setDisable(true);
            }
        });

        // if user select an item in the Store Stock ListView, enable the 'Remove from Cart' button
        view.getCurrentCartList().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                view.getButtonPane().getRemoveButton().setDisable(false);
            } else {
                view.getButtonPane().getRemoveButton().setDisable(true);
            }
        });

        // if user selects an item in the Most Popular Items ListView, disable the 'Add to Cart' button
        view.getMostPopItemsList().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                view.getButtonPane().getAddButton().setDisable(true);
            }
        });

        updateMostPopList();

        // add button event
        view.getButtonPane().getAddButton().setOnAction(event -> {
            int selectedIndex = view.getStoreStockList().getSelectionModel().getSelectedIndex();
            Product selectedProduct = model.getStock()[selectedIndex];

            // update the stock quantity when products are added to cart
            selectedProduct.setStockQuantity(selectedProduct.getStockQuantity() - 1);

            int cartIndex = view.getCurrentCart().indexOf(selectedProduct);
            if (cartIndex >= 0) {
                view.getpQuantity().set(cartIndex, view.getpQuantity().get(cartIndex) + 1);
            }
            else {
                view.getCurrentCart().add(selectedProduct);
                view.getpQuantity().add(1);
            }

            if (view.getCurrentCart().size() > 0) {
                view.getButtonPane().getCompleteButton().setDisable(false);
            }

            updateCurrentCartList();
            view.updateCartLabel(getTotalCost(view.getCurrentCart(), view.getpQuantity()));
            if (selectedProduct.getStockQuantity() == 0) {
                view.updateStoreStockList();
            }
        });

        // remove button event
        view.getButtonPane().getRemoveButton().setOnAction(actionEvent -> {
            int selectedIndex = view.getCurrentCartList().getSelectionModel().getSelectedIndex();
            Product selectedProduct = view.getCurrentCart().get(selectedIndex);

            // update stock quantity when remove from cart
            selectedProduct.setStockQuantity(selectedProduct.getStockQuantity() + 1);

            int newQuantity = view.getpQuantity().get(selectedIndex) - 1;

            if (newQuantity == 0) {
                view.getCurrentCart().remove(selectedIndex);
                view.getpQuantity().remove(selectedIndex);
            }
            else {
                view.getpQuantity().set(selectedIndex, newQuantity);
            }

            if (view.getCurrentCart().size() == 0) {
                view.getButtonPane().getCompleteButton().setDisable(true);
            }

            updateCurrentCartList();
            view.updateCartLabel(getTotalCost(view.getCurrentCart(), view.getpQuantity()));
            view.updateStoreStockList();
        });

        // update when complete sale button is clicked
        view.getButtonPane().getCompleteButton().setOnAction(event -> {
            double totalCost = getTotalCost(view.getCurrentCart(), view.getpQuantity());
            model.setRevenue(model.getRevenue() + totalCost);
            model.setSales(model.getSales() + 1);
            model.setPricePerSale(model.getRevenue() / model.getSales());

            view.getsNumberField().setText(String.valueOf(model.getSales()));
            view.getrField().setText(String.format("%.2f", model.getRevenue()));
            view.getsAmountField().setText(String.format("%.2f", model.getPricePerSale()));

            for (int i = 0; i < view.getCurrentCart().size(); i++) {
                Product p = view.getCurrentCart().get(i);
                p.setSoldQuantity(p.getSoldQuantity() + view.getpQuantity().get(i));
            }

            updateMostPopList();
            view.getCurrentCart().clear();
            view.getpQuantity().clear();
            updateCurrentCartList();
            view.updateCartLabel(0.0);
        });

        // reset button functions
        view.getButtonPane().getResetButton().setOnAction(event -> {
            model = ElectronicStore.createStore(); // reset the ElectronicStore view

            List<String> storeStockItems1 = new ArrayList<>();
            for (Product p : model.getStock()) {
                if (p != null) {
                    storeStockItems1.add(p.toString());
                }
            }
            view.getStoreStockList().getItems().setAll(storeStockItems1);

            updateMostPopList();

            view.getsNumberField().setText("0");
            view.getrField().setText("0.00");
            view.getsAmountField().setText("N/A");

            view.getCurrentCart().clear();
            view.getpQuantity().clear();

            updateCurrentCartList();
            view.updateCartLabel(0.0);
        });


        // Add the view to the pane
        Pane aPane = new Pane(view);
        primaryStage.setTitle("Electronic Store Application - " + model.getName());
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(aPane, 800, 400));
        primaryStage.show();
    }

    // method to update popular item list
    public void updateMostPopList() {
        List<Product> sortedProducts = new ArrayList<>();
        for (Product p : model.getStock()) {
            if (p != null) {
                sortedProducts.add(p);
            }
        }

        for (int i = 0; i < sortedProducts.size(); i++) {
            for (int j = 0; j < sortedProducts.size() - i - 1; j++) {
                if (sortedProducts.get(j).getSoldQuantity() < sortedProducts.get(j + 1).getSoldQuantity()) {
                    Product temp = sortedProducts.get(j);
                    sortedProducts.set(j, sortedProducts.get(j + 1));
                    sortedProducts.set(j+1, temp);
                }
            }
        }

        List<String> mostPopItems = new ArrayList<>();
        for (int i = 0; i < Math.min(3, sortedProducts.size()); i++) {
            mostPopItems.add(sortedProducts.get(i).toString());
        }
        view.getMostPopItemsList().getItems().setAll(mostPopItems);
    }


    public void updateCurrentCartList() {
        List<String> currentCartItems = new ArrayList<>();
        for (int i = 0; i < view.getCurrentCart().size(); i++) {
            Product p = view.getCurrentCart().get(i);
            if (p != null) {
                currentCartItems.add(String.format("%d x %s", view.getpQuantity().get(i), p));
            }
        }
        view.getCurrentCartList().getItems().setAll(currentCartItems);
    }

    public double getTotalCost(ArrayList<Product> currentCart, ArrayList<Integer> pQuantity) {
        double totalCost = 0;
        for (int i = 0; i < currentCart.size(); i++) {
            totalCost += currentCart.get(i).getPrice() * pQuantity.get(i);
        }
        return totalCost;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
