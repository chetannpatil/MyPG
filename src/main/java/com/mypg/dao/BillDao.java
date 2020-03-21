package com.mypg.dao;

import com.mypg.model.Bill;

public interface BillDao
{
	void create(Bill bill);
	Bill read(long billId);
	void update(Bill bill);
	void delete(long billId);
}
