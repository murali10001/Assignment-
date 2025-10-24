package Distributed_Computing;

import java.io.*;
import java.util.Random;

public class Process implements Runnable {
    private int id;
    private int state;
    private boolean active = true;

    public Process(int id) {
        this.id = id;
        this.state = 0;
    }

    public void run() {
        Random rand = new Random();
        while (active) {
            state++;
            System.out.println("Process " + id + " running. State: " + state);

            // Simulate random failure (~10% chance)
            if (rand.nextInt(10) == 5) {
                System.out.println("Process " + id + " encountered a FAILURE!");
                throw new RuntimeException("Simulated Failure in Process " + id);
            }

            try { Thread.sleep(1000); } catch (InterruptedException e) {}
        }
    }

    public void saveCheckpoint() throws IOException {
        try (FileWriter fw = new FileWriter("checkpoint_" + id + ".txt")) {
            fw.write(String.valueOf(state));
        }
        System.out.println("Process " + id + " checkpoint saved.");
    }

    public void restoreCheckpoint() throws IOException {
        File file = new File("checkpoint_" + id + ".txt");
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                state = Integer.parseInt(br.readLine());
            }
            System.out.println("Process " + id + " restored to state " + state);
        }
    }

    public void stop() { active = false; }
}
