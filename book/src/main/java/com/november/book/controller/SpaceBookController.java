package com.november.book.controller;

import com.november.book.model.Book;
import com.november.book.param.BookParam;
import com.november.book.service.SpaceBookService;
import com.november.common.JsonData;
import com.november.exception.ParamException;
import com.november.model.BookExcel;
import com.november.util.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping(value = "/spacebook")
public class SpaceBookController {

    @Resource(name = "spacebookService")
    private SpaceBookService spacebookservice;

    @ResponseBody
    @RequestMapping("/selectBook.json")                         //"1,2,3,4,5"
    public JsonData selectBook(@RequestParam(value = "bookspaceid") String bookspaceid
            , HttpServletRequest request) {
        log.info("获取书籍信息", bookspaceid);
        //layui 获取网页的分页 创建变量page，limit
        int page = Integer.parseInt(request.getParameter("page"));//第几页
        int limit = Integer.parseInt(request.getParameter("limit"));//每页显示条数
        //创建Integer类型的list集合
        List<Integer> list = new ArrayList<>();
        //创建String类型的params[]数组用split进行分割。。(page - 1) * limit
        String params[] = bookspaceid.split(",");//参数jie()
        //用循环往Integer类型的list集合里添加params[]数组Integer.valueOf
        for (int i = 0; i < params.length; i++) {
            list.add(Integer.valueOf(params[i]));
        }
        //创建变量count接收Booklimit查询的所有数据总数
        int count = spacebookservice.Booklimit(list);
        //创建对象BookParam对变量赋值
        BookParam bookParam = new BookParam();
        //Wherelist
        bookParam.setWhereList(list);
        //Limit
        bookParam.setLimit(limit);
        //Page
        bookParam.setPage((page - 1) * limit);
        //创建对象集合接收selectSpaceBook方法
        List<Book> listBook = spacebookservice.selectSpaceBook(bookParam);
        return JsonData.pageSuccess(listBook, count, limit);


    }

    /*
        移动空间
     */
    @ResponseBody
    @RequestMapping("/spaceBookremove.json")
    public JsonData remove(@RequestParam(value = "bookId") Integer bookId) {
        //调用BookSpaceremove接口
        spacebookservice.BookSpaceremove(null, bookId);
        return JsonData.success();
    }
    /*
        空间书籍添加
     */
    @ResponseBody
    @RequestMapping("/spaceBookAdd.json")
    public JsonData bookSpaceAdd(
            @RequestParam(value = "bookpid") Integer bookpid,
            @RequestParam(value = "bookCode") String bookCode,
            @RequestParam(value = "status") Integer status
    ) {
        //调用BookSpaceAdd接口
        spacebookservice.BookSpaceAdd(bookpid, bookCode, status);
        return JsonData.success();
    }

    @ResponseBody
    @RequestMapping("/spaceBookAddList.json")
    public JsonData bookSpaceAddList(
            @RequestParam(value = "status") Integer status,
            @RequestParam(value = "bookspaceid") Integer bookpid,
            @RequestParam(value = "backImageFile", required = false) MultipartFile file) {
        //创建BookExcel类型的List集合接收ExcelUtil的loadBookExcel
        List<BookExcel> bookExcels = ExcelUtil.loadBookExcel(file);
        //BookExcel转换成list,因为excel第一行是标题,所以要-1
        List<Book> spaceBooks = new ArrayList<>(bookExcels.size() - 1);
        //循环
        for (int i = 1; i < bookExcels.size(); i++) {
            //使用try.catch来检查Excel的数据异常情况
            try {
                //创建书籍对象
                Book book = new Book();
                book.setBookCode(bookExcels.get(i).getBookCode());
                book.setBookSpaceId(bookpid);
                spaceBooks.add(book);
             //NumberFormatException异常
            } catch (NumberFormatException e) {
                throw new ParamException("错误，请检查Excel的第" + (i + 1) + "行数据");
             //  Exception 异常
            } catch (Exception e) {
                throw new ParamException("加载数据失败");
            }
        }
        spacebookservice.BookSpaceAddList(status, bookpid, spaceBooks);
        return JsonData.success();
    }
}
