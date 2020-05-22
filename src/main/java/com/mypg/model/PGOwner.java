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
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

@Entity
public class PGOwner
{

	@Id
	@GeneratedValue
	private long pgOwnerId;
	
	//^[a-zA-Z0-9]+$
	//@Pattern(regexp="(^[a-zA-Z]+$)",message="Name should contain only letters")
	@NotEmpty(message="FirstName is mandatory")
	@Pattern(regexp="(^[a-zA-Z]{2,16})",message="First Name should contain only letters & should be in the range 2 to 16")
	private String firstName;
	
	//@Pattern(regexp="(^[a-zA-Z]{6,16})",message="Name should contain only letters")
	private String lastName ;
	
	//@NumberFormat(pattern="##########")
	//@Pattern(regexp="(^$|[0-9]{10})",message="Invalid Phone number")
	@NotEmpty(message="Phone number is mandatory")
	@Pattern(regexp="(^[0-9]{10})",message="Invalid Phone number")
	private String phoneNumber ;
	
	private String aadhaarNumber ;
	
	@Embedded
    private Address address;
	
    @DateTimeFormat(pattern="dd/MM/yyyy")
    @Past(message="You can not become a owner since you are not yet born")
    private Date dob ;
    
    //@NotNull(message="You can not become a admin/PG Owner without a PG ")
    @OneToOne(mappedBy="myOwner",fetch=FetchType.EAGER)
    private PG myPG ;

    //@OneToMany(mappedBy="myOwner",fetch=FetchType.EAGER)
   // private Set<PG> myPGs ;
    
    @NotEmpty(message="If you do not wish to set password then how you will login ?")
    private String password;
    
    @NotEmpty(message="Cant you remember the password you just entered above ? Please enter same password.")
    private transient String repeatPassword ;
    
    //GETTERS & SETTERS
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public PGOwner() {
		super();
	}

	

	public void setPgOwnerId(long pgOwnerId) {
		this.pgOwnerId = pgOwnerId;
	}

	public String getRepeatPassword() {
		return repeatPassword;
	}

	public void setRepeatPassword(String repeatPassword) {
		this.repeatPassword = repeatPassword;
	}

	//constructor
	
/*	public PGOwner(long pgOwnerId, String firstName, String lastName, String phoneNumber, String aadhaarNumber,
			Address address, Date dob, PG myPG, String password, String repeatPassword) {
		super();
		this.pgOwnerId = pgOwnerId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.aadhaarNumber = aadhaarNumber;
		this.address = address;
		this.dob = dob;
		this.myPG = myPG;
		this.password = password;
		this.repeatPassword = repeatPassword;
	}*/

/*	public PGOwner(long pgOwnerId, String firstName, String lastName, String phoneNumber, String aadhaarNumber,
			Address address, Date dob, Set<PG> myPGs, String password, String repeatPassword) {
		super();
		this.pgOwnerId = pgOwnerId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.aadhaarNumber = aadhaarNumber;
		this.address = address;
		this.dob = dob;
		this.myPGs = myPGs;
		this.password = password;
		this.repeatPassword = repeatPassword;
	}*/

/*	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((myPG == null) ? 0 : myPG.hashCode());
		result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
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
		PGOwner other = (PGOwner) obj;
		if (myPG == null) {
			if (other.myPG != null)
				return false;
		} else if (!myPG.equals(other.myPG))
			return false;
		if (phoneNumber == null) {
			if (other.phoneNumber != null)
				return false;
		} else if (!phoneNumber.equals(other.phoneNumber))
			return false;
		return true;
	}*/
	/*@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((myPG == null) ? 0 : myPG.hashCode());
		result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
		
		return (this.phoneNumber).hashCode();
	}*/
	
	/*public PGOwner(long pgOwnerId, String firstName, String lastName, String phoneNumber, String aadhaarNumber,
			Address address, Date dob, PG myPG) {
		super();
		this.pgOwnerId = pgOwnerId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.aadhaarNumber = aadhaarNumber;
		this.address = address;
		this.dob = dob;
		this.myPG = myPG;
	}*/



	@Override
	public int hashCode() 
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
		return result;
	}

	public PGOwner(long pgOwnerId, String firstName, String lastName, String phoneNumber, String aadhaarNumber,
			Address address, Date dob, PG myPG, String password, String repeatPassword) {
		super();
		this.pgOwnerId = pgOwnerId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.aadhaarNumber = aadhaarNumber;
		this.address = address;
		this.dob = dob;
		this.myPG = myPG;
		this.password = password;
		this.repeatPassword = repeatPassword;
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
		PGOwner other = (PGOwner) obj;
		if (phoneNumber == null) {
			if (other.phoneNumber != null)
				return false;
		} else if (!phoneNumber.equals(other.phoneNumber))
			return false;
		return true;
	}

	public String getFirstName() {
		return firstName;
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

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public long getPgOwnerId() {
		return pgOwnerId;
	}


/*	@Override
	public String toString() 
	{
		return "PGOwner [pgOwnerId=" + pgOwnerId + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", phoneNumber=" + phoneNumber + ", aadhaarNumber=" + aadhaarNumber + ", address=" + address
				+ ", dob=" + dob + ", myPG=" + myPG + "]";
	}*/
    
    


/*	public boolean addAPG(PG pg)
	{
		if(this.myPGs.add(pg) == false)
			return false ;
		else
			return true ;
	}
	
	public boolean removeAPG(PG pg)
	{
		if(this.myPGs.remove(pg) == false)
			return false ;
		else
			return true ;
	}*/
	// non behaviours
	public static boolean isPasswordMatchingRepeatPassword(String psw, String repPsw)
	{
		
		if(psw.trim().length() != repPsw.trim().length())
			return false ;
		if(psw.trim().equals(repPsw.trim()) == false)
			return false ;
		else
			return true ;
	}

	public PG getMyPG() {
		return myPG;
	}

	public void setMyPG(PG myPG) {
		this.myPG = myPG;
	}

	@Override
	public String toString() {
		return "PGOwner [pgOwnerId=" + pgOwnerId + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", phoneNumber=" + phoneNumber + ", aadhaarNumber=" + aadhaarNumber + ", address=" + address
				+ ", dob=" + dob + ", password=" + password + "]";
	}

/*	@Override
	public String toString() {
		return "PGOwner [pgOwnerId=" + pgOwnerId + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", phoneNumber=" + phoneNumber + ", aadhaarNumber=" + aadhaarNumber + ", address=" + address
				+ ", dob=" + dob + ", myPG=" + myPG + ", password=" + password + "]";
	}*/

	
}
