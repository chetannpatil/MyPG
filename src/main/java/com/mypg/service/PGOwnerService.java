package com.mypg.service;

import java.util.List;

import com.mypg.model.PGOwner;

public interface PGOwnerService 
{
	void create(PGOwner pgOwner);
	PGOwner read(long pgOwnerId);
	void update(PGOwner pgOwner);
	void delete(long pgOwnerId);
	List<PGOwner> loadAllPGOwners();
	PGOwner findPGOwner(String phoneNumber,String password);
}
