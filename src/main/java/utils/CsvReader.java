package utils;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

/**
 * Created by  on 16-05-31.
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

    /*
    position= la position de l'attribut full_name dans le fichier result.csv
    position=8--->MIM
    position=3--->NLMR
    position=7--->LIC
    position=3--->HBR
    position=3--->HAS

     */
    public static HashSet<String> formatCsv(String file_name, int position){
        HashSet<String> toFill = new HashSet<>();

        ArrayList<String> csv_reader = CsvReader.csv(file_name);
        for (String e : csv_reader) {
            String [] split = e.split(",");
            toFill.add(split[position]);
        }

        return toFill;
    }




    public static List<String[]> readAllDataAtOnce(String file)
    {
        List<String[]> allData = null;
        try {
            // Create an object of file reader
            // class with CSV file as a parameter.
            FileReader filereader = new FileReader(file);

            // create csvReader object and skip first Line
            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withSkipLines(1)
                    .build();
             allData = csvReader.readAll();


        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return allData;
    }

    public static void writeData(String filePath, List<String[]> data )
    {
        // first create file object for file placed at location
        // specified by filepath
        File file = new File(filePath);
        try {
            // create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(file);
            //FileWriter outputfile = new FileWriter(file,true);


            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputfile);

            //adding header to csv
            String[] header = { "Path" };
            writer.writeNext(header);

            // add data to csv

            writer.writeAll(data);

            // closing writer connection
            writer.close();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
