package com.november.book;/*
 *
 **/

import com.november.model.ExcelHead;
import org.junit.Test;

public class test {
    /*public void testEx()throws IOException, InvalidFormatException {
        *//*File xlsFile = new File("C:\\Users\\len\\Desktop\\table_1.xls");
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
        System.out.println("");*//*
    }*/

    @Test
    public void test(){
        String[] a= ExcelHead.bookHead;
        for(String str:a){
            System.out.println(str);
        }
        System.out.println(a.length);

    }
}
