package com.mypg.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mypg.dao.PGOwnerDao;
import com.mypg.model.PGOwner;
import com.mypg.service.PGOwnerService;

@Service("pgOwnerService")
@Transactional(propagation=Propagation.SUPPORTS,rollbackFor=Exception.class)
public class PGOwnerServiceImpl implements PGOwnerService
{

	@Autowired
	PGOwnerDao pgOwnerDao ;
	
	@Override
	public void create(PGOwner pgOwner) 
	{
		pgOwnerDao.create(pgOwner);
	}

	@Override
	public PGOwner read(long pgOwnerId)
	{
		return pgOwnerDao.read(pgOwnerId);
	}

	@Override
	public void update(PGOwner pgOwner) 
	{
		pgOwnerDao.update(pgOwner);
	}

	@Override
	public void delete(long pgOwnerId) 
	{
     //pgOwnerDao.delete(pgOwnerId);		
	}

	@Override
	public List<PGOwner> loadAllPGOwners()
	{
		return pgOwnerDao.loadAllPGOwners();
	}

	@Override
	public PGOwner findPGOwner(String phoneNumber, String password) 
	{
		return pgOwnerDao.findPGOwner(phoneNumber, password);
	}

}
