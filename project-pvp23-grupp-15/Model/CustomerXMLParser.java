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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CustomerXMLParser {
    public static Customer customer(String xml){
        try {
            // Create a DocumentBuilderFactory and DocumentBuilder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Parse the XML string
            String xmlString = xml;
            InputStream inputStream = new ByteArrayInputStream(xmlString.getBytes("UTF-8"));
            Document document = builder.parse(inputStream);

            Customer customer = new Customer();
            customer.setCustomerNo(document.getDocumentElement().getAttribute("customerNo"));
            customer.setOptLockVersion(document.getDocumentElement().getAttribute("optLockVersion"));
            customer.setFirstName(getElementValue(document, "firstName"));
            customer.setLastName(getElementValue(document, "lastName"));
            customer.setBirthDate(parseDate(getElementValue(document, "birthDate")));
            customer.setStreetAddress(getElementValue(document, "streetAddress", "address"));
            customer.setPostalCode(getElementValue(document, "postalCode", "address"));
            customer.setPostOffice(getElementValue(document, "postOffice", "address"));
            customer.setCountry(getElementValue(document, "country", "address"));
            customer.setCardNumber(getElementValue(document, "number", "bonusCard"));
            customer.setGoodThruMonth(Integer.parseInt(getElementValue(document, "goodThruMonth", "bonusCard")));
            customer.setGoodThruYear(Integer.parseInt(getElementValue(document, "goodThruYear", "bonusCard")));
            customer.setBlocked(Boolean.parseBoolean(getElementValue(document, "blocked", "bonusCard")));
            customer.setExpired(Boolean.parseBoolean(getElementValue(document, "expired", "bonusCard")));
            customer.setHolderName(getElementValue(document, "holderName", "bonusCard"));
            customer.setSex(getElementValue(document, "sex"));
            return customer;

        } catch (ParserConfigurationException | SAXException | IOException | ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getElementValue(Document document, String tagName) {
        NodeList nodeList = document.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return "";
    }

    private static String getElementValue(Document document, String tagName, String parentTagName) {
        NodeList parentList = document.getElementsByTagName(parentTagName);
        if (parentList.getLength() > 0) {
            Element parentElement = (Element) parentList.item(0);
            NodeList nodeList = parentElement.getElementsByTagName(tagName);
            if (nodeList.getLength() > 0) {
                return nodeList.item(0).getTextContent();
            }
        }
        return "";
    }

    private static Date parseDate(String dateStr) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        return dateFormat.parse(dateStr);
    }
}

