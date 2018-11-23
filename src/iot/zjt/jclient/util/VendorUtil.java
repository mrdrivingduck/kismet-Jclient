package iot.zjt.jclient.util;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

/**
 * Used for getting manufactor from MAC address.
 * Data from : https://standards.ieee.org/products-services/regauth/index.html#
 * @author Mr Dk.
 * @version 2018.11.23
 */

public class VendorUtil {

    private static final VendorUtil instance = new VendorUtil();
    private Map<String, String> vendorMap = new HashMap<>(26000);

    private final String OUI_PATH = "data/oui.csv";
    private final int INDEX_MAC = 1;
    private final int INDEX_ORG = 2;

    private VendorUtil() {
        try {
            Reader in = new FileReader(OUI_PATH);
            Iterable<CSVRecord> records = CSVFormat
                .RFC4180
                .parse(in);
            for (CSVRecord record : records) {
                String mac = record.get(INDEX_MAC);
                String organization = record.get(INDEX_ORG);
                vendorMap.put(mac, organization);
            }
        } catch(IOException e) {
            System.err.println("Failed to load " + OUI_PATH);
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

        StringBuilder key = new StringBuilder(splits[0])
            .append(splits[1])
            .append(splits[2]);
        return vendorMap.containsKey(key.toString()) ?
            vendorMap.get(key.toString()) : "";
    }
}