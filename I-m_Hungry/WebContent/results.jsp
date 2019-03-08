<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Results</title>
		<link rel="stylesheet" type="text/css" href="./CSS/results.css">
		<script src="./JS/forwardToInfoPage.js"></script>
	</head>
	<body>
		<div id="collage">
			<div id = "innerCollage">
				<c:forEach items="${images}" var="image">
					<img class="image" src="${image}" />
				</c:forEach>
				<script src="./JS/scatterCollage.js"></script>
			</div>
		</div>
		
		<table id="dropdownButtons">
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
						<input type="hidden" style="display: none" name="operation" value="display">
						<button type="submit">Manage List</button>
					</form>
				</td>
			</tr>
			<tr>
				<td>
					<button type="button" onclick="location.href='search.jsp'">Return to Search</button>
				</td>
			</tr>
		</table>
		
		<h1>Results for ${query}</h1>
		
		<form id="restaurantForm" method="get">
			<table id="restaurantTable">
				<thead>
					<tr>
						<td>
							<h2>Restaurants</h2>
						</td>
					</tr>
				</thead>
				<c:forEach items="${restaurantResults}" var="result" varStatus="loop">
					<tr>
						<td class="restaurantCell" id="${result.getID()}"
						style="background-color: ${loop.index % 2 eq 0 ? '#cccccc' : '#b3b3b3'}"
						onclick="forwardToInfoPage('restaurantForm', './RestaurantInfo', ${result.getID()})">
							<table class="restaurantCellTable">
								<tr>
									<td class="restaurantName" colspan="2">
										${result.getName()}
									</td>
									<td>
										<div class="star-ratings">
											<div class="star-top" style="width: ${result.getRating()}%"><span>★</span><span>★</span><span>★</span><span>★</span><span>★</span></div>
											<div class="star-bottom"><span>★</span><span>★</span><span>★</span><span>★</span><span>★</span></div>
										</div>
									</td>
								</tr>
								<tr>
									<td class="driving">
										Driving time:<br>
										${result.getMinutesFromTT()} ${result.getMinutesFromTT() eq 1 ? 'minute' : 'minutes'}
									</td>
									<td class='address'>
										${result.getLocation().getAddress()}
									</td>
									<td class="price" style="text-align: right">
										${result.getPriceRange()}
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</c:forEach>
			</table>
		</form>
		
		<form id="recipeForm" method="get">
			<table id="recipeTable">
				<thead>
					<tr>
						<td>
							<h2>Recipes</h2>
						</td>
					</tr>
				</thead>
				<c:forEach items="${recipeResults}" var="result" varStatus="loop">
					<tr>
						<td class="recipeCell"
						style="background-color: ${loop.index % 2 eq 0 ? '#cccccc' : '#b3b3b3'}"
						onclick="forwardToInfoPage('recipeForm', './RecipeInfo', ${result.getID()})">
							<table class="recipeCellTable">
								<tr>
									<td class="recipeName" colspan="2">
										${result.getName()}
									</td>
									<td>
										<div class="star-ratings">
											<div class="star-top" style="width: ${result.getScore()}%"><span>★</span><span>★</span><span>★</span><span>★</span><span>★</span></div>
											<div class="star-bottom"><span>★</span><span>★</span><span>★</span><span>★</span><span>★</span></div>
										</div>
									</td>
								</tr>
								<tr>
									<td class="preptime">
										${result.useTotalMinutes() ?
											("Ready in ".concat(result.getTotalMinutes()).concat(" minutes"))
											: ("Prep time: ".concat(result.getPrepMinutes()).concat(" minutes"))}
									</td>
									<td class="cooktime">
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
				</c:forEach>
			</table>
		</form>
	</body>
</html>