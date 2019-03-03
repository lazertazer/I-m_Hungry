<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>List Management</title>
		<link rel="stylesheet" type="text/css" href="./CSS/list_management.css">
		<script src="./JS/forwardToInfoPage.js"></script>
	</head>
	<body>
		
		<table id="dropdownButtons">
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
					<button type="button" onclick="location.href='search.jsp'">Back to Search</button>
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
					<form action="./UserLists" id="dropdown" method="get">
						<button type="submit">Manage List</button>
						<input type="hidden" style="display: none" name="operation" value="display">
					</form>
				</td>
			</tr>
			
		</table>
		
		<h1>${currentList.getListName()}</h1> 
		
		<form id="listItemForm" method="get">
			<table id="listItemTable">
				<!-- TODO: add to loop through items using Lazlo's variables -->
				<c:forEach items="${currentList.getAllItems()}" var="result" varStatus="loop">
					<c:choose>
						<c:when test="${result.getType() == 'restaurant'}">
							<tr>
								<td class="listItemCell"
								style="background-color: ${loop.index % 2 eq 0 ? '#cccccc' : '#b3b3b3'}"
								onclick="forwardToInfoPage('listItemForm', './RestaurantInfo', ${result.getID()})">
									<table class="listItemCellTable">
										<tr>
											<td class="listItemName">
												${result.getName()}
											</td>
											<td colspan="2">
												<div class="star-ratings">
													<div class="star-top" style="width: ${result.getRating()}%"><span>★</span><span>★</span><span>★</span><span>★</span><span>★</span></div>
													<div class="star-bottom"><span>★</span><span>★</span><span>★</span><span>★</span><span>★</span></div>
												</div>
											</td>
										</tr>
										<tr>
											<td>
												Driving time:<br>
												${result.getMinutesFromTT()} ${result.getMinutesFromTT() eq 1 ? 'minute' : 'minutes'}
											</td>
											<td>
												${result.getLocation().getAddress()}
											</td>
											<td style="text-align: right">
												${result.getPriceRange()}
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</c:when>
					
						<c:otherwise>
							<tr>
								<td class="listItemCell"
								style="background-color: ${loop.index % 2 eq 0 ? '#cccccc' : '#b3b3b3'}"
								onclick="forwardToInfoPage('listItemForm', './RecipeInfo', ${result.getID()})">
									<table class="listItemCellTable">
										<tr>
											<td class="listItemName">
												${result.getName()}
											</td>
											<td colspan="2">
												<div class="star-ratings">
													<!-- TODO figure out what to do instead of stars -->
													<div class="star-top" style="width: ${result.getScore()}%"><span>★</span><span>★</span><span>★</span><span>★</span><span>★</span></div>
													<div class="star-bottom"><span>★</span><span>★</span><span>★</span><span>★</span><span>★</span></div>
												</div>
											</td>
										</tr>
										<tr>
											<td>
												${result.useTotalMinutes() ?
													("Ready in ".concat(result.getTotalMinutes()).concat(" minutes"))
													: ("Prep time: ".concat(result.getPrepMinutes()).concat(" minutes"))}
											</td>
											<td>
												${result.useTotalMinutes() ?
													(''.concat(result.getServings()).concat(" servings"))
													: ("Cook time: ".concat(result.getCookMinutes()).concat(" minutes"))}
											</td>
											<td style="text-align: right">
												${result.getIngredientInfo().getNumIngredients()} ingredients
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</c:otherwise>
					</c:choose>
				<!-- TODO: Add buttond -->
				
				</c:forEach>
				
			</table>
		</form>
		
	</body>
</html>