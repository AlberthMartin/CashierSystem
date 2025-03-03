package Controller;

import Model.*;
import View.MainApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;
import javafx.application.Platform;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.*;

public class CashierController implements Initializable {


    public Label infoLabel;
    public Label infoToUserLabel;
    public Button paymentStatusButton;
    public ListView<Product> productCatalogListView;
    private ObservableList<Product> productCatalogListItems;
    private Sale sale;
    private MainApplication view;
    private List<Sale> SaleList;
    @FXML
    private TextField BarcodeTextField;
    @FXML
    private CheckBox BonusRegisteredCheckBox;
    @FXML
    private ListView<Product> productsListView;
    // The products in the list
    private ObservableList<Product> listItems;
    @FXML
    private Button addItemButton;
    @FXML
    private Button removeItemButton;
    @FXML
    public Label totalPriceLabel;
    public CustomerRegister customerRegister = new CustomerRegister();

    public CashierController(){
        this.SaleList = new ArrayList<>();
        this.sale = new Sale();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //To be able to display the products that are added to the list
        listItems = FXCollections.observableArrayList();
        productsListView.setItems(listItems);
        productsListView.setCellFactory(param -> new ListCell<>(){
            @Override
            protected void updateItem(Product product, boolean empty){
                super.updateItem(product, empty);
                if (empty || product == null) {
                    setText(null);
                } else {
                    String priceText = "";
                    if (sale.isBonusRegistered() && product.getDiscountType().equals("bonusc")) {
                        priceText = product.getBonusCustomerPrice()+ " € ";
                    }
                    else if(product.getDiscountType().equals("everyone")){
                        priceText = product.getDiscountForEveryonePrice()+ " € ";
                    }
                    else if(product.getDiscountType().equals("temp")){
                        priceText = product.getTemporaryDiscountPrice()+ " € ";
                    }
                    else {
                        priceText = product.getPrice()+ " €";
                    }
                    setText(product.getName() + " " + priceText);
                }
            }
        });
        //Product catalog list items
        productCatalogListItems = FXCollections.observableArrayList();
        productCatalogListView.setItems(productCatalogListItems);
    }
    public void registerView(MainApplication view){
        this.view = view;
    }

    public Sale getSale(){
        return sale;
    }

    public void setProductCatalogListItems(ListView<Product> productcatalogitems){
        //Clears the list
        productCatalogListItems.clear();
        //Adds all the products from the salesman controller to the cashiercontroller
        for(Product product : productcatalogitems.getItems()){
            productCatalogListItems.add(product);
        }
    }

    //Removes the selected items from the listview
    @FXML
    private void handleButtonRemoveItemClick(){
        //Removes the selected product from the sale
        List<Product> products = getSelectedProductsFromSale();

        for(Product product : products){
            if(product.getDiscountType().equals("everyone")){
                sale.removeDiscountedProduct(product);
            }
            else if(product.getDiscountType().equals("bonusc")){
                sale.removeDiscountedProduct(product);
            }
            else{
                sale.removeProduct(product);
            }
        }
        view.UpdateUI();
    }

    /*Adds a product to the sale by:
    1. Barcode in textfield
    2. Selected product in productCatalogListView
    3. By getting the barcode from a barcode scanner
     */
    @FXML
    private void handleAddProductToSale(){
        Product selectedProductInProductCatalog = getSelectedProductFromProductCatalog();

        // Trim to remove leading/trailing whitespace
        String barcode = BarcodeTextField.getText().trim();
        //If cashier has written the barcode in the barcodetextfield
        if(!barcode.isEmpty()){
            //Finds the product in the product catalog
            Product product = findByBarCode(barcode);
            if(product != null){
                //Adds the product to the sale
                if(product.getDiscountType().equals("everyone")){
                    sale.addDiscountedProduct(product);
                }
                else if(product.getDiscountType().equals("bonusc")&& sale.isBonusRegistered()){
                    sale.addDiscountedProduct(product);
                }
                else{
                    sale.addProduct(product);
                }
                //Clear the textfield
                BarcodeTextField.clear();
                //Updates the view
                view.UpdateUI();
            }
        }

        //If the cashier has a product selected in the productCatalog
        if(selectedProductInProductCatalog != null){

            //Adds the product to the sale
            if(selectedProductInProductCatalog.getDiscountType().equals("everyone")){
                sale.addDiscountedProduct(selectedProductInProductCatalog);
            }
            else if(selectedProductInProductCatalog.getDiscountType().equals("bonusc")&& sale.isBonusRegistered()){
                sale.addDiscountedProduct(selectedProductInProductCatalog);
            }
            else{
                sale.addProduct(selectedProductInProductCatalog);
            }
            //Deselects the selected product in the produCatalog
            productCatalogListView.getSelectionModel().clearSelection();

            //Updates the view
            view.UpdateUI();
        }

        /*
        Koden om det skulle finnas en barcode scanner:
        
        String barcode = BarcodeScanner.getBarcode()

         if(!barcode.isEmpty()){
            //Finds the product in the product catalog
            Product product = findByBarCode(barcode);
            if(product != null){
                //Adds the product to the sale
                if(product.getDiscountType().equals("everyone")){
                    sale.addDiscountedProduct(product);
                }
                else if(product.getDiscountType().equals("bonusc")&& sale.isBonusRegistered()){
                    sale.addDiscountedProduct(product);
                }
                else{
                    sale.addProduct(product);
                }

                //Updates the view
                view.UpdateUI();
            }
          }
        }
         */
    }

    //Stores the current sale and starts a new one
    @FXML
    private void handleHoldSaleButton(){
        //Adds the sale to the sale's to store it
        SaleList.add(this.sale);
        //Starts a new sale
        handleStartNewSaleButton();
    }
    //Restores the held sale should only be called when no other sale is active:
    @FXML
    private void handleGetBackSaleButton(){
        if(listItems.isEmpty()){
            //Gets back the stored sale
            this.sale = SaleList.get(0);
            //Sets the sale so that the customerController and CashierController has the same sale
            view.setSale();
            //Updates customer and cashier view
            view.UpdateUI();
        }
        else{
            InfoToCashier("Finnish the current sale first");
        }
    }
    //Cancel the current sale and starts a new sale
    @FXML
    private void handleStartNewSaleButton(){
        view.reset();
        //removes the temporary discounts
        removeTemporaryDiscount(sale.getProductList());
        //Automatically starts a new sale
        this.sale = new Sale();
        //Sets the sale to customer controller
        view.setSale();
        //Updates the view for cashier and customer
        view.UpdateUI();

    }
    @FXML
    private void handlePayWithCashButton(){
        //Gets the sum that the customer pays in cash with
        double PaidAmount = promptForGetDoubleFromUser("Pay with cash", "Enter sum");
        //Opens the cashbox
        Cashbox.openCashbox();
        //If the customer gives to much money
        if(PaidAmount>sale.getTotalPrice()){
            double toBeReturned = PaidAmount - sale.getTotalPrice();
            InfoToCashier("Return" + roundToTwoDecimals(toBeReturned)+" € to customer");
            //Updates the price of the sale to 0
            sale.setTotalPrice(0);
            totalPriceLabel.setText("Price: 0");
        }
        //If the customer pays the exact sum or to little
        else if(PaidAmount<=sale.getTotalPrice()){
            sale.setTotalPrice(sale.getTotalPrice() - PaidAmount);
            InfoToCashier("The customer still need to pay: "+ roundToTwoDecimals(sale.getTotalPrice()));
            view.UpdateUI();
        }

    }
    @FXML
    private void handlePayWithCardButton(){
        //Resets the cardreader if there is anyting left from earlier.
        CardReader.resetPayment();
        //Gets amount that should be paid from cashier

        String amount = promptToGetStringFromUser("Pay with card", "Enter sum");

        CardReader.waitingForPayment(amount);
        //Displays the info to the cashier
        infoLabel.setText("Customer can now pay with card: " + amount);

        //Sets a timer to check the status of the card reader every 3 seconds
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    String currentStatus = CardReader.paymentStatus();
                    //If the payment is OK
                    if ("done".equals(currentStatus)) {
                        //Checks if the payment is accepted
                        if(CardReader.getPaymentStateFromPaymentResult().toLowerCase().equals("accepted")) {
                            double PaidAmount = Double.parseDouble(amount);
                            //Updates the price of the sale
                            sale.setTotalPrice(sale.getTotalPrice() - PaidAmount);
                            infoLabel.setText("Customer paid " + amount);
                            view.UpdateUI();
                            //cancels the timer
                            timer.cancel();
                        }else {
                            infoLabel.setText("Card payment failed due to: "
                                    + CardReader.getPaymentStateFromPaymentResult());
                            timer.cancel();
                        }

                    }
                });
            }
        }, 0, 3000);//Checks the status every 3 seconds

    }


    @FXML
    private void handleSwipeBonusCardButton(){
        CardReader.resetPayment();
        CardReader.waitingForPayment("0");
        infoLabel.setText("Customer can now swipe bonus card");
        List<Product> productsToAddDiscountTo = new ArrayList<>();

        //Sets a timer to check if the card has been swiped
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    //If the card reader is in DONE state (only time you can get the bonusCard result from it)
                    if ("done".equals(CardReader.paymentStatus())) {
                        //Checks if the card is swiped and accepted
                        if(CardReader.getBonusStateFromPaymentResult().toLowerCase().equals("accepted")) {
                            //if the card is accepted
                            //Sets so that the sale is registered as a bonus sale
                            sale.setBonusRegistered(true);
                            //Updates the prices in the sale

                            for (Product product : sale.getProductList()) {
                                if (product.getDiscountType().equals("bonusc")) {
                                    productsToAddDiscountTo.add(product);
                                }
                            }
                            //Finds the customer, (gets CuNr. from CardReader and add the goodtru month and year manually)
                            Customer customer = customerRegister.findByBonusCard(
                                    CardReader.getBonusCardNumberFromPaymentResult(),"2024","6");
                            //Sets the bonus points
                            customer.addBonusPoints(sale.getTotalPrice()*0.05);
                            //BonusPoints with only 2 decimals
                            String bonuspoints = doubleToStringWithTwoDecimals(sale.getTotalPrice()*0.05);
                            //Display it
                            infoLabel.setText("Customer got: "+ bonuspoints+" bonuspoints");

                            for (Product product : productsToAddDiscountTo) {
                                sale.removeProduct(product);
                                sale.addDiscountedProduct(product);
                            }

                            //Updates the UI
                            view.UpdateUI();
                            //Cancels the timer
                            timer.cancel();

                        }else {
                            //If the card swipe failed
                            infoLabel.setText("Card swipe failed due to: "
                                    + CardReader.getBonusStateFromPaymentResult());
                            timer.cancel();
                        }

                    }
                });
            }
        }, 0, 3000);
    }
    @FXML
    private void handleOpenCashBoxButton(){
        Cashbox.openCashbox();
    }
    @FXML
    private void handlePrintReceiptButton(){
        sale.printReceipt();
    }
    @FXML
    private void handleCancelPaymentButton(){
        CardReader.abortPayment();
        CardReader.resetPayment();
        infoLabel.setText("Payment aborted");
    }
    @FXML
    public void handleAddDiscountButton() {
        double discountAmount = promptForGetDoubleFromUser("Discount selected products", "Enter percentage");
        //Tar de selectade producterna i carten
        List<Product> productsToDiscount = getSelectedProductsFromSale();

        if(productsToDiscount.isEmpty()){
            InfoToCashier("Select a product in the sale!");
            return;
        }
        for(Product product : productsToDiscount){
            double originalPrice = product.getPrice();
            double discount = originalPrice * (discountAmount / 100.0);
            double discountedPrice = originalPrice - discount;

            // Ensure that the discounted price doesn't go below zero
            if (discountedPrice < 0.0) {
                discountedPrice = 0.0;
            }
            //Stores the discount type
            product.setDiscountType("temp");

            product.setTemporaryDiscountPrice(roundToTwoDecimals(discountedPrice));

            //Updates the price of the sale
            sale.setTotalPrice(sale.getTotalPrice()-discount);
        }
        // Update the UI only in cashierView
        view.UpdateUI();
        }


    public void updateUI(){
        listItems.setAll(sale.getProductList());
        //Makes the price only have 2 decimals
        String totalPrice = doubleToStringWithTwoDecimals(sale.getTotalPrice());
        totalPriceLabel.setText("Total price: "+ totalPrice+ " €");
        //Sets the checkbox to checked if the sale is registered as a bonus sale
        if(sale.isBonusRegistered()){
            BonusRegisteredCheckBox.setSelected(true);
        }
        productsListView.refresh();


    }
    public void resetUI(){
        //Removes all the temporary discounts
        removeTemporaryDiscount(sale.getProductList());
        listItems.clear();
        totalPriceLabel.setText("Price: 0 €");
        BonusRegisteredCheckBox.setSelected(false);
        infoLabel.setText("");
        productsListView.refresh();


    }

    /*****************
    HELPER METHODS BELOW
     ****************/
    /*
    Pop up window to get the amount to pay inputed from the cashier.
     */
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
    public Double promptForGetDoubleFromUser(String Title, String ContextText) {
        TextInputDialog dialog = new TextInputDialog();

        dialog.setTitle(Title);
        dialog.setHeaderText(null);
        dialog.setContentText(ContextText);
        Optional<String> result = dialog.showAndWait();
        if(result.isPresent()) {
            return Double.parseDouble(result.get());
        } else {
            return null;
        }
    }
    /*
    Is used to display some kind of information to the user
     */
    public void InfoToCashier(String information){
        Label infoLabel = new Label(information);

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.getDialogPane().setContent(infoLabel);

        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

        alert.showAndWait();
    }

    //Returns the selected products in the listview
    public List<Product> getSelectedProductsFromSale(){
        ObservableList<Product> items = productsListView.getSelectionModel().getSelectedItems();
        List<Product> products = new ArrayList<>(items);
        return products;
    }
    //Returns one selected item from the product catalog
    public Product getSelectedProductFromProductCatalog(){
        ObservableList<Product> items = productCatalogListView.getSelectionModel().getSelectedItems();
        List<Product> products = new ArrayList<>(items);

        if(items.isEmpty()){
            return null;
        }
        return products.get(0);
    }
    //Finds a product in the productcatalogList by the barcode
    public Product findByBarCode(String barcode){
        for(Product product : productCatalogListView.getItems()){
            if(product.getBarCode().equals(barcode)){
                return product;
            }
        }
        return null;
    }
    public String doubleToStringWithTwoDecimals(double Double){
        // Define the format to limit to two decimal places
        DecimalFormat df = new DecimalFormat("#.##");
        // Format the number
        String formattedNumber = df.format(Double);
        return formattedNumber;
    }
    public double roundToTwoDecimals(double value) {
        return (double) Math.round(value * 100) / 100;
    }
    public void removeTemporaryDiscount(List<Product> products){
        for(Product product : products){
            if(product.getDiscountType().equals("temp")){
                product.setDiscountType("");
            }
        }
    }


}