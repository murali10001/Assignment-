package Distributed_Computing;

import java.io.IOException;
import java.util.List;

public class Coordinator {
    private List<Process> processes;

    public Coordinator(List<Process> processes) {
        this.processes = processes;
    }

    // Trigger coordinated checkpoint
    public void initiateCheckpoint() {
        System.out.println("\nInitiating Coordinated Checkpoint...");
        for (Process p : processes) {
            try {
                p.saveCheckpoint();
            } catch (IOException e) {
                System.out.println("Error saving checkpoint for process " + p);
            }
        }
        System.out.println("Checkpoint completed successfully.\n");
    }

    // Recover all processes from last checkpoint
    public void recoverSystem() {
        System.out.println("\nRecovering system from checkpoints...");
        for (Process p : processes) {
            try {
                p.restoreCheckpoint();
            } catch (IOException e) {
                System.out.println("Error restoring process " + p);
            }
        }
        System.out.println("System recovered.\n");
    }
}
