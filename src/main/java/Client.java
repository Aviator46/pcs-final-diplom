import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    public static final String HOST = "127.0.0.1";
    public static final int PORT = 8989;

    public static void main(String[] args) {

        try (Socket clientSocket = new Socket(HOST, PORT);
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        ) {
            writer.println("бизнес");
            System.out.println(print(bufferedReader.readLine()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String print(String json) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(json);
        return gson.toJson(element);
    }
}