$(document).ready(function () {
    $(".open-edit-book-modal").click(function() {
        $("#edit-book-modal" + $(this).data("isbn")).show();
    })

    $(".edit-book-form").submit(function() {
        editBook(this);
    })

    $(".close-edit-book-modal").click(function() {
        $("#edit-book-modal" + $(this).data("isbn")).hide();
    })
});

function editBook(element){
    event.preventDefault();
    $.ajax({
        type: "POST",
        url: "/update-book",
        data: new FormData(element),
        processData: false,
        contentType: false,
        timeout: 5000,
        success: function (data) {
            if ($(data).find(".field-error").length) {
                $(element).html(data);

                //Rerun since the button was reloaded
                $(element).find(".edit-book-form").submit(function() {
                    editBook(this);
                })
                $(element).find(".close-edit-book-modal").click(function() {
                    $("#edit-book-modal" + $(element).data("isbn")).hide();
                })
            } else {
                getBookTable();
            }
        }
    });
}