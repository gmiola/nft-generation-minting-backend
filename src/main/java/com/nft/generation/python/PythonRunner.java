package com.nft.generation.python;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
//import java.io.*;



@Service
public class PythonRunner {
    private ProcessBuilder pb;
    private String pythonPath = "C:\\Users\\giach\\AppData\\Local\\Programs\\Python\\Python37\\python.exe";
    private String execPath = "C:\\Users\\giach\\Desktop\\Tirocinio\\generation\\GenerativeArt\\GenerativeArt1.2.py";

    public PythonRunner() {}

    public void start(JsonNode config){
        System.out.println("Args of ProcessBuilder:" + config.toString());
        List<String> command = Arrays.asList(pythonPath, execPath, config.toString().replace("\"", "\\\""));;
        this.pb = new ProcessBuilder(command);
        try {
            pb.directory(new File("C:\\Users\\giach\\Desktop\\Tirocinio\\generation\\GenerativeArt"));
            Process p = pb.start();
            System.out.println("Running Python starts: ");
            int exitCode = p.waitFor();
            BufferedReader bfr = new BufferedReader(new InputStreamReader(p.getInputStream()));
            System.out.println("Exit Code : "+exitCode);
            String line = "";
            System.out.println("Python output: ");
            while ((line = bfr.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
