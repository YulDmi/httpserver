import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class HttpServer {
    private final int port;
    private Handler handler = new Handler();


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
        try (BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter output = new PrintWriter(clientSocket.getOutputStream())) {

            String line = br.readLine();
            System.out.println(line);

            String method = line.split(" ")[0];
            String URI = line.split(" ")[1];

            int statusCode = 200;
            String statusText = "OK";
            String text = "";
            if (method.trim().equals("GET")) {
                if (URI.trim().equals("/persons")) {
                    text = handler.getAllPerson();
                } else {
                    statusCode = 404;
                    statusText = "NOT FOUND";
                }
            } else if (method.trim().equals("POST")) {
                if (URI.trim().startsWith("/delete")) {
                    handler.delete();
                } else if (URI.trim().startsWith("/update")) {
                    handler.update();
                } else if (URI.trim().startsWith("/insert")) {
                    handler.insert();
                } else {
                    statusCode = 404;
                    statusText = "NOT FOUND";
                }
            } else {
                statusCode = 400;
                statusText = "BAD REQUEST";
            }

            output.write(getResponse(statusCode, statusText, text));
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String getResponse(int statusCode, String statusText, String text) {

        StringBuilder builder = new StringBuilder("HTTP/1.1 ");
        builder.append(statusCode).append(" ").append(statusText).append("\r\n");
        builder.append("Content-Type: text/html; charset=utf-8 \r\n\n");
        builder.append(text);
        return builder.toString();
    }


    public static void main(String[] args) {
        HttpServer httpServer = new HttpServer(8187);
        httpServer.start();
    }
}
