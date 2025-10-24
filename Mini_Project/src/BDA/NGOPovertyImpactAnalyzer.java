package BDA;

import java.io.*;
import java.util.*;

public class NGOPovertyImpactAnalyzer {

    static String[] keywords = {"poverty", "income", "education", "employment", "health", "training", "women", "sanitation"};

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter NGO report file path: ");
        String path = "C:/Users/manis/eclipse-workspace/Mini_Project/src/BDA/ngo_report1";

        String content = readFile(path).toLowerCase();
        Map<String, Integer> keywordCount = new HashMap<>();

        for (String key : keywords) {
            int count = countWord(content, key);
            keywordCount.put(key, count);
        }

        double impactScore = calculateImpact(keywordCount);
        System.out.println("\n=== NGO Poverty Reduction Impact Report ===");
        System.out.println("File: " + path);
        for (String key : keywordCount.keySet()) {
            System.out.println(key + " : " + keywordCount.get(key));
        }
        System.out.println("\nImpact Score: " + impactScore);
    }

    static String readFile(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append(" ");
        }
        br.close();
        return sb.toString();
    }

    static int countWord(String text, String word) {
        int count = 0;
        String[] arr = text.split("\\W+");
        for (String s : arr) {
            if (s.equals(word)) count++;
        }
        return count;
    }

    static double calculateImpact(Map<String, Integer> map) {
        int sum = 0;
        for (int val : map.values()) sum += val;
        return sum / (double) map.size();
    }
}

