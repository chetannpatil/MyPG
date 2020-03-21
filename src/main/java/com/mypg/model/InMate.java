package com.mypg.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import com.mypg.util.CanNotAddABillException;
import com.mypg.util.CanNotAddAComplaintException;
import com.mypg.util.CanNotRemoveTheBillException;
import com.mypg.util.CanNotRemoveTheComplaintException;

@Entity
public class InMate implements Comparable<InMate>
{

	@Id
	@GeneratedValue
	private long inMateId ; 
	
	@NotEmpty(message="FirstName is mandatory")
	//@Pattern(regexp="^[A-Za-z]*$")
	@Pattern(regexp="(^[a-zA-Z]{3,16})",message="First Name should contain only letters & should be in the range 6 to 16")
	private String firstName;
	
	@Pattern(regexp="^[A-Za-z]*$",message="Last Name should contain only letters & should be in the range 6 to 16 and also cant add middle name here")
	private String lastName ;
	
	//@NumberFormat(pattern="##########")
	@NotEmpty(message="Phone number is mandatory")
	@Pattern(regexp="(^$|[0-9]{10})",message="Invalid Phone number")
	private String phoneNumber ;
	
	private String aadhaarNumber ;
	
	/*@OneToOne
	private HomeAddress homeAddress;
	
	@ManyToOne
	private OfficeAddress officeAddress;*/
	
	private Address address;
	
	private String occupation ;
	
	@Email(message="Not at all a email id")
	private String emailId ;
	
	
	@Past(message="You can not add a person who is not yet born")
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private Date dob ;
	
	@NotNull(message="Please add the date when the InMate has joined")
	@Past(message="Cant add InMate who is not yet joined")
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private Date dateOfJoining;
	
	@OneToMany(mappedBy="billOfInMate",fetch=FetchType.EAGER)
	private Set<Bill> myBills ;
	
	@OneToMany(mappedBy="complaintRaisedBy",fetch=FetchType.EAGER)
	private Set<Complaint> myComplaints ;
	
	//@NotNull
	@ManyToOne
	private Room myRoom ;
	
	//his pg
	@ManyToOne
	private PG myPG;
	
	public PG getMyPG()
	{
		return myPG;
		//myComplaints.s
	}

	public void setMyPG(PG myPG) {
		this.myPG = myPG;
	}

	public void setMyBills(Set<Bill> myBills) {
		this.myBills = myBills;
	}

	public void setMyComplaints(Set<Complaint> myComplaints) {
		this.myComplaints = myComplaints;
	}
	//@NotEmpty(message="If you do not wish to set password then how you will login ?")
    private String password;
    
   // @NotEmpty(message="Cant you remember the password you just entered above ? Please enter same password.")
   // private transient String repeatPassword ;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setInMateId(long inMateId) {
		this.inMateId = inMateId;
	}

	public void setDateOfJoining(Date dateOfJoining) {
		this.dateOfJoining = dateOfJoining;
	}

	public void setMyRoom(Room myRoom) {
		this.myRoom = myRoom;
	}

	//only arg constructor taking mandatory fields
	public InMate(String firstName, String phoneNumber, Date dateOfJoining, Room myRoom) 
	{
		super();
		this.firstName = firstName;
		this.phoneNumber = phoneNumber;
		this.dateOfJoining = dateOfJoining;
		this.myRoom = myRoom;
	}

	//arg constructir taking all inst var
	/*public InMate(String firstName, String lastName, String phoneNumber, String aadhaarNumber, Address homeAddress,
			Address officeAddress, String occupation, Date dob, Date dateOfJoining, Room myRoom)
	{
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.aadhaarNumber = aadhaarNumber;
		this.homeAddress = homeAddress;
		this.officeAddress = officeAddress;
		this.occupation = occupation;
		this.dob = dob;
		this.dateOfJoining = dateOfJoining;
		this.myRoom = myRoom;
	}*/

	public String getFirstName() {
		return firstName;
	}

	

	public InMate(long inMateId, String firstName, String lastName, String phoneNumber, String aadhaarNumber,
			Address address, String occupation, String emailId, Date dob, Date dateOfJoining, Set<Bill> myBills,
			Set<Complaint> myComplaints, Room myRoom) {
		super();
		this.inMateId = inMateId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.aadhaarNumber = aadhaarNumber;
		this.address = address;
		this.occupation = occupation;
		this.emailId = emailId;
		this.dob = dob;
		this.dateOfJoining = dateOfJoining;
		this.myBills = myBills;
		this.myComplaints = myComplaints;
		this.myRoom = myRoom;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public InMate(long inMateId, String firstName, String lastName, String phoneNumber, String aadhaarNumber,
			Address address, String occupation, Date dob, Date dateOfJoining, Set<Bill> myBills,
			Set<Complaint> myComplaints, Room myRoom) {
		super();
		this.inMateId = inMateId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.aadhaarNumber = aadhaarNumber;
		this.address = address;
		this.occupation = occupation;
		this.dob = dob;
		this.dateOfJoining = dateOfJoining;
		this.myBills = myBills;
		this.myComplaints = myComplaints;
		this.myRoom = myRoom;
	}

	public InMate() 
	{
		System.out.println("InMate()");
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAadhaarNumber() {
		return aadhaarNumber;
	}

	public void setAadhaarNumber(String aadhaarNumber) {
		this.aadhaarNumber = aadhaarNumber;
	}



	

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}



	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public long getInMateId() {
		return inMateId;
	}

	public Date getDateOfJoining() {
		return dateOfJoining;
	}

	public Set<Bill> getMyBills() {
		return myBills;
	}

	public Set<Complaint> getMyComplaints() {
		return myComplaints;
	}

	public Room getMyRoom() {
		return myRoom;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InMate other = (InMate) obj;
		if (phoneNumber == null) {
			if (other.phoneNumber != null)
				return false;
		} else if (!phoneNumber.equals(other.phoneNumber))
			return false;
		return true;
	}



	@Override
	public String toString() {
		return "InMate [inMateId=" + inMateId + ", firstName=" + firstName + ", lastName=" + lastName + ", phoneNumber="
				+ phoneNumber + ", aadhaarNumber=" + aadhaarNumber + ", address=" + address + ", occupation="
				+ occupation + ", emailId=" + emailId + ", dob=" + dob + ", dateOfJoining=" + dateOfJoining
				+ ", myBills=" + myBills + ", myComplaints=" + myComplaints + ", myRoom=" + myRoom + "]";
	}

	@Override
	public int compareTo(InMate inMate)
	{
	/*	if(this.equals(inMate))
			return 0;
		else if( (this.firstName.equalsIgnoreCase(inMate.firstName) ) == false)
			return (this.firstName.compareToIgnoreCase(inMate.firstName));
		else*/
			//return (this.phoneNumber - inMate.phoneNumber);
	 	/*    if(this.phoneNumber == null)
	 	    	return 1;
	 	    else
	 	    {
	 	    	System.out.println("Inamte compareTo(InMate inMate) this.phno = ");
	 	    	System.out.println(this.phoneNumber);
	 	    	int phNo1 = Integer.parseInt(this.phoneNumber);
				int phNo2 = Integer.parseInt(inMate.phoneNumber);
				
				return (phNo1 - phNo2); 
	 	    }*/
		return (this.phoneNumber.compareToIgnoreCase(inMate.getPhoneNumber()));	
	}
	
	//behaviours
	public void addABill(Bill bill)
	{
		if(bill != null)
		{
			if(this.myBills.add(bill) == false)
				throw new CanNotAddABillException("Could not add a Bill "+bill.getBillId()+" to InMate "+this.firstName);
		}
	}
	
	public void removeABill(Bill bill)
	{
		if(bill != null)
		{
			if(this.myBills.remove(bill) == false)
				throw new CanNotRemoveTheBillException("Could not remove the Bill "+bill.getBillId()+" from InMate "+this.firstName);
		}
	}
	
	public void addAComplaint(Complaint complaint)
	{
		if(complaint != null)
		{
			if(this.myComplaints.add(complaint) == false)
				throw new CanNotAddAComplaintException("Could not add a complaint "+complaint.getComplaintId());
		}
	}
	
	public void removeTheComlaint(Complaint complaint)
	{
		if(complaint != null)
		{
			if(this.myComplaints.remove(complaint) == false)
				throw new CanNotRemoveTheComplaintException("Could not remove the complaint "+complaint.getComplaintId());
		}
	}
	//non behaviours
	//paswrd genrtor
	public static String generatePassword()
	{
		double d =( Math.random()*1000000);
		System.out.println("Inmate  generatePassword() ");
		System.out.println("\n paswrd = "+d);
		long password = (long)d;
		System.out.println("\n paswrd lomng = "+password);
		return password+"";
	}
	
}
