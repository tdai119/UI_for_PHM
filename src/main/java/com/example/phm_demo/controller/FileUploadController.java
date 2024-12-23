package com.example.phm_demo.controller;

import com.example.phm_demo.util.PHMAlgorithm;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Controller
public class FileUploadController {

    private final String uploadDir;

    public FileUploadController() {
        // Dynamically resolve the upload directory within the project directory
        this.uploadDir = new File("").getAbsolutePath() + File.separator + "uploads";
    }

    @GetMapping("/")
    public String index() {
        clearUploadsFolder();
        return "upload"; // Render the file upload form
    }

    private void clearUploadsFolder() {
        File directory = new File(uploadDir);
        if (directory.exists() && directory.isDirectory()){
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        file.delete();
                    }
                }
            }
        }
    }

    @PostMapping("/process")
    public String handleFileUpload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("minutil") int minUtil,
            @RequestParam("minper") int minPer,
            @RequestParam("maxper") int maxPer,
            @RequestParam("minavgper") int minAvgPer,
            @RequestParam("maxavgper") int maxAvgPer,
            Model model) {
        try {
            // Ensure the upload directory exists
            File directory = new File(uploadDir);
            if (!directory.exists() && !directory.mkdirs()) {
                throw new IOException("Failed to create upload directory: " + directory.getAbsolutePath());
            }

            // Save uploaded file in the resolved directory
            File inputFile = new File(directory, file.getOriginalFilename());
            file.transferTo(inputFile);

            // Run PHM algorithm
            String outputPath = uploadDir + File.separator + "output.txt";
            PHMAlgorithm.runPHM(inputFile.getAbsolutePath(), outputPath, minUtil, minPer, maxPer, minAvgPer, maxAvgPer);

            // Prepare model attributes for result display
            model.addAttribute("message", "File processed successfully!");
            model.addAttribute("outputPath", outputPath);

            // Get the entire output result and add to model for preview
            List<String> resultPreview = Files.readAllLines(new File(outputPath).toPath());
            model.addAttribute("preview", resultPreview); // Pass entire list, no subList limitation

        } catch (IOException e) {
            model.addAttribute("message", "Failed to process file: " + e.getMessage());
            e.printStackTrace(); // Log stack trace for debugging
        }
        return "result";
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam("path") String filePath) {
        try {
            File file = new File(filePath);

            // Check if file exists
            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = new FileSystemResource(file);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"");
            headers.add(HttpHeaders.CONTENT_TYPE, "text/plain");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}