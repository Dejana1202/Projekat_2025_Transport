package com.example.projekat.utils;

import com.example.projekat.models.TransportData;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class TransportDataUtil
{
//    public static final String DATAPATH = "transport_data.json";
//    public TransportDataUtil(){}
//    public static TransportData writeData(String path){
//        ObjectMapper om = new ObjectMapper();
//
//        try {
//            return (TransportData) om.readValue(new File(path), TransportData.class);
//        }catch (IOException e){
//            e.printStackTrace();
//            return null;
//        }
//    }
    private static final ObjectMapper MAPPER = new ObjectMapper();
    public static TransportData readFromFile(String fileName) throws IOException{
        return MAPPER.readValue(new File(fileName), TransportData.class);
    }
}
