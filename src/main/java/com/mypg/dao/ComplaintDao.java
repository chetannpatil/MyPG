package com.mypg.dao;

import java.util.List;

import com.mypg.model.Complaint;
import com.mypg.model.PG;

public interface ComplaintDao
{

	void create(Complaint complaint);
	Complaint read(long complaintId);
	void update(Complaint complaint);
	void delete(long complaintId);
	List<Complaint> loadAllComplaintsOfAPG(PG pgBean);
}
