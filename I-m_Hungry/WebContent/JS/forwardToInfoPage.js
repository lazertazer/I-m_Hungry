function forwardToInfoPage(form, path, ID) {
	var form = document.getElementById(form);
	form.setAttribute("action", path);
	var hiddenField = document.createElement("input");
	hiddenField.setAttribute("type", "hidden");
	hiddenField.setAttribute("name", "id");
	hiddenField.setAttribute("value", ID);
	form.appendChild(hiddenField);
	form.submit();
}