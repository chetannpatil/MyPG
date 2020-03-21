package com.mypg.service;

import java.util.List;

import com.mypg.model.PG;
import com.mypg.model.Room;

public interface RoomService 
{

	void create(Room room);
	Room read(long roomId);
	void update(Room room);
	//void delete(long roomId);
	void delete(Room roomBean);
	List<Room> loadAllRoomsOfAPG(PG pgBean);
}
