package com.mypg.service;

import com.mypg.model.Bill;

public interface BillService 
{
	void create(Bill bill);
	Bill read(long billId);
	void update(Bill bill);
	void delete(long billId);
}
