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

    //updateSortArrows(attribute, currentSort.ascending); to be re-added when css spacing is implemented
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