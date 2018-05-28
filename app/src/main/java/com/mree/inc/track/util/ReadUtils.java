package com.mree.inc.track.util;

import android.util.Log;

import com.mree.inc.track.db.persist.Product;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class ReadUtils {
    public static List<Product> readCsvFile(String path) {
        List<Product> list = new ArrayList<>();
        File file = new File(path);
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(fis, Charset.forName("UTF-8")));
                String line = "";
                int counter = 0;
                try {
                    while ((line = reader.readLine()) != null) {
                        if (counter == 0) {
                            counter++;
                            continue;
                        }
                        String[] tokens = line.split(",");
                        if (tokens == null || tokens.length < 5) {
                            tokens = line.split(";");
                        }
                        Product product = new Product();
                        product.setBarcode(tokens[0]);
                        product.setProductCode(tokens[1]);
                        product.setName(tokens[2]);
                        product.setStock(tokens[3]);
                        product.setSource(tokens[4]);
                        list.add(product);
                    }
                } catch (IOException e1) {
                    Log.e("MainActivity", "Error" + line, e1);
                    e1.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
        return list;
    }

    public static List<Product> readExcelFile(String path) {
        return new ArrayList<>();
    }
}
