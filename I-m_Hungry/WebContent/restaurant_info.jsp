<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${restaurant.getName()} - Restaurant Information</title>
		<link rel="stylesheet" type="text/css" href="./CSS/restaurant_info.css">
	</head>
	<body>
		<h1>${restaurant.getName()}</h1>

		<table id="dropdownButtons">
			<tr>
				<td>
					<!-- TODO figure this out -->
					<button>Printable Version</button>
				</td>
			</tr>
			<tr>
				<td>
					<form action="./Search" method="get">
						<button type="submit">Back to Results</button>
						<input type="hidden" style="display: none" name="back" value="true">
					</form>
				</td>
			</tr>
			<tr>
				<td>
					<select name="list" form="dropdown" required>
						<option value="" disabled selected />
						<option value="FAV">Favorites</option>
						<option value="DNS">Do Not Show</option>
						<option value="XPL">To Explore</option>
					</select>
				</td>
			</tr>
			<tr>
				<td>
					<form action="./ListManagementServlet" id="dropdown" method="get">
						<button type="submit">Add to List</button>
						<input type="hidden" style="display: none" name="operation" value="addRestaurant">
						<input type="hidden" style="display: none" name="id" value="${restaurant.getID()}">
					</form>
				</td>
			</tr>
		</table>
		
		<c:if test="${!restaurant.getImageURL().equals('')}">
			<img src="${restaurant.getImageURL()}">
		</c:if>
		
		<table>
			<tr>
				<td class="firstTD">
					Address:
				</td>
				<td>
					<a href="${restaurant.getDirectionsURL()}" target="_blank">
						${restaurant.getLocation().getAddress()}
					</a>
				</td>
			</tr>
			<tr>
				<td class="firstTD">
					Phone Number:
				</td>
				<td>
					TODO
				</td>
			</tr>
			<tr>
				<td colspan="2" id="site">
					<a href="${restaurant.getURL()}" target="_blank">Restaurant website and additional information</a>
				</td>
			</tr>
		</table>
	</body>
</html>