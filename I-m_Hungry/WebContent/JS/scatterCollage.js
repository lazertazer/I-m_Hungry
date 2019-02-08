var images = document.getElementsByClassName("image");
for (var x = 0; x < images.length; x++) {
	im = images[x];
	im.style.left = (5*x) + 'vw';						//Spread images horizontally
	im.style.zIndex = x;								//Allow overlapping
	var degree = Math.floor(Math.random() * 91) - 45;	//Apply random rotation between -45 and 45
	var rotate = 'rotate(' + degree + 'deg)';
	im.style.WebkitTransform = rotate;
	im.style.MozTransform = rotate;
	im.style.OTransform = rotate;
	im.style.msTransform = rotate;
	im.style.transform = rotate;
}