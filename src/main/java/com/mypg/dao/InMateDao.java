package com.mypg.dao;

import java.util.List;

import com.mypg.model.InMate;
import com.mypg.model.PG;

public interface InMateDao
{

	void create(InMate inMate);
	InMate read(long inMateId);
	void update(InMate inMate);
	//void delete(long inMateId);
	List<InMate> loadAllInMatesOfAPG(PG pgBean);
	void delete(InMate inMateBean);
}
