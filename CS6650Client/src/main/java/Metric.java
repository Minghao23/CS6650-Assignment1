
class Metric {
    private int successRequestsNum = 0;
    private int sentRequestsNum = 0;
    private long totalLatency = 0;

    protected void addRequest(long latency, boolean isSent, boolean isSuccess) {
        totalLatency += latency;
        if(isSent) {
            sentRequestsNum += 1;
        }
        if(isSuccess) {
            successRequestsNum += 1;
        }
    }

    protected int getSuccessRequestsNum() {
        return successRequestsNum;
    }

    protected int getSentRequestsNum() {
        return sentRequestsNum;
    }

    protected long getTotalLatency() {
        return totalLatency;
    }
}
