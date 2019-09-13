import java.util.function.Supplier;

public class RetryCommand<T> {

    private int retryCounter;
    private int maxRetries;

    public RetryCommand(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public void run(Supplier<T> function) {
        try {
            function.get();
        } catch (Exception e) {
            retry(function);
        }
    }

    public int getRetryCounter() {
        return retryCounter;
    }

    private void retry(Supplier<T> function) {
        System.out.println("Execution failed, will be retried " + maxRetries + " times.");
        retryCounter = 0;
        while (retryCounter < maxRetries) {
            try {
                function.get();
            } catch (Exception ex) {
                retryCounter++;
                System.out.println("Execution failed on retry " + retryCounter + " of " + maxRetries + " error: " + ex);
                if (retryCounter >= maxRetries) {
                    System.out.println("Max retries exceeded.");
                    throw ex;
                }
            }
        }
    }

}
