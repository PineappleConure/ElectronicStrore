import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;


public class ElectronicStoreView extends Pane {
    private ListView<String> mostPopItemsList, storeStockList, currentCartList;
    private TextField sNumberField, rField, sAmountField;
    private ButtonPane buttonPane;
    private Label currentCartLabel;
    private ArrayList<Product> currentCart;
    private ElectronicStore model;
    private ArrayList<Integer> pQuantity;

    public ListView<String> getStoreStockList() {return storeStockList;}
    public ListView<String> getMostPopItemsList() {return mostPopItemsList;}
    public ListView<String> getCurrentCartList() {return currentCartList;}
    public TextField getrField() {return rField;}
    public TextField getsAmountField() {return sAmountField;}
    public TextField getsNumberField() {return sNumberField;}
    public ButtonPane getButtonPane() {return buttonPane;}
    public ArrayList<Integer> getpQuantity() {return pQuantity;}


    public ElectronicStoreView(ElectronicStore model) {
        // create labels
        Label label1 = new Label("Store Summary:");
        label1.relocate(40, 10);
        Label label2 = new Label("Store Stock:");
        label2.relocate(330, 10);
        currentCartLabel = new Label("Current Cart ($0.00):");
        currentCartLabel.relocate(610, 10);
        Label label4 = new Label("# Sales:");
        label4.relocate(30, 40);
        Label label5 = new Label("Revenue:");
        label5.relocate(20, 70);
        Label label6 = new Label("$ / Sale:");
        label6.relocate(26, 100);
        Label label7 = new Label("Most Popular Items: ");
        label7.relocate(26, 130);

        currentCart = new ArrayList<>();
        pQuantity = new ArrayList<>();
        this.model = model;

        storeStockList = new ListView<String>();
        storeStockList.relocate(230, 30);
        storeStockList.setPrefSize(270, 300);

        currentCartList = new ListView<String>();
        currentCartList.relocate(510, 30);
        currentCartList.setPrefSize(270, 300);

        mostPopItemsList = new ListView<String>();
        mostPopItemsList.relocate(16, 150);
        mostPopItemsList.setPrefSize(200, 180);

        sNumberField = new TextField();
        sNumberField.relocate(75, 37);
        sNumberField.setPrefSize(141, 27);

        rField = new TextField();
        rField.relocate(75, 67);
        rField.setPrefSize(141, 27);

        sAmountField = new TextField();
        sAmountField.relocate(75, 97);
        sAmountField.setPrefSize(141, 27);

        buttonPane = new ButtonPane();
        buttonPane.relocate(50, 340);

        getChildren().addAll(label1, label2, currentCartLabel, label4, label5, label6, label7, storeStockList,
                currentCartList, mostPopItemsList, sNumberField, rField, sAmountField, buttonPane);

        setPrefSize(800, 400);
    }

    public ArrayList<Product> getCurrentCart() {return currentCart;}

    // update the Current Cart label with amount
    public void updateCartLabel(double totalAmount) {
        currentCartLabel.setText(String.format("Current Cart ($%.2f)", totalAmount));
    }



    public void updateStoreStockList() {
        List<String> storeStockItems = new ArrayList<>();
        for (Product p : model.getStock()) {
            if (p != null && p.getStockQuantity() > 0) {
                storeStockItems.add(p.toString());
            }
        }
        storeStockList.getItems().setAll(storeStockItems);
    }


}
