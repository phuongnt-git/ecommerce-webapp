function clearFilter() {
    window.location = moduleUrl;
}

$(document).ready(function () {
    $(".link-delete").on("click", function (e) {
        e.preventDefault();
        showDeleteConfirmModal($(this), entityName);
    });
});

function showDeleteConfirmModal(link, entityName) {
    let entityId = link.attr("entityId");

    $("#yesButton").attr("href", link.attr("href"));
    $("#confirmText").text("Are you sure you want to delete this " + entityName + " ID " + entityId + "?");
    $("#confirmModal").modal('show');
}

function handleDetailLinkClick(cssClass, modalId) {
    $(cssClass).on("click", function(e) {
        e.preventDefault();
        let linkDetailUrl = $(this).attr("href");
        $(modalId).modal('show').find(".modal-content").load(linkDetailUrl);
    });
}

function handleDefaultDetailLinkClick() {
    handleDetailLinkClick(".link-detail", "#detailModal");
}