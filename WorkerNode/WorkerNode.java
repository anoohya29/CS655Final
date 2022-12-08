import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class WorkerNode {
    
    //Attributes
    private final int portNumber;

    //Constructor
    public WorkerNode(int portNumber) {
        this.portNumber = portNumber;
    }

    //Methods

    //Function to initialize the worker node.
    public void init() throws IOException{

        Socket rstSender = null;

        System.out.println("The worker is working on port: " + portNumber);
        while (true) {
            try {
                //Start a connection on giver port
                ServerSocket serverSocket = new ServerSocket(portNumber);
                Socket socket = serverSocket.accept();

                //To get Data
                InputStream inputStream = socket.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                String message = br.readLine();

                //Print the message received from the Node Manager
                System.out.println("Receive message: " + message);  

                //Message will be splited
                String[] messages = message.split(",");


                String md5String = messages[0].split(":")[1]; 
                String ipManager = messages[1].split(":")[1]; 

                //Manager's port number
                int port = Integer.parseInt(messages[2].split(":")[1]);

                inputStream.close();

                //Available range the worker can work on.
                String[] arr = {"aaaaa", "ZZZZZ"}; 
                List<String[]> list = new ArrayList<>();
                list.add(arr);

                // find the result
                String res = PasswordCracker.findPassword(list, md5String);   
                //Printing the result found
                System.out.println("The result for the request: " + md5String + " is: " + res);

                //To send the result to the nodeManager.
                rstSender = new Socket(ipManager, port);
                OutputStream outputStream = rstSender.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
                bw.write(res); 
                bw.flush();
                outputStream.close();
                socket.close();
                serverSocket.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }finally{
                rstSender.close();
            }
        }
    }

    //Main method
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Please insert all required params.");
            return;
        }
        int portNumber = Integer.parseInt(args[0]);
        if (portNumber < 58000 || portNumber > 58999) {
            System.out.println("Try another port number.");
            return;
        }

        WorkerNode worker = new WorkerNode(portNumber);
        worker.init();
    }
}
