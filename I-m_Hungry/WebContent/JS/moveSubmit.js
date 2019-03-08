function moveSubmit(select, ID) {
	var form = select.form;
	var hiddenField = document.createElement("input");
	hiddenField.setAttribute("type", "hidden");
	hiddenField.setAttribute("name", "id");
	hiddenField.setAttribute("value", ID);
	form.appendChild(hiddenField);
	form.submit();
}