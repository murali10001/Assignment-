package Computer_network;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

class Frame {
 int size; // Size of each frame in bytes
 Frame(int size) {
     this.size = size;
 }
}

//Simulator class for frame aggregation
class AggregationSimulator {
 ArrayList<Frame> frames;
 int headerSize = 30; // Header size in bytes
 int dataRate = 54000; // Data rate in Kbps (example: 54 Mbps)

 AggregationSimulator(int frameCount, int frameSize) {
     frames = new ArrayList<>();
     for (int i = 0; i < frameCount; i++) {
         frames.add(new Frame(frameSize));
     }
 }

 // Transmission time without aggregation (in seconds)
 double withoutAggregation() {
     double totalBits = 0;
     for (Frame f : frames) {
         totalBits += (f.size + headerSize) * 8;
     }
     return totalBits / (dataRate * 1000.0);
 }

 // Transmission time with aggregation (in seconds)
 double withAggregation(int aggregationSize) {
     double totalBits = 0;
     int i = 0;
     while (i < frames.size()) {
         int aggData = 0;
         for (int j = 0; j < aggregationSize && i < frames.size(); j++, i++) {
             aggData += frames.get(i).size;
         }
         totalBits += (aggData + headerSize) * 8; // One header per aggregated frame
     }
     return totalBits / (dataRate * 1000.0);
 }
}

//GUI Application Class
public class Frame_Aggregation extends JFrame implements ActionListener {
 JTextField tfFrames, tfSize, tfAgg;
 JTextArea output;
 JButton simulateBtn;

 Frame_Aggregation() {
     setTitle("Frame Aggregation Analysis in WLANs");
     setSize(550, 450);
     setDefaultCloseOperation(EXIT_ON_CLOSE);
     setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

     add(new JLabel("Number of Frames:"));
     tfFrames = new JTextField("1000", 10);
     add(tfFrames);

     add(new JLabel("Frame Size (bytes):"));
     tfSize = new JTextField("500", 10);
     add(tfSize);

     add(new JLabel("Aggregation Size:"));
     tfAgg = new JTextField("5", 10);
     add(tfAgg);

     simulateBtn = new JButton("Simulate");
     simulateBtn.addActionListener(this);
     add(simulateBtn);

     output = new JTextArea(15, 45);
     output.setEditable(false);
     output.setFont(new Font("Monospaced", Font.PLAIN, 12));
     add(new JScrollPane(output));

     setVisible(true);
 }

 public void actionPerformed(ActionEvent e) {
     int frameCount = Integer.parseInt(tfFrames.getText());
     int frameSize = Integer.parseInt(tfSize.getText());
     int aggSize = Integer.parseInt(tfAgg.getText());

     AggregationSimulator sim = new AggregationSimulator(frameCount, frameSize);

     double tNoAgg = sim.withoutAggregation(); // Time in seconds
     double tAgg = sim.withAggregation(aggSize); // Time in seconds

     // Throughput = Total data bits / transmission time
     double totalDataBits = frameCount * frameSize * 8;
     double throughputNoAgg = (totalDataBits / tNoAgg) / 1000; // in Kbps
     double throughputAgg = (totalDataBits / tAgg) / 1000;     // in Kbps

     double gain = ((throughputAgg / throughputNoAgg) - 1) * 100;

     output.setText("=== FRAME AGGREGATION ANALYSIS ===\n\n");
     output.append("Number of Frames       : " + frameCount + "\n");
     output.append("Frame Size (bytes)     : " + frameSize + "\n");
     output.append("Aggregation Size       : " + aggSize + "\n\n");

     output.append(String.format("Time without Aggregation : %.4f ms\n", tNoAgg * 1000));
     output.append(String.format("Time with Aggregation    : %.4f ms\n", tAgg * 1000));

     output.append(String.format("\nThroughput without Aggregation : %.2f Kbps", throughputNoAgg));
     output.append(String.format("\nThroughput with Aggregation    : %.2f Kbps", throughputAgg));

     output.append(String.format("\n\nEfficiency Gain : %.2f%%", gain));

     if (gain > 0)
         output.append("\n\nResult: Aggregation improves efficiency!");
     else
         output.append("\n\nResult: No improvement observed.");
 }

 public static void main(String[] args) {
     new Frame_Aggregation();
 }
}

