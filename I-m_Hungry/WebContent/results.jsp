<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<title>Results</title>
		<link rel="stylesheet" type="text/css" href="./CSS/results.css">
		<script>
    		makeCollage();
    		async function makeCollage() {
    			var cse_url = 'https://www.googleapis.com/customsearch/v1?';	//Google Custom Search Engine (CSE) API
    			cse_url += 'key=AIzaSyAVrKq--dMNOkfH4p6kjrKIiqGhZ4alq5k';		//API key
    			cse_url += '&cx=016870486013668844652:p_im0w326so';				//CSE identifier
    			cse_url += '&searchType=image&num=10';							//Set to search images
    			cse_url += '&num=10';											//Return 10 results
    			cse_url += '&filter=1';											//Turn on duplicate content filter
    			cse_url += '&q=pizza';									//Search query
    		
    			const response = await fetch(cse_url);					//Call API
        		const responseJson = await response.json();				//extract JSON from the http response
				var results = responseJson.items;						//get array of results
			
        		var collage = document.getElementById("collage");
				collage.style.display = 'none';							//Hide collage while images are being added
				
        		for (x in results) {
        			var image = document.createElement("img");
        			
        			image.src = results[x].link;
        			image.style.maxWidth = '70%';						//Scale down images to fit collage
        			image.style.maxHeight = '70%';
        			image.style.position = 'absolute';
       				image.style.top = '5vh';
       				image.style.left = (5*x) + 'vw';					//Spread images across width of collage
        			image.style.zIndex = x;								//Allow images to overlap
        		
        			var degree = Math.floor(Math.random() * 91) - 45;	//Apply random rotation between -45 and 45
        			var rotate = 'rotate(' + degree + 'deg)';
        			image.style.WebkitTransform = rotate;
        			image.style.MozTransform = rotate;
        			image.style.OTransform = rotate;
        			image.style.msTransform = rotate;
        			image.style.transform = rotate;
        		
        			collage.appendChild(image);							//Add image to collage
        		}
        		collage.style.display = 'block';						//Show collage
    		}
    	</script>
    	
    	<!-- The following are CDNs for Bootstrap which I think we should use (but don't HAVE to), learn how at w3schools.com -->
		<!-- Latest compiled and minified CSS -->
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
		<!-- jQuery library -->
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
		<!-- Latest compiled JavaScript -->
		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
	</head>
	<body>
		<div id="collage"></div>
	</body>
</html>