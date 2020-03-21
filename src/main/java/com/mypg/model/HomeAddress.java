package com.mypg.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

//@Entity
public class HomeAddress extends Address
{

	//@Id
	//@GeneratedValue
	private long homeAddressId;
	
	//@OneToOne
	private InMate houseOwner;

	public HomeAddress(long addresId, String houseNumber, String street, String disrtict, String state, String country,
			String pin, long homeAddressId, InMate houseOwner) {
		super(addresId, houseNumber, street, disrtict, state, country, pin);
		this.homeAddressId = homeAddressId;
		this.houseOwner = houseOwner;
	}

	public InMate getHouseOwner() {
		return houseOwner;
	}

	public void setHouseOwner(InMate houseOwner) {
		this.houseOwner = houseOwner;
	}

	public long getHomeAddressId() {
		return homeAddressId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((houseOwner == null) ? 0 : houseOwner.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		HomeAddress other = (HomeAddress) obj;
		if (houseOwner == null) {
			if (other.houseOwner != null)
				return false;
		} else if (!houseOwner.equals(other.houseOwner))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "HomeAddress [homeAddressId=" + homeAddressId + ", houseOwner=" + houseOwner + "]";
	}
	
	
}
