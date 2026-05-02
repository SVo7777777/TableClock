package com.example.screenclock;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import android.annotation.SuppressLint;

public class PhoneFromFile {
    public static String[] phoneFromFile(String file){
        String line1 = null;
        String line2 = null;
        String[] smsonoff = new String[2];
        StringBuilder sb = new StringBuilder();
        try (FileInputStream fis = new FileInputStream(file);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader br = new BufferedReader(isr)) {

            String line = br.readLine();
            if (line != null) {
                line1 = line;
                line = br.readLine();
                if (line != null) {
                    line2 = line;
                }
            }
            System.out.println(line1);
            System.out.println(line2);

            smsonoff[0]=line1;
            smsonoff[1]=line2;



        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return smsonoff;
    }
}
