$(document).ready(function() {		
	$("a[name='linkRemoveDetail']").each(function(index) {
		$(this).click(function() {
			removeDetailSectionByIndex(index);
		});
	});
});

function addNextDetailSection() {
	let allDivDetails = $("[id^='divDetail']");
	let divDetailsCount = allDivDetails.length;

	let htmlDetailSection = `
		<div class="form-group d-flex justify-content-center align-items-center" id="divDetail${divDetailsCount}">
			<input type="hidden" name="detailIDs" value="0" />
			<label class="text-right mr-3 font-weight-bold">Name:</label>
			<input type="text" class="form-control w-25 m-3" name="detailNames" maxlength="255" />
			<label class="text-right ml-3 mr-3 font-weight-bold">Value:</label>
			<input type="text" class="form-control w-25 m-3" name="detailValues" maxlength="255" />
		</div>
	`;
	
	$("#divProductDetails").append(htmlDetailSection);

	let previousDivDetailSection = allDivDetails.last();
	let previousDivDetailID = previousDivDetailSection.attr("id");

	let htmlLinkRemove = `
		<a class="btn fa-solid fa-times-circle fa-3x icon-dark"
			href="javascript:removeDetailSectionById('${previousDivDetailID}')"
			title="Remove this detail"></a>
	`;
	
	previousDivDetailSection.append(htmlLinkRemove);
	
	$("input[name='detailNames']").last().focus();
}

function removeDetailSectionById(id) {
	$("#" + id).remove();
}

function removeDetailSectionByIndex(index) {
	$("#divDetail" + index).remove();
}