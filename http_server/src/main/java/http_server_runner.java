import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.*;

public class http_server_runner {


    public static dbCon db = new dbCon();


    public http_server_runner() {
        db.CreateTable();
    }

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(7575), 0);
        server.createContext("/", new MyHttpHandler());



        server.start();
        System.out.println("Server start");
    }



    private static class MyHttpHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println(exchange.getRequestMethod()  + " " + exchange.getRequestURI() );


            String current_url = String.valueOf(exchange.getRequestURI());

            if (exchange.getRequestMethod().equals("OPTIONS")) {
                makeResponseMessage(exchange, "hello Options");
            }else {

                switch (current_url) {

                    case "/getTodos":
                        SendAllTodos(exchange);
                        break;

                    case "/insertTodo":
                        byte[] bytes = exchange.getRequestBody().readAllBytes();

                        String response = new String(bytes);
                        JsonObject jsonRes = JsonParser.parseString(response).getAsJsonObject();

                        String todo = jsonRes.get("todo").getAsString();
                        String status = jsonRes.get("status").getAsString();
                        db.insertToDb(todo,status);

                    default:
                        makeResponseMessage(exchange, "#ti_debil");

                }

            }





        }


        public HttpExchange addHeaders(Map<String, String> headers, HttpExchange exchange) {

            for (Map.Entry<String, String> pair : headers.entrySet()) {
                exchange.getResponseHeaders().add(pair.getKey(), pair.getValue());
            }

            return exchange;


        }

        public HttpExchange SendAllTodos(HttpExchange exchange){

            try {

                byte[] response = db.getTodosJson().getBytes();
                CheckMethods(exchange, response,"json");

                exchange.sendResponseHeaders(200, response.length);
                OutputStream os = exchange.getResponseBody();

                os.write(response);
                os.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return  exchange;
        }


        public void makeResponseMessage(HttpExchange exchange , String message) {

            try {
                byte[] response = message.getBytes();
                CheckMethods(exchange, response,"html");

                exchange.sendResponseHeaders(200, response.length);
                OutputStream os = exchange.getResponseBody();

                os.write(response);
                os.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        public HttpExchange CheckMethods(HttpExchange exchange , byte[] size_response,String contentType) {
            Map<String, String> headers = new HashMap<String, String>();

            String htmlType = "text/html";
            String JsonType = "application/json";

            switch (contentType){

                case "html":
                    contentType = htmlType;
                    break;

                case "json":
                    contentType = JsonType;

            }



            switch (exchange.getRequestMethod()) {
                case "OPTIONS":
                    headers.put("Access-Control-Allow-Headers", "content-type");
                    headers.put("Access-Control-Allow-Methods", "DELETE, GET, HEAD, OPTIONS, PATCH, POST, PUT");
                    headers.put("Access-Control-Allow-Origin", "http://127.0.0.1:8080");
                    headers.put("Allow", "HEAD, GET, OPTIONS");
                    headers.put("Content-Length", "0");
                    headers.put("Content-Type", contentType+",charset=utf-8");
                    headers.put("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:99.0) Gecko/20100101 Firefox/99.0");
                    headers.put("server", "you_eblan");
                    return addHeaders(headers, exchange);
                case "POST":
                    headers.put("Access-Control-Allow-Origin", "http://127.0.0.1:8080");
                    headers.put("Allow", "HEAD, GET, OPTIONS");
                    headers.put("Content-Length", String.valueOf(size_response.length));
                    headers.put("Content-Type", contentType+",charset=utf-8");
                    headers.put("Server", "you_eblan");
                    return addHeaders(headers, exchange);

                case "GET":
                    headers.put("Access-Control-Allow-Origin", "http://127.0.0.1:8080");
                    headers.put("Allow", "HEAD, GET, OPTIONS");
                    headers.put("Content-Length", String.valueOf(size_response.length));
                    headers.put("Content-Type", contentType+",charset=utf-8");
                    headers.put("Server", "you_eblan");

                    return addHeaders(headers, exchange);

                default:
                    return exchange;


            }

        }
    }
}




