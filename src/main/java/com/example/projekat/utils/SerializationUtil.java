package com.example.projekat.utils;

import com.example.projekat.models.Bill;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Klasa za serijalizaciju. Od podataka o kupovini jedne karte formiramo racun za prodaju.
 * Podatke o racunu serijalizujemo, a kasnije podatke kao sto su cijene svih racuna deserijalizujemo.
 * Takve podatke koristimo, da bismo imali uvid u podatke o ukupnoj zaradi i broju prodati karata.
 */
public class SerializationUtil {

    private static int billId=0;
    /**
     * @param billCounter brojac koliko ima racuna
     */
    private static int billCounter=0;
    /**
     * pattern za pronalazak fajlova u formatu billN.ser
     */
    private static final Pattern BILL_NAME_PATTERN = Pattern.compile("bill(\\d+)\\.ser", Pattern.CASE_INSENSITIVE);
    private SerializationUtil(){super();}
    /**
     * Cuvanje podataka o racunu
     * */
    public static synchronized boolean saveBill(Bill bill, String path){
        if (bill==null) return false;

        File dir = new File(path);
        if (!dir.exists()){
            if (!dir.mkdirs()) return false;
        }
        if (billId ==0){
            initCountersFromFolder(dir);
        }
        billId++;
        String filename="bill"+billId+".ser";
        File outFile = new File(dir, filename);

        bill.setId(billId);

        ObjectOutputStream objectOutputStream  = null;
        try(FileOutputStream fileOutputStream = new FileOutputStream(outFile)) {
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(bill);
            objectOutputStream.flush();

            billCounter++;
            return true;
        }
        catch (IOException e){
            e.printStackTrace();
            return false;
        }
        finally {
            if (objectOutputStream != null){
                try {
                    objectOutputStream.close();
                }
                catch (IOException ignored){}
            }
        }
    }
    /**
     * Inicijalizacija ako je billId = 0
     * */
    private static void initCountersFromFolder(File dir){
        File[] files = dir.listFiles(
                (d, name) -> BILL_NAME_PATTERN.matcher(name).matches()
        );

        if (files == null || files.length==0){
            billId = 0;
            billCounter = 0;
            return;
        }
        int maxId = 0;
        int count = 0;

        for (File f : files){
            Matcher m = BILL_NAME_PATTERN.matcher(f.getName());
            if (m.matches()){
                try {
                    int val = Integer.parseInt(m.group(1));
                    if (val>maxId) maxId=val;
                }catch (NumberFormatException ignored){}
                count++;
            }
        }
        billId = maxId;
        billCounter = count;
    }
    /**
     * Deserijalizacija svih racuna. Citanje podataka o cijenama svih racuna i suma svih.
     * */
    public static synchronized int sumPricesFromBills(String path){
        File dir = new File(path);
        if (!dir.exists() || !dir.isDirectory()){
            return 0;
        }

        initCountersFromFolder(dir);

        int total=0;
        File[] files = dir.listFiles(
                (d,name)->BILL_NAME_PATTERN.matcher(name).matches()
        );
        if (files==null || files.length==0){
            billCounter=0;
            return 0;
        }

        int counted = 0;
        for (File f : files){
            ObjectInputStream objectInputStream = null;
            try(FileInputStream fileInputStream = new FileInputStream(f)) {
                objectInputStream = new ObjectInputStream(fileInputStream);

                Bill bill = (Bill) objectInputStream.readObject();

                total+=bill.getPrice();
                counted++;
            }
            catch (IOException | ClassNotFoundException e){
                e.printStackTrace();
            }
            finally {
                if (objectInputStream!= null){
                    try {
                        objectInputStream.close();
                    }catch (IOException ignored){}
                }
            }
        }
        billCounter = counted;return total;

    }
    public static synchronized int getBillCounter(){
        return billCounter;
    }
    public static synchronized void setBillCounter(int counter){
        billCounter=counter;
    }


}
