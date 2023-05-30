import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class ButtonPane extends Pane{

    private Button resetButton, addButton, removeButton, completeButton;

    public Button getAddButton() {return addButton;}
    public Button getCompleteButton() {return completeButton;}
    public Button getRemoveButton() {return removeButton;}
    public Button getResetButton() {return resetButton;}

    public ButtonPane() {
        Pane innerPane = new Pane();

        resetButton = new Button("Reset Store");
        resetButton.setStyle("-fx-font: 12 arial; -fx-base: rgb(215,215,215); -fx-text-fill: rgb(0,0,0);");
        resetButton.relocate(0, 0);
        resetButton.setPrefSize(130, 40);

        addButton = new Button("Add to Cart");
        addButton.setStyle("-fx-font: 12 arial; -fx-base: rgb(215,215,215); -fx-text-fill: rgb(0,0,0);");
        addButton.relocate(250, 0);
        addButton.setPrefSize(130, 40);

        removeButton = new Button("Remove from Cart");
        removeButton.setStyle("-fx-font: 12 arial; -fx-base: rgb(215,215,215); -fx-text-fill: rgb(0,0,0);");
        removeButton.relocate(470, 0);
        removeButton.setPrefSize(130, 40);

        completeButton = new Button("Complete Sale");
        completeButton.setStyle("-fx-font: 12 arial; -fx-base: rgb(215,215,215); -fx-text-fill: rgb(0,0,0);");
        completeButton.relocate(600, 0);
        completeButton.setPrefSize(130, 40);

        innerPane.getChildren().addAll(resetButton, addButton, removeButton, completeButton);

        getChildren().addAll(innerPane);
    }

}
