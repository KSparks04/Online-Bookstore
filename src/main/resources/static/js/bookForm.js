$(document).ready(function () {
    // Initialize modal form behavior
    initAddBookForm();

    $("#close-add-book-modal").click(function() {
        $("#add-book-modal").hide();
    });
});

function initAddBookForm() {
    const form = $("#add-book-form");

    // Disable submit initially
    const submitButton = form.find("button[type='submit']");
    submitButton.prop("disabled", true);

    // Live ISBN validation
    form.find("#ISBN").on("input", function() {
        const isbn = $(this).val();
        const errorDiv = form.find("#isbn-error");

        if (isValidISBN13(isbn)) {
            errorDiv.text("");
            submitButton.prop("disabled", false);
        } else {
            errorDiv.text("Invalid ISBN-13");
            submitButton.prop("disabled", true);
        }
    });

    // Handle form submission via AJAX
    form.off("submit").on("submit", function(event) {
        event.preventDefault();
        addBook(this);
    });
}

// AJAX submit function
function addBook(element) {
    const form = $(element);
    const isbnValue = form.find("#ISBN").val();

    $.ajax({
        type: "POST",
        url: "/add-book",
        data: new FormData(element),
        processData: false,
        contentType: false,
        timeout: 5000,
        success: function (data) {
            if ($(data).find(".field-error").length) {
                form.html(data);
                // Reinitialize behavior since form was replaced
                initAddBookForm();
            } else {
                form.trigger("reset");
                getBookTable(); // refresh book table
                $("#add-book-modal").hide();
            }
        }
    });
}

// ISBN-13 validation
function isValidISBN13(isbn) {
    if(Math.floor(Math.log10(isbn)) !== 12) return false;

    var sum = 0;
    for(i = 0; i < 12; i++){
        const digit = Math.floor(isbn/(10**(12-i))) % 10;
        sum += i % 2 == 0 ? digit : digit * 3;
    }

    const calculatedChecksum = (10 - (sum % 10)) % 10;

    const lastDigit = isbn % 10;

    return calculatedChecksum === lastDigit;
}

$(document).ready(function(){
    $('#seriesSelector').select2({
        tags:true,
        placeholder:'Select a series or Add a new series'
    });
});