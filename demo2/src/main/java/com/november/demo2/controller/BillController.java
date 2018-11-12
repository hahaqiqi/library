package com.november.demo2.controller;

import com.november.demo2.entity.Bill;
import com.november.demo2.service.BillService;
import com.november.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@Controller
@RequestMapping("/bill")
public class BillController {
	@Resource(name = "billService")
	private BillService billService;

	@RequestMapping(value = "/aa")
	@ResponseBody
	public List<Bill> getAllBill(){
        String a= TimeUtil.getTimr();
		return billService.getBillList();
	}


}
