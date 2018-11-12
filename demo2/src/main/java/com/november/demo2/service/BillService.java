package com.november.demo2.service;

import com.november.demo2.entity.Bill;
import org.springframework.stereotype.Service;

import java.util.List;
@Service(value = "billService")
public interface BillService {
	/**
	 * 增加订单
	 * @param bill
	 * @return
	 */
	public int add(Bill bill);


	/**
	 * 通过条件获取订单列表-模糊查询-billList
	 * @return
	 */
	public List<Bill> getBillList();
	
	/**
	 * 通过billId删除Bill
	 * @param delId
	 * @return
	 */
	public int deleteBillById(String delId);
	
	
	/**
	 * 通过billId获取Bill
	 * @param id
	 * @return
	 */
	public Bill getBillById(String id);
	
	/**
	 * 修改订单信息
	 * @param bill
	 * @return
	 */
	public int modify(Bill bill);

	/**
	 * 根据供应商id返回该供应商下订单的数量
	 * @param id
	 * @return
	 */
	public int getBillCountByProviderId(String id);
	
}
