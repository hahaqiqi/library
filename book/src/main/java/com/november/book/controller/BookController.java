package com.november.book.controller;

import com.november.book.model.Book;
import com.november.book.param.BookParam;
import com.november.book.service.BookService;
import com.november.book.service.BookTypeService;
import com.november.book.util.IsEmpty;
import com.november.common.JsonData;
import com.november.model.BookExcel;
import com.november.util.Email;
import com.november.util.ExcelUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/book")
public class BookController {
    @Resource(name = "bookService")
    private BookService bookService;
    @Resource(name = "bookTypeService")
    private BookTypeService bookTypeService;

    private BookParam filtrateBookParam = null; //筛选条件

    @RequestMapping(value = "/book.html")
    public String toBook() {
        return "book";
    }

    @RequestMapping(value = "/bookAdd.html")
    public String toBookAdd() {
        return "bookAdd";
    }

    @RequestMapping(value = "/bookBorrow.html")  //直接拦截借书的页面
    public String toBookBorrow() {
        return "bookBorrow";
    }

    @RequestMapping(value = "/save.json", method = RequestMethod.POST)
    @ResponseBody
    public JsonData saveBookType(BookParam param) {
        bookService.saveBook(param);
        return JsonData.success();
    }

    @RequestMapping(value = "/update.json", method = RequestMethod.POST)
    @ResponseBody
    public JsonData updateBookType(BookParam param) {
        bookService.updateBook(param);
        return JsonData.success();
    }

    @RequestMapping(value = "/updateLeaseId.json", method = RequestMethod.POST)//修改单本书籍的租借id
    @ResponseBody
    public JsonData updateBookTypeLeaseIdByBookId(Integer bookId, Integer leaseId) {
        bookService.updateBookLeaseIdByBookId(bookId, leaseId);
        return JsonData.success();
    }

    @RequestMapping(value = "/batchUpdate.json", method = RequestMethod.POST)  //修改
    @ResponseBody
    public JsonData batchUpdateBookType(HttpServletRequest request, BookParam param) {
        try {
            if (IsEmpty.isAllFieldNull(param)) {
                return JsonData.fail("无更改");
            }
        } catch (Exception e) {
            return JsonData.fail("未知错误");
        }
        HttpSession session = request.getSession();
        BookParam bookParam = null;
        try {
            bookParam = (BookParam) session.getAttribute("bookParam");
            param.setWhereList(bookParam.getWhereList());
        } catch (Exception ex) {

        }
        bookService.batchUpdate(param);
        return JsonData.success();
    }

    @RequestMapping(value = "/delete.json", method = RequestMethod.GET)
    @ResponseBody
    public JsonData deleteBookType(Integer id) {
        bookService.deleteBook(id);
        return JsonData.success();
    }

    @RequestMapping(value = "/reFiltrate.json", method = RequestMethod.GET)
    @ResponseBody
    public JsonData reFiltrate() {
        filtrateBookParam = null;
        return JsonData.success();
    }

    @RequestMapping(value = "/setFiltrate.json", method = RequestMethod.GET)
    @ResponseBody
    public JsonData setFiltrate(HttpServletRequest request, BookParam bookParam) {
        try {
            if (!IsEmpty.isAllFieldNull(bookParam)) {
                List<Book> list = bookService.whereListBook(bookParam);
                List<Integer> listInt = new ArrayList<>();
                for (Book book : list) {
                    listInt.add(book.getId());
                }
                bookParam.setWhereList(listInt);
                filtrateBookParam = bookParam;
            }
        } catch (Exception e) {

        }
        return JsonData.success();
    }

    @RequestMapping(value = "/list.json", method = RequestMethod.GET)//查 包括高级查询
    @ResponseBody
    public JsonData listBookType(HttpServletRequest request) {
        int count;
        if (filtrateBookParam == null) {
            count = bookService.bookCount();
        } else {
            count = filtrateBookParam.getWhereList().size();
        }
        int page = Integer.parseInt(request.getParameter("page"));//第几页
        int limit = Integer.parseInt(request.getParameter("limit"));//每页显示条数
        List<Book> listBook = bookService.pageListBook(page, limit, filtrateBookParam);
        return JsonData.pageSuccess(listBook, count, limit);
    }

    /*@RequestMapping(value = "/list.json", method = RequestMethod.GET)//查 包括高级查询
    @ResponseBody
    public JsonData listBookType(HttpServletRequest request) {
        int count;
        if(filtrateBookParam==null){
            count=bookService.bookCount();
        }else {
            count = filtrateBookParam.getWhereList().size();
        }
        int page = Integer.parseInt(request.getParameter("page"));//第几页
        int limit = Integer.parseInt(request.getParameter("limit"));//每页显示条数
        List<Book> listBook = bookService.pageListBook(page, limit, filtrateBookParam);
        return JsonData.pageSuccess(listBook, count, limit);
    }*/

    @RequestMapping(value = "/select.json", method = RequestMethod.GET)
    @ResponseBody
    public JsonData selectBookType(Integer id) {

        return JsonData.success();
    }


    @RequestMapping(value = "/batchDelete.json", method = RequestMethod.GET)//批量删除
    @ResponseBody
    public JsonData batchDeleteBookType(String batchStrId) {
        List<Integer> list = new ArrayList<>();
        String params[] = batchStrId.split(",");//参数jie()
        for (int i = 0; i < params.length; i++) {
            list.add(Integer.valueOf(params[i]));
        }
        int count = bookService.batchDeleteBook(list);
        return JsonData.success(null, count + "");
    }

    @RequestMapping(value = "/changeBookStatus.json", method = RequestMethod.GET)//改变单本书籍的状态
    @ResponseBody
    public JsonData changeBookStatus(Integer id, Integer statusId) {
        bookService.changeBookStatus(id, statusId);
        return JsonData.success();
    }

    @RequestMapping(value = "/searchBook.json", method = RequestMethod.GET)
    @ResponseBody
    public JsonData searchBook(String searchVal) {
        String params[] = searchVal.split(",");
        List<String> listStr = new ArrayList<>();
        for (String str : params) {
            listStr.add(str);
        }
        List<Book> list = bookService.getBookByCode(listStr);
        return JsonData.success(list);
    }

    @RequestMapping(value = "/bookState.json", method = RequestMethod.GET)
    @ResponseBody
    public JsonData bookState(Integer id) {//根据书籍id判断 未上架,可借阅,被借阅

        return JsonData.success();
    }

    @RequestMapping(value = "/getCode.json", method = RequestMethod.GET)
    @ResponseBody
    public JsonData getCode(String email) {
        String code = Email.GetCode(email);
        return JsonData.success(code);
    }

    @RequestMapping(value = "/downloadBookExcel.json", method = RequestMethod.GET)
    @ResponseBody
    public JsonData excelTest(HttpServletResponse response, String fileName) throws IOException {
        List<BookExcel> bookExcels = new ArrayList<>();
        List<Book> listBook = bookService.pageListBook(null, null, filtrateBookParam);
        for (Book book : listBook) {
            BookExcel bookExcel = BookExcel.builder()
                    .bookName(book.getBookName())
                    .bookCode(book.getBookCode())
                    .authorName(book.getAuthorName())
                    .price(book.getPrice().toString())
                    .pressName(book.getPressName())
                    .remark(book.getRemark())
                    .bookTypeId(bookTypeService.byIdBookType(book.getBookTypeId()).getTypeName())
                    .build();
            if (book.getBookChcoType() == 0) {
                bookExcel.setBookChcoType("免费");
            } else {
                bookExcel.setBookChcoType("收费");
            }
            bookExcels.add(bookExcel);
        }
        ExcelUtil e = new ExcelUtil();
        HSSFWorkbook workbook = e.downloadBook(bookExcels);
        response.reset();
        response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xlsx");
        OutputStream os = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/vnd.ms-excel;charset=UTF-8"); //将excel写入到输出流中
        workbook.write(os);
        os.flush();
        os.close();
        return null;
    }

    @RequestMapping(value = "/getBookExcel.json", method = RequestMethod.POST)
    @ResponseBody
    public JsonData getExcelTest(HttpServletResponse response
            , @RequestParam(value = "backImageFile", required = false) MultipartFile file) {
        if (file.isEmpty()) {
            return JsonData.fail("file不能为空");
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
                    bookExcel = new BookExcel(); //获得第 j 行
                    HSSFRow row = sheet.getRow(j);
                    row.setRowNum(7);
                    bookExcel.setBookName(row.getCell(0).getStringCellValue());
                    bookExcel.setBookCode(row.getCell(1).getStringCellValue());
                    bookExcel.setAuthorName(row.getCell(2).getStringCellValue());
                    bookExcel.setPrice(row.getCell(3).getStringCellValue());
                    bookExcel.setPressName(row.getCell(4).getStringCellValue());
                    bookExcel.setBookTypeId(row.getCell(5).getStringCellValue());
                    bookExcel.setBookChcoType(row.getCell(6).getStringCellValue());
                    bookExcel.setRemark(row.getCell(7).getStringCellValue());
                    bookExcels.add(bookExcel);
                }
            }
        } catch (IOException e) {
            return JsonData.fail(e.getMessage());
        } catch (Exception e) {
            return JsonData.fail("读取失败");
        }
        //BookExcel 转 Book
        List<Book> books = new ArrayList<>(bookExcels.size() - 1);  //-1 排除标题
        for (int i = 1; i < bookExcels.size(); i++) {
            Book book = new Book();
            book.setBookName(bookExcels.get(i).getBookName());
            book.setBookCode(bookExcels.get(i).getBookCode());
            book.setAuthorName(bookExcels.get(i).getAuthorName());
            book.setPrice(Double.parseDouble(bookExcels.get(i).getPrice()));
            book.setPressName(bookExcels.get(i).getPressName());
            book.setBookChcoType(0);
            if ("收费".equals(bookExcels.get(i).getBookChcoType())) {
                book.setBookChcoType(1);
            }
            book.setRemark(bookExcels.get(i).getRemark());
            books.add(book);
        }
        return JsonData.success(books);
    }

}
