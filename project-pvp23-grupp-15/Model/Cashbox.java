package Model;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Cashbox {

    /**
     * NOTE: You must execute the Cashbox.jar prior to executing this code
     * in order for it to work
     *
     */

    private static String cashBoxStatus = "http://localhost:9001/cashbox/status";
    private static String cashBoxOpen = "http://localhost:9001/cashbox/open";



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
    public static HttpResponse<String> httpPOST(String uri) {

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

    //This function opens the cash box
    public static void openCashbox(){
        HttpResponse<String> post = httpPOST(cashBoxOpen);
    }

    //This function gives the status of the cashbox open/closed
    public static void getCashboxStatus(){
        HttpResponse<String> get = httpGET(cashBoxStatus);
        System.out.println(get);
        System.out.print(get.body());
    }
}