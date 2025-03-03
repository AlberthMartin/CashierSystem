package Model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SaleTest {
    private Sale sale;
    private Product product1;
    private Product product2;

    @Before
    public void setUp() {
        sale = new Sale();
        product1 = new Product("Product1", 10.0);
        product2 = new Product("Product2", 15.0);
    }

    @Test
    public void testAddProduct() {
        sale.addProduct(product1);
        assertEquals(10.0, sale.getTotalPrice(), 0.01);
    }

    @Test
    public void testRemoveProduct() {
        sale.addProduct(product1);
        sale.addProduct(product2);
        sale.removeProduct(product1);
        assertEquals(15.0, sale.getTotalPrice(), 0.01);
    }

    @Test
    public void testAddDiscountedProductWithEveryoneDiscount() {
        product1.setDiscountType("everyone");
        product1.setDiscountForEveryonePrice(5.0);
        sale.addDiscountedProduct(product1);
        assertEquals(5.0, sale.getTotalPrice(), 0.01);
    }

    @Test
    public void testAddDiscountedProductWithBonusCustomerDiscount() {
        sale.setBonusRegistered(true);
        product1.setDiscountType("bonusc");
        product1.setBonusCustomerPrice(7.0);
        sale.addDiscountedProduct(product1);
        assertEquals(7.0, sale.getTotalPrice(), 0.01);
    }

    @Test
    public void testPrintReceipt() {
        sale.addProduct(product1);
        sale.addProduct(product2);
        sale.printReceipt(); // You can't test the actual file creation, but you can ensure no exceptions are thrown
    }

    @Test
    public void testReceiptProducts() {
        sale.addProduct(product1);
        sale.addProduct(product2);
        String expectedReceipt = "Product1 10.0\nProduct2 15.0\n";
        assertEquals(expectedReceipt, sale.ReceiptProducts());
    }

    @Test
    public void testReceiptTotPrice() {
        sale.addProduct(product1);
        sale.addProduct(product2);
        assertEquals("25.0", sale.ReceiptTotPrice());
    }

    @Test
    public void testBonusRegistered() {
        sale.setBonusRegistered(true);
        assertTrue(sale.isBonusRegistered());
    }

    @Test
    public void testSetBonusRegistered() {
        sale.setBonusRegistered(true);
        assertTrue(sale.isBonusRegistered());
    }
}