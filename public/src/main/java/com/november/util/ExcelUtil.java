package com.november.util;/*
 *
 **/

import com.november.common.JsonData;
import com.november.exception.ParamException;
import com.november.model.BookExcel;
import com.november.model.ExcelHead;

import java.awt.print.Book;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;


public class ExcelUtil {

    public HSSFWorkbook downloadBook(List<BookExcel> list) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        //创建一个Excel表单,参数为sheet的名字
        HSSFSheet sheet = workbook.createSheet("书籍表");
        setTitle(workbook, sheet,ExcelHead.bookHead);
        //新增数据行，并且设置单元格数据
        int rowNum = 1;
        for (BookExcel bookExcel : list) {
            HSSFRow row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue(bookExcel.getBookName());
            row.createCell(1).setCellValue(bookExcel.getBookCode());
            row.createCell(2).setCellValue(bookExcel.getAuthorName());
            row.createCell(3).setCellValue(bookExcel.getPrice());
            row.createCell(4).setCellValue(bookExcel.getPressName());
            row.createCell(5).setCellValue(bookExcel.getBookTypeId());
            row.createCell(6).setCellValue(bookExcel.getBookChcoType());
            row.createCell(7).setCellValue(bookExcel.getRemark());
            rowNum++;
        }
        return workbook;
    }

    /***
     * 设置表头
     * @param workbook
     * @param sheet
     */
    private void setTitle(HSSFWorkbook workbook, HSSFSheet sheet,String[] head) {
        HSSFRow row = sheet.createRow(0); //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        HSSFCell cell;
        for(int i=0;i<head.length;i++){
            sheet.setColumnWidth(i, 25 * 256);
            cell = row.createCell(i);
            cell.setCellValue(head[i]);
        }

    }


    public static List<BookExcel> loadBookExcel(MultipartFile file){
        if (file.isEmpty()) {
            throw new ParamException("文件为空");
        }
        List<BookExcel> bookExcels = new ArrayList<>();
        try {
            HSSFWorkbook workbook = new HSSFWorkbook(new POIFSFileSystem(file.getInputStream())); //有多少个sheet
            int sheets = workbook.getNumberOfSheets();

            for (int i = 0; i < sheets; i++) {

                HSSFSheet sheet = workbook.getSheetAt(i); //获取多少行
                int rows = sheet.getPhysicalNumberOfRows();
                BookExcel bookExcel = null; //遍历每一行，注意：第 0 行为标题
                for (int j = 0; j < rows; j++) {
                    try {
                        bookExcel = new BookExcel();
                        HSSFRow row = sheet.getRow(j);//获得第 j 行
                        int lie = sheet.getRow(0).getPhysicalNumberOfCells();
                        for (int k = 0; k < lie; k++) {
                            if (row.getCell(k) == null) {
                                continue;
                            }
                            switch (sheet.getRow(0).getCell(k).getStringCellValue()) {
                                case "书籍名称":
                                    bookExcel.setBookName(row.getCell(k).getStringCellValue());
                                    break;
                                case "书籍编号":
                                    bookExcel.setBookCode(row.getCell(k).getStringCellValue());
                                    break;
                                case "作者":
                                    bookExcel.setAuthorName(row.getCell(k).getStringCellValue());
                                    break;
                                case "价格":
                                    bookExcel.setPrice(row.getCell(k).getStringCellValue());
                                    break;
                                case "出版社":
                                    bookExcel.setPressName(row.getCell(k).getStringCellValue());
                                    break;
                                case "书籍类型":
                                    bookExcel.setBookTypeId(row.getCell(k).getStringCellValue());
                                    break;
                                case "收费方式":
                                    bookExcel.setBookChcoType(row.getCell(k).getStringCellValue());
                                    break;
                                case "备注":
                                    bookExcel.setRemark(row.getCell(k).getStringCellValue());
                                    break;
                            }

                        }
                    } catch (Exception e) {
                        throw new ParamException("错误，请检查Excel的第" + (j + 1) + "行数据");
                    }
                    bookExcels.add(bookExcel);
                }

            }

        } catch (IOException e) {
            throw new ParamException("读取Excel失败");
        }
        return bookExcels;
    }
}