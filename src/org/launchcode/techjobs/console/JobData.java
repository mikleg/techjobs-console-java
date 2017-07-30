package org.launchcode.techjobs.console;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LaunchCode
 */
public class JobData {

    private static final String DATA_FILE = "resources/job_data.csv";
    private static Boolean isDataLoaded = false;

    private static ArrayList<HashMap<String, String>> allJobs;

    /**
     * Fetch list of all values from loaded data,
     * without duplicates, for a given column.
     *
     * @param field The column to retrieve values from
     * @return List of all of the values of the given field
     */
    public static ArrayList<String> findAll(String field) {

        // load data, if not already loaded
        loadData();

        ArrayList<String> values = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {
            String aValue = row.get(field);

            if (!values.contains(aValue)) {
                values.add(aValue);
            }
        }

        return values;
    }

    public static ArrayList<HashMap<String, String>> findAll() {

        // load data, if not already loaded
        loadData();

        return allJobs;
    }

    /**
     * Returns results of search the jobs data by key/value, using
     * inclusion of the search term.
     *
     * For example, searching for employer "Enterprise" will include results
     * with "Enterprise Holdings, Inc".
     *
     * @param column   Column that should be searched.
     * @param value Value of teh field to search for
     * @return List of all jobs matching the criteria
     */
 /*   public static ArrayList<HashMap<String, String>> findByColumnAndValue(String column, String value) {

        // load data, if not already loaded
        loadData();

        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {

            String aValue = row.get(column);

            if (aValue.contains(value)) {
                jobs.add(row);
            }
        }

        return jobs;
    }*/
 //case insensitive version
     public static ArrayList<HashMap<String, String>> findByColumnAndValue(String column, String value) {

        // load data, if not already loaded
        loadData();

        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {

            String aValue = row.get(column);

            if (searchStringIC(aValue,value))
            {
                jobs.add(row);
            }
        }

        return jobs;
    }
/*    public static ArrayList<HashMap<String, String>> findByValue(String value)
    {
        loadData();

        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();

        for (HashMap<String, String> row : allJobs)
        {

            for (Map.Entry<String, String> myEntry : row.entrySet())
            {
                String aValue = myEntry.getValue().toLowerCase();
                String aKey = myEntry.getKey().toLowerCase();
                if (aValue.contains(value.toLowerCase())||aKey.contains(value.toLowerCase()))
                {
                    jobs.add(row);
                    break;
                }
            }
        }
        return jobs;
    }*/
    public static ArrayList<HashMap<String, String>> findByValue(String value)
        {
            loadData();

            ArrayList<HashMap<String, String>> jobs = new ArrayList<>();

            for (HashMap<String, String> row : allJobs)
            {

                for (Map.Entry<String, String> myEntry : row.entrySet())
                {
                    String aValue = myEntry.getValue();
                    String aKey = myEntry.getKey();
                    if (searchStringIC(aValue,value)||searchStringIC(aKey,value))
                    {
                        jobs.add(row);
                        break;
                    }
                }
            }
            return jobs;
        }
    public static boolean searchStringIC(String myString,String value)
    {
        int max_iter = myString.length()-value.length();
        if (max_iter>=0)
        {
            for (int i = 0;i <= max_iter;i++)
            {
                if (value.equalsIgnoreCase(myString.substring(i,i+value.length())))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Read in data from a CSV file and store it in a list
     */
    private static void loadData() {

        // Only load data once
        if (isDataLoaded) {
            return;
        }

        try {

            // Open the CSV file and set up pull out column header info and records
            Reader in = new FileReader(DATA_FILE);
            CSVParser parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            List<CSVRecord> records = parser.getRecords();
            Integer numberOfColumns = records.get(0).size();
            String[] headers = parser.getHeaderMap().keySet().toArray(new String[numberOfColumns]);

            allJobs = new ArrayList<>();

            // Put the records into a more friendly format
            for (CSVRecord record : records) {
                HashMap<String, String> newJob = new HashMap<>();

                for (String headerLabel : headers) {
                    newJob.put(headerLabel, record.get(headerLabel));
                }

                allJobs.add(newJob);
            }

            // flag the data as loaded, so we don't do it twice
            isDataLoaded = true;

        } catch (IOException e) {
            System.out.println("Failed to load job data");
            e.printStackTrace();
        }
    }

}
