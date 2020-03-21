package com.mypg.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mypg.dao.PGDao;
import com.mypg.model.PG;

@Repository("pgDao")
public class PGDaoImpl implements PGDao
{

	@Autowired
	private SessionFactory sessionFactory ;
	
	private Session getSession()
	{
		return sessionFactory.getCurrentSession() ;
	}

	@Override
	public void create(PG pg) 
	{
		getSession().save(pg);
	}

	@Override
	public PG read(long pgId)
	{
		return getSession().get(PG.class, pgId);
	}

	@Override
	public void update(PG pg)
	{
		System.out.println("");
		System.out.println("PGDaoImpl pg = ");
		System.out.println(pg);
		getSession().update(pg);
	}

	@Override
	public void delete(long pgId)
	{
		getSession().delete(pgId);
	}

	@Override
	public List<PG> loadAllPGs() 
	{
		
		Query query = getSession().createQuery("from PG");
		List<PG> pgsList = query.list();
		
		return pgsList ;
	}
	
	
}
