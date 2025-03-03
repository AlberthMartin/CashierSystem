package Model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ProductXMLParser {
    public Product product(String Xml) {
        Product productInfo = null;
        try {
            // Create a DocumentBuilderFactory and DocumentBuilder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Parse the XML string
            String xmlString = Xml;
            InputStream inputStream = new ByteArrayInputStream(xmlString.getBytes("UTF-8"));
            Document document = builder.parse(inputStream);

            // Get the list of product elements
            NodeList productList = document.getElementsByTagName("product");

            // Create a list to store the extracted values
            List<Product> productInfoList = new ArrayList<>();

            // Iterate through the product elements
            for (int i = 0; i < productList.getLength(); i++) {
                Element productElement = (Element) productList.item(i);

                // Extract the values
                String name = productElement.getElementsByTagName("name").item(0).getTextContent();
                String vat = productElement.getElementsByTagName("vat").item(0).getTextContent();
                String keyword = productElement.getElementsByTagName("keyword").item(0).getTextContent();
                String barCode = productElement.getElementsByTagName("barCode").item(0).getTextContent();

                // Create a ProductInfo object and add it to the list
                productInfo = new Product(name, vat, keyword, barCode);
                productInfoList.add(productInfo);
            }


        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return productInfo;
    }
}

