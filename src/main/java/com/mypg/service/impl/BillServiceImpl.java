package com.mypg.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mypg.dao.BillDao;
import com.mypg.model.Bill;
import com.mypg.service.BillService;

@Service("billService")
@Transactional(propagation=Propagation.SUPPORTS,rollbackFor=Exception.class)
public class BillServiceImpl implements BillService
{

	@Autowired
	public BillDao billDao ;

	@Override
	public void create(Bill bill) 
	{
		billDao.create(bill);
	}

	@Override
	public Bill read(long billId)
	{
		return billDao.read(billId);
	}

	@Override
	public void update(Bill bill)
	{
		billDao.update(bill);
	}

	@Override
	public void delete(long billId) 
	{
		billDao.delete(billId);
	}
	
	
}
