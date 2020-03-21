package com.mypg.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mypg.dao.RoomDao;
import com.mypg.model.PG;
import com.mypg.model.Room;

@Repository("roomDao")
public class RoomDaoImpl implements RoomDao
{

	@Autowired
	private SessionFactory sessionFactory;
	
	
	private Session getSession()
	{
		return sessionFactory.getCurrentSession() ;
	}

	@Override
	public void create(Room room)
	{
		getSession().save(room);
	}

	@Override
	public Room read(long roomId)
	{
		System.out.println("\n RoomDaoImpl read(long roomId) with roomid= "+roomId);
		return  getSession().get(Room.class, roomId);
	}

	@Override
	public void update(Room room)
	{
		getSession().update(room);
	}

	@Override
	public void delete(Room roomBean)
	{
		getSession().delete(roomBean);
		
	}

	@Override
	public List<Room> loadAllRoomsOfAPG(PG pgBean)
	{
		//roomBelongsTo
		Query query = getSession().createQuery("from Room where roomBelongsTo =:pg");
		query.setParameter("pg", pgBean);
		List<Room> roomsList = (List<Room>)query.list();
		return roomsList;
	}

	/*@Override
	public void delete(long roomId)
	{
		getSession().delete(roomId);
		//getSession().del
		//getSession().dele
	}*/
	
}
