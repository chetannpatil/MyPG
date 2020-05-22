package com.mypg.dao.impl;

import java.util.LinkedList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mypg.dao.PGOwnerDao;
import com.mypg.model.PGOwner;
import com.mypg.util.InvalidCredentialsException;

@Repository("pgOwnerDao")
public class PGOwnerDaoImpl implements PGOwnerDao
{

	@Autowired
	SessionFactory sessionFactory ;
	
	private Session getSession()
	{
		return sessionFactory.getCurrentSession() ;
	}
	
	@Override
	public void create(PGOwner pgOwner)
	{
		getSession().save(pgOwner);
	}

	@Override
	public PGOwner read(long pgOwnerId)
	{
		return getSession().get(PGOwner.class, pgOwnerId);
	}

	@Override
	public void update(PGOwner pgOwner)
	{
		System.out.println("PGOwnerDaoImpl update(PGOwner pgOwner)");
		getSession().update(pgOwner);
	}

	@Override
	public void delete(long pgOwnerId)
	{
		
	}

	@Override
	public List<PGOwner> loadAllPGOwners()
	{
		
		Query query = getSession().createQuery("from PGOwner");
		
		List<PGOwner> pgOwnersList  = query.list();
		
		return pgOwnersList;
	}

	@Override
	public PGOwner findPGOwner(String phoneNumber, String password)
	{
		//		Query q = getSession().createQuery("from User where email = :e");
		//q.setParameter("e", email);
		Query query = getSession().createQuery(" from PGOwner where phoneNumber = :ph and password = :pwd");
		query.setParameter("ph", phoneNumber);
		query.setParameter("pwd", password);
		List<PGOwner> pgOwnerList = (List<PGOwner> ) query.list();
		if(pgOwnerList == null || pgOwnerList.size() == 0)
			throw new InvalidCredentialsException("Phone number & password does not match");
		else
		{
			System.out.println("");
			System.out.println("PGOwnerDaoImpl  findPGOwner else");
			System.out.println("pgwnlist size = "+pgOwnerList.size());
			return pgOwnerList.get(0);
		}
	}

	@Override
	public PGOwner findPGOwnerByPhoneNumber(String phoneNumber)
	{
		System.out.println("PGOwnerDaoImpl  findPGOwnerByPhoneNumber ");
		Query query = getSession().createQuery(" from PGOwner where phoneNumber = :ph");
		query.setParameter("ph", phoneNumber);
		List<PGOwner> pgOwnerList = (List<PGOwner> ) query.list();

		if(pgOwnerList == null || pgOwnerList.size() == 0)
			return null ;
		else
			return pgOwnerList.get(0);
	}

}
