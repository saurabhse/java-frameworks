public class Service {
    public static void main(String[] args) {
        callMethod();
    }

    private static void callMethod(){
        RetryCommand retryCommand = new RetryCommand(3);
        try{
            retryCommand.run(() -> {
                System.out.println("Calling service");
                callService();
                return null;
            });
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private static void callService(){

    }
}
