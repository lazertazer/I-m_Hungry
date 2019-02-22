<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<title>${recipe.getName()} - Recipe Information</title>
		<link rel="stylesheet" type="text/css" href="./CSS/recipe_info.css">
	</head>
	<body>
		<h1>${recipe.getName()}</h1>
		<img src="${recipe.getImageURL()}"><br>
		
		<c:set var="useTot" value="${recipe.useTotalMinutes()}" />
		<c:set var="totMins" value="${recipe.getTotalMinutes()}" />
		<c:set var="prpMins" value="${recipe.getPrepMinutes()}" />
		<c:set var="cookMins" value="${recipe.getCookMinutes()}" />
		<c:set var="serv" value="${recipe.getServings()}" />
		
		<div class="metaInfo">
			${useTot ?
				("Total time:&emsp;".concat(totMins))
				: ("Prep time:&emsp; ".concat(prpMins)) } minutes
		</div>
			
		<div class="metaInfo">
			${useTot ?
				("Servings: &emsp; ".concat(serv))
				: ("Cook time:&emsp;".concat(cookMins).concat(" minutes"))}
		</div>
		
		<div class="metaInfo">
			${useTot ? "" : "Servings: &emsp; ".concat(serv)}
		</div>
		
		<h3>Ingredients</h3>
		<ul id="ingredientList">
			<% boolean alt = false; %>
			<c:forEach items="${recipe.getIngredientInfo().getIngredients()}" var="ingredient" varStatus="loop">
				<li class="${loop.index % 2 eq 0 ? 'evIng' : 'odIng'}">
					&bull; ${ingredient.getAmount()} ${ingredient.getUnit()} ${ingredient.getName()}
				</li>
				<%
					if (alt) {
						out.print("<br>");
					}
					alt = !alt;
				%> 
			</c:forEach>
		</ul>
	</body>
</html>