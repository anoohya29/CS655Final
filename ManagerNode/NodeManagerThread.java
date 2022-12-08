import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class NodeManagerThread implements Runnable {
    
    //Attributs
    private Socket socket;
    private Worker worker;
    private int idNodeManagerThread;
    private String message;

    //Constructor
    public NodeManagerThread(ClientInformations clientInfo, Worker worker, int idNodeManagerThread) {
        this.socket = clientInfo.getClientSocket();
        this.message = clientInfo.getMessage();
        this.worker = worker;
        this.idNodeManagerThread = idNodeManagerThread;
        new Thread(this).start();
    }

    //Methods
    @Override
    public void run() {
        try {
            // process the message sent from the client
            message = message.split(" ")[1];    // extract the md5 string from the message
            String key = message.substring(6, 38);
            System.out.println("The worker: " + idNodeManagerThread + " received a request: " + key);

            long startTime = System.currentTimeMillis();    // start time of the cracking task
            String rst = getResult(key, worker.getIpWorker(), worker.getIpManager(), worker.getPortNumber());
            long endTime = System.currentTimeMillis();  // end time of the cracking task
            System.out.println("The worker: " + idNodeManagerThread + " used " + ((double) (endTime - startTime)) / 1000.0 + " seconds for the request: " + key);

            // generate the HTTP respond with the result of the cracking
            String response = "";
            response += "HTTP/1.1 200 OK\nAccess-Control-Allow-Origin:*\nContent-Type: text\\plain\nContent-Length: " + rst.length() + "\n";
            response += rst;

            OutputStream outputStream = socket.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            bufferedWriter.write(response);
            bufferedWriter.flush();

            outputStream.close();

            // make the worker available  to other request
            NodeManager.available[this.idNodeManagerThread].set(true);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (Exception e) {
                    socket = null;
                    e.printStackTrace();
                }
            }
        }
    }

    public String getResult(String key, String targetIp, String selfIp, int port) {
        try {
            // assign the task to a worker
            ServerSocket serverSocket;
            int rstPort = 58000;
            while (true) {
                // find an appropriate port to receive the number
                try {
                    serverSocket = new ServerSocket(rstPort);
                    break;
                } catch (Exception e) {
                    rstPort++;
                }
            }
            Socket workerSender = new Socket(targetIp, port);
            OutputStream outputStreamWorker = workerSender.getOutputStream();
            BufferedWriter bwWorker = new BufferedWriter(new OutputStreamWriter(outputStreamWorker));
            System.out.println("The worker: " + idNodeManagerThread + " sent the request to: " + targetIp);
            // send the request to the worker
            bwWorker.write("key:" + key + ",ip:" + selfIp + ",port:" + rstPort);
            bwWorker.flush();
            outputStreamWorker.close();
            workerSender.close();

            // get result from the worker
            Socket workerReceiver = serverSocket.accept();
            InputStream inputStreamWorker = workerReceiver.getInputStream();
            BufferedReader brWorker = new BufferedReader(new InputStreamReader(inputStreamWorker));
            String rst = brWorker.readLine();
            inputStreamWorker.close();
            serverSocket.close();   // close the manager to worker connection
            System.out.println("The worker: " + idNodeManagerThread + " got result from: " + targetIp);
            System.out.println("The worker: " + idNodeManagerThread + " got result for: " + key + " is: " + rst);
            return rst;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
