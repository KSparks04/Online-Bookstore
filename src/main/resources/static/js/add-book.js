$(document).ready(function () {
    $("#add-book-form").submit(function() {
        addBook(this);
    })

    $("#close-add-book-modal").click(function() {
        $("#add-book-modal").hide();
    })

    $("#close-add-book-success-modal").click(function() {
        $("#add-book-success-modal").hide();
    })
});

function addBook(element){
    event.preventDefault();
    $.ajax({
        type: "POST",
        url: "/add-book",
        data: new FormData(element),
        processData: false,
        contentType: false,
        timeout: 5000,
        success: function (data) {
            if ($(data).find(".field-error").length) {
                $(element).html(data);
                //Rerun since the button was reloaded
                $("#add-book-form").submit(function() {
                    addBook(this);
                })
                $("#close-add-book-modal").click(function() {
                    $("#add-book-modal").hide();
                })

            } else {
                $(element).trigger("reset");
                getBookTable();
                $("#add-book-modal").hide();
                $("#add-book-success-modal").show();
            }
        }
    });
}