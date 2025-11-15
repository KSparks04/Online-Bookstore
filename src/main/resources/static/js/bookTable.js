let currentSort = {
    attribute: null,
    ascending: true
}

async function sortBy(attribute){
    if(currentSort.attribute === attribute){
        currentSort.ascending = !currentSort.ascending;
    }else{
        currentSort.attribute = attribute;
        currentSort.ascending = true;
    }


    const response = await fetch(`/sortFragment/${attribute}/${currentSort.ascending}`);
    const html = await response.text();
    const tbody = document.querySelector("table tbody").innerHTML = html;

}

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