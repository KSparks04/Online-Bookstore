$(document).ready(function () {
    $(".open-edit-book-modal").click(function() {
        event.preventDefault();
        setEditFormData(this);
        $("#edit-book-modal" + $(this).data("isbn")).show();
    })
});

/**
 * Sets the edit form data
 * @param element the button element
 */
function setEditFormData(element){
    const isbn = $(element).data("isbn");
    $.ajax({
        type: "GET",
        url: "/edit-book/" + isbn,
        timeout: 5000,
        success: function (data){
            $("#edit-book-form" + isbn).html(data);
            $("#edit-book-form" + isbn).find("form").submit(function () {
                editBook(this);
            })
            $("#close-edit-book-modal" + isbn).click(function() {
                $("#edit-book-modal" + isbn).hide();
            })
        }
    });
}

/**
 * Run ajax to edit the book without reloading the page
 * @param element the form element
 */
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
            } else {
                getBookTable();
            }
        }
    });
}