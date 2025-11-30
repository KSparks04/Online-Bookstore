let currentSort = {
    attribute: null,
    ascending: true
}

$(document).on("click", ".sortBy", function() {
    var attribute = $(this).data("attribute");
    if(currentSort.attribute === attribute){
        currentSort.ascending = !currentSort.ascending;
    }else{
        currentSort.attribute = attribute;
        currentSort.ascending = true;
    }

    $.ajax({
        type: "GET",
        url:`/sortFragment/${attribute}/${currentSort.ascending}`,
        timeout: 5000,
        success: function (data){
            $("#book-table").html(data);
        }
    })

});

$(document).ready(function () {
    $("#open-add-book-modal").click(function() {
        $("#add-book-modal").show();
    })
});

//Get book table data and update book table
function getBookTable(){
    $.ajax({
        type: "GET",
        url: "/get-book-list?function=refresh",
        timeout: 5000,
        success: function (data){
            $("#book-table").html(data);
        }
    });
}