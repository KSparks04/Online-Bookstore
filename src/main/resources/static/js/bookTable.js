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

    updateSortArrows(attribute, currentSort.ascending);
}

function updateSortArrows(attribute, ascending){
    document.querySelectorAll(".sort-arrow").forEach(el=>el.remove());

    const header = document.querySelector(`[data-sort="${attribute}"]`);
    if(!header) return;

    const arrow = document.createElement("span");
    arrow.classList.add("sort-arrow");
    arrow.textContent = ascending ? " ▲" : " ▼";

    header.appendChild(arrow);
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