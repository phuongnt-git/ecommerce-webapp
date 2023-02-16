$(document).ready(function () {
    $("#buttonCancel").on("click", function () {
        history.go(-1);
    });

    $("#fileImage").change(function () {
        if (!checkFileSize(this)) {
            return;
        }
        showImageThumbnail(this);
    });
});

function checkFileSize(fileInput) {
    let fileSize = fileInput.files[0].size;

    if (fileSize > 1048576) {
        fileInput.setCustomValidity("Please choose an image less than 1 MB");
        fileInput.reportValidity();
        return false;
    } else {
        fileInput.setCustomValidity("");
        return true;
    }
}

function showImageThumbnail(fileInput) {
    let file = fileInput.files[0];
    let reader = new FileReader();
    reader.onload = function (e) {
        $("#thumbnail").attr("src", e.target.result);
    };

    reader.readAsDataURL(file);
}

function showModalDialog(title, message) {
    $("#modalTitle").text(title);
    $("#modalBody").text(message);
    $("#modalDialog").modal('show');
}

function showErrorModal(message) {
    showModalDialog("Error", message);
}

function showWarningModal(message) {
    showModalDialog("Warning", message);
}