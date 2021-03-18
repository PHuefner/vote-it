package voteit.libs.serverhttp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * ServerHttp
 *
 * A simple Http Server based on threading. Should be replaced by a better
 * server framework in the final version.
 */
public class ServerHttp {

    private HashMap<String, Handler> routeMap;
    private Thread mainThread;

    public ServerHttp(int port) throws IOException {
        this.routeMap = new HashMap<String, Handler>();

        ServerSocket serverSocket = new ServerSocket(port);

        this.mainThread = new Thread(new ServerRunner(this, serverSocket));
    }

    public void addRoute(String route, Handler handler) {
        routeMap.put(route, handler);
    }

    public void start() throws Exception {
        mainThread.setDaemon(false);
        mainThread.start();
    }

    protected Handler getRoute(String route) throws RouteNotConfiguredException {
        Handler handler = routeMap.get(route);
        if (handler == null) {
            throw new RouteNotConfiguredException("No Handler for route " + route);
        } else {
            return handler;
        }
    }

}

/**
 * ServerRunner
 *
 * This Thread runs the actual server thread. It opens a new ServerSocket and
 * starts a new ConnectionHandler thread for each new connection
 */
class ServerRunner implements Runnable {

    ServerHttp server;
    ServerSocket serverSocket;

    @Override
    public void run() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                ConnectionHandler handler = new ConnectionHandler(socket, server);
                new Thread(handler).start();
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Couldnt accept a new connection");
            }
        }
    }

    public ServerRunner(ServerHttp parent, ServerSocket serverSocket) {
        this.server = parent;
        this.serverSocket = serverSocket;
    }

}

/**
 * ConnectionHandler
 *
 * An instance of this thread is started for each accepted connection. it parses
 * the request and route, creates a context and response object and then
 * executes the corrospoding handler
 */
class ConnectionHandler implements Runnable {

    ServerHttp server;
    Socket socket;
    PrintWriter writer;
    BufferedReader reader;

    public ConnectionHandler(Socket socket, ServerHttp server) throws IOException {
        this.server = server;
        this.socket = socket;
    }

    @Override
    public void run() {

        // Open streams
        try {
            this.writer = new PrintWriter(socket.getOutputStream(), true);
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e1) {
            System.out.println("Couldnt create IO handlers for socket: " + e1.getMessage());
            return;
        }

        // Handle request
        try {
            // TODO support post requests
            Context context = parseRequest();
            Response response = server.getRoute(context.route).handle(context);
            writer.print(response.build());
            writer.flush();

        } catch (RequestParsingFailedException e1) {
            System.out.println("Request failed: " + e1.getMessage());
        } catch (IOException e2) {
            e2.printStackTrace();
            System.out.println("Request parsing failed on IO: " + e2.getMessage());
        } catch (RouteNotConfiguredException e3) {
            System.out.println("Route not configured: " + e3.getMessage());
        }

        // Close socket
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failure closing socket");
        }
    }

    private Context parseRequest() throws RequestParsingFailedException, IOException {
        String[] firstLine = reader.readLine().split(" ");

        // Set method
        METHOD method;
        switch (firstLine[0]) {
        case "GET":
            method = METHOD.GET;
            break;
        case "POST":
            method = METHOD.POST;
            break;
        default:
            throw new RequestParsingFailedException("Unsupported Request Method: " + firstLine[0]);
        }

        // Set route
        String route = firstLine[1];

        // Parse headers
        String line = reader.readLine();
        HashMap<String, String> header = new HashMap<String, String>();
        while (!line.isEmpty()) {
            String key = "";
            String value = "";
            boolean onValue = false;
            boolean readSeperator = false;
            for (char c : line.toCharArray()) {
                if (c == ':' && !readSeperator) { // Reading seperator
                    readSeperator = true;
                } else {
                    if (!onValue && !readSeperator) { // On field name
                        key += c;
                    } else if (readSeperator) { // On values
                        if (c == ' ' && !onValue) { // Skip spaces before value
                            continue;
                        } else {
                            onValue = true;
                            value += c;
                        }
                    }
                }
            }
            header.put(key, value);
            line = reader.readLine();
        }

        // Parse post request
        String postData = "";
        if (method == METHOD.POST) {
            int length = Integer.parseInt(header.get("Content-Length"));
            char[] buffer = new char[length];
            reader.read(buffer, 0, length);
            StringBuilder content = new StringBuilder();
            for (char c : buffer) {
                content.append(c);
            }
            postData = content.toString();
        }

        // Parse cookies
        String cookieString = header.get("Cookie");
        HashMap<String, String> cookies = new HashMap<String, String>();
        if (cookieString != null) {
            cookies = parseCookies(cookieString);
        }

        if (!postData.isEmpty()) {
            return new Context(method, route, header, cookies, postData);
        } else {
            return new Context(method, route, header, cookies);
        }
    }

    private HashMap<String, String> parseCookies(String cookies) {
        HashMap<String, String> cookiesMap = new HashMap<String, String>();

        // Parse cookies
        String key = "";
        String value = "";
        Boolean onValue = false;

        for (char c : cookies.toCharArray()) {
            switch (c) {
            case ' ': // Just skip spaces
                break;
            case '=': // Switch between key and value
                onValue = true;
                break;
            case ';': // Save cookie in HashMap and reset
                cookiesMap.put(key, value);
                onValue = false;
                key = "";
                value = "";
                break;
            default:
                if (!onValue) {
                    key += c;
                } else {
                    value += c;
                }
                break;
            }
        }
        cookiesMap.put(key, value); // Last cookie is not (;) terminated
        return cookiesMap;
    }

}
