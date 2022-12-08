import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Configuration {

    //Attributs
    public static int numberOfWorkers;
    public static int portNumberOfWorker;
    public static Map<Integer, Worker> workerMap = new HashMap<>();

    //Methods
    public static void init(String configurationFile){
        try {
            FileInputStream fileInputStream = new FileInputStream(configurationFile);

            Scanner sc = new Scanner(fileInputStream);
            numberOfWorkers = sc.nextInt();
            portNumberOfWorker = sc.nextInt();
            sc.nextLine();

            int idWorker = 0;   // Initialise id given to each worker

            for(int i = 0; i < numberOfWorkers; i++){
                String line = sc.nextLine();    // read the line on the file
                String[] arr = line.split(" "); // to split the line by spaces
                String ipManager = arr[0];
                String ipWorker = arr[1];
                Worker worker = new Worker(ipWorker, ipManager, portNumberOfWorker, idWorker);  // create a worker instance.
                workerMap.put(idWorker, worker);    // added the worker to our hash map
                idWorker++; // increment id worker to make sure every worker has a unique id.
            }

            sc.close();
            System.out.println(workerMap.toString());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}