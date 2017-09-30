
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class Static {
    private int successRequestsNum = 0;
    private int sentRequestsNum = 0;
    private int totalRequest = 0;
    private long totalLatency = 0;
    private boolean isSorted = true;
    private List<Long> latencyList;

    Static() {
        this.latencyList = new ArrayList<Long>();
    }

    void add(Metric metric) {
        successRequestsNum += metric.getSuccessRequestsNum();
        sentRequestsNum += metric.getSentRequestsNum();
        totalLatency += metric.getTotalLatency();
        latencyList.add(metric.getTotalLatency());
        totalRequest++;
        isSorted = false;
    }

    long median(){
        return get(0.5);
    }

    long mean(){
        if(totalRequest ==0 ){
            return -1;
        }
        if(isSorted == false){
            sort();
        }
        return totalLatency/totalRequest;
    }

    long get(double percent){
        if(latencyList.size() == 1){
            return latencyList.get(0);
        }

        double temp = percent * latencyList.size();
        int index = (int)temp - 1;

        if(latencyList.isEmpty() || index >= latencyList.size() || index <0){
            return -1;
        }

        return latencyList.get(index);
    }

    int getSuccessRequestsNum() {
        return successRequestsNum;
    }

    int getSentRequestsNum() {
        return sentRequestsNum;
    }

    public void sort() {
        latencyList.sort(Comparator.naturalOrder());
        isSorted = true;
    }
}
