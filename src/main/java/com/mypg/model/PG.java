package com.mypg.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import com.mypg.util.CanNotAddAComplaintException;
import com.mypg.util.CanNotRemoveTheComplaintException;
import com.mypg.util.CanNotRemoveTheRoomException;
import com.mypg.util.DuplicateRoomException;

@Entity
public final class PG implements Comparable<PG>
{

	@Id
	@GeneratedValue
	private long pgId;
	
	@NotEmpty(message="why cant you name your PG?")
	private String name ;
	
	//@NotNull(message="You have to reveal where you have constructed your PG")
	//@NotBlank(message="You have to reveal where you have constructed your PG")
	//@Null(message="You have to reveal where you have constructed your PG")
	//@NotEmpty(message="You have to reveal where you have constructed your PG")
	//all @NotNull @NotBlank @Null @NotEmpty dosenot work
	@Embedded
	private Address address ;
	
	private int totalRooms;
	
	@OneToMany(mappedBy="roomBelongsTo",fetch=FetchType.EAGER)
	private Set<Room> rooms ;
	
	@OneToMany(mappedBy="complaintBelongsTo",fetch=FetchType.EAGER)
	private Set<Complaint> complaintBox;
	
	private Date pgStartedDate;
	
	@OneToMany(fetch=FetchType.EAGER)
	private Set<Bill> bills;
	
	public void setPgId(long pgId) {
		this.pgId = pgId;
	}

	public Set<Bill> getBills() {
		return bills;
	}

	public void setBills(Set<Bill> bills) {
		this.bills = bills;
	}

	//@NotNull(message="You can not create a PG without a Owner/care taker of it")
	//@OneToOne(mappedBy="myPG")
	@OneToOne
	private PGOwner myOwner ;
	
	
	public PG(long pgId, String name, Address address, int totalRooms, Set<Room> rooms, Set<Complaint> complaintBox,
			Date pgStartedDate, PGOwner myOwner) {
		super();
		this.pgId = pgId;
		this.name = name;
		this.address = address;
		this.totalRooms = totalRooms;
		this.rooms = rooms;
		this.complaintBox = complaintBox;
		this.pgStartedDate = pgStartedDate;
		this.myOwner = myOwner;
	}

	public void setMyOwner(PGOwner myOwner) {
		this.myOwner = myOwner;
	}

	public PGOwner getMyOwner() {
		return myOwner;
	}

	public void setTotalRooms(int totalRooms) {
		this.totalRooms = totalRooms;
	}

	public PG(long pgId, String name, Address address, int totalRooms, Set<Room> rooms, Set<Complaint> complaintBox,
			Date pgStartedDate) {
		super();
		this.pgId = pgId;
		this.name = name;
		this.address = address;
		this.totalRooms = totalRooms;
		this.rooms = rooms;
		this.complaintBox = complaintBox;
		this.pgStartedDate = pgStartedDate;
	}

	public Date getPgStartedDate() {
		return pgStartedDate;
	}

	public void setPgStartedDate(Date pgStartedDate) {
		this.pgStartedDate = pgStartedDate;
	}


	@Override
	public String toString() {
		return "PG [pgId=" + pgId + ", name=" + name + ", address=" + address + ", totalRooms=" + totalRooms
				+ ", rooms=" + rooms + ", complaintBox=" + complaintBox + ", pgStartedDate=" + pgStartedDate
				+ ", myOwner=" + myOwner + "]";
	}

	public void setRooms(Set<Room> rooms) {
		this.rooms = rooms;
	}

	public void setComplaintBox(Set<Complaint> complaintBox) {
		this.complaintBox = complaintBox;
	}

	//behaviours
	/*public void addRoom(Room room)
	{
		if(room != null)
		{
			if(this.rooms.add(room) == false)
			{
				throw new DuplicateRoomException("Could not add the room "+room.getRoomNumber()+ " to PG");
			}
			else
			{
				System.out.println("");
				System.out.println("PG addRoom() room = ");
				System.out.println(room);
				this.totalRooms = this.totalRooms+1;
			}
		}
	}*/
	public boolean addRoom(Room room)
	{
		if(room != null)
		{
			if(this.rooms != null)
			{
				System.out.println("PG addrom ()rooms set is  not null");
				if(this.rooms.add(room) == false)
				{
					System.out.println("PG addrom () Could not add the room "+room.getRoomNumber()+ " to PG");
					//throw new DuplicateRoomException("Could not add the room "+room.getRoomNumber()+ " to PG");
					return false ;
				}
				else
				{
					System.out.println("");
					System.out.println("PG addRoom() room = ");
					System.out.println(room);
					this.totalRooms = this.totalRooms+1;
					return true ;
				}
			}
			else
			{
				System.out.println("PG addrom ()rooms set is null");
				return false ;
			}
			
		}
		else
			return false;
	}
	public boolean removeRoom(Room room)
	{
		if(room != null)
		{
			if(this.rooms.remove(room) == false)
			{
				//throw new CanNotRemoveTheRoomException("Could not remove the room "+room.getRoomNumber()+ " from PG");
				return false ;
			}
			else
			{
				System.out.println("");
				System.out.println("PG removeRoom() room = ");
				System.out.println(room);
				this.totalRooms = this.totalRooms - 1;
				return true;
			}
		}
		else
			return false;
	}
	/*public void removeRoom(Room room)
	{
		if(room != null)
		{
			if(this.rooms.remove(room) == false)
			{
				throw new CanNotRemoveTheRoomException("Could not remove the room "+room.getRoomNumber()+ " from PG");
			}
			else
			{
				System.out.println("");
				System.out.println("PG removeRoom() room = ");
				System.out.println(room);
				this.totalRooms = this.totalRooms - 1;
			}
		}
	}*/
	
	public void addAComplaint(Complaint complaint)
	{
		if(complaint != null)
		{
			if(this.complaintBox.add(complaint) == false)
				throw new CanNotAddAComplaintException("Could not add a complaint "+complaint.getComplaintId());
		}
	}
	
	public void removeTheComlaint(Complaint complaint)
	{
		if(complaint != null)
		{
			if(this.complaintBox.remove(complaint) == false)
				throw new CanNotRemoveTheComplaintException("Could not remove the complaint "+complaint.getComplaintId());
		}
	}

	public PG(long pgId, String name, Address address, int totalRooms, Set<Room> rooms, Set<Complaint> complaintBox) {
		super();
		this.pgId = pgId;
		this.name = name;
		this.address = address;
		this.totalRooms = totalRooms;
		this.rooms = rooms;
		this.complaintBox = complaintBox;
	}

	public PG() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public long getPgId() {
		return pgId;
	}

	public int getTotalRooms() 
	{
		if(this.rooms != null)
		{
			return rooms.size();
		}
		else
			return this.totalRooms ;
	}

	public Set<Room> getRooms() {
		return rooms;
	}

	public Set<Complaint> getComplaintBox() {
		return complaintBox;
	}

	@Override
	public int compareTo(PG pg)
	{
		return ( (this.myOwner.toString()+this.address).compareToIgnoreCase(pg.myOwner.toString()+pg.address) );
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PG other = (PG) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		return true;
	}

/*	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((myOwner == null) ? 0 : myOwner.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) 
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PG other = (PG) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (myOwner == null) {
			if (other.myOwner != null)
				return false;
		} else if (!myOwner.equals(other.myOwner))
			return false;
		return true;
	}*/
	
	//non behavoural
	
	
}
