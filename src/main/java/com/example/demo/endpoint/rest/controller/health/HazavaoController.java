package com.example.demo.endpoint.rest.controller.health;

import com.example.demo.PojaGenerated;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@PojaGenerated
@RestController
@AllArgsConstructor
public class HazavaoController {

  private static final String API_URL = "https://api.openai.com/v1/chat/completions";
  private static final String API_KEY = System.getenv("OPENAI_API_KEY");
  private static final HttpClient CLIENT = HttpClient.newBuilder()
      .connectTimeout(Duration.ofSeconds(10))
      .build();

  @GetMapping("/hazavao")
  public ResponseEntity<String> hazavao(@RequestParam("teny") String teny) {
    try {
      String requestBody = """
        {
          "model": "gpt-3.5-turbo",
          "messages": [
            {
              "role": "user",
              "content": "Hazavao amin'ny teny malagasy ilay teny hoe: %s"
            }
          ]
        }
        """.formatted(teny);

      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(API_URL))
          .timeout(Duration.ofSeconds(20))
          .header("Authorization", "Bearer " + API_KEY)
          .header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(requestBody))
          .build();

      HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
      return new ResponseEntity<>(response.body(), HttpStatus.OK);

    } catch (Exception e) {
      return new ResponseEntity<>("Tsy afaka namaly ny fangatahana.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
