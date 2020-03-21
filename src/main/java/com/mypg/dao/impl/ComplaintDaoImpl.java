package com.mypg.dao.impl;

import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mypg.dao.ComplaintDao;
import com.mypg.model.Complaint;
import com.mypg.model.PG;

@Repository("complaintDao")
public class ComplaintDaoImpl implements ComplaintDao
{

	@Autowired
	SessionFactory sessionFactory ;
	private static final Logger LOGGER = Logger.getLogger(ComplaintDaoImpl.class.getName());
	
	private Session getSession()
	{
		LOGGER.info("ComplaintDaoImpl  getSession()");
		return sessionFactory.getCurrentSession();
	}
	
	@Override
	public void create(Complaint complaint)
	{
		LOGGER.info("ComplaintDaoImpl  create(Complaint complaint)");
		getSession().save(complaint);
	}

	@Override
	public Complaint read(long complaintId)
	{
		LOGGER.info("ComplaintDaoImpl  read(long complaintId)");
		return getSession().get(Complaint.class, complaintId);
	}

	@Override
	public void update(Complaint complaint) 
	{
		LOGGER.info("ComplaintDaoImpl  update(Complaint complaint) ");
		getSession().update(complaint);
	}

	@Override
	public void delete(long complaintId)
	{
		LOGGER.info("ComplaintDaoImpl  delete(long complaintId)");
		getSession().delete(complaintId);
	}

	@Override
	public List<Complaint> loadAllComplaintsOfAPG(PG pgBean)
	{
		//=:pg");
		System.out.println("COmplainDAoImpl laodALlCOmlntsofPG()");
		Query query = getSession().createQuery("from Complaint where complaintBelongsTo=:pg");
		query.setParameter("pg", pgBean);
		List<Complaint> complaintsList = (List<Complaint>)query.list();
		return complaintsList ;
	}

}
