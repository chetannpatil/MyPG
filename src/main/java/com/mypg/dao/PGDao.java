package com.mypg.dao;

import java.util.List;

import com.mypg.model.PG;

public interface PGDao
{

	void create(PG pg);
	PG read(long pgId);
	void update(PG pg);
	void delete(long pgId);
	List<PG> loadAllPGs();
}
