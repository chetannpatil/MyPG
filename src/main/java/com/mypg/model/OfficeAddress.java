package com.mypg.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

//@Entity
public class OfficeAddress extends Address
{

	//@Id
	//@GeneratedValue
	private long officeAddressId;
	
	//@OneToMany(mappedBy="officeAddress")
	private Set<InMate> employees;

	public OfficeAddress(long addresId, String houseNumber, String street, String disrtict, String state,
			String country, String pin, long officeAddressId, Set<InMate> employees) {
		super(addresId, houseNumber, street, disrtict, state, country, pin);
		this.officeAddressId = officeAddressId;
		this.employees = employees;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((employees == null) ? 0 : employees.hashCode());
		result = prime * result + (int) (officeAddressId ^ (officeAddressId >>> 32));
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
		OfficeAddress other = (OfficeAddress) obj;
		if (employees == null) {
			if (other.employees != null)
				return false;
		} else if (!employees.equals(other.employees))
			return false;
		if (officeAddressId != other.officeAddressId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OfficeAddress [officeAddressId=" + officeAddressId + ", employees=" + employees + "]";
	}
	
	
}
