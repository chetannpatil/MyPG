package com.mypg.controller;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.jdbc.support.GeneratedKeyHolder;

import com.mypg.model.PGOwner;
import com.mypg.model.Room;
import com.mypg.util.IllegalAccessOfPGDetailsPageException;

/**
 * Servlet Filter implementation class OwnerAccessFilter
 */
public class OwnerAccessFilter implements Filter
{

    /**
     * Default constructor. 
     */
    public OwnerAccessFilter() 
    {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy()
	{
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException 
	{
		try
		{
			System.out.println("");
			System.out.println("b4 filetr");
			HttpServletRequest hsr = null ;
			if(request instanceof HttpServletRequest)
			{
				 hsr = (HttpServletRequest)request;
				 HttpSession hs = hsr.getSession(false);
					PGOwner pgOwnerBean  = (PGOwner)hs.getAttribute("pgOwnerBean");
					if((pgOwnerBean == null) || !(pgOwnerBean instanceof PGOwner))
					{
						//System.out.println("PGHC openViewAndEditPGDetailsView() try if falied");
						
						throw new IllegalAccessOfPGDetailsPageException("Unauthorised access");
						
					}
					/*if((pgOwnerBean != null) &&(pgOwnerBean instanceof PGOwner))
					{
						System.out.println("");
						
						//System.out.println(pgOwnerBean.getMyPG());
						m.addAttribute("roomBean", new Room());
						return "AddRoom";
					}*/
					
			}
		   
			
			chain.doFilter(request, response);
			System.out.println("aftr filter");
		}
		catch(IllegalAccessOfPGDetailsPageException e)
		{
			///hs.invalidate();
			//m.addAttribute("errorMessage", e.getLocalizedMessage());
			request.setAttribute("errorMessage", e.getLocalizedMessage());
			RequestDispatcher rd = request.getRequestDispatcher("/");
			rd.forward(request, response);
			//return "Welcome";
		}
		catch (Exception e)
		{
			request.setAttribute("errorMessage", e.getLocalizedMessage());
			RequestDispatcher rd = request.getRequestDispatcher("/");
			rd.forward(request, response);
		}
	
		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException
	{
		
	}

}
