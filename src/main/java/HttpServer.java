import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class HttpServer {
    private final int port;


    public HttpServer(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket socket = new ServerSocket(port)) {
            System.out.println("Server started");
            while (true) {
                Socket clientSocket = socket.accept();
                new Thread(() -> readMessage(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readMessage(Socket clientSocket) {
        try (InputStream input = clientSocket.getInputStream();
             PrintWriter output = new PrintWriter(clientSocket.getOutputStream())) {
            Scanner scanner = new Scanner(input).useDelimiter("\r\n");
            String line = scanner.nextLine();
            String URL = line.split(" ")[1];
            int statusCode;
            if (URL.endsWith("/text")) {
                statusCode = 200;
            } else {
                statusCode = 404;
            }

            output.write(getResponse(statusCode));
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String getResponse(int statusCode) {
        StringBuilder stringBuilder = new StringBuilder("HTTP/1.1 ");
        String statusText;
        if (statusCode == 200) {
            statusText = "OK";
            stringBuilder.append(statusCode).append(" ").append(statusText).append("\r\n");
            stringBuilder.append("Content-Type: text/html; charset=utf-8 \r\n\n");
            stringBuilder.append("<html>\n" +
                    "  <head>\n" +
                    "    <title>An Example Page</title>\n" +
                    "  </head>\n" +
                    "  <body>\n" +
                    "    <h1>Hello World</h1>\n" +
                    "  </body>\n" +
                    "</html>");

        } else {
            statusText = "NOT FOUND";
            stringBuilder.append(statusCode).append(" ").append(statusText).append("\r\n\n");
        }
        return stringBuilder.toString();
    }


    public static void main(String[] args) {
        HttpServer httpServer = new HttpServer(8187);
        httpServer.start();


    }
}
