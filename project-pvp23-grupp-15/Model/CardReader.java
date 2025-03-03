package Model;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class CardReader {

    private static String waitingForThePayment = "http://localhost:9002/cardreader/waitForPayment";
    private static String abortThePayment = "http://localhost:9002/cardreader/abort";
    private static String thePaymentStatus = "http://localhost:9002/cardreader/status";
    private static String thePaymentResult = "http://localhost:9002/cardreader/result";
    private static String theResetPayment = "http://localhost:9002/cardreader/reset";

    /**********
     * To get info from the card reader if it is in done state:
     *
     */
    public static String getBonusCardNumberFromPaymentResult() {
        HttpResponse<String> get = httpGET(thePaymentResult);
        String xmlResponse = get.body();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xmlResponse)));

            Element root = document.getDocumentElement();

            NodeList bonusCardNumberList = root.getElementsByTagName("bonusCardNumber");
            if (bonusCardNumberList.getLength() > 0) {
                return bonusCardNumberList.item(0).getTextContent();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Unknown";
    }

    public static String getPaymentStateFromPaymentResult() {
        HttpResponse<String> get = httpGET(thePaymentResult);
        String xmlResponse = get.body();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xmlResponse)));

            Element root = document.getDocumentElement();

            NodeList paymentStateList = root.getElementsByTagName("paymentState");
            if (paymentStateList.getLength() > 0) {
                return paymentStateList.item(0).getTextContent();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Unknown";
    }

    public static String getBonusStateFromPaymentResult() {
        HttpResponse<String> get = httpGET(thePaymentResult);
        String xmlResponse = get.body();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xmlResponse)));

            Element root = document.getDocumentElement();

            NodeList bonusStateList = root.getElementsByTagName("bonusState");
            if (bonusStateList.getLength() > 0) {
                return bonusStateList.item(0).getTextContent();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Unknown";
    }
    public static String getSomeThingFromPaymentResult(String someting) {
        HttpResponse<String> get = httpGET(thePaymentResult);
        String xmlResponse = get.body();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xmlResponse)));

            Element root = document.getDocumentElement();

            NodeList bonusStateList = root.getElementsByTagName(someting);
            if (bonusStateList.getLength() > 0) {
                return bonusStateList.item(0).getTextContent();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Unknown";
    }

    //Insert amount to be paid
    public static void waitingForPayment(String amount){

        HttpResponse<String> post = httpPOST(waitingForThePayment, amount);

    }
    // returns the status of card reader: idle/waiting for payment/done
    public static String paymentStatus(){
        HttpResponse<String> get = httpGET(thePaymentStatus);
        String responseBody = get.body().toLowerCase();

        if(responseBody.contains("idle")){
            return "idle";
        }
        else if (responseBody.contains("waiting for payment")){
            return "waiting for payment";
        }
        else if (responseBody.contains("done")) {
            return "done";
        }
        else{
            //Handle an unkown status or error case
            return "unknown";
        }

    }

    //Gets the result of the card: bonusCardNumber, bonusState, paymentCardNumber, paymentState and paymentCardType
    public static void paymentResult(){
        HttpResponse<String> get = httpGET(thePaymentResult);
        System.out.println(get);
        System.out.println(get.body());
    }


    //Resets the card reader
    public static void resetPayment(){
        HttpResponse<String> post = httpPOSTWithoutParam(theResetPayment);
    }
    //Aborts the payment
    public static void abortPayment(){
        HttpResponse<String> post = httpPOSTWithoutParam(abortThePayment);
    }


    /**
     * Creates a GET request
     *
     */
    public static HttpResponse<String> httpGET(String uri) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .GET()
                .build();

        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Creates a POST request without any parameters
     *
     */
    public static HttpResponse<String> httpPOST(String uri,String amount) {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .POST(HttpRequest.BodyPublishers.ofString("amount="+amount))
                .build();

        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static HttpResponse<String> httpPOSTWithoutParam(String uri) {


        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

}
