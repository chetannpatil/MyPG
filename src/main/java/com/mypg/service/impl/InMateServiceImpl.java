package com.mypg.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mypg.dao.InMateDao;
import com.mypg.model.InMate;
import com.mypg.model.PG;
import com.mypg.service.InMateService;

@Service("inMateService")
@Transactional(propagation=Propagation.SUPPORTS,rollbackFor=Exception.class)
public class InMateServiceImpl implements InMateService
{

	@Autowired
	private InMateDao inMateDao ;

	@Override
	public void create(InMate inMate)
	{
		inMateDao.create(inMate);
	}

	@Override
	public InMate read(long inMateId)
	{
		return inMateDao.read(inMateId);
	}

	@Override
	public void update(InMate inMate)
	{
		inMateDao.update(inMate);
	}

	/*@Override
	public void delete(long inMateId)
	{
		inMateDao.delete(inMateId);
	}*/

	@Override
	public List<InMate> loadAllInMatesOfAPG(PG pgBean)
	{
		return inMateDao.loadAllInMatesOfAPG(pgBean);
	}

	@Override
	public void delete(InMate inMateBean)
	{
		inMateDao.delete(inMateBean);
	}

	/*@Override
	public List<InMate> loadAllInMatesOfAPG()
	{
		return inMateDao.loadAllInMatesOfAPG();
	}*/
	
	
	
	
}
