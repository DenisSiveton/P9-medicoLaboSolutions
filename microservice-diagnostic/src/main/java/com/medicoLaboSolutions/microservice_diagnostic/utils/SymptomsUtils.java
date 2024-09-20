package com.medicoLaboSolutions.microservice_diagnostic.utils;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Data
@Configuration
public class SymptomsUtils {
    private List<String> listOfSymptoms;

    private final String FILE_NAME = "symptoms.txt";

    public SymptomsUtils() {
        this.listOfSymptoms = createListSymptoms(FILE_NAME);
    }

    private List<String> createListSymptoms(String fileName) {
        List<String> listOfSymptoms = new ArrayList<>();
        if (FILE_NAME != null) {
            BufferedReader reader = null;

            try {
                ClassLoader classLoader = getClass().getClassLoader();
                InputStream inputStream = classLoader.getResourceAsStream(FILE_NAME);
            reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

                for(String line = reader.readLine(); line != null; line = reader.readLine()) {
                    listOfSymptoms.add(line);
                }
            } catch (IOException var12) {
                var12.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException var11) {
                        var11.printStackTrace();
                    }
                }

            }
        } else {
            System.out.println("Input file does not exist, please try another file.\n");
        }
        return listOfSymptoms;
    }
}
