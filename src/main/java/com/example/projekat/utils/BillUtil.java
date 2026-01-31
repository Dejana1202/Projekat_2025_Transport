package com.example.projekat.utils;

import com.example.projekat.models.Bill;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * BillUtil je pomocna klasa za obradu racuna tj. klase @see Bill. Racun nastaje pri kupovini prevozne karte.
 */
public class BillUtil {
    public BillUtil(){}

    /**
     * Cuvanje podataka o racunu u tekstualni fajl.
     * @param bill racun, nastao kupovinom prevozne karte.
     * @param path putanja, na kojoj smjestamo fajl racuna.
     * @return
     */
    public static boolean saveBillToTxt(Bill bill, String path){
        if (bill==null) return false;

        int currentBillNumber = SerializationUtil.getBillCounter();
        String fileName = "bill"+currentBillNumber+".txt";

        File dir = new File(path);
        if (!dir.exists()){
            if (!dir.mkdirs()) return false;
        }
        File outFile = new File(dir, fileName);

        try (FileWriter writer= new FileWriter(outFile)) {
            writer.write(bill.toString());
            writer.flush();
            return true;
        }
        catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }
}
