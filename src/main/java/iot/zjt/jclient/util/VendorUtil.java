package iot.zjt.jclient.util;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

/**
 * Used for getting manufactor from MAC address.
 * Data from : https://standards.ieee.org/products-services/regauth/index.html#
 * @author Mr Dk.
 * @version 2019.1.10
 */

public class VendorUtil {

    private static final VendorUtil instance = new VendorUtil();

    /**
     * key - MAC address block
     * value - Organization name
     */
    private Map<String, String> largeBlock = new HashMap<>(26000);
    private Map<String, String> mediumBlock = new HashMap<>(2500);
    private Map<String, String> smallBlock = new HashMap<>(3200);

    /**
     * CSV file path
     */
    private final String LARGE_BLOCK_PATH = "/oui.csv";
    private final String MEDIUM_BLOCK_PATH = "/mam.csv";
    private final String SMALL_BLOCK_PATH = "/oui36.csv";

    /**
     * Index of columns in CSV file
     * [1] - MAC address block
     * [2] - Organization name
     */
    private final int INDEX_MAC = 1;
    private final int INDEX_ORG = 2;

    private VendorUtil() {
        try {
            Reader in = new InputStreamReader(this.getClass().getResourceAsStream(LARGE_BLOCK_PATH));
            Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(in);
            for (CSVRecord record : records) {
                String mac = record.get(INDEX_MAC);
                String organization = record.get(INDEX_ORG);
                largeBlock.put(mac, organization);
            }
            in.close();

            in = new InputStreamReader(this.getClass().getResourceAsStream(MEDIUM_BLOCK_PATH));
            records = CSVFormat.RFC4180.parse(in);
            for (CSVRecord record : records) {
                String mac = record.get(INDEX_MAC);
                String organization = record.get(INDEX_ORG);
                mediumBlock.put(mac, organization);
            }
            in.close();

            in = new InputStreamReader(this.getClass().getResourceAsStream(SMALL_BLOCK_PATH));
            records = CSVFormat.RFC4180.parse(in);
            for (CSVRecord record : records) {
                String mac = record.get(INDEX_MAC);
                String organization = record.get(INDEX_ORG);
                smallBlock.put(mac, organization);
            }
            in.close();

        } catch(IOException e) {
            System.err.println("Failed to load CSV files");
        }
    }

    public static VendorUtil getInstance() {
        return instance;
    }

    /**
     * Get organization from MAC address
     * @param mac in the form of "XX:XX:XX:XX:XX:XX"
     * @return organization string
     */
    public String getOrganization(String mac) {
        String[] splits = mac.split(":");
        if (splits.length != 6) {
            return "";
        }

        StringBuilder key = new StringBuilder();
        for (String split : splits) {
            key.append(split);
        }

        /**
         * Small block address : 36-bit (9-bit in HEX)
         */
        if (smallBlock.containsKey(key.toString().substring(0, 9))) {
            return smallBlock.get(key.toString().substring(0, 9));
        }

        /**
         * Medium block address : 28-bit (7-bit in HEX)
         */
        if (mediumBlock.containsKey(key.toString().substring(0, 7))) {
            return mediumBlock.get(key.toString().substring(0, 7));
        }

        /**
         * Small block address : 24-bit (6-bit in HEX)
         */
        if (largeBlock.containsKey(key.toString().substring(0, 6))) {
            return largeBlock.get(key.toString().substring(0, 6));
        }

        return "";
    }
}