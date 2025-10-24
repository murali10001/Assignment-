package Distributed_Computing;
import java.util.Arrays;
import java.util.List;

public class MainApp {
    public static void main(String[] args) throws Exception {

        // Create processes
        Process p1 = new Process(1);
        Process p2 = new Process(2);
        List<Process> processes = Arrays.asList(p1, p2);

        // Coordinator
        Coordinator coord = new Coordinator(processes);

        // Start processes in threads with failure handling
        Thread t1 = new Thread(() -> {
            try { p1.run(); } 
            catch (RuntimeException e) {
                System.out.println(e.getMessage());
                coord.recoverSystem();
            }
        });

        Thread t2 = new Thread(() -> {
            try { p2.run(); } 
            catch (RuntimeException e) {
                System.out.println(e.getMessage());
                coord.recoverSystem();
            }
        });

        t1.start();
        t2.start();

        // Let processes run for a few seconds
        Thread.sleep(5000);

        // Trigger manual coordinated checkpoint
        coord.initiateCheckpoint();

        // Let processes continue running for demo
        Thread.sleep(10000);

        // Stop processes manually at the end
        p1.stop();
        p2.stop();
    }
}
