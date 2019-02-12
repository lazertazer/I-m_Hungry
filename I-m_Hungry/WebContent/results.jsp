<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<title>Results</title>
		<link rel="stylesheet" type="text/css" href="./CSS/results.css">
	</head>
	<body>
		<div id="collage">
			<c:forEach items="${images}" var="image">
				<img class="image" src="${image}" />
			</c:forEach>
			<script src="./JS/scatterCollage.js"></script>
		</div>
		
		<table id="dropdownButtons">
			<tr>
				<td>
					<select>
						<option value="" disabled selected />
						<option value="0">Favorites</option>
						<option value="1">Do Not Show</option>
						<option value="2">To Explore</option>
					</select>
				</td>
			</tr>
			<tr>
				<td>
					<button type="button">Manage List</button>
				</td>
			</tr>
			<tr>
				<td>
					<button type="button">Return to Search</button>
				</td>
			</tr>
		</table>
		
		<h1>Results for ${query}</h1>
		
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
					<td class="restaurantCell" style="background-color: ${loop.index % 2 eq 0 ? '#cccccc' : '#b3b3b3'}">
						<table class="restaurantCellTable">
							<tr>
								<td class="restaurantName">
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
									(Distance to TT)
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
			</c:forEach>
		</table>
		
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
					<td class="recipeCell" style="background-color: ${loop.index % 2 eq 0 ? '#cccccc' : '#b3b3b3'}">
						<table class="recipeCellTable">
							<tr>
								<td class="recipeName">
									${result.getName()}
								</td>
								<td colspan="2">
									<div class="star-ratings">
										<!-- TODO figure out what to do instead of stars -->
										<div class="star-top" style="width: 0%"><span>★</span><span>★</span><span>★</span><span>★</span><span>★</span></div>
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
								<td>
									${result.getIngredientInfo().getNumIngredients()} ingredients
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</c:forEach>
		</table>
	</body>
</html>