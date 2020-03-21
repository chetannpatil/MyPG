package com.mypg.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mypg.dao.RoomDao;
import com.mypg.model.PG;
import com.mypg.model.Room;
import com.mypg.service.RoomService;

@Service("roomService")
@Transactional(propagation=Propagation.SUPPORTS,rollbackFor=Exception.class)
public class RoomServiceImpl implements RoomService
{

	@Autowired
	private RoomDao roomDao;

	@Override
	public void create(Room room) 
	{
		roomDao.create(room);
	}

	@Override
	public Room read(long roomId)
	{
		return roomDao.read(roomId);
	}

	@Override
	public void update(Room room)
	{
		roomDao.update(room);
	}

	@Override
	public void delete(Room roomBean)
	{
		roomDao.delete(roomBean);
	}

	@Override
	public List<Room> loadAllRoomsOfAPG(PG pgBean)
	{
		System.out.println("roomServiceImpl loadallrooms()");
		return roomDao.loadAllRoomsOfAPG(pgBean);
	}

/*	@Override
	public void delete(long roomId)
	{
		roomDao.delete(roomBean);
	}*/

	/*@Override
	public void delete(long roomId) 
	{
		roomDao.delete(roomId);
	}*/
	
}
