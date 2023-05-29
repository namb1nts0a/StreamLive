package com.StreamLive.stream;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class UserCreationTest {

    public static void main(String[] args) throws IOException{
        HttpClient httpClient = HttpClient.newHttpClient();

        String url = "http://localhost:8080/users";

        String json = "{\"username\":\"john\", \"password\":\"pass123\", \"email\":\"john@example.com\"}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        CompletableFuture<HttpResponse<String>> responseFuture = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        responseFuture.thenAccept(response -> {
            // Affiche le code de statut et la réponse
            System.out.println("Status code: " + response.statusCode());
            System.out.println("Response: " + response.body());

            // Vérifie si la requête a réussi (statut 2xx)
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                System.out.println("L'utilisateur a été créé avec succès.");
            } else {
                System.out.println("Erreur lors de la création de l'utilisateur.");
            }
        });

        // Attends la fin de l'exécution
        responseFuture.join();
    }
}
