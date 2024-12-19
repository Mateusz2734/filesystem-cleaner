package pl.edu.agh.to2.cleaner.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.*;
import java.util.Map;
import java.util.HashMap;

public class EmbeddingServerClient {
    private static final String url = "http://localhost:7777/";
    private static final OkHttpClient CLIENT = new OkHttpClient();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static void main(String[] args) {
        var start = System.currentTimeMillis();
        new Thread(EmbeddingServerClient::run).start();

        while (!EmbeddingServerClient.ping()) {
            System.out.println("Server is not running yet.");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Server is running after " + (System.currentTimeMillis() - start) + "ms.");

        start = System.currentTimeMillis();
        EmbeddingServerClient.fetchEmbedding("Macbeth");
        System.out.println("Embedding fetched after " + (System.currentTimeMillis() - start) + "ms.");

        EmbeddingServerClient.shutdown();
    }

    public static void run() {
        try {
            if (ping()) {
                return;
            }

            var process = new ProcessBuilder("uvicorn", "embed_server:app", "--port=7777").redirectErrorStream(true).start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Uvicorn running on")) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Fatal error occurred while starting the server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean ping() {
        try {
            Request request = new Request.Builder().url(url + "ping").get().build();
            try (Response response = CLIENT.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    return false;
                }
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public static void shutdown() {
        for (int i = 0; i < 5; i++) {
            try {
                Request request = new Request.Builder().url(url + "shutdown").get().build();
                CLIENT.newCall(request).execute().close();
                return;
            } catch (IOException ignored) {
            }
        }
    }

    public static Float[] fetchEmbedding(String input) {
        Map<String, String> map = new HashMap<>();
        map.put("text", input);

        String contents;
        try {
            contents = OBJECT_MAPPER.writeValueAsString(map);
        } catch (IOException e) {
            return null;
        }

        var body = RequestBody.create(contents, MediaType.parse("application/json"));

        var request = new Request.Builder().url(url).post(body).build();
        try (var response = CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return null;
            }

            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                return null;
            }

            return OBJECT_MAPPER.readValue(responseBody.string(), Float[].class);
        } catch (IOException e) {
            return null;
        }
    }
}
