package com.mypg.service;

import java.util.List;

import com.mypg.model.PG;

public interface PGService
{


	void create(PG pg);
	PG read(long pgId);
	void update(PG pg);
	void delete(long pgId);
	List<PG> loadAllPGs();
}
