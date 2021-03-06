package com.mypg.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import com.mypg.util.CanNotAddAInMateException;
import com.mypg.util.CanNotRemoveAInMateException;
import com.mypg.util.DuplicateInMateException;
import com.mypg.util.InMatesOverFlowInARoomException;

@Entity
public class Room implements Comparable<Room>
{

	@Id
	@GeneratedValue
	private long roomId;
	
	@OneToMany(mappedBy="myRoom",fetch=FetchType.EAGER)
	private Set<InMate> roomMates ;
	
	//@Min(1)
	//private int numberOfBeds ;
	//@Pattern(regexp="(^[0-9]{10})",message="Number of beds should be numeric,do not tell that in words pls")
	@Min(1)
	@Max(10)
	@NotNull(message="There must be atleast 1 bed in the room pls")
	@NumberFormat(style=Style.NUMBER)
	private Integer  numberOfBeds ;
	//@Pattern(regexp="(^[a-zA-Z]{2,16})",message="First Name should contain only letters & should be in the range 2 to 16")
	//@Pattern(regexp="(^[0-9]{10})",message="Invalid Phone number")
	//@Min(1)
	@NotEmpty(message="Room number is required")
	@Pattern(regexp="([0-9]{3,4})",message="Room number shouldbe in the range 3digits to 4digits, ex: 402,where 4 = 4th floor, 2 = second room")
	private String roomNumber;
	//final private String roomNumber;
	
	@ManyToOne
	private PG roomBelongsTo ;

	@NotNull(message="you must reveal room rent per head for this room")
	@NumberFormat(style=Style.CURRENCY)
	private Double costPerBed ;
	
	//constructor
	public Room()
	{
		super();
		//this.roomNumber = generateRoomNumber()+"";
		
	}

	public Room(long roomId, Set<InMate> roomMates, Integer numberOfBeds, String roomNumber, PG roomBelongsTo,
			double costPerBed) {
		super();
		this.roomId = roomId;
		this.roomMates = roomMates;
		this.numberOfBeds = numberOfBeds;
		this.roomNumber = roomNumber;
		this.roomBelongsTo = roomBelongsTo;
		this.costPerBed = costPerBed;
	}



	public Double getCostPerBed() {
		return costPerBed;
	}

	public void setCostPerBed(Double costPerBed) {
		this.costPerBed = costPerBed;
	}

	public void setNumberOfBeds(Integer numberOfBeds) {
		this.numberOfBeds = numberOfBeds;
	}

	public Room(int numberOfBeds, String roomNumber) 
	{
		super();
		System.out.println("in Room(int numberOfBeds, int roomNumber) ");
		this.numberOfBeds = numberOfBeds;
		this.roomNumber = roomNumber;
	}

	/*public int getNumberOfBeds() 
	{
		return numberOfBeds;
	}*/



	public Integer getNumberOfBeds() {
		return numberOfBeds;
	}

	public void setRoomMates(Set<InMate> roomMates) {
		this.roomMates = roomMates;
	}

	public Set<InMate> getRoomMates() 
	{
		return roomMates;
	}

	public String getRoomNumber() {
		return roomNumber;
	}





	/*public Room(long roomId, Set<InMate> roomMates, int numberOfBeds, String roomNumber, PG roomBelongsTo) {
		super();
		this.roomId = roomId;
		this.roomMates = roomMates;
		this.numberOfBeds = numberOfBeds;
		this.roomNumber = roomNumber;
		this.roomBelongsTo = roomBelongsTo;
	}*/

	public Room(long roomId, Set<InMate> roomMates, Integer numberOfBeds, String roomNumber, PG roomBelongsTo) {
		super();
		this.roomId = roomId;
		this.roomMates = roomMates;
		this.numberOfBeds = numberOfBeds;
		this.roomNumber = roomNumber;
		this.roomBelongsTo = roomBelongsTo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((roomNumber == null) ? 0 : roomNumber.hashCode());
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
		Room other = (Room) obj;
		if (roomNumber == null) {
			if (other.roomNumber != null)
				return false;
		} else if (!roomNumber.equals(other.roomNumber))
			return false;
		return true;
	}

/*	@Override
	public String toString() {
		return "Room [roomId=" + roomId + ", roomMates=" + roomMates + ", numberOfBeds=" + numberOfBeds
				+ ", roomNumber=" + roomNumber + ", roomBelongsTo=" + roomBelongsTo + ", costPerBed=" + costPerBed
				+ "]";
	}*/

	@Override
	public int compareTo(Room room)
	{
		int rmNo1 = Integer.parseInt(this.roomNumber);
		int rmNo2 = Integer.parseInt(room.roomNumber);
		return (rmNo1 - rmNo2 );
	}
	
	//behaviours
	public void addBed(int noOfbeds)
	{
		validateNoOfBeds(noOfbeds);
		this.numberOfBeds = this.numberOfBeds + noOfbeds ;
	}
	public void removeBed(int noOfbeds)
	{
		validateNoOfBeds(noOfbeds);
		this.numberOfBeds = this.numberOfBeds - noOfbeds ;
	}
/*	public void addInMate(InMate inMate)
	{
		if(inMate != null)
		{
			//check vacancy
			if(this.numberOfBeds > this.roomMates.size())
			{
				if(this.roomMates.add(inMate) == false)
					throw new CanNotAddAInMateException("Could not add InMate "+inMate.getFirstName()+" to the room");
			}
			else
			{
				throw new InMatesOverFlowInARoomException("There is no vacancy in the room = "+this.roomNumber);
			}
		}
	}*/
	public boolean addInMate(InMate inMate)
	{
		if(inMate != null)
		{
			//check vacancy
			if(this.numberOfBeds > this.roomMates.size())
			{
				if(this.roomMates.add(inMate) == false)
				{
					return false;
					//throw new DuplicateInMateException("Could not add InMate "+inMate.getFirstName()+" to the room");
				}
				else
					return true ;
			}
			else
			{
				throw new InMatesOverFlowInARoomException("There is no vacancy in the room = "+this.roomNumber);
			}
		}
		else
			return false;
	}
	/*public void removeInMate(InMate inMate)
	{
		if(inMate != null)
		{
			if(this.roomMates.remove(inMate) == false)
				throw new CanNotRemoveAInMateException("Could not remove InMate "+inMate.getFirstName()+" from the room");
		}
	}*/
	
	//validations
	private static void validateNoOfBeds(int noOfbeds)
	{
		if(noOfbeds <= 0)
			throw new IllegalArgumentException("You can not add/remove "+noOfbeds+" into/from a room");
	}
			
	public long getRoomId() {
		return roomId;
	}

	public void setRoomId(long roomId) {
		this.roomId = roomId;
	}

	public PG getRoomBelongsTo() {
		return roomBelongsTo;
	}

	public void setRoomBelongsTo(PG roomBelongsTo) {
		this.roomBelongsTo = roomBelongsTo;
	}

	public void setNumberOfBeds(int numberOfBeds) {
		this.numberOfBeds = numberOfBeds;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}

	//non behavioral
	private static int generateRoomNumber()
	{
		int randomNumber = 0 ;
		randomNumber =( int)(Math.random()*10);
		
		return randomNumber ;
	}
	
	public static boolean isValidNumberOfBeds(int numberOfBeds)
	{
		
		if(numberOfBeds <= 0)
			return false;
		else
			return true ;
	}
	
	
}
