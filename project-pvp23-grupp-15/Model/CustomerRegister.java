package Model;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CustomerRegister {


    private String findCustomerByNumber = "http://localhost:9004/rest/findByCustomerNo/";
    private String findCustomerByBonuscard = "http://localhost:9004/rest/findByBonusCard/";



    ProductXMLParser xmlToProduct = new ProductXMLParser();

    /**
     * Creates a GET request
     *
     */
    public HttpResponse<String> httpGET(String uri, String thing) {

        uri = uri + thing;

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
    public HttpResponse<String> httpPOST(String uri) {

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



    public Customer findByCustomerNo(String custNo){
        HttpResponse<String> get = httpGET(findCustomerByNumber, custNo);
        Customer customer = CustomerXMLParser.customer(get.body());

        return customer;
    }

    public Customer findByBonusCard(String number, String goodThruyear, String goodThruMonth) {
        String url = findCustomerByBonuscard + number+"/"+ goodThruyear +"/"+ goodThruMonth;
        HttpResponse<String> get = httpGET(url, "");
        Customer customer = CustomerXMLParser.customer(get.body());

        return customer;
    }




}
