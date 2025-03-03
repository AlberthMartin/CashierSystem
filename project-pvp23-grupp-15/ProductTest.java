package Model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

public class ProductTest {
    private Product product;

    @Before
    public void setUp() {
        product = new Product("TestProduct", 10.0);
    }

    @Test
    public void testSetAndGetPrice() {
        product.setPrice(20.0);
        assertEquals(20.0, product.getPrice(), 0.01);
    }

    @Test
    public void testSetAndGetAttributes() {
        product.setName("NewName");
        product.setVat("NewVAT");
        product.setKeyword("NewKeyword");
        product.setBarCode("NewBarCode");

        assertEquals("NewName", product.getName());
        assertEquals("NewVAT", product.getVat());
        assertEquals("NewKeyword", product.getKeyword());
        assertEquals("NewBarCode", product.getBarCode());
    }

    @Test
    public void testAddToSoldProducts() {
        product.addToSoldProducts();
        assertEquals(1, product.getSoldProducts());
    }

    @Test
    public void testSubToSoldProducts() {
        product.addToSoldProducts();
        product.addToSoldProducts();
        product.subToSoldProducts();
        assertEquals(1, product.getSoldProducts());
    }

    @Test
    public void testSaleHistory() {
        product.saleHistory(new Date());
        assertFalse(product.getProductSaleHistoryList().isEmpty());
    }

    @Test
    public void testDiscountPrice() {
        product.setDiscountType("everyone");
        product.setDiscountForEveryonePrice(8.0);
        assertEquals(8.0, product.getDiscountForEveryonePrice(), 0.01);

        product.setDiscountType("bonusc");
        product.setBonusCustomerPrice(7.0);
        assertEquals(7.0, product.getBonusCustomerPrice(), 0.01);
    }

    @Test
    public void testToString() {
        assertEquals("TestProduct 10.0 €", product.toString());
    }
    @Test
    public void testDefaultAttributes() {
        // Ensure default attributes have appropriate initial values
        assertEquals(0, product.getSoldProducts());
        assertEquals("", product.getDiscountType());
        assertEquals(0.0, product.getDiscountForEveryonePrice(), 0.01);
        assertEquals(0.0, product.getBonusCustomerPrice(), 0.01);
        assertEquals(0.0, product.getDiscountPrice(), 0.01);
        assertTrue(product.getProductSaleHistoryList().isEmpty());
    }

    @Test
    public void testAddToSoldProductsAndSubToSoldProducts() {
        // Test adding and subtracting from sold products
        product.addToSoldProducts();
        product.addToSoldProducts();
        assertEquals(2, product.getSoldProducts());

        product.subToSoldProducts();
        assertEquals(1, product.getSoldProducts());

        product.subToSoldProducts(); // Should not go below 0
        assertEquals(0, product.getSoldProducts());
    }

    @Test
    public void testDiscountPriceWithInvalidDiscountType() {
        // Test setting an invalid discount type
        product.setDiscountType("invalidType");
        assertEquals(0.0, product.getDiscountPrice(), 0.01);
    }
}