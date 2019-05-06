package com.gcp.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.gcp.model.EntityLite;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class Utilities {

    public static final String EMPTY_STRING = "";
    public static final String DELIMITER = " || ";
    public static final String FILE_PATH = "C:\\workspace\\documents\\ocr_files\\";


    public static String convertObjectToJsonString(Object obj) {
        String jsonString = EMPTY_STRING;
        try {
            ObjectMapper mapper = new ObjectMapper();
            jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonString;
    }

    public static void convertAndWriteJsonToFile(Object obj, File file){
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValue(file,obj);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void writeEntitiesToFile(List<EntityLite> entities, String filePath ) throws IOException{

        filePath = filePath + System.currentTimeMillis() + ".txt";
        OutputStream out = new FileOutputStream(new File(filePath));
        for(EntityLite e:entities) {
            out.write(e.toString().getBytes());
        }

    }

    public static String writeDelimitedTextToFile(String description,String filePath) throws IOException {
        filePath = filePath + System.currentTimeMillis() + ".txt";
        OutputStream out = new FileOutputStream(new File(filePath));
        description = description.replace("\n",DELIMITER);
        out.write(description.getBytes());
        out.close();
        return description;
    }

    public static void writeTextToFile(String filePath,List<String> tokens) throws IOException{
        filePath = filePath+ "\\tokens\\" + System.currentTimeMillis() + "_token.txt";
        OutputStream out = new FileOutputStream(new File(filePath));
        for(String s:tokens) {
            out.write(s.getBytes());
        }
        out.close();
    }


}
