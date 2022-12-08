import java.net.Socket;

public class ClientInformations {
    
    //Attributs
    private Socket clientSocket;
    private String message;
    private long waitingTime;

    //Constructor
    public ClientInformations(Socket clientSocket, String message, long waitingTime){
        this.clientSocket = clientSocket;
        this.message = message;
        this.waitingTime = waitingTime;
    }

    //Getters and Setters
    public Socket getClientSocket() {
        return this.clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getWaitingTime() {
        return this.waitingTime;
    }

    public void setWaitingTime(long waitingTime) {
        this.waitingTime = waitingTime;
    }

}
