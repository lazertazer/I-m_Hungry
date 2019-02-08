<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<title>Results</title>
		<link rel="stylesheet" type="text/css" href="./CSS/results.css">

    	<!-- The following are CDNs for Bootstrap which I think we should use (but don't HAVE to), learn how at w3schools.com -->
		<!-- Latest compiled and minified CSS -->
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
		<!-- jQuery library -->
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
		<!-- Latest compiled JavaScript -->
		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
		
	</head>
	<body>
		<div id="collage">
			<c:forEach items="${images}" var="image">
				<img class="image" src="${image}" />
			</c:forEach>
			<script src="./JS/scatterCollage.js"></script>
		</div>
	</body>
</html>