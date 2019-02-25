function removeElement (elementID) {
	var element = document.getElementById(elementID);
	element.parentNode.removeChild(element);
}