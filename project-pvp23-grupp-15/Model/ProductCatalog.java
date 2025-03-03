package Model;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ProductCatalog {

    private String findByBarcode = "http://localhost:9003/rest/findByBarCode/";
    private String findByKeyWord = "http://localhost:9003/rest/findByKeyword/";
    private String findByName = "http://localhost:9003/rest/findByName/";

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



    public Product findByBarCode(String barCode){
        HttpResponse<String> get = httpGET(findByBarcode, barCode);

        Product product = xmlToProduct.product(get.body());
        return product;


    }

    public Product findByKeyWord(String keyWord) {
        HttpResponse<String> get = httpGET(findByKeyWord, keyWord);

        Product product = xmlToProduct.product(get.body());
        return product;
    }

    public Product findByName(String name) {
        HttpResponse<String> get = httpGET(findByName, name);
        Product product = xmlToProduct.product(get.body());
        return product;
    }

}
