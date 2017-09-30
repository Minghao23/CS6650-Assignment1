
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class MyClient {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int threadNum = 10;
        int iterationNum = 100;
        String ip;
        int port = 8080;

        if(args.length == 1) {
            ip = args[0];
        }else{
            threadNum = Integer.parseInt(args[0]);
            iterationNum = Integer.parseInt(args[1]);
            ip = args[2];
            port = Integer.parseInt(args[3]);
        }

        ExecutorService executorService = Executors.newCachedThreadPool();

        List<Task> tasks = new ArrayList<Task>();
        for (int i = 0; i < threadNum; i++) {
            tasks.add(new Task(ip, port, iterationNum));
        }


        System.out.printf("client %d %d %s %d\n", threadNum, iterationNum, ip, port);
        long startTime = System.currentTimeMillis();
        System.out.println("MyClient starting... Time: " + startTime);
        System.out.println("All threads running...");
        List<Future<Metric>> futures = executorService.invokeAll(tasks);
        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        System.out.println("All threads complete... Time: " + System.currentTimeMillis());

        Static statics = new Static();
        for (Future<Metric> future : futures) {
            statics.add(future.get());
        }
        System.out.println("Total number of requests sent: " + statics.getSentRequestsNum());
        System.out.println("Total number of successful responses: " + statics.getSuccessRequestsNum());
        System.out.printf("Total wall time: %.2f seconds\n", (System.currentTimeMillis() - startTime) / 1000.0);
        System.out.printf("Mean latency is %.2f seconds\n", statics.mean() / 1000.0);
        System.out.printf("Median latency is %.2f seconds\n", statics.median() / 1000.0);
        System.out.printf("99th percentile latency is %.2f seconds\n", statics.get(0.99) / 1000.0);
        System.out.printf("95th percentile latency is %.2f seconds\n", statics.get(0.95) / 1000.0);

    }
}

