package com.buildit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SitemapGenerator {

    private static Logger log = LoggerFactory.getLogger(SitemapGenerator.class);
    private String outputLoc;
    private File output;
    FileWriter fw;
    BufferedWriter bw;
    AppConfig config;

    public SitemapGenerator(){
        this.init();
    }

    private void init(){
        config = AppConfig.getInstance();
        outputLoc = config.getProperty("output.file");
        output = new File(outputLoc);
        try {
            if (!output.exists()) {
                output.createNewFile();
            }
            fw = new FileWriter(output.getAbsoluteFile());
        } catch (IOException e) {
            log.error("Unable to initialize output file !! ",e.getMessage());
        }
        bw = new BufferedWriter(fw);
    }

    public void writeLineToFile(String string) {
        writeToFile(string,true);
    }

    public void writeToFile(String string, boolean newLine) {
        try {
            bw.write(string);
            if(newLine){
                bw.newLine();
            }
        } catch (IOException e) {
            log.error("Unable to write to output file !! ",e.getMessage());
        }
    }

    public String getOutputLoc() {
        return outputLoc;
    }
}
