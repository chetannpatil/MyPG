package com.mypg.dao.impl;

import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mypg.dao.InMateDao;
import com.mypg.model.InMate;
import com.mypg.model.PG;

@Repository("inMateDao")
public class InMateDaoImpl implements InMateDao
{

	@Autowired
	private  SessionFactory sessionFactory ;
	
	private Logger LOGGER = Logger.getLogger(InMateDaoImpl.class.getName());
	
	private Session getSession()
	{
		return sessionFactory.getCurrentSession();
	}
	
	@Override
	public void create(InMate inMate)
	{
		getSession().save(inMate);
	}

	@Override
	public InMate read(long inMateId)
	{
		return getSession().get(InMate.class, inMateId);
	}

	@Override
	public void update(InMate inMate)
	{
		getSession().update(inMate);
	}

	/*@Override
	public void delete(long inMateId)
	{
		getSession().delete(inMateId);
	}*/

	@Override
	public List<InMate> loadAllInMatesOfAPG(PG pgBean)
	{
		System.out.println("InmateDao loadAllInMatesOfAPG(PG pgBean)");
        Query query = getSession().createQuery("from InMate where myPG =:pg");
        System.out.println("\nInmateDao loadAllInMatesOfAPG(PG pgBean) after creatQuery)(");
		query.setParameter("pg", pgBean);
		 System.out.println("\nInmateDao loadAllInMatesOfAPG(PG pgBean) setPAramter()");
		return (List<InMate>)query.list();
	}

	@Override
	public void delete(InMate inMateBean)
	{
		LOGGER.info("InMateDai impl - delete()");
		getSession().delete(inMateBean);
	}

	/*@Override
	public List<InMate> loadAllInMatesOfAPG()
	{
		Query query = getSession().createQuery("from InMate where");
		
		return (List<InMate>)query.list();
	}*/

	
}
