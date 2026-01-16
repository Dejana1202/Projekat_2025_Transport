package com.example.projekat.utils;

import com.example.projekat.models.Bill;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class BillUtil {
    public BillUtil(){}
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
