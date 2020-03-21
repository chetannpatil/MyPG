package com.mypg.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mypg.dao.ComplaintDao;
import com.mypg.model.Complaint;
import com.mypg.model.PG;
import com.mypg.service.ComplaintService;

@Service("complaintService")
@Transactional(propagation=Propagation.SUPPORTS,rollbackFor=Exception.class)
public class ComplaintServiceImpl implements ComplaintService
{

	@Autowired
	private ComplaintDao complaintDao;

	@Override
	public void create(Complaint complaint)
	{
		complaintDao.create(complaint);
	}

	@Override
	public Complaint read(long complaintId)
	{
		return  complaintDao.read(complaintId);
	}

	@Override
	public void update(Complaint complaint) 
	{
		complaintDao.update(complaint);
	}

	@Override
	public void delete(long complaintId)
	{
		complaintDao.delete(complaintId);
	}

	@Override
	public List<Complaint> loadAllComplaintsOfAPG(PG pgBean)
	{
		return complaintDao.loadAllComplaintsOfAPG(pgBean);
	}
	
	
}
