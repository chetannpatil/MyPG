package com.mypg.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mypg.dao.PGDao;
import com.mypg.model.PG;
import com.mypg.service.PGService;

@Service("pgService")
@Transactional(propagation=Propagation.SUPPORTS,rollbackFor=Exception.class)
public class PGServiceImpl implements PGService
{

	@Autowired
	private PGDao pgDao ;

	@Override
	public void create(PG pg)
	{
		pgDao.create(pg);
	}

	@Override
	public PG read(long pgId)
	{
		return pgDao.read(pgId);
	}

	@Override
	public void update(PG pg) 
	{
		pgDao.update(pg);
	}

	@Override
	public void delete(long pgId)
	{
		pgDao.delete(pgId);
	}

	@Override
	public List<PG> loadAllPGs()
	{
		return pgDao.loadAllPGs();
	}
	
	
}
