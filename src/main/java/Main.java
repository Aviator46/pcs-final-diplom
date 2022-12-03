import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Main {

    private static final int PORT = 8989;
    private static final String DIRNAME = "pdfs";
    public static void main(String[] args) throws Exception {
        BooleanSearchEngine engine = new BooleanSearchEngine(new File(DIRNAME));

        try(ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                try (
                        Socket socket = serverSocket.accept();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter writer = new PrintWriter(socket.getOutputStream())
                ) {
                    String word = bufferedReader.readLine();
                    List<PageEntry> result = engine.search(word);
                    JSONArray jsonArray = new JSONArray();
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    for (PageEntry pageEntry : result) {
                        jsonArray.add(gson.toJson(pageEntry));
                    }
                    writer.println(jsonArray);
                } catch (IOException e) {
                    System.out.println("Не могу запустить сервер");
                    e.printStackTrace();
                }
            }
        }
        // здесь создайте сервер, который отвечал бы на нужные запросы
        // слушать он должен порт 8989
        // отвечать на запросы /{word} -> возвращённое значение метода search(word) в JSON-формате
    }
}