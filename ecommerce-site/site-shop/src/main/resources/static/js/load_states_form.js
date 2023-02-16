let countryList;
let stateList;

$(document).ready(function() {
    countryList = $("#country");
    stateList = $("#state");

    countryList.change(function() {
        loadStates();
    });
});

function loadStates() {
    let selectedCountry = $("#country option:selected");
    let countryId = selectedCountry.val();

    let url = contextPath + "loadStatesByCountry/" + countryId;
    $.get(url, function(responseJSON) {
        stateList.empty();
        $.each(responseJSON, function (index, state) {
            $("<option>").val(state.name).text(state.name).appendTo(stateList);
        });
    }).fail(function() {
        alert("Failed to connect to server, please try again!")
    });
}