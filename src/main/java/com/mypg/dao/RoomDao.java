package com.mypg.dao;

import java.util.List;

import com.mypg.model.PG;
import com.mypg.model.Room;

public interface RoomDao 
{

	void create(Room room);
	Room read(long roomId);
	void update(Room room);
	//void delete(long roomId);
	void delete(Room roomBean);
	
	//load all rooms of a PG
	List<Room> loadAllRoomsOfAPG(PG pgBean);
}
