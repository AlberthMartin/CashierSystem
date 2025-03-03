package Controller;

import Model.Product;
import Model.ProductCatalog;
import View.MainApplication;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.stage.StageStyle;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.StageStyle;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

public class SalesmanController implements Initializable {
    public ListView<Product> productListView;
    public ListView<String> saleHistory;
    private ObservableList<Product> ProductCatalogListItems = FXCollections.observableArrayList();
    private ObservableList<String> theSaleHistory = FXCollections.observableArrayList();
    public Button addProductByBarcodeButton;
    public Button addProductByNameButton;
    public Button FindByKeywordButton;
    public Button setPriceButton;
    public MainApplication view;

    ProductCatalog productCatalog = new ProductCatalog();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productListView.setItems(ProductCatalogListItems);
        saleHistory.setItems(theSaleHistory);
    }

    public ListView<Product> getProductCatalogListView(){
        return this.productListView;
    }




    public void registerView(MainApplication view){
        this.view = view;

    }

    public void handleAddProductByBarcodeButton(){
        Product product;
        String barcode = promptToGetStringFromUser("Get barcode", "Enter barcode");

        //Gets the product from the product catalog
        product = productCatalog.findByBarCode(barcode);
        //Checks if the product exists or if its already added
        if(product != null){
            if(!catalogContainsProduct(productListView, product)){
                //Adds the product to the product to the UI
                ProductCatalogListItems.add(product);
                //Updates the UI for cashierview
                view.updateProductCalalog();
        }   else{
                InfoToSalesman("Product already added");
            }
        }
        else{
            InfoToSalesman("Product not found");
        }
    }

    public void handleAddProductByNameButton() {
        Product product;
        String name = promptToGetStringFromUser("Get name", "Enter name");
        product = productCatalog.findByName(name);
        //Checks if the product exists or if its already added
        if(product != null){
            if(!catalogContainsProduct(productListView, product)){
                //Adds the product to the product to the UI
                ProductCatalogListItems.add(product);
                //Updates the UI for cashierview
                view.updateProductCalalog();
            }   else{
                InfoToSalesman("Product already added");
            }
        }
        else{
            InfoToSalesman("Product not found");
        }
    }

    public void handleAddProductByKeywordButton() {
        Product product;
        String keyword = promptToGetStringFromUser("Get keywords", "Enter keywords");
        product = productCatalog.findByKeyWord(keyword);
        //Checks if the product exists or if its already added
        if(product != null){
            if(!catalogContainsProduct(productListView, product)){
                //Adds the product to the product to the UI
                ProductCatalogListItems.add(product);
                //Updates the UI for cashierview
                view.updateProductCalalog();
            }   else{
                InfoToSalesman("Product already added");
            }
        }
        else{
            InfoToSalesman("Product not found");
        }
    }

    /*
    Sets the price of the selected item in the product catalog
     */
    public void handleSetPrice() {
        List<Product> products = getSelectedProducts();
        for(Product product : products){
            //ProductCatalogListItems.remove(product);
            String price = promptToGetStringFromUser("Get price", "Enter price");
            Double Price = Double.parseDouble(price);
            product.setPrice(Price);
            //ProductCatalogListItems.add(product);
        }
        view.updateProductCalalog();
        view.UpdateUI();
        productListView.refresh();
    }
    /******************
    HELPER METHODS BELOW:
     *******************/
    //plural
    public List<Product> getSelectedProducts(){
        ObservableList<Product> items = productListView.getSelectionModel().getSelectedItems();
        List<Product> products = new ArrayList<>(items);
        return products;
    }
    //singular
    public Product getSelectedProduct(){
        Product item = productListView.getSelectionModel().getSelectedItem();
        return item;
    }

    public void InfoToSalesman(String information){
        Label infoLabel = new Label(information);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.getDialogPane().setContent(infoLabel);

        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

        alert.showAndWait();
    }
    public String promptToGetStringFromUser(String Title, String ContextText) {
        TextInputDialog dialog = new TextInputDialog();

        dialog.setTitle(Title);
        dialog.setHeaderText(null);
        dialog.setContentText(ContextText);
        Optional<String> result = dialog.showAndWait();
        if(result.isPresent()) {
            return result.get();
        } else {
            return null;
        }
    }

    public boolean catalogContainsProduct(ListView<Product> catalog, Product productToFind){
        for(Product product : catalog.getItems()){
            if(product.toString().equals(productToFind.toString())){
                return true;
            }
        }
        return false;
    }
    public double roundToTwoDecimals(double value) {
        return (double) Math.round(value * 100) / 100;
    }

    //Sets discount on selected products, and the duration of discount(special offer)
    public void handleSetDiscount(ActionEvent actionEvent) {
        //Marks a product as having a offer
        String specialOffer = "*";
        //Gets discount percentage from salesman
        String procent = promptToGetStringFromUser("Discount", "Set discount percentage (exclude %)");
        double procentDouble = Double.parseDouble(procent);

        //Gets the discount duration
        String duration = promptToGetStringFromUser("Discount duration", "Set discount duration in minutes");
        double durationDouble = Double.parseDouble(duration);

        //Salesman selects the type of the discount
        //"everyone" or "bonusc"
        String customerType = showCustomerTypePrompt();

        //Gets the selected products from the product catalog
        List<Product> productsToDiscount = getSelectedProducts();

        //Checks if any products is selected
        if (productsToDiscount.isEmpty()) {
            InfoToSalesman("No products selected");
        } else {
            for (Product product : productsToDiscount) {
                //Calculates the discounted price
                double discountedPrice = product.getPrice() * ((100 - procentDouble) / 100);
                //Stores the discounted price
                if(customerType.equals("everyone")){
                    product.setDiscountType("everyone");
                    product.setDiscountForEveryonePrice(roundToTwoDecimals(discountedPrice));
                }
                else if (customerType.equals("bonusc")){
                    product.setDiscountType("bonusc");
                    product.setBonusCustomerPrice(roundToTwoDecimals(discountedPrice));
                }
                else{
                    InfoToSalesman("Something went wrong when discounting the products");
                }
                /****
                 * Rabaterade priset syns endast när produkten läggs till i
                 * köpet (annars finns det bara en stjärna vid producten), och så att om det är
                 * frågan om ett pris som endast är till för bonuskunder
                 * så ändras priset först efter att man swipat bonus kortet och det accepterats
                 */
                product.setName(product.getName() + specialOffer);
                productListView.refresh();

            }
        }
        view.updateProductCalalog();
        view.UpdateUI();

        // Start the timer for the duration of the discount
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (Product product : productsToDiscount) {
                    //Reverses the discounted price back to normal
                    double normalPrice = product.getDiscountPrice() / ((100 - procentDouble) / 100);
                    double normalPrice2Decimals = roundToTwoDecimals(normalPrice);

                    if(customerType.equals("everyone")){
                        product.setDiscountType("");
                        product.setDiscountForEveryonePrice(roundToTwoDecimals(normalPrice2Decimals));
                        product.setName(product.getName().replace(specialOffer, ""));
                    }
                    else if (customerType.equals("bonusc")){
                        product.setDiscountType("");
                        product.setBonusCustomerPrice(roundToTwoDecimals(normalPrice2Decimals));
                        product.setName(product.getName().replace(specialOffer, ""));
                    }
                    else{
                        InfoToSalesman("Something went wrong when discounting the products");
                    }

                }
            }
        }, (long) (durationDouble * 60 * 1000));
        view.updateProductCalalog();
        view.UpdateUI();
    }

    public String showCustomerTypePrompt() {
        ButtonType forEveryoneButtonType = new ButtonType("For Everyone");
        ButtonType forBonusCustomersButtonType = new ButtonType("For Bonus Customers");

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Choose Customer Type");
        alert.setHeaderText("Select the customer type:");
        alert.getButtonTypes().setAll(forEveryoneButtonType, forBonusCustomersButtonType);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent()) {
            if (result.get() == forEveryoneButtonType) {
                return "everyone";
            } else if (result.get() == forBonusCustomersButtonType) {
                return "bonusc";
            }
        }
        // Default return value if the dialog is closed or no option is selected
        return "None";
    }

    public void handleGetProductInfo() {
        theSaleHistory.clear();
        String amountSold = getSelectedProduct().getSoldProducts() + " " + getSelectedProduct().getName() + " sold in total.";
        theSaleHistory.add(amountSold);
        //theSaleHistory
        for(String info : getSelectedProduct().getProductSaleHistoryList()){
            theSaleHistory.add(info);
        }
        view.updateProductCalalog();
        view.UpdateUI();
    }
}

