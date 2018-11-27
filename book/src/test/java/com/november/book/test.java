package com.november.book;/*
 *
 **/

import com.november.book.model.Book;
import com.november.book.util.BookCodeUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.junit.Test;
import sun.net.www.content.image.x_xpixmap;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class test {
    @Test
    public void testEx()throws IOException, InvalidFormatException {
        File xlsFile = new File("C:\\Users\\len\\Desktop\\table_1.xls");
        // 获得工作簿
        Workbook workbook = WorkbookFactory.create(xlsFile);
        // 获得工作表个数
        int sheetCount = workbook.getNumberOfSheets();
        // 遍历工作表
        for (int i = 0; i < sheetCount; i++)
        {
            Sheet sheet = workbook.getSheetAt(i);
            // 获得行数
            int rows = sheet.getLastRowNum() + 1;
            // 获得列数，先获得一行，在得到改行列数
            Row tmp = sheet.getRow(0);
            if (tmp == null)
            {
                continue;
            }
            int cols = tmp.getPhysicalNumberOfCells();
            // 读取数据
            for (int row = 0; row < rows; row++)
            {
                Row r = sheet.getRow(row);
                for (int col = 0; col < cols; col++)
                {
                    if(r.getCell(col)!=null){
                        r.getCell(col).setCellType(CellType.STRING);
                        System.out.println("==>>"+ r.getCell(col).getStringCellValue());
                    }
                }
            }
        }
        System.out.println("");
    }
}
