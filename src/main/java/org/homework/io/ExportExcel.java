package org.homework.io;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableModel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExportExcel {

    JTable table;
    FileOutputStream fos;
    JFileChooser jfc = new JFileChooser();

    public ExportExcel(JTable table) {
        this.table = table;
        jfc.addChoosableFileFilter(new FileFilter() {
            public boolean accept(File file) {
                return (file.getName().indexOf("xls") != -1);
            }

            public String getDescription() {
                return "Excel";
            }
        });

        jfc.showSaveDialog(null);
        File file = jfc.getSelectedFile();
        if(file != null){
            try {
                this.fos = new FileOutputStream(file + ".xls");
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void export() {

        if(fos == null)
            return;
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet hs = wb.createSheet();
        TableModel tm = table.getModel();
        int row = tm.getRowCount();
        int cloumn = tm.getColumnCount();
        // System.out.println("row " + row + "  column  " + cloumn);
        HSSFCellStyle style = wb.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        HSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        HSSFCellStyle style1 = wb.createCellStyle();
        style1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style1.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style1.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style1.setFillForegroundColor(HSSFColor.ORANGE.index);
        style1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        HSSFFont font1 = wb.createFont();
        font1.setFontHeightInPoints((short) 15);
        font1.setBoldweight((short) 700);
        style1.setFont(font);

        for (int i = 0; i < row + 1; i++) {
            HSSFRow hr = hs.createRow(i);
            for (int j = 0; j < cloumn; j++) {
                if (i == 0) {
                    String value = tm.getColumnName(j);
                    // System.out.println("value " + value);
                    int len = value.length();
                    hs.autoSizeColumn(j);
//                    hs.setColumnWidth(j,   200);
                    HSSFRichTextString srts = new HSSFRichTextString(value);
                    HSSFCell hc = hr.createCell((short) j);
                    hc.setCellStyle(style1);
                    hc.setCellValue(srts);
                } else {
//                    System.out.println("vlue  " + tm.getValueAt(i - 1, j));
                    if (tm.getValueAt(i - 1, j) != null) {
                        String value = tm.getValueAt(i - 1, j).toString();
                        HSSFRichTextString srts = new HSSFRichTextString(value);
                        HSSFCell hc = hr.createCell((short) j);
                        hc.setCellStyle(style);

                        if (value.equals("") || value == null) {
                            hc.setCellValue(new HSSFRichTextString(""));
                        } else {
                            hc.setCellValue(srts);
                        }
                    }
                }
            }
        }

        try {
            wb.write(fos);
            fos.close();
            JOptionPane.showMessageDialog(null,"导出成功！");
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,"导出失败！");
        }
    }
}