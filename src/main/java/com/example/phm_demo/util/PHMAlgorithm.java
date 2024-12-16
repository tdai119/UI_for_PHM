package com.example.phm_demo.util;

import java.io.IOException;

public class PHMAlgorithm {

    public static void runPHM(String inputFilePath, String outputFilePath, int minUtil, int minPer, int maxPer, int minAvgPer, int maxAvgPer) throws IOException {
        // Command to execute SPMF PHM Algorithm
        String command = String.format(
                "java -jar spmf/spmf.jar run PHM %s %s %d %d %d %d %d",
                inputFilePath, outputFilePath, minUtil, minPer, maxPer, minAvgPer, maxAvgPer
        );

        Process process = Runtime.getRuntime().exec(command);
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            throw new IOException("Algorithm execution interrupted", e);
        }
    }
}