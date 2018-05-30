package com.mree.inc.track.util;

import android.util.Log;

import com.mree.inc.track.db.persist.Product;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class ReadUtils {
    private static final String TAG = ReadUtils.class.getSimpleName();

    public static List<Product> readCsvFile(String path) {
        List<Product> list = new ArrayList<>();
        File file = new File(path);
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                BufferedReader reader = null;
                if (Charset.forName("UTF-8") == detectCharset(file, Charset.forName("UTF-8"))) {
                    reader = new BufferedReader(
                            new InputStreamReader(fis, Charset.forName("UTF-8")));
                } else {
                    reader = new BufferedReader(
                            new InputStreamReader(fis, Charset.forName("ISO-8859-1")));
                }
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
                        product.setBarcode(getStrAsUtf8(tokens[0]));
                        product.setProductCode(getStrAsUtf8(tokens[1]));
                        product.setName(getStrAsUtf8(tokens[2]));
                        product.setStock(getStrAsUtf8(tokens[3]));
                        product.setSource(getStrAsUtf8(tokens[4]));
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


    private static String getStrAsUtf8(String str) {
        byte[] array = str.getBytes();

        try {
            return new String(array, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return str;
        }
    }

//    public static List<Product> readXLSFile(String path) throws IOException {
//        List<Product> list = new ArrayList<>();
//        InputStream ExcelFileToRead = new FileInputStream(path);
//        HSSFWorkbook wb = new HSSFWorkbook(ExcelFileToRead);
//
//        HSSFSheet sheet = wb.getSheetAt(0);
//        HSSFRow row;
//        HSSFCell cell;
//
//        Iterator rows = sheet.rowIterator();
//        int counter = 0;
//        while (rows.hasNext()) {
//            if (counter == 0) {
//                counter++;
//                continue;
//            }
//            row = (HSSFRow) rows.next();
//            Iterator cells = row.cellIterator();
//            Product product = new Product();
//            int cellCounter = 0;
//            while (cells.hasNext()) {
//                cell = (HSSFCell) cells.next();
//                switch (cellCounter) {
//                    case 0:
//                        product.setBarcode(cell.getStringCellValue());
//                        break;
//                    case 1:
//                        product.setProductCode(cell.getStringCellValue());
//                        break;
//                    case 2:
//                        product.setName(cell.getStringCellValue());
//                        break;
//                    case 3:
//                        product.setStock(cell.getStringCellValue());
//                        break;
//                    case 4:
//                        product.setSource(cell.getStringCellValue());
//                        break;
//                }
//                list.add(product);
//                cellCounter++;
//                if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
//                    System.out.print(cell.getStringCellValue() + " ");
//                } else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
//                    System.out.print(cell.getNumericCellValue() + " ");
//                } else {
//                    //U Can Handel Boolean, Formula, Errors
//                }
//
//            }
//        }
//        return list;
//    }

//    public static List<Product> readXLSXFile(String path) {
//        List<Product> list = new ArrayList<>();
//        try {
//            InputStream ExcelFileToRead = new FileInputStream(path);
//            XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);
//
//            XSSFWorkbook test = new XSSFWorkbook();
//
//            XSSFSheet sheet = wb.getSheetAt(0);
//            XSSFRow row;
//            XSSFCell cell;
//
//            Iterator rows = sheet.rowIterator();
//            int counter = 0;
//            while (rows.hasNext()) {
//                if (counter == 0) {
//                    counter++;
//                    continue;
//                }
//                row = (XSSFRow) rows.next();
//                Product product = new Product();
//                Iterator cells = row.cellIterator();
//                int cellCounter = 0;
//                while (cells.hasNext()) {
//
//                    cell = (XSSFCell) cells.next();
//                    switch (cellCounter) {
//                        case 0:
//                            product.setBarcode(cell.getRawValue());
//                            break;
//                        case 1:
//                            product.setProductCode(cell.getRawValue());
//                            break;
//                        case 2:
//                            product.setName(cell.getRawValue());
//                            break;
//                        case 3:
//                            product.setStock(cell.getRawValue());
//                            break;
//                        case 4:
//                            product.setSource(cell.getRawValue());
//                            break;
//                    }
//                    list.add(product);
//                    cellCounter++;
//                    /*if (cell.getCellTypeEnum() == CellType.STRING) {
//                        System.out.print(cell.getStringCellValue() + " ");
//                    } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
//                        System.out.print(cell.getNumericCellValue() + " ");
//                    } else {
//                        //U Can Handel Boolean, Formula, Errors
//                    }*/
//                }
//            }
//        } catch (IOException e) {
//            Log.e(TAG, e.getMessage() + "");
//        }
//        return list;
//    }

    public static List<Product> readExcelFile(String path) {
        Workbook workbook = null;
        List<Product> list = new ArrayList<>();
        try {
            WorkbookSettings ws = new WorkbookSettings();
            ws.setGCDisabled(true);

            workbook = Workbook.getWorkbook(new File(path), ws);
            Sheet sheet = workbook.getSheet(0);

            int rowCount = sheet.getRows();
            String[][] result = new String[rowCount][];
            for (int i = 0; i < rowCount; i++) {
                Product product = new Product();

                Cell[] row = sheet.getRow(i);
                result[i] = new String[row.length];
                for (int j = 0; j < row.length; j++) {
                    result[i][j] = row[j].getContents();
                    switch (j) {
                        case 0:
                            product.setBarcode(row[j].getContents());
                            break;
                        case 1:
                            product.setProductCode(row[j].getContents());
                            break;
                        case 2:
                            product.setName(row[j].getContents());
                            break;
                        case 3:
                            product.setStock(row[j].getContents());
                            break;
                        case 4:
                            product.setSource(row[j].getContents());
                            break;
                    }
                }
            }


        } catch (BiffException e) {
            Log.e(TAG, e.getMessage() + "");
        } catch (IOException e) {
            Log.e(TAG, e.getMessage() + "");
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
        } finally {
            if (workbook != null) {
                workbook.close();
            }
        }

        return list;

    }


//    public static List<Product> readExcelFile(String path) {
//        try {
//            if (path.endsWith(".xlsx")) {
//                return readXLSXFile(path);
//            } else if (path.endsWith(".xls")) {
//                return readXLSFile(path);
//            } else {
//                Toast.makeText(TrackApp.getContext(), "Desteklenmeyen dosya formatı!!!", Toast
//                        .LENGTH_LONG).show();
//            }
//        } catch (Exception e) {
//            Log.e(TAG, e.getMessage() + "");
//        }
//        return new ArrayList<>();
//    }
//
//


    private static Charset detectCharset(File f, Charset charset) {
        try {
            BufferedInputStream input = new BufferedInputStream(new FileInputStream(f));

            CharsetDecoder decoder = charset.newDecoder();
            decoder.reset();

            byte[] buffer = new byte[512];
            boolean identified = false;
            while ((input.read(buffer) != -1) && (!identified)) {
                identified = identify(buffer, decoder);
            }

            input.close();

            if (identified) {
                return charset;
            } else {
                return null;
            }

        } catch (Exception e) {
            return null;
        }
    }

    private static boolean identify(byte[] bytes, CharsetDecoder decoder) {
        try {
            decoder.decode(ByteBuffer.wrap(bytes));
        } catch (CharacterCodingException e) {
            return false;
        }
        return true;
    }
}
