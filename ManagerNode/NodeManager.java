import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class NodeManager {
    
    //Attributs
    public int portNumber;
    public static AtomicBoolean[] available;
    public static BlockingQueue<ClientInformations> clientQueue = new LinkedBlockingQueue<>();

    //Constructor
    public NodeManager(int portNumber) {
        available = new AtomicBoolean[Configuration.numberOfWorkers];
        for(int i = 0; i < available.length; i++) {
            // set all workers are available at the beginning of the program
            available[i] = new AtomicBoolean(true);
        }
        this.portNumber = portNumber;
    }

    //Methods
    public void init() throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(portNumber, 100);
            System.out.println("Server started on port: " + portNumber);
            new QueueThread();
            while (true) {
                // Connection has been made successfully with server.
                Socket client = serverSocket.accept(); 

                //We will print the client informations
                System.out.println(client.toString());

                //InputStream to insert the data.
                InputStream inputStream = client.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

                // when a new connection is established, we will immediately read the request to classify the reqeust
                String message = br.readLine();

                //If message is null mean it not acceptable.
                if (message == null) {
                    // invalid request
                    System.out.println("Please try another request.");
                    OutputStream outputStream = client.getOutputStream();
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
                    bw.write("");
                    bw.flush();

                    //Close the connection
                    client.close();

                //If the message is acceptable
                }else if (message.contains("connection-check")) {

                    // Print on console that the server has receiver a connection check request
                    System.out.println("Received a connection check request.");

                    // if the message is the connection check message, simply response the front end with "OK"
                    String response = "";
                    response += "HTTP/1.1 200 OK\n";
                    response += "Access-Control-Allow-Origin:*\n";
                    response += "Content-Type: text\\plain\n";
                    response += "Content-Length: " + "OK".length() + '\n';
                    response += "\n";
                    response += "OK";

                    OutputStream outputStream = client.getOutputStream();
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
                    bw.write(response);
                    bw.flush();
                    client.close();
                } else {

                    //If the request sent is to crack a password, then the connection info are saved and added to the queue.
                    ClientInformations clientInfo = new ClientInformations(client, message, System.currentTimeMillis());
                    clientQueue.add(clientInfo);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //Main method
    public static void main(String[] args) {

        //If user didn't insert all required informations.
        if (args.length == 0) {
            System.out.println("Please insert all necessary parameters.");
            return;
        }

        //Get the port number.
        int portNumber = Integer.parseInt(args[0]);
        if (portNumber < 58000 || portNumber > 58999) {
            System.out.println("Try another port number.");
            return;
        }

        String configFilename = args[1];
        Configuration.init(configFilename);

        NodeManager server = new NodeManager(portNumber);

        try {
            //We try to start the server
            server.init();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
