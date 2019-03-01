<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>List Management</title>
		<link rel="stylesheet" type="text/css" href="./CSS/list_management.css">
		

	</head>
	<body>
		
		<table id="dropdownButtons">
			<tr>
				<td>
					<button type="button" onclick="location.href='results.jsp'">Back to Results</button>
				</td>
			</tr>
			<tr>
				<td>
					<button type="button" onclick="location.href='search_page.html'">Back to Search</button>
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
				<c:forEach items="${currentList}" var="result" varStatus="loop">
					<c:choose>
						<c:when test="${result.class.simpleName == 'Restaurant'}">
							<tr>
								<td class="listItemCell"
								style="background-color: ${loop.index % 2 eq 0 ? '#cccccc' : '#b3b3b3'}"
								onclick="forwardToInfoPage('restaurantForm', './RestaurantInfo', ${result.getID()})">
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
								onclick="forwardToInfoPage('recipeForm', './RecipeInfo', ${result.getID()})">
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
				</c:forEach>
			</table>
		</form>
		
	</body>
</html>