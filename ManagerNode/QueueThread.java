public class QueueThread implements Runnable {
    
    //Constructor
    public QueueThread() {
        new Thread(this).start();
    }

    //Methods
    @Override
    public void run() {
        try {
            while(true) {
                if (NodeManager.clientQueue.isEmpty()) continue;
                for (int i = 0; i < NodeManager.available.length; i++) {
                    //if a worker is available, start a thread to process the message and send request to worker node
                    if (NodeManager.available[i].compareAndSet(true, false) && !NodeManager.clientQueue.isEmpty()) {
                        ClientInformations clientInfo = NodeManager.clientQueue.poll();
                        double queueTime = (double)(System.currentTimeMillis() - clientInfo.getWaitingTime()) / 1000.0;
                        System.out.println("client: " + clientInfo.getClientSocket().toString() + "finish queueing and assigned to worker " + i);
                        System.out.println("Queue time is " + queueTime + "seconds");
                        new NodeManagerThread(clientInfo, Configuration.workerMap.get(i), i);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
