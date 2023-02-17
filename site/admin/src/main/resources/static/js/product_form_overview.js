dropdownBrands = $("#brand");
dropdownCategories = $("#category");

$(document).ready(function() {
	$('#shortDescription').summernote({
		height: 100,
		toolbar: [
			['style', ['bold', 'italic', 'underline', 'clear']],
			['font', ['strikethrough', 'superscript', 'subscript']],
			['fontsize', ['fontsize']],
			['color', ['color']],
			['para', ['ul', 'ol', 'paragraph']],
			['insert', ['link']]
		]
	});

	$('#fullDescription').summernote({
		height: 300,
		toolbar: [
			['style', ['bold', 'italic', 'underline', 'clear']],
			['font', ['strikethrough', 'superscript', 'subscript']],
			['fontsize', ['fontsize']],
			['color', ['color']],
			['para', ['ul', 'ol', 'paragraph']],
			['insert', ['link', 'picture', 'video']]
		]
	});

	dropdownBrands.change(function() {
		dropdownCategories.empty();
		getCategories();
	});	
	
	getCategoriesForNewForm();
});

function getCategoriesForNewForm() {
	let catIdField = $("#categoryId");
	let editMode = false;
	
	if (catIdField.length) {
		editMode = true;
	}
	
	if (!editMode) getCategories();
}

function getCategories() {
	let brandId = dropdownBrands.val();
	let url = brandModuleUrl + "/" + brandId + "/categories";
	
	$.get(url, function(responseJson) {
		$.each(responseJson, function(index, category) {
			$("<option>").val(category.id).text(category.name).appendTo(dropdownCategories);
		});			
	});
}

function checkUnique(form) {
	let productId = $("#id").val();
	let productName = $("#name").val();
	let csrfValue = $("input[name='_csrf']").val();
	let params = {id: productId, name: productName, _csrf: csrfValue};
	
	$.post(checkUniqueUrl, params, function(response) {
		if (response === "OK") {
			form.submit();
		} else if (response === "Duplicate") {
			showWarningModal("There is another product having the name " + productName);	
		} else {
			showErrorModal("Unknown response from server");
		}
	}).fail(function() {
		showErrorModal("Could not connect to the server");
	});
	return false;
}	