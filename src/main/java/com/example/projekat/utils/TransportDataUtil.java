package com.example.projekat.utils;

import com.example.projekat.models.TransportData;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

/**
 * Pomocna klasa, za dobavljanje podataka o mapi drzave iz JSON fajla
 */
public class TransportDataUtil
{

    private static final ObjectMapper MAPPER = new ObjectMapper();
    public static TransportData readFromFile(String fileName) throws IOException{
        return MAPPER.readValue(new File(fileName), TransportData.class);
    }
}
