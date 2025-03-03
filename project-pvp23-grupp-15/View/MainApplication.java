//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package View;

import Controller.CashierController;
import Controller.CustomerController;
import Controller.SalesmanController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {

    CashierController cashierController;
    CustomerController customerController;
    SalesmanController salesmanController;
    public MainApplication() {
    }

    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader1 = new FXMLLoader(this.getClass().getResource("CashierView.fxml"));
        Parent root1 = fxmlLoader1.load();
        cashierController = fxmlLoader1.getController();

        Scene scene = new Scene(root1);
        stage.setTitle("CashierSystem");
        stage.setScene(scene);
        stage.show();

        FXMLLoader fxmlLoader2 = new FXMLLoader(this.getClass().getResource("CustomerView.fxml"));
        Parent root2 = fxmlLoader2.load();
        customerController = fxmlLoader2.getController();

        Scene scene2 = new Scene(root2);
        Stage stage2 = new Stage();
        stage2.setTitle("Customer View");
        stage2.setScene(scene2);
        stage2.show();

        FXMLLoader fxmlLoader3 = new FXMLLoader(this.getClass().getResource("SalemanView.fxml"));
        Parent root3 = fxmlLoader3.load();
        salesmanController = fxmlLoader3.getController();

        Scene scene3 = new Scene(root3);
        Stage stage3 = new Stage();
        stage3.setTitle("Salesman View");
        stage3.setScene(scene3);
        stage3.show();

        cashierController.registerView(this);
        salesmanController.registerView(this);

        customerController.setSale(cashierController.getSale());
        //Komm ihåg att setta salen varje gång man lagar en ny sale annars updateras inte Cusotmer GUI

        salesmanController.getProductCatalogListView().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Your event handling code here
            salesmanController.handleGetProductInfo();
        });
    }

    public void UpdateUI(){
        cashierController.updateUI();
        customerController.updateUI();
    }
    public void reset(){
        cashierController.resetUI();
        customerController.resetUI();
    }

    public void setSale(){
        customerController.setSale(cashierController.getSale() );
    }

    public void updateProductCalalog(){
        cashierController.setProductCatalogListItems(salesmanController.getProductCatalogListView());
    }
    public static void main(String[] args) {
        launch(args);
    }
}
