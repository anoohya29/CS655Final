public class Worker {
    
    //Attributs
    private String ipWorker;
    private String ipManager;
    private int portNumber;
    private int idWorker;

    //Constructor
    public Worker(String ipWorker, String ipManager, int portNumber, int idWorker) {
        this.ipWorker = ipWorker;
        this.ipManager = ipManager;
        this.portNumber = portNumber;
        this.idWorker = idWorker;
    }

    //Getters and Setters
    public String getIpWorker() {
        return this.ipWorker;
    }

    public void setIpWorker(String ipWorker) {
        this.ipWorker = ipWorker;
    }

    public String getIpManager() {
        return this.ipManager;
    }

    public void setIpManager(String ipManager) {
        this.ipManager = ipManager;
    }

    public int getPortNumber() {
        return this.portNumber;
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    public int getIdWorker() {
        return this.idWorker;
    }

    public void setIdWorker(int idWorker) {
        this.idWorker = idWorker;
    }

    //To String
    @Override
    public String toString() {
        return "{" +
            " ipWorker='" + getIpWorker() + "'" +
            ", ipManager='" + getIpManager() + "'" +
            ", portNumber='" + getPortNumber() + "'" +
            ", idWorker='" + getIdWorker() + "'" +
            "}";
    }
}
