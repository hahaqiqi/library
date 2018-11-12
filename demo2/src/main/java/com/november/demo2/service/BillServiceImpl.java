package com.november.demo2.service;

import com.november.demo2.dao.BillMapper;
import com.november.demo2.entity.Bill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("billService")
public class BillServiceImpl implements BillService {
	@Autowired
	public BillMapper billMapper;
	
	public int add(Bill bill) {
		return billMapper.add(bill);
	}

	public List<Bill> getBillList() {
		return billMapper.getBillList();
	}

	public int deleteBillById(String delId) {
		return billMapper.deleteBillById(delId);
	}

	public Bill getBillById(String id) {
		return billMapper.getBillById(id);
	}

	public int modify(Bill bill) {
		return billMapper.modify(bill);
	}

	public int getBillCountByProviderId(String id) {
		return billMapper.getBillCountByProviderId(id);
	}
	

}
