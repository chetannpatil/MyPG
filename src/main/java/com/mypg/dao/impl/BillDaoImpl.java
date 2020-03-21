package com.mypg.dao.impl;

import java.util.logging.Logger;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mypg.dao.BillDao;
import com.mypg.model.Bill;

@Repository("billDao")
public class BillDaoImpl implements BillDao
{

	@Autowired
	SessionFactory sessionFactory;
	
	private static final Logger LOGGER = Logger.getLogger(BillDaoImpl.class.getName());
	
	private Session getSession()
	{
		System.out.println("BillDaoImpl getSession()");
		LOGGER.info("BillDaoImpl getSession()");
		return sessionFactory.getCurrentSession();
	}

	@Override
	public void create(Bill bill)
	{
		System.out.println("BillDaoImpl create(Bill bill)");
		LOGGER.info("BillDaoImpl create(Bill bill)");
		getSession().save(bill);
		
	}

	@Override
	public Bill read(long billId)
	{
		LOGGER.info("BillDaoImpl read(long billId");
		return getSession().get(Bill.class, billId);
		
	}

	@Override
	public void update(Bill bill) 
	{
		LOGGER.info("BillDaoImpl update(Bill bill");
		getSession().update(bill);
	}

	@Override
	public void delete(long billId) 
	{
		LOGGER.info("BillDaoImpl delete(long billId)");
		getSession().delete(billId);
	}


}
