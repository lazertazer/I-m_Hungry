<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
	<script>
	function validate(){
		var requeststr = "Search?";
		requeststr += "q="+document.searchform.search.value;
		requeststr +="num"+document.searchform.searchfood.value;
		
		var xhttp = new XMLHttpRequest();
		xhttp.open("GET", requeststr, false);
		xhttp.send();
		
		
	}
	
	</script>
	<head>
		<meta charset="ISO-8859-1">
		<title>I'm Hungry</title>
		<link rel="stylesheet" type="text/css" href="./CSS/search_page.css">
		
		
	</head>
	<body>
		<h1>I'm Hungry</h1>
		
		<form method="GET" action="./Search" class="searchform" onsubmit="return validate();">
			<input class="foodsearch" type="text" name="q" placeholder="Enter Food">
			<div class="tooltip"><input class="numberfood" type="number" name="num" step="1" value="5"><span class="tooltiptext">Number of items to show in results</span></div>
			<br/>
			<br/>
			<input type="submit" name="Submit" value="Submit"/>
		</form>
	</body>
</html>