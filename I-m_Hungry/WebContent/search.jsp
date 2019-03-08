<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>I'm Hungry</title>
		<link rel="stylesheet" type="text/css" href="./CSS/search_page.css">
	</head>
	<body>
		<h1>I'm Hungry</h1>
		
		<form method="GET" action="./Search" class="searchform">
			<input class="foodsearch" type="text" name="q" placeholder="Enter Food" required>
			<div class="tooltip">
				<input class="numberfood" type="number" name="num" min="1" max="30" value="5" required>
				<span class="tooltiptext">Number of items to show in results</span>
			</div>
			<br/>
			<br/>
			<input type="submit" value="Feed Me!"/>
		</form>
	</body>
</html>