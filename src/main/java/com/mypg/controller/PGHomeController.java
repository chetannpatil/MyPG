package com.mypg.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.mypg.model.Address;
import com.mypg.model.Bill;
import com.mypg.model.Complaint;
import com.mypg.model.InMate;
import com.mypg.model.PG;
import com.mypg.model.PGOwner;
import com.mypg.model.Room;
import com.mypg.service.BillService;
import com.mypg.service.ComplaintService;
import com.mypg.service.InMateService;
import com.mypg.service.PGOwnerService;
import com.mypg.service.PGService;
import com.mypg.service.RoomService;
import com.mypg.util.CanNotRemoveTheRoomException;
import com.mypg.util.DuplicateInMateException;
import com.mypg.util.DuplicateOwnerException;
import com.mypg.util.DuplicatePGException;
import com.mypg.util.DuplicateRoomException;
import com.mypg.util.InMatesOverFlowInARoomException;
import com.mypg.util.InvalidCredentialsException;
import com.mypg.util.PGAddressCanNotBeEmptyException;
import com.mypg.util.PasswordMissMatchException;
import com.mypg.util.RoomDoesNotExistExcepton;
import com.mypg.util.Util;

@Controller("/")
public class PGHomeController 
{

	@Autowired
	BillService billService;
	
	@Autowired
	ComplaintService complaintService;
	
	@Autowired
	InMateService inMateService;
	
	@Autowired
	PGService pgService;
	
	@Autowired
	RoomService roomService;
	
	@Autowired
	PGOwnerService pgOwnerService;
	
	private static final Logger LOGGER = Logger.getLogger(PGHomeController.class.getName());
	
	private static boolean isItDuplicatePG(List<PG> pgList,PG pgBean)
	{
		//List<PG> pgList = pgService.loadAllPGs();
		Set<PG> pgSet = new HashSet<PG>(pgList);
		if(pgSet.add(pgBean) == false)
		{
			return true ;
		}
		else
			return false ;
	
	}
	@RequestMapping("/")
	public String welcome()
	{
		LOGGER.info("\nPGHC---This is info logger ");
		//LOGGER.
		return "Welcome";
	}
	
	@RequestMapping("/contactMe")
	public String contact()
	{
		return "Contact";
	}
	
	//pg woner registertion
	@RequestMapping("/openPGOwnerRegistrationView")
	public String openPGOwnerRegistrationView(Model m)
	{
		PGOwner pgOwnerBean = new PGOwner();
		m.addAttribute("pgOwnerBean", pgOwnerBean);
		return "RegisterPGOwner";
	}
	
	//registerPGOwner
	@RequestMapping(value="/registerPGOwner",method=RequestMethod.POST)
	public String registerPGOwner(@ModelAttribute("pgOwnerBean")@Valid PGOwner pgOwnerBean,BindingResult br,Model m,HttpSession hs)
	{
		if(br.hasErrors())
		{
			return "RegisterPGOwner";
		}
		else
		{
			try
			{
				String pwd = pgOwnerBean.getPassword();
				String repeatPwd = pgOwnerBean.getRepeatPassword();
				//removeExtra Space from each has a
			//	pgOwnerBean.setFirstName(RemoveExtraSpacesFromALine.removeExtraSpace(pgOwnerBean.getFirstName()));
				if(PGOwner.isPasswordMatchingRepeatPassword(pwd, repeatPwd) == false)
				{
				String errorStr = "Do you think password = \""+pwd+"\" "+" and repeat password = \" "+repeatPwd+"\" are same ?";
					throw new PasswordMissMatchException(errorStr);
				}
				// check for duplicate owner here
				PGOwner foundPGOwnerByPhoneNumber = pgOwnerService.findPGOwnerByPhoneNumber(pgOwnerBean.getPhoneNumber());
				
				if(foundPGOwnerByPhoneNumber != null)
				{
					throw new DuplicateOwnerException("A account allready exist  with same phone number = "+pgOwnerBean.getPhoneNumber());

				}
				
				/*
				 * List<PGOwner> pgOwnersList = pgOwnerService.loadAllPGOwners();
				 * if(pgOwnersList != null) { for(PGOwner pgOwner:pgOwnersList) {
				 * if(pgOwner.getPhoneNumber().equals(pgOwnerBean.getPhoneNumber())) { throw new
				 * DuplicateOwnerException("A account allready exist  with same phone number = "
				 * +pgOwnerBean.getPhoneNumber()); } } }
				 */	
				
		
				//do not create a pgOwnerbean now
				////pgOwnerService.create(pgOwnerBean);
				//redirect to crate pg page
				
				//need to add pgbean to M
			PG pgBean = new PG();
			m.addAttribute("pgBean", pgBean);
				//m.addAttribute("pgOwnerBean",pgOwnerBean);
				//HttpRequest request =
			//	hsr.setAttribute("pgOwnerBean", pgOwnerBean);
				hs.setAttribute("pgOwnerBean", pgOwnerBean);
				return "CreatePG";
				//pgOwnerService.create(pgOwnerBean);
				//return "OwnerHome";
			}
			catch(DuplicateOwnerException e)
			{
				e.printStackTrace();
				m.addAttribute("dupUserErrorMessage", e.getLocalizedMessage());
				return "RegisterPGOwner";
			}
			catch(PasswordMissMatchException e)
			{
				m.addAttribute("pswErrorMessage",e.getLocalizedMessage());
				return "RegisterPGOwner";
			}
			catch(Exception e)
			{
				System.out.println("HC registerPGOwner catch e =");
				System.out.println(e);
				e.printStackTrace();
				String errorStr = "Due to some raesons we couldnt create your account very sorry";
				m.addAttribute("errorMessage",errorStr+" i.e."+e.getLocalizedMessage());
				return "Error";
			}
			//return "RegisterPGOwner";
		}
	}
	
	//cretaePG
	@RequestMapping("/createPG")
	public String createPG(@ModelAttribute("pgBean")@Valid PG pgBean,BindingResult br,Model m,HttpSession hs)
	{
		//find pgOwnerBean from HSR pgOwnerBean
		PGOwner pgOwnerBean = (PGOwner)hs.getAttribute("pgOwnerBean");
		
		System.out.println("from HS pgwnereban = "+pgOwnerBean);
		if(br.hasErrors())
		{
			//pgAddressErrorMessage
			return "CreatePG";
		}
		else
		{
			try
			{
				//check for all fileds of address of pg
				List<String> emptyAddressFieldsList = Address.addressValidation(pgBean.getAddress().getHouseNumber(),
						pgBean.getAddress().getStreet(),
						pgBean.getAddress().getDisrtict(),
						pgBean.getAddress().getState(),
						pgBean.getAddress().getCountry(),
						pgBean.getAddress().getPin());
				if(emptyAddressFieldsList == null || (emptyAddressFieldsList.isEmpty() == false))
				{
					throw new PGAddressCanNotBeEmptyException(emptyAddressFieldsList.toString());
				}
				
				
				
				//add myPg to PGownerBean coll now
							
				//check for duplicate Pg 
				List<PG> pgList = pgService.loadAllPGs();
				boolean isItDuplicatePG = isItDuplicatePG(pgList,pgBean);
				if(isItDuplicatePG == true)
				{
					Address address = pgBean.getAddress() ;
					String addressStr = address.getHouseNumber()+" "+address.getStreet()
					                   +" "+address.getDisrtict()+" "+address.getState()+" "+address.getCountry()
					                   +" "+address.getPin();
					String duplicatePGErrorMessageStr = "Hello "+pgOwnerBean.getFirstName()+", some one has already "
							+ "constructed a PG at same address = "+addressStr+", so pls enter your PG's address";
									
					throw new DuplicatePGException(duplicatePGErrorMessageStr);
				}
				
		    	//add pgstrtdat eto pg
				pgBean.setPgStartedDate(new Date());
				//set mypg for owner
				pgOwnerBean.setMyPG(pgBean);
				//ceraet pgowneben
				pgOwnerService.create(pgOwnerBean);
				
				//set & create PGbena
				pgBean.setMyOwner(pgOwnerBean);
				pgService.create(pgBean);
				
				return "OwnerHome";
			}
			catch(PGAddressCanNotBeEmptyException e)
			{
				System.out.println("e.tostring = ");
				System.out.println(e.toString());
				String subStr =(e.toString()).substring(2, e.toString().length()-1);
				System.out.println("substr = ");
				System.out.println(subStr);
				
				String []sa1 = (e.toString()).split(":");
				System.out.println("sa1 cintent");
				//System.out.println(Arrays.toString(sa1));
				for(String s:sa1)
				{
					System.out.println("sa1 content");
					System.out.println(s);
				}
				String subS = sa1[1].substring(2, sa1[1].length()-1);
				String [] sa = subS.split(" ");
				ArrayList<String> al = new ArrayList<>();
				for(String str:sa)
				{
					System.out.println("sa content");
					System.out.println(str);
					al.add(str);
				}
				
					if(al.contains("HOUSE-NUMBER"))
					{
						m.addAttribute("houseNumberErrorMessage","HOUSE-NUMBER can not be empty");
					}
					if(al.contains("STREET"))
					{
						m.addAttribute("streetErrorMessage","STREET can not be empty");
					}
					if(al.contains("DISTRICT"))
					{
						m.addAttribute("districtErrorMessage","DISTRICT can not be empty");
					}
					if(al.contains("STATE"))
					{
						m.addAttribute("stateErrorMessage","STATE can not be empty");
					}
					if(al.contains("COUNTRY"))
					{
						m.addAttribute("countryErrorMessage","COUNTRY can not be empty");
					}
					if(al.contains("PIN"))
					{
						m.addAttribute("pinErrorMessage","PIN can not be empty");
					}
					
				
				return "CreatePG";
			}
			catch (DuplicatePGException e)
			{
				m.addAttribute("duplicatePGErrorMessage", e.getLocalizedMessage());
				m.addAttribute("pgBean", pgBean);
				return "CreatePG";
			}
			/*catch(DuplicateOwnerException e)
			{
				e.printStackTrace();
				String duplicatePGErrorMessageStr = "Hello "+pgOwnerBean.getFirstName()+" you have already "
						+ "account with same phone number = "+pgOwnerBean.getPhoneNumber()+" at same PG address = "
								+ " "+pgBean.getAddress();
				duplicatePGErrorMessageStr = duplicatePGErrorMessageStr+" So please create another PG with different address[ your same phone number will be considered]";
				m.addAttribute("duplicatePGErrorMessage", e.getLocalizedMessage());
				//return "RegisterPGOwner";
				m.addAttribute("pgBean", pgBean);
				return "CreatePG";
			}*/
			catch(Exception e)
			{
				e.printStackTrace();	
				System.out.println("e = "+e);
				m.addAttribute("errorMessage", e.getLocalizedMessage());
				return "Error";
			}
		}
	}
	
	
	//logIn 
	//login must be POST where as logout mist be DELETE
	@RequestMapping(value = "/logIn")
	public String logIn(@RequestParam("phoneNumber") String phoneNumber,@RequestParam("password") String password,Model m,HttpSession hs)
	{
		try
		{
			//check for validations
			if(phoneNumber == null || phoneNumber.trim().length() == 0)
			{
				throw new IllegalArgumentException("Phone number can not be empty");
			}
			if(password == null || password.trim().length() == 0)
			{
				m.addAttribute("phoneNumber", phoneNumber);
				throw new IllegalArgumentException("Password is also required");
			}
			//try to find a PGOwnerBean with given credentials
			PGOwner pgOwnerBean  = pgOwnerService.findPGOwner(phoneNumber, password);
			hs.setAttribute("pgOwnerBean", pgOwnerBean);
			PG pgBean = pgOwnerBean.getMyPG();
			hs.setAttribute("pgBean", pgBean);
			return "OwnerHome";
			/*if(pgOwnerBean != null)
			{
				hs.setAttribute("pgOwnerBean", pgOwnerBean);
				return "OwnerHome";
			}
			else
			{
				return "Welcome";
			}*/
			//check wether phoneNumber exist and matchse any record
		/*	List<PGOwner> pgOwnersList = pgOwnerService.loadAllPGOwners();
			if(pgOwnersList != null)
			{
				for(PGOwner pgOwner:pgOwnersList)
				{
					if( (phoneNumber.equals(pgOwner.getPhoneNumber() )) && (password.equals(pgOwner.getPassword() ) ))
							{
						//throw new InvalidCredentialsException("Phone number & password does not match");
						
							}
				}
			}*/
			//allow him to login
			
			
			
		}
		catch(IllegalArgumentException e)
		{
			m.addAttribute("loginErrorMessage",e.getLocalizedMessage());
			return "Welcome";
		}
		catch(InvalidCredentialsException e)
		{
			m.addAttribute("loginErrorMessage",e.getLocalizedMessage());
			return "Welcome";
		}
		catch (Exception e)
		{
			//return to welcome page
			String loginErrorMessageStr = "Sorry we could not allow you to login due to some reasons,i.e";
			m.addAttribute("loginErrorMessage",loginErrorMessageStr+" ,"+ e.getLocalizedMessage());
			return "Welcome";
		}
	}
	
	//logout
	@RequestMapping("/logOut")
	public String logOut(Model m,HttpSession hs)
	{
		try
		{
			hs.invalidate();
			String signOutStr = "You are signed out";
	m.addAttribute("signOutMessage", signOutStr);
			return "Welcome";
		}
		catch (Exception e)
		{
			m.addAttribute("errorMessage", e.getLocalizedMessage());
			return "Error";
		}
	}
	
	//OpenViewAndEditPGDetails
	@RequestMapping("/openViewAndEditPGDetails")
	public String openViewAndEditPGDetailsView(HttpSession hs,Model m)
	{
		try
		{
			System.out.println("PGHC openViewAndEditPGDetailsView() try");
			PGOwner pgOwnerBean  = (PGOwner)hs.getAttribute("pgOwnerBean");
			//load from db and pass
			PG pgBean = pgService.read(pgOwnerBean.getMyPG().getPgId());
			m.addAttribute("pgBean", pgBean);
			return "ViewAndEditPGDetails";
			
		/*	if((pgOwnerBean != null) &&(pgOwnerBean instanceof PGOwner))
			{
				System.out.println("");
				System.out.println("PGHC openViewAndEditPGDetailsView() try if passed");
				//do not tske pg from HS rather take it form pgowenrBean
				//PG pgBean = (PG)hs.getAttribute("pgBean");
				System.out.println("");
				//System.out.println("from hs pgbean = ");
				//System.out.println(pgBean);
				System.out.println("from owner pg = ");
				System.out.println(pgOwnerBean.getMyPG());
				m.addAttribute("pgBean", pgOwnerBean.getMyPG());
				return "ViewAndEditPGDetails";
			}
			else
			{
				System.out.println("PGHC openViewAndEditPGDetailsView() try if falied");
				
				throw new IllegalAccessOfPGDetailsPageException("Unauthorised access");
				
			}*/
		}
	/*	catch(IllegalAccessOfPGDetailsPageException e)
		{
			hs.invalidate();
			m.addAttribute("errorMessage", e.getLocalizedMessage());
			return "Welcome";
		}*/
		catch (Exception e)
		{
			m.addAttribute("errorMessage", e.getLocalizedMessage());
			return "OwnerHome";
		}
	}
	
	//updatePG
	@RequestMapping(value="/updatePG",method=RequestMethod.POST)
	public String updatePGDetails(@ModelAttribute("pgBean")@Valid PG pgBean,BindingResult br,Model m,HttpSession hs )
	{
		//@RequestParam("pgStartedDate") String pgStartedDate
		try
		{
			if(br.hasErrors())
			{
				System.out.println("yes br had errors & br =");
				System.out.println(br);
				return "ViewAndEditPGDetails";
			}
			else
			{
				System.out.println("no brdidnt have errors");
				PGOwner pgOwnerBean = (PGOwner)hs.getAttribute("pgOwnerBean");
				//check for duplicatePG 
				List<PG> pgList = pgService.loadAllPGs();
				//remove current ownerbean
				if(pgList.remove(pgOwnerBean.getMyPG()))
				{
					boolean isItDuplicatePG = isItDuplicatePG(pgList,pgBean);
					if(isItDuplicatePG == true)
					{
						Address address = pgBean.getAddress() ;
						String addressStr = address.getHouseNumber()+" "+address.getStreet()
						                   +" "+address.getDisrtict()+" "+address.getState()+" "+address.getCountry()
						                   +" "+address.getPin();
						String duplicatePGErrorMessageStr = "Hello "+pgOwnerBean.getFirstName()+", you'r trying to change your "
								+ "PG 's address to the place = "+addressStr+", which belongs to someone else";
										
						throw new DuplicatePGException(duplicatePGErrorMessageStr);
					}
				}
				
				//setBack original ogId to new pgBean since its been set i n sform
				pgBean.setPgId(pgOwnerBean.getMyPG().getPgId());
				
				//set back pgStartedDate
				pgBean.setPgStartedDate(pgOwnerBean.getMyPG().getPgStartedDate());
				
				//System.out.println("");
				//System.out.println("pgStartedDate = ");
				//System.out.println(pgStartedDate);
				//pgBean.setPgStartedDate(new Date("pgStartedDate"));
				
				//no need to update pgOwner's my pg and save 
				
				//pgOwnerBean.setMyPG(pgBean);
				//pgOwnerService.update(pgOwnerBean);
				
				
				
				//Pg's myowner to new values update 
				// set 
				pgBean.setMyOwner(pgOwnerBean);
				pgService.update(pgBean);
				
				
				//remove the old pgBean & owner from HS
				//hs.removeAttribute("pgBean");
				//hs.removeAttribute("pgOwnerBean");
				
				//add new pgBean & owner to HS
				//hs.setAttribute("pgBean", pgBean);
				pgOwnerBean.setMyPG(pgBean);
				
	//			hs.setAttribute("pgOwnerBean", pgOwnerBean);
				
				String pgUpdatedSuccessMessageStr = "Your PG details have been updated successfully";
				m.addAttribute("pgUpdatedSuccessMessage",pgUpdatedSuccessMessageStr);
				return "OwnerHome";
			}
			
		}
		catch (DuplicatePGException e)
		{
			m.addAttribute("duplicatePGErrorMessage", e.getLocalizedMessage());
			m.addAttribute("pgBean", pgBean);
			return "ViewAndEditPGDetails";
		}
		catch (Exception e) 
		{
			m.addAttribute("errorMessage", e.getLocalizedMessage());
			return "Error";
		}
	}
	
	//viewPGDetails
	@RequestMapping("/viewPGDetails")
	public String viewPGDetails(Model m,HttpSession hs)
	{
		try 
		{
			PGOwner pgOwnerBean  = (PGOwner)hs.getAttribute("pgOwnerBean");
			PG pgBean = pgService.read(pgOwnerBean.getMyPG().getPgId());
			
			m.addAttribute("pgBean", pgBean);
			return "ViewPGDetails";
			/*PGOwner pgOwnerBean  = (PGOwner)hs.getAttribute("pgOwnerBean");
			if((pgOwnerBean != null) &&(pgOwnerBean instanceof PGOwner))
			{
				System.out.println("");
				
				//do not tske pg from HS rather take it form pgowenrBean
				//PG pgBean = (PG)hs.getAttribute("pgBean");
				System.out.println("");
				//System.out.println("from hs pgbean = ");
				//System.out.println(pgBean);
				//System.out.println("from owner pg = ");
				//System.out.println(pgOwnerBean.getMyPG());
				m.addAttribute("pgBean", pgOwnerBean.getMyPG());
				return "ViewPGDetails";
			}
			else
			{
				System.out.println("PGHC openViewAndEditPGDetailsView() try if falied");
				hs.invalidate();
				throw new IllegalAccessOfPGDetailsPageException("Unauthorised access");
				
			}*/
		}
		/*catch(IllegalAccessOfPGDetailsPageException e)
		{
			hs.invalidate();
			m.addAttribute("errorMessage", e.getLocalizedMessage());
			return "Welcome";
		}*/
		catch (Exception e)
		{
			m.addAttribute("errorMessage", e.getLocalizedMessage());
			return "OwnerHome";
		}
	}
	
	////back to owenHome from viewpgdetaisl
	@RequestMapping("/back")
	public String backToOwnerHome(Model m)
	{
		try
		{
			return "OwnerHome";
		}
		catch (Exception e)
		{
			m.addAttribute("errorMessage", e.getLocalizedMessage());
			return "Error";
		}
	}
	
	//openAddroomView
	@RequestMapping("/openAddARoom")
	public String openAddARoom(Model m,HttpSession hs)
	{
		try
		{
			System.out.println("in PGHC  openAddARoom try");
			
			//System.out.println(pgOwnerBean.getMyPG());
			m.addAttribute("roomBean", new Room());
			return "AddRoom";
		/*	PGOwner pgOwnerBean  = (PGOwner)hs.getAttribute("pgOwnerBean");
			if((pgOwnerBean != null) &&(pgOwnerBean instanceof PGOwner))
			{
				System.out.println("");
				
				//System.out.println(pgOwnerBean.getMyPG());
				m.addAttribute("roomBean", new Room());
				return "AddRoom";
			}
			else
			{
				System.out.println("PGHC openViewAndEditPGDetailsView() try if falied");
				
				throw new IllegalAccessOfPGDetailsPageException("Unauthorised access");
				
			}*/
			
		}
	/*	catch(IllegalAccessOfPGDetailsPageException e)
		{
			hs.invalidate();
			m.addAttribute("errorMessage", e.getLocalizedMessage());
			return "Welcome";
		}*/
		catch (Exception e)
		{
			m.addAttribute("errorMessage", e.getLocalizedMessage());
			return "Error";
		}
	}
	   
	//	@RequestMapping(value="/registerPGOwner",method=RequestMethod.POST)
	//(@ModelAttribute("pgOwnerBean")@Valid PGOwner pgOwnerBean,BindingResult br,Model m,HttpSession hs)
	   //addARoomaddARoom
	   //@RequestMapping(value="/addARoom",method=RequestMethod.POST)
	
	/*   @RequestMapping(value="/addARoom",method=RequestMethod.POST)
	   public String addARoom(@ModelAttribute("roomBean")@Valid Room roomBean,Model m,BindingResult br,HttpSession hs)
		{
			try
			{
				PGOwner pgOwnerBean  = (PGOwner)hs.getAttribute("pgOwnerBean");
				System.out.println("");
				System.out.println("pgowner from hs = ");
				System.out.println(pgOwnerBean);
				
				PG pgBean = pgOwnerBean.getMyPG();
				System.out.println("");
				System.out.println("pgbean took = ");
				System.out.println(pgBean);
				if(br.hasErrors())
				{
					return "AddRoom";
				}
				else
				{
					boolean isValidNumberOfBeds = Room.isValidNumberOfBeds(roomBean.getNumberOfBeds());
					if(isValidNumberOfBeds == false)
					{
						throw new IllegalArgumentException("If you dont add beds in room at all then where InMates will sleep ?");
					}
					//add rooom to pg
					pgBean.addRoom(roomBean);
					
					//add back pg to owner
					pgOwnerBean.setMyPG(pgBean);
					
					//update both pg and owner
					pgService.update(pgBean);
					
					pgOwnerService.update(pgOwnerBean);
					String roomAdditionSuccessMessageStr = "Room with room Number = "+roomBean.getRoomNumber()+" added to ur pg ";
					m.addAttribute("roomAdditionSuccessMessage", roomAdditionSuccessMessageStr);
					return "OwnerHome";
				}
			}
			catch(RuntimeException e)
			{
				m.addAttribute("errorMessage", e.getLocalizedMessage());
				return "AddRoom";
			}
			catch (Exception e)
			{
				m.addAttribute("errorMessage", e.getLocalizedMessage());
				return "Error";
			}
		}*/
	
	@RequestMapping(value="/addARoom",method=RequestMethod.POST)
	public String addRoom(@ModelAttribute("roomBean")@Valid Room roomBean,BindingResult br,Model m,HttpSession hs)
	{
		try
		{
			if(br.hasErrors())
			{
				System.out.println("addrOmm() br yes br has errir");
				
				return "AddRoom";
			}
			else
			{
				PGOwner pgOwnerBean  = (PGOwner)hs.getAttribute("pgOwnerBean");
				System.out.println("");
				System.out.println("PGHC addRoom() --try--pgowner from hs = ");
				System.out.println(pgOwnerBean);
				
				//laod rooms  from dao
				List<Room> roomsOfAPGList = roomService.loadAllRoomsOfAPG(pgOwnerBean.getMyPG());
				
				Set<Room> roomsOfAPGSet = new LinkedHashSet<>(roomsOfAPGList);
				//PG pgBean =  
					//	    pgService.
						    
				//PG pgBean = pgOwnerBean.getMyPG();
				System.out.println("");
				System.out.println("PGHC addRoom() --try--  pgbean took = ");
				//System.out.println(pgBean);
				
				/*String beds = roomBean.getNumberOfBeds()+"";
				System.out.println("beds = ");
				System.out.println(beds);*/
			//	if(beds == null || beds.trim().length() == 0)
			//		throw new IllegalArgumentException("Number of beds can not be empty ");
			/*	if(roomBean.getNumberOfBeds()+"".trim().length() == 0 )
					throw new IllegalArgumentException("Number of beds can not be empty ");*/
				
				//check for numberodbeds is int or not
			/*	if((roomBean.getNumberOfBeds() instanceof Integer) == false)
				{
					System.out.println("\n addRoom() no its nt instance");
					throw new IllegalArgumentException("Please do not tell no of beds in words");
				}
				else
				{
					System.out.println("\n addRoom() yes its  instance");
				}*/
				boolean isValidNumberOfBeds = Room.isValidNumberOfBeds(roomBean.getNumberOfBeds());
				if(isValidNumberOfBeds == false)
				{
					System.out.println("\n no not valid nofbeds");
					throw new IllegalArgumentException("If you dont add beds in room at all then where InMates will sleep ?");
				}
				//check for duplicate room
				//pgBean.addRoom(roomBean);
			    if(roomsOfAPGSet.add(roomBean) == false)
			    {
			    	System.out.println("couldnt add room "+roomBean.getRoomNumber()+" to set");
			    	throw new DuplicateRoomException();
			    }
			    else
			    {
			    	System.out.println("succssfuly add room "+roomBean.getRoomNumber()+" to set");
			    	// addd roombelongsto
					roomBean.setRoomBelongsTo(pgOwnerBean.getMyPG());
					
					//save room
					roomService.create(roomBean);
					
					//no need to add back pg to owner
					//pgOwnerBean.setMyPG(pgBean);
					
					//no need to update both pg and owner
					//pgService.update(pgBean);
					
					//pgOwnerService.update(pgOwnerBean);
					System.out.println("---------------------------------------------------------------");
					System.out.println("owner = ");
					System.out.println(pgOwnerBean);
					System.out.println("pg = ");
					System.out.println(pgOwnerBean.getMyPG());
					//add rrom to pg of owner in hs
					pgOwnerBean.getMyPG().addRoom(roomBean);
					// addBack updated owner to HS
					hs.setAttribute("pgOwnerBean", pgOwnerBean);
					System.out.println("\n room addewd sucfully returning ot ownerhome");
					String roomAdditionSuccessMessageStr = "Room with room Number = "+roomBean.getRoomNumber()+" added to ur pg ";
					m.addAttribute("roomAdditionSuccessMessage", roomAdditionSuccessMessageStr);
					return "OwnerHome";
					
			    }
			/*	if( ( pgBean.getRooms().add(roomBean)) == false)
				{
					String duplicateRoomStr = "The Room with same room number ="
							+ " "+roomBean.getRoomNumber()+" already exist, so pls give different number for ur new room";
					throw new DuplicateRoomException(duplicateRoomStr);
				}*/
				
				//second time ,do not add rooom to pg
			//	pgBean.addRoom(roomBean);
				
			}
		}
		catch(DuplicateRoomException e)
		{
			System.out.println("catch (DuplicateRoomException e)");
			String duplicateRoomStr = "The Room with same room number ="
					+ " "+roomBean.getRoomNumber()+" already exist, so pls give different number for ur new room";
			m.addAttribute("errorMessage", duplicateRoomStr);
			return "AddRoom";
		}
		catch(RuntimeException e)
		{
			System.out.println("catch (RuntimeException e)n e = "+e.getLocalizedMessage());
			e.printStackTrace();
			m.addAttribute("errorMessage", e.getLocalizedMessage());
			return "AddRoom";
		}
		catch (Exception e)
		{
			System.out.println("catch (eException e)n e)");
			m.addAttribute("errorMessage", e.getLocalizedMessage());
			return "Error";
		}
	}
	
	//viewRooms
	@RequestMapping("/viewRooms")
	public String viewRooms(Model m,HttpSession hs)
	{
		try
		{
			PGOwner pgOwnerBean = (PGOwner)hs.getAttribute("pgOwnerBean");
			PG pgBean = pgOwnerBean.getMyPG();
			List<Room> roomsList = roomService.loadAllRoomsOfAPG(pgBean);
			//Set<Room> roomsSet = pgOwnerBean.getMyPG().getRooms();
			//Set<Room> roomsSet = new LinkedHashSet<Room>(roomsList);
			Set<Room> roomsSet = new TreeSet<Room>(roomsList);
			m.addAttribute("roomsSet", roomsSet);
			//if no rooms in pg yet 
			if(roomsSet.size() == 0)
			{
				String emptyPGMessageStr = "There are no rooms in your pg,please add ASAP to add InMates";
				m.addAttribute("emptyPGMessage", emptyPGMessageStr);
			}
			return "ViewRooms";
		}
		catch (Exception e)
		{
			m.addAttribute("errorMessage", e.getLocalizedMessage());
			return "Error";
		}
	}
	
	//openEditRoomView
	@RequestMapping(value="/openEditRoomView",method=RequestMethod.POST)
	public String openEditRoomView(Model m,@RequestParam("roomId") long roomId,HttpSession hs)
	{
		try
		{
			PGOwner pgOwnerBean = (PGOwner)hs.getAttribute("pgOwnerBean");
			PG pgBean = pgOwnerBean.getMyPG() ;
			Set<Room> roomsSet = pgBean.getRooms();
			Room roomBean = null ;
			for(Room r :roomsSet)
			{
				if(r.getRoomId() == roomId)
				{
					roomBean = r ;
				}
			}
			if(roomBean == null)
			{
				throw new RoomDoesNotExistExcepton("The room u r trying to edit no more exist");
			}
			else
			{
				m.addAttribute("roomBean", roomBean);
				System.out.println("PGHC editRoom try ");
				return "EditRoom";
			}
			
		}
		catch(RoomDoesNotExistExcepton e)
		{
			m.addAttribute("errorMessage", e.getLocalizedMessage());
			return "OwnerHome";
		}
		catch (Exception e)
		{
			m.addAttribute("errorMessage", e.getLocalizedMessage());
			return "Error";
		}
	}
	
	//editRoom
	@RequestMapping(value="/updateRoom",method=RequestMethod.POST)
	public String updateRoom(@ModelAttribute("roomBean")@Valid Room roomBean,
			BindingResult br,HttpSession hs,@RequestParam("roomId")long roomId,Model m)
	{
		
		try
		{
			if(br.hasErrors())
			{
				return "EditRoom";
			}
			else
			{
				PGOwner pgOwnerBean = (PGOwner)hs.getAttribute("pgOwnerBean");
				System.out.println("-------------------------");
				PG pgBean = pgOwnerBean.getMyPG();
				System.out.println("from HS pg srooms =  ");
				for(Room r1 :pgBean.getRooms())
				{
					System.out.println("");
					System.out.println(r1);
				}
				System.out.println("PGHC updateRoom() try");
				//check for beds validity
				boolean isValidNumberOfBeds = Room.isValidNumberOfBeds(roomBean.getNumberOfBeds());
				if(isValidNumberOfBeds == false)
				{
					throw new IllegalArgumentException("If you dont add beds in room at all then where InMates will sleep ?");
				}
				//load old room from pg using roomID
				Room roomOld = null;
				Set<Room> roomsSet = pgBean.getRooms();
				for(Room r:roomsSet)
				{
					if(r.getRoomId() == roomId)
					{
						roomOld = r;
						System.out.println("");
						System.out.println("roomOld = ");
						System.out.println(roomOld);
					}
				}
				if(roomOld == null)
				{
					throw new RoomDoesNotExistExcepton("The room u r trying to edit no more exist");
				}
				else
				{
					//check no of inmate is lessthan or equal to no of beds
					int totalInMatesOfOldRoom = roomOld.getRoomMates().size();
					if(roomBean.getNumberOfBeds() < totalInMatesOfOldRoom)
					{
						String inMatesOverFlowErrorMessageStr = "You'r trying to reduce the number of beds to = "+
					     roomBean.getNumberOfBeds()+" but there are still "+totalInMatesOfOldRoom+" InMates in that"
					     		+ " room ,would u like to let them stay on teres or corridor ?";
					     
						throw new InMatesOverFlowInARoomException(inMatesOverFlowErrorMessageStr);
					}
					//remove old 
					if(pgBean.removeRoom(roomOld) == true)
					{
						System.out.println("afr removing room pgrooms = ");
						System.out.println(pgBean.getRooms());
						
						//assign all stuffs of old to new
						roomBean.setRoomBelongsTo(pgBean);
						roomBean.setRoomMates(roomOld.getRoomMates());
						
						//set back all original ins val
						roomBean.setRoomId(roomId);
						
						//check for duplicate room i.e. just try to add room to pg
						if(pgBean.addRoom(roomBean) == true)
						{
							//add back pg to owner
							pgOwnerBean.setMyPG(pgBean);
							
							//update room
							roomService.update(roomBean);
							
							//do not update pg snd owner
							//pgService.update(pgBean);
							//pgOwnerService.update(pgOwnerBean);
							
							//add back owner to HS
							hs.setAttribute("pgOwnerBean", pgOwnerBean);
							
							System.out.println("///////////");
							//System.out.println("owner fro HS  = ");
							//System.out.println(pgOwnerBean);
							String roomUpdatedSuccessMessageStr = "The room details have been updated to ur wish";
							m.addAttribute("roomUpdatedSuccessMessage",roomUpdatedSuccessMessageStr);
							return "ViewRooms";
						}
						else
						{
						   pgBean.addRoom(roomOld);
							throw new DuplicateRoomException();
						}
					}
					else
					{
						throw  new DuplicateRoomException();
					}
				}
			}
		}
		catch(InMatesOverFlowInARoomException e)
		{
			m.addAttribute("errorMessage", e.getLocalizedMessage());
			return "EditRoom";
		}
		catch(RoomDoesNotExistExcepton |CanNotRemoveTheRoomException e)
		{
			//String errorMessageStr = "The room u r trying to edit no more exist";
			m.addAttribute("errorMessage", e.getLocalizedMessage());
			return "OwnerHome";
		}
		catch (DuplicateRoomException e)
		{
			String duplicateRoomStr = "You'r trying to change room number to ="
					+ " "+roomBean.getRoomNumber()+" ,but unfortunatelly that room number already exist in ur PG,"
							+ " so pls give different number";
			m.addAttribute("errorMessage", duplicateRoomStr);
			return "EditRoom";
		}
		catch(RuntimeException e)
		{
			m.addAttribute("errorMessage", e.getLocalizedMessage());
			return "EditRoom";
		}
		catch (Exception e)
		{
			m.addAttribute("errorMessage", e.getLocalizedMessage());
			return "Error";
		}
	}
	
	//openRemoveRoomView
	@RequestMapping(value="/openRemoveRoomView",method=RequestMethod.POST)
	public String openRemoveRoomView(@RequestParam("roomId")long roomId,Model m)
	{
		try
		{
			System.out.println("\nPGHC--openRemoveRoomView try\n");
			Room roomBean = roomService.read(roomId);
			m.addAttribute("roomBean",roomBean);
			return "RemoveRoom";
		}
		catch (Exception e)
		{
			m.addAttribute("errorMessage", e.getLocalizedMessage());
			return "Error";
		}
	}
	
	//removeRoom
	@RequestMapping(value="/removeRoom",method=RequestMethod.POST)
	public String removeRoom(Model m,@RequestParam("roomId") long roomId,HttpSession hs)
	{
		try
		{
			System.out.println("");
			System.out.println("\nPGHC--RemoveRoom() try\n with romId = "+roomId);
			LOGGER.info("\nPGHC--RemoveRoom() try\n");
			Room roomBean = roomService.read(roomId);
			
			//if there are inmates in room then do not allow
			if(roomBean.getRoomMates().size() > 0)
			{
				throw new CanNotRemoveTheRoomException();
			}
			else
			{
				System.out.println("\nPGHC--RemoveRoom() deleteing");
			//do not delete by id
				//roomService.delete(roomId);
				//delet by obj 
				roomService.delete(roomBean);
				
				//now update back HS's owner
				/*PGOwner pgOwnerBean = (PGOwner)hs.getAttribute("pgOwnerBean");
				pgOwnerBean.getMyPG().removeRoom(roomBean);
				
				//add back owner to hs
				hs.setAttribute("pgOwnerBean", pgOwnerBean);*/
				
				String roomRemovalSuccessMessageStr = "The room with room number = "+roomBean.getRoomNumber()+" "
						+ " removed from ur PG = "+roomBean.getRoomBelongsTo().getName();
				m.addAttribute("roomRemovalSuccessMessage", roomRemovalSuccessMessageStr);
				
				return "OwnerHome";
			}
			
		}
		catch(CanNotRemoveTheRoomException e)
		{
			String cantRemoveRoomErrorMessageStr = "You'r trying to remove room in which there is/are still few InMates"
					+ ",So we do not entertain you very sorry. "
					+ "If you still want to continue first give "
					+ "sendOff to those InMates of that room from PG";
			m.addAttribute("errorMessage", cantRemoveRoomErrorMessageStr);
			return "RemoveRoom";
		}
		catch (Exception e)
		{
			m.addAttribute("errorMessage", e.getLocalizedMessage());
			return "Error";
		}
	}
	
	//viewOwnerDetails
	@RequestMapping(value="/viewOwnerDetails")
	public String viewOwnerDetails(Model m)
	{
		try
		{   
			//since user is alredy logged in no need to retrieve pgowerbean from session
			//PGOwner pgOwnerBean = (PGOwner) hs.getAttribute("pgOwnerBean");
			
			//since pgOwnerbean avilable in session no need to add to model
			//m.addAttribute("pgOwnerBean",pgOwnerBean);
			return "ViewOwnerDetails";
		}
		catch (Exception e)
		{
			e.printStackTrace();
			m.addAttribute("errorMessage", e.getLocalizedMessage());
			return "Error";
		}
	}
	
	//openEditOwnerDetails
	@RequestMapping(value="openEditOwnerDetails")
	public String openEditOwnerDetails(Model m,HttpSession hs)
	{
		try
		{
			PGOwner pgOwnerBean = (PGOwner)hs.getAttribute("pgOwnerBean");
			m.addAttribute("pgOwnerBean",pgOwnerBean);
			return "EditOwnerDetails";
		}
		catch (Exception e)
		{
			m.addAttribute("errorMessage", e.getLocalizedMessage());
			return "Error";
		}
	}
	
	//updateOwner @Valid 
	@RequestMapping(value="/updateOwner",method=RequestMethod.POST)
	public String updateOwner(@ModelAttribute("pgOwnerBean")@Valid PGOwner pgOwnerBean,BindingResult br,Model m,HttpSession hs)
	{
		//return "Welcome";
		try
		{
			if(br.hasErrors())
			{
				System.out.println("");
				System.out.println("yes br had errorrs");
				return "EditOwnerDetails";
			}
			else
			{
				System.out.println("");
				System.out.println("no br dint have errorrs");
				PGOwner pgOwnerOldBean = (PGOwner)hs.getAttribute("pgOwnerBean");
				System.out.println("pwOnerBena = ");
				System.out.println(pgOwnerBean);
				// check for duplicate owner here
				List<PGOwner> pgOwnersList =  pgOwnerService.loadAllPGOwners();
				//first remove old owner
				if(pgOwnersList.remove(pgOwnerOldBean) == false)
				{
					
					throw new Exception("Due to some reasons we could not update ur details try after sometime");
				}
				else
				{
					Set<PGOwner> pgOwnersSet = new LinkedHashSet<PGOwner>(pgOwnersList);
					if(pgOwnersSet.add(pgOwnerBean) == false)
					{
						String duplicateOwnerErrorMessageStr = "You'r trying to change your phone number to "
								+pgOwnerBean.getPhoneNumber()+" which belongs to someone else"
										+ ",So could you please either enter ur own new phone number"
										+ " or retain the old one.";
						throw new DuplicateOwnerException(duplicateOwnerErrorMessageStr);
					}
					else
					{
						System.out.println("ownerId= ");
						System.out.println(pgOwnerBean.getPgOwnerId());
						System.out.println("pwsd = "+pgOwnerBean.getPassword());
						//do not need to retain all old details
						//pgOwnerBean.setPgOwnerId(pgOwnerOldBean.getPgOwnerId());
						
						//test do not add MyPg since pwnwer table doest have colun alrdy mapped by pg table
						
						//no need to set pswd since its been set in spring form as hidden  
						//pgOwnerBean.setPassword(pgOwnerOldBean.getPassword());
						
						//update
						pgOwnerService.update(pgOwnerBean);
						//set back to HS
						hs.setAttribute("pgOwnerBean", pgOwnerBean);
						String ownerDetailsUpdatedSuccesMessageStr = "Your details have been updated successfully as follows";
						m.addAttribute("ownerDetailsUpdatedSuccesMessage", ownerDetailsUpdatedSuccesMessageStr);
						return "ViewOwnerDetails";
					}
				}
			}
			
		}
		catch(DuplicateOwnerException e)
		{
			e.printStackTrace();
			m.addAttribute("duplicateOwnerErrorMessage", e.getLocalizedMessage());
			return "EditOwnerDetails";
		}
		catch (Exception e)
		{
			m.addAttribute("errorMessage", e.getLocalizedMessage());
			return "Error";
		}
	}
	//pgOwnerId
	/*@RequestMapping(value="/updateOwner",method=RequestMethod.POST)
	public String updateDummyOwner(@ModelAttribute("pgOwnerBean")@Valid PGOwner pgOwnerBean,BindingResult br,Model m,HttpSession hs)
	{
		return "Welcome";
	}*/
	
	    //OpenAddInMateView
		@RequestMapping("/openAddInMateView")
		public String openAddInMateView(Model m,HttpSession hs)
		{
			try
			{
			    PGOwner pgOwnerBean = (PGOwner)hs.getAttribute("pgOwnerBean");
			    
				Set<Room> roomsSet = new LinkedHashSet<Room>(roomService.loadAllRoomsOfAPG(pgOwnerBean.getMyPG()));
				if(roomsSet.size() == 0)
				{
					String emptyPGMessageStr = "There'r no rooms at all in ur PG, so first add a room";
					m.addAttribute("emptyPGMessage",emptyPGMessageStr);
					m.addAttribute("roomsSet", roomsSet);
					return "AddInMate";
				}
				else
				{
					//InMate inMateBean = new InMate();
					//m.addAttribute("inMateBean", inMateBean);
					m.addAttribute("roomsSet", roomsSet);
					return "AddInMate";
				}
				
			}
			catch(Exception e)
			{
				m.addAttribute("errorMessage", "Sorry we could not allow you to add InMate now,because "+e.getLocalizedMessage());
				return "Error";
			}
		}
		
		//@ModelAttribute("inMateBean")@Valid InMate inMateBean,BindingResult br,HttpSession hs, Model m
		//addInMate
		@RequestMapping(value="/addInMate",method=RequestMethod.POST)
		public String addInMate(@RequestParam("roomId")long roomId,Model m)
		{
			try
			{
			
					System.out.println("PGHC addInMate(Model m) with roomid= "+roomId);
					InMate inMateBean = new InMate();
					m.addAttribute("inMateBean", inMateBean);
					//return "Welcome";
					Room roomBean = roomService.read(roomId);
					System.out.println("room to be used = "+roomBean);
					
					m.addAttribute("roomNumber",roomBean.getRoomNumber());
					return "AddInMate2";
				
				
			}
			catch (Exception e)
			{
				m.addAttribute("errorMessage", "Sorry we could not allow you to add InMate now,because "+e.getLocalizedMessage());
				return "Error";
			}
		}
		
		//addInMate2
		@RequestMapping(value="/addInMate2",method=RequestMethod.POST)
		public String addInMate2(@ModelAttribute("inMateBean")@Valid InMate inMateBean,
				BindingResult br,Model m,@RequestParam("roomNumber")String roomNumber,HttpSession hs)
		{
			try 
			{
				if(br.hasErrors())
				{
					System.out.println("\n PGHC addInmate2()  br has errors");
					m.addAttribute("roomNumber", roomNumber);
					return "AddInMate2";
				}
				else
				{
					PGOwner pgOwnerBean = (PGOwner)hs.getAttribute("pgOwnerBean");
					
					//duplicate validations of inamte
					//1st check wether is he owner
					if(pgOwnerBean.getPhoneNumber().equals(inMateBean.getPhoneNumber()))
					{
						String inMateCantBeOwnerErrorMessageStr = "The InMate u r trying to add is yourself"
								+ " please change his phone number if I am wrong";
						throw new DuplicateOwnerException(inMateCantBeOwnerErrorMessageStr);
					}
					//check all inmates in that pg
					List<InMate> inMatesList = inMateService.loadAllInMatesOfAPG(pgOwnerBean.getMyPG());
					
					System.out.println("\n loaded nmateslist size = "+inMatesList.size());
					Set<InMate> inMatesSet = new TreeSet<InMate>(inMatesList);
					System.out.println("\n inmtsset size \n = "+inMatesSet.size());
					for(InMate i:inMatesSet)
					{
						System.out.println("inmate = ");
						System.out.println(i.getFirstName()+" "+i.getPhoneNumber());
					}
					if(inMatesSet.add(inMateBean) == false)
					{
						System.out.println("inmtesseta.add falied");
						//String duplicateInMateErrorMessageStr ="InMate u r trying to add already exist "
						//		+ "If we 'r wrong change his phone number";
						throw new DuplicateInMateException();
					}
					
					List<Room> roomsList = roomService.loadAllRoomsOfAPG(pgOwnerBean.getMyPG());
					//find owner hs rroom and add inmate
					Set<Room> roomsSet = new TreeSet<Room>(roomsList);//pgOwnerBean.getMyPG().getRooms()
					
					//search for selected room
					Room roomBean = null;
					for(Room r:roomsSet)
					{
						if(r.getRoomNumber().equals(roomNumber))
						{
							roomBean = r;
							//for hs
							//roomsSet.add(r);
							break;
							
						}
					}
					if(roomBean != null)
					{
						//set his passwrod
						inMateBean.setPassword(InMate.generatePassword());
						//set for inamte
						inMateBean.setMyRoom(roomBean);
						//set his pg
						inMateBean.setMyPG(pgOwnerBean.getMyPG());
						//1st add for room and come to know is there vacancy
						if(roomBean.addInMate(inMateBean))
						{
							/*//W testting for roomset added
							for(Room r:roomsSet)
							{
								if(r.getRoomNumber().equals(roomNumber))
								{
									System.out.println("room imates = ");
									for(InMate i:r.getRoomMates())
									{
										System.out.println("inamte = ");
										System.out.println(i);
									}
								}
							}
							//w test over
*/							
							//System.out.println("PGHC addInMate2(Model m)");
							inMateService.create(inMateBean);
							
							
							//find owner hs rroom and add inmate
							//Set<Room> roomsSet = new TreeSet<Room>(pgOwnerBean.getMyPG().getRooms());
							/*for(Room r :roomsSet)
							{
								if(r.getRoomNumber().equals(roomNumber))
								{
									roomsSet.add(r);
								}
							}*/
							//now add for HS 
							pgOwnerBean.getMyPG().setRooms(roomsSet);
							//add bcak to HS
							hs.setAttribute("pgOwnerBean", pgOwnerBean);
							
							String inMateAddedSuccessMessageStr =" InMate = "+inMateBean.getFirstName()+" has been placed"
									+ " in room number = "+roomNumber;
							m.addAttribute("inMateAddedSuccessMessage", inMateAddedSuccessMessageStr);
							return "OwnerHome";
						}
						else
						{
							throw new DuplicateInMateException();
						}
					}
					else
					{
						throw new Exception("We couldn't find the room u r trying to add InMate");
					}
					
				//	return "Welcome";
				}
				
			}
			catch(DuplicateInMateException e)
			{
				String duplicateInMateErrorMessageStr ="InMate u r trying to add already exist "
						+ "If we 'r wrong change his phone number";
				m.addAttribute("roomNumber", roomNumber);
				m.addAttribute("errorMessage",duplicateInMateErrorMessageStr);
				return "AddInMate2";
			}
			catch(DuplicateOwnerException e)
			{
				m.addAttribute("roomNumber", roomNumber);
				m.addAttribute("errorMessage",e.getLocalizedMessage());
				return "AddInMate2";
			}
			catch (Exception e)
			{
				System.out.println("pghc addinmate2 catch() e = "+e.getLocalizedMessage());
				m.addAttribute("errorMessage", "Sorry we could not allow you to add InMate now,because "+e.getLocalizedMessage());
				e.printStackTrace();
				return "Error";
			}
		}
		
		//ViewAllInMates
		@RequestMapping("/viewAllInMates")
		public String viewAllInMates(Model m,HttpSession hs)
		{
			try
			{
				PGOwner pgOwnerBean = (PGOwner)hs.getAttribute("pgOwnerBean");
				List<InMate> inMatesList = inMateService.loadAllInMatesOfAPG(pgOwnerBean.getMyPG());
				if(inMatesList.size() == 0)
				{
					String emptyRoomsMessageStr = "There'r no InMates in PG yet,please add";
					m.addAttribute("emptyRoomsMessage",emptyRoomsMessageStr);
					return "ViewAllInMates";
				}
				else
				{
					Set<InMate> inMatesSet = new TreeSet<>(inMatesList);
					m.addAttribute("inMatesSet",inMatesSet);
					return "ViewAllInMates";
				}
			}
			catch (Exception e)
			{
				m.addAttribute("errorMessage",e.getLocalizedMessage());
				return "Error";
			}
		}
		
		//openEditInMateView
		@RequestMapping(value="/openEditInMateView",method=RequestMethod.POST)
		public String openEditInMateView(@RequestParam("inMateId")long inMateId,Model m)
		{
			try
			{
				InMate inMateBean  = inMateService.read(inMateId);
				if(inMateBean == null )
				{
					String inMateIsNoMoreErrorMessageStr = "The InMate = "+
							" U'r trying to edit no more exist in PG,may be u have alredy removed him";
					throw new Exception(inMateIsNoMoreErrorMessageStr);
				}
				else
				{
					m.addAttribute("inMateBean", inMateBean);
					Room roomBean = inMateBean.getMyRoom();
					m.addAttribute("roomNumber",roomBean.getRoomNumber());
					return "EditInMate";
				}
			} 
			catch (Exception e)
			{
				m.addAttribute("errorMessage",e.getLocalizedMessage());
				return "Error";
			}
		}
		
		//updateInMate @RequestParam("inMateId")long inMateId)
		@RequestMapping(value="updateInMate",method=RequestMethod.POST)
		public String updateInMate(@ModelAttribute("inMateBean")@Valid InMate inMateBean,BindingResult br,
				Model m,@RequestParam("roomNumber")String roomNumber,HttpSession hs)
		{
			PGOwner pgOwnerBean = (PGOwner)hs.getAttribute("pgOwnerBean");
			try
			{
				if(br.hasErrors())
				{
					LOGGER.info("PGHC -> updateInMate br has errors ");
					System.out.println("PGHC -> updateInMate br has errors ");
					m.addAttribute("roomNumber",roomNumber);
					return "EditInMate";
				}
				else
				{
					//check fro isitowner number 
					if(inMateBean.getPhoneNumber().equals(pgOwnerBean.getPhoneNumber()))
					{
						String inMateCantBeOwnerErrorMessageStr = "The InMate u r trying to change InMate's phone number to = "
								+inMateBean.getPhoneNumber()+" which is your own phone number "
								+ " please either change his phone number to other or keep the old one";
						throw new DuplicateOwnerException(inMateCantBeOwnerErrorMessageStr);
					}
					
					//validations for update d inmate
					List<InMate> inMatesList = inMateService.loadAllInMatesOfAPG(pgOwnerBean.getMyPG());
					
					Set<InMate> inMatesSet = new TreeSet<InMate>(inMatesList);
					//load old inamte
					InMate oldInMateBean = inMateService.read(inMateBean.getInMateId());
					
					//now remove old inmayebean
					if(inMatesSet.remove(oldInMateBean))
					{
						//now try to add & check for duplicate
						if(inMatesSet.add(inMateBean))
						{
							//set back his original states
							//inMateBean.setInMateId(inMateId);
							
							//set pg
							inMateBean.setMyPG(pgOwnerBean.getMyPG());
							
							List<Room> roomsList = roomService.loadAllRoomsOfAPG(pgOwnerBean.getMyPG());
							//find out inmtes room
							Room inMatesRoom = null ;
							for(Room r:roomsList)
							{
								if(r.getRoomNumber().equals(roomNumber))
								{
									inMatesRoom = r ;
									break ;
								}
							}
							if(inMatesRoom != null)
							{
								//set inamtes old room
								inMateBean.setMyRoom(inMatesRoom);
								
								//now set back to HS owner
								//remove inmtes room from hs'pg;roomset & and add back updated
								if(pgOwnerBean.getMyPG().getRooms().remove(inMatesRoom))
								{
									//now add back updated
									if(pgOwnerBean.getMyPG().getRooms().add(inMatesRoom))
									{
										inMateService.update(inMateBean);
										String inMateUpdatedSuccessMessageStr = "The InMate = "+inMateBean.getFirstName()+
												" details have been updated successfully";
										m.addAttribute("inMateUpdatedSuccessMessage",inMateUpdatedSuccessMessageStr);
										return "OwnerHome";
									}
									else
									{
										throw new Exception("Couldnt update InMate couldnt add new inmate to HS room");
									}
								}
								else
								{
									throw new Exception("Couldnt update InMate couldnt remove new inmate to HS room");
								}
							}
							else
							{
								throw new Exception("Couldnt update InMate since inmateroom is null");
							}
							
							
						}
						else
						{
							throw new DuplicateInMateException();
						}
					}
					else
					{
						//couldnt remove oldiamte
						throw new Exception("Could not remove oldInmate from inmtesSet ");
					}
				}
			}
			catch(DuplicateOwnerException e)
			{
				m.addAttribute("roomNumber", roomNumber);
				m.addAttribute("errorMessage",e.getLocalizedMessage());
				return "EditInMate";
			}
			catch(DuplicateInMateException e)
			{
				String duplicateInMateErrorMessageStr ="U r trying to change InMate's phone number = "
						+ " "+inMateBean.getPhoneNumber()+" which belongs to other InMate of your own PG = "
								+ " "+pgOwnerBean.getMyPG().getName();
				m.addAttribute("errorMessage",duplicateInMateErrorMessageStr);
				return "EditInMate";
			}
			catch (Exception e)
			{
				e.printStackTrace();
				String editInMateErrorMessageStr = "We couldnt update your inmate's details sorry because of ";
				m.addAttribute("errorMessage",editInMateErrorMessageStr+" "+e.getLocalizedMessage());
				return "Error";	
			}
		}
		
		//openRemoveInMateView
		@RequestMapping(value="/openRemoveInMateView",method=RequestMethod.POST)
		public String openRemoveInMateView(@RequestParam("inMateId")long inMateId, Model m)
		{
			try
			{
				InMate inMateBean = inMateService.read(inMateId);
				if(inMateBean != null)
				{
					m.addAttribute("inMateBean", inMateBean);
					return "RemoveInMate";
				}
				else
				{
					throw new Exception("The InMate u r trying to remove does not exist");
				}
			}
			catch (Exception e)
			{
				m.addAttribute("errorMessage",e.getLocalizedMessage());
				return "Error";	
			}
		}
		
		//removeInMate
		@RequestMapping(value="/removeInMate",method=RequestMethod.POST)
		public String removeInMate(@RequestParam("inMateId")long inMateId,Model m,
				HttpSession hs,@RequestParam("roomNumber") String roomNumber)
		{
			try
			{
				InMate inMateBean = inMateService.read(inMateId);
				if(inMateBean != null)
				{
					//upadte to HS abour exist of inmate
					PGOwner pgOwnerBean  = (PGOwner)hs.getAttribute("pgOwnerBean");
					/*for(Room r:pgOwnerBean.getMyPG().getRooms())
					{
						System.out.println("");
						System.out.println("roo");
					}*/
					Set<Room> roomsSet = pgOwnerBean.getMyPG().getRooms();
					boolean removalSuccessBool = false;
					for(Room r : roomsSet)
					{
						if(r.getRoomNumber().equals(roomNumber))
						{
							if(r.getRoomMates().remove(inMateBean))
							{
								removalSuccessBool = true ;
								break ;
							}
						}
					}
					if(removalSuccessBool)
					{
						inMateService.delete(inMateBean);
		//				hs.setAttribute("pgOwnerBean", pgOwnerBean);
						String inMateRemovalSuccessMessageStr = "The unwanted InMate = "+inMateBean.getFirstName()
						+" has been removed from PG";
						m.addAttribute("inMateRemovalSuccessMessage", inMateRemovalSuccessMessageStr);
						return "OwnerHome";
					}
					else
					{
						throw new Exception("The InMate couldn't be removed from PG");
					}
					
				}
				else
				{
					throw new Exception("The InMate u r trying to remove does not exist");
				}
			}
			catch (Exception e)
			{
				m.addAttribute("errorMessage",e.getLocalizedMessage());
				return "Error";
			}
		}
		
		//viewDues
		@RequestMapping("/viewDues")
		public String viewDues(Model m,HttpSession hs)
		{
			try
			{
				PGOwner pgOwnerBean = (PGOwner)hs.getAttribute("pgOwnerBean");
				
				//check who all have due
				//load all inmtes of a pg
				List<InMate> inMatesList = inMateService.loadAllInMatesOfAPG(pgOwnerBean.getMyPG());
				
				//Map<InMate, Map<Double,Double>> allInMatesDuesMap = new TreeMap<InMate, Map<Double,Double>>();
				
				//one map to hold all inmates dues with total months count and total dues so far
				Map<InMate, List<String>> allInMatesDuesMap = new TreeMap<InMate,List<String>>();
				
				Date today = new Date();
				String currentMonthStr = today.toString().substring(0,19);
				System.out.println("curnmtStr = ");
				System.out.println(currentMonthStr);
				//search for bill in each inmate
				for(InMate i:inMatesList)
				{
					System.out.println("\n\n FOR INMATE --------= ");
					System.out.println(i.getFirstName());
					//check his joined date and noofbills available
					Date inMatesJoinedDate = i.getDateOfJoining();
					
					//calculate how many months he haas stayed in pg
				//	System.out.println("dirct date substrction = ");
				//	System.out.println(today.getTime() - inMatesJoinedDate.getTime());
					
					//int totalMonths = (yearCount *12) +monthCount ;
					int totalMonths = Util.getTotalMonths(inMatesJoinedDate);
					System.out.println("toatal months = "+totalMonths);
					
					//chaeck his bills col
					Set<Bill> inMateBillsSet = new LinkedHashSet<Bill>(i.getMyBills());
					
					int duesMonthCount = 0;
					//check how many bills he has 
					//declare one list fro one inmate to hold duesmonths list
					List<String> oneInMatesDuesYearNMonthsList = null;
					if(inMateBillsSet.size() != totalMonths)
					{
						duesMonthCount = totalMonths - inMateBillsSet.size() ;
						
						
						int thisYear = today.getYear();
						int thisMonth = today.getMonth();
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
						//create those many dues
					//	Map<Integer, Integer> duesYearNMonthsMap = Util.calculateDueMonths(duesMonthCount);
					//	Properties p = Util.calculateDueMonths(duesMonthCount);/
						
						 oneInMatesDuesYearNMonthsList = Util.calculateDuesMonthNYear(duesMonthCount);
						
						//System.out.println("duesYearNMonths.size() = "+duesYearNMonthsList.size());
						
					/*	int k =0;
						for(String s:duesYearNMonthsList)
						{
							System.out.println("month "+(++k));
							System.out.println(s);
						}*/
					/*	for( Entry e:p.entrySet())
						{
							System.out.println("month "+(++k));
							System.out.println(e.getKey()+" - "+e.getValue());
						}*/
					//	System.out.println("duesYearNMonthsMap.size() = "+duesYearNMonthsMap.size());
						/*for(Entry<Integer , Integer> e:duesYearNMonthsMap.entrySet())
						{
							System.out.println("month "+(++k));
							System.out.println(e.getKey()+" - "+e.getValue());
						}*/
				/*counter:for(int count= 1 ;count<=duesMonthCount;count++)
						{
							//creta counted back month with year
						 // Date dueDate = sdf.parse("01"+(thisMonth-count)+thisYear);
						  System.out.println("due date =");
						  System.out.println(dueDate);
							//System.out.println("year = "+year+" ,month =  "+(today.getMonth()-count));
							
						}*/
					/*counter:	for(int count= 1 ;count<=duesMonthCount;count++)
						{
							int year = today.getYear();
							//creta counted back month with year
							if(count % 12 == 0)
							{
								year--;
							}
							System.out.println("year = "+year+" ,month =  "+(today.getMonth()-count));
							
						}*/
					}
					if(duesMonthCount > 0)
					{
						System.out.println("\n yes there are dues since dumnth count > 0\n");
						//calculate his total dues amount
						double oneMonthCost = i.getMyRoom().getCostPerBed();
						
						double totalCost = oneMonthCost * duesMonthCount ;
						
						oneInMatesDuesYearNMonthsList.add(totalCost+"");
						//allInMatesDuesMap.put(i, duesMonthCount+"-"+totalCost);
						
						allInMatesDuesMap.put(i,oneInMatesDuesYearNMonthsList);
						
						System.out.println("i, duesMonthCount+\"-\"+totalCost");
						System.out.println(duesMonthCount+"-"+totalCost);
					}
					/*for(Bill b:inMateBillsSet)
					{
						String billOfMonthStr = b.getBillOfMonth().toString().substring(0,19);
						System.out.println("billofmonthtStr = ");
						System.out.println(billOfMonthStr);
						if(billOfMonthStr.equals(currentMonthStr))
						{
							
						}
					}*/
					
				}
				if(allInMatesDuesMap.isEmpty())
				{
					String noDuesAtAllMessageStr = "There are no pending dues ";
					m.addAttribute("noDuesAtAllMessage", noDuesAtAllMessageStr);
					return "Dues";
				}
				else
				{
					m.addAttribute("allInMatesDuesMap", allInMatesDuesMap);
					return "Dues";
				}
				
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
				m.addAttribute("errorMessage",e.getLocalizedMessage());
				return "Error";
			}
		}
		
		//openPayRentView
		@RequestMapping(value="/openPayRentView",method=RequestMethod.POST)
		public String openPayRentView(@RequestParam("inMateId")long inMateId, Model m,
				@RequestParam("duesMonthCount") int duesMonthCount,@RequestParam("totalDue")double totalDue)
		{
			try
			{
				System.out.println("PGHC openPayRentView");
				//Map<InMate, String> oneInMateDuesMap = new TreeMap<InMate, String>();
				InMate inMateBean = inMateService.read(inMateId);
				if(inMateBean == null)
				{
					throw new Exception("We could'nt find the InMate in ur PG for whom u r trying to get rent");
				}
				else
				{
					m.addAttribute("inMateBean", inMateBean);
					m.addAttribute("duesMonthCount", duesMonthCount);
					m.addAttribute("totalDue", totalDue);
					return "PayRent";
				}
				
			}
			catch (Exception e)
			{
				m.addAttribute("errorMessage",e.getLocalizedMessage());
				return "Error";
			}
		}
		
		
		//payRent Model m
		@RequestMapping(value="/payRent",method=RequestMethod.POST)
		public String payRentView(@RequestParam("inMateId")long inMateId, Model m,
				@RequestParam("duesMonthCount") int duesMonthCount,@RequestParam("totalDue")double totalDue
				,@RequestParam("numberOfMonthsInMateWantsToPay")String numberOfMonthsInMateWantsToPay)
		{
			System.out.println("PGHC payRent(Model m)");
			System.out.println("\ninMateId"+inMateId);
			System.out.println("\nduesMonthCount"+duesMonthCount);
			System.out.println("\n"+totalDue);
			//return "Welcome";
			System.out.println("numberOfMonthsInMateWantsToPay "+numberOfMonthsInMateWantsToPay);
		   
			InMate inMateBean = inMateService.read(inMateId);
			 
			//return "Welcome";
			try
			{
				//validate for numberOfMonthsInMateWantsToPay 
				//if its negative 
				if(numberOfMonthsInMateWantsToPay == null || numberOfMonthsInMateWantsToPay.trim().length() == 0)
				{
					throw new IllegalArgumentException("Number of months can not be empty");
				}
				else
				{
					int numberOfMonthsInMateWantsToPayInt = Integer.parseInt(numberOfMonthsInMateWantsToPay);
					if(numberOfMonthsInMateWantsToPayInt <= 0)
					{
						throw new IllegalArgumentException("say number of months in a positive number");
					}
					else if(numberOfMonthsInMateWantsToPayInt > duesMonthCount)
					{
						String payMentErrorMessageStr = "Why more number of months thann dues ? Did he pay advance rent ?"
								+ " We cant add for next months rents.";
						throw new IllegalArgumentException(payMentErrorMessageStr);
					}
					else
					{
						
						List<Date> payMentMonthsList = Util.generatePaymentMonths(numberOfMonthsInMateWantsToPayInt);
					/*	for(Date d:payMentMonthsList)
						{
							System.out.println("\n for d = \n");
							System.out.println(d);
						}*/
					//create those many bills for that inmate 
						for(Date d:payMentMonthsList)
						{
							System.out.println("\n for d = \n");
							System.out.println(d);
							Bill bill = new Bill();
							bill.setAmount(inMateBean.getMyRoom().getCostPerBed());
							bill.setBillOf(inMateBean);
							bill.setBillOfMonth(d);
							bill.setDateOfRentPaid(new Date());
							bill.setRoomNumber(Integer.parseInt(inMateBean.getMyRoom().getRoomNumber()));
							bill.setBillOfPG(inMateBean.getMyPG());
							
							//is it necessary to add bill to inmate ?
							//inMateBean.addABill(bill);
							
							//inMateBean.getMyPG().getBills().add(bill);
							//add that bill to pg
							PG pgBean = pgService.read(inMateBean.getMyPG().getPgId());
							
							pgBean.getBills().add(bill);
							
							//save bill 
							billService.create(bill);
							//update pg
							pgService.update(pgBean);
							
						}
						String paymentSuccessMessageStr = numberOfMonthsInMateWantsToPay+" months rent added to "+inMateBean.getFirstName()
						+" 's bill list ";
						m.addAttribute("paymentSuccessMessage", paymentSuccessMessageStr);
						return "OwnerHome";
					}
				}
				
				
			}
			catch(NumberFormatException e)
			{
				String payMentErrorMessageStr = "Tell number of months in number, not in words";
				m.addAttribute("payMentErrorMessage",payMentErrorMessageStr);
				//addback all holdings
				m.addAttribute("inMateBean", inMateBean);
				m.addAttribute("duesMonthCount", duesMonthCount);
				m.addAttribute("totalDue", totalDue);
				return "PayRent";
			}
			catch(RuntimeException e)
			{
				e.printStackTrace();
				m.addAttribute("payMentErrorMessage", e.getLocalizedMessage());
				//addback all holdings
				m.addAttribute("inMateBean", inMateBean);
				m.addAttribute("duesMonthCount", duesMonthCount);
				m.addAttribute("totalDue", totalDue);
				return "PayRent";
			}
			catch (Exception e) 
			{
				e.printStackTrace();
				m.addAttribute("errorMessage",e.getLocalizedMessage());
				return "Error";
			}
		}
	/*	@RequestMapping(value="/payRent",method=RequestMethod.POST)
		public String payRent(@RequestParam("numberOfMonthsInMateWantsToPay") int numberOfMonthsInMateWantsToPay
								,@RequestParam("duesMonthCount")int duesMonthCount
				,@RequestParam("totalDue")double totalDue,Model m)
		{
			System.out.println("in PGHC payrent");
			//System.out.println("inMateId = "+inMateId);
			return "PayRent";
		
		}*/
		
		//openComplaintBox
		@RequestMapping(value="/openComplaintBox",method=RequestMethod.GET)
		public String openComplaintBox(Model m,HttpSession hs)
		{
			try
			{
				PGOwner pgOwnerBean = (PGOwner)hs.getAttribute("pgOwnerBean");
				System.out.println("PGHC openComplaintBox(Model m)");
				//load all complaints from db
				List<Complaint> complaintsList = complaintService.loadAllComplaintsOfAPG(pgOwnerBean.getMyPG());
				
				//if no complaints 
				if(complaintsList != null)
				{
					if(complaintsList.size() == 0)
					{
						System.out.println("\nPGHC opencomlBOx -> comlist size = 0");
						String emptyComplaintBoxMessageStr = "There are no complaints in your PG";
						m.addAttribute("emptyComplaintBoxMessage", emptyComplaintBoxMessageStr);
						return "ViewAllComplaintsOfPG";
					}
					else
					{
						System.out.println("\nPGHC opencomlBOx -> comlist size > 0");
						Set<Complaint> complaintsSet = new LinkedHashSet<Complaint>(complaintsList);
						int complaintSLNumber = 1;
						m.addAttribute("complaintSLNumber", complaintSLNumber);
						m.addAttribute("complaintsSet", complaintsSet);
						return "ViewAllComplaintsOfPG";
					}
					
				}
				else
				{
					throw new Exception("complaintsList is null");
				}
				
			}
			catch (Exception e)
			{
				e.printStackTrace();
				m.addAttribute("errorMessage",e.getLocalizedMessage());
				return "Error";
			}
		}
}
