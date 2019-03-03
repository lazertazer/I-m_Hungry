<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>${recipe.getName()} - Recipe Information</title>
		<link rel="stylesheet" type="text/css" href="./CSS/recipe_info.css">
		<script src="./JS/removeElement.js"></script>
	</head>
	<body>
		<h1>${recipe.getName()}</h1>
		
		<table id="dropdownButtons">
			<tr>
				<td>
					<button onclick="removeElement('dropdownButtons')">Printable Version</button>
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
					<form action="./UserLists" id="dropdown" method="get">
						<button type="submit">Add to List</button>
						<input type="hidden" style="display: none" name="operation" value="addRecipe">
						<input type="hidden" style="display: none" name="id" value="${recipe.getID()}">
					</form>
				</td>
			</tr>
		</table>
		
		<img src="${recipe.getImageURL()}"><br>
		
		<c:set var="useTot" value="${recipe.useTotalMinutes()}" />
		<c:set var="totMins" value="${recipe.getTotalMinutes()}" />
		<c:set var="prpMins" value="${recipe.getPrepMinutes()}" />
		<c:set var="cookMins" value="${recipe.getCookMinutes()}" />
		<c:set var="serv" value="${recipe.getServings()}" />
		<c:set var="instructions" value="${recipe.getInstructions().getInstructionSets()}" />
		
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
		
		<h2 id="ingredientHeader">Ingredients</h2>
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
			<%
				if (alt) {
					out.print("<br>");
				}
			%>
		</ul>
		
		<h2 id="instructionHeader">Instructions</h2>
		<c:choose>
			<c:when test="${instructions != null && instructions.size() != 0}">
				<c:forEach items="${instructions}" var="instructionSet">
					<table class="instructionTable">
						<thead>
							<tr>
								<td>
									<h4>${instructionSet.getName()}</h4>
								</td>
							</tr>
						</thead>
						<c:forEach items="${instructionSet.getSteps()}" var="step">
							<tr>
								<td>
									${step.getStepNum()}.&emsp;${step.getStep()}
								</td>
							</tr>
						</c:forEach>
					</table>
				</c:forEach>
			</c:when>
			<c:otherwise>
				This recipe has no associated instructions.
			</c:otherwise>
		</c:choose>
	</body>
</html>