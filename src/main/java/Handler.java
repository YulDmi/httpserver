import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Handler implements Runnable{
    private Socket socket;

    public Handler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

            try (InputStream input = socket.getInputStream();
                 PrintWriter output = new PrintWriter(socket.getOutputStream())) {
                Scanner scanner = new Scanner(input).useDelimiter("\r\n");
                System.out.println(" Scanner ready");
                String line = scanner.nextLine();
                System.out.println("line : " + line);
                String URL = line.split(" ")[1];
                System.out.println(URL);
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
        System.out.println("Response : " + stringBuilder);
        return stringBuilder.toString();
    }
}
