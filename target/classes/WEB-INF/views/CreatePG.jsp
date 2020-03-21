<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>CreatePG</title>
</head>
<body>
<h3>Congratulations you have successfully registered as a PG owner</h3>
<pre>
Note :
You have to complete this step to create your PG registration
to which you want to become owner 
else your registration will be auto-cancelled
</pre>
<h1 align="center">ALL FIELDS ARE MANDATORY </h1>
<h5>${duplicatePGErrorMessage}</h5>
<div align="center">
<h4>${houseNumberErrorMessage}</h4>
<h4>${streetErrorMessage}</h4>
<h4>${districtErrorMessage}</h4>
<h4>${stateErrorMessage}</h4>
<h4>${countryErrorMessage}</h4>
<h4>${pinErrorMessage}</h4>
</div>
<s:form action="createPG.owner"  modelAttribute="pgBean">

<table align="center" border="0" bordercolor="maroon">
<tr>
<td>
PG NAME
</td>
<td>
<s:input path="name"/> 
<s:errors path="name" style="color:red"></s:errors>
</td>
</tr>

<tr>
<td colspan="2" align="center" >ADDRESS  
</td>
</tr>

<tr>
<td>
HOUSE-NUMBER  
</td>
<td>
<s:input path="address.houseNumber"></s:input>
<s:errors path="address.houseNumber" style="color:red"></s:errors>
</td>
</tr>

<tr>
<td>
STREET  
</td>
<td>
<s:input path="address.street"></s:input>
<s:errors path="address.street" style="color:red"></s:errors>
</td>
</tr>

<tr>
<td>
DISTRICT
</td>
<td>
<s:input path="address.disrtict"></s:input>
<s:errors path="address.disrtict" style="color:red"></s:errors>
</td>
</tr>

<tr>
<td>
STATE  
</td>
<td>
<s:input path="address.state"></s:input>
<s:errors path="address.state" style="color:red"></s:errors>
</td>
</tr>

<tr>
<td>
COUNTRY 
</td>
<td>
<s:input path="address.country"></s:input>
<s:errors path="address.country" style="color:red"></s:errors>
</td>
</tr>

<tr>
<td>
PIN
</td>
<td>
<s:input path="address.pin"></s:input>
<s:errors path="address.pin" style="color:red"></s:errors>
</td>
</tr>

<tr>
<td colspan="2" align="center">
<input type="submit" value="CreatePG"/> 
</td>
</tr>


</table>
</s:form>
</body>
</html>