package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Created by Mehdi on 16-05-31.
 */
public class CsvReader {

    public static ArrayList csv(String name){
        Scanner scanner = null;
        ArrayList<String> smell_list = new ArrayList<String>();
        try {
            scanner = new Scanner(new File(name+".csv"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }

        scanner.next();

        while (scanner.hasNext())
        {
            String a = scanner.next();
            smell_list.add(a);
        }

        scanner.close();
        return smell_list;
    }

    public static HashSet<String> formatCsv_MIM(String file_name){
        HashSet<String> toFill = new HashSet<>();

        ArrayList<String> csv_reader = CsvReader.csv(file_name);
        for (String e : csv_reader) {
            String [] split = e.split(",");
            toFill.add(split[8]);
        }

        return toFill;
    }

    public static HashSet<String> formatCsvHahs(String file_name){
        HashSet<String> toFill = new HashSet<>();

        ArrayList<String> csv_reader = CsvReader.csv(file_name);
        for (String e : csv_reader) {
            String [] split = e.split(",");
            String [] splitHash = split[1].split("#");
            toFill.add(splitHash[1]);
        }
        return toFill;
    }

}
