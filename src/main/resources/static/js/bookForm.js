function initBookForm(retries = 10, delay = 100) {
    const container = document.getElementById("addBookForm");
    if (!container) {
        if (retries > 0) setTimeout(() => initBookForm(retries - 1, delay), delay);
        return;
    }

    const form = container.querySelector("form");
    if (!form) {
        if (retries > 0) setTimeout(() => initBookForm(retries - 1, delay), delay);
        return;
    }

    form.addEventListener("submit", async (e) => {
        e.preventDefault();
        const formData = new FormData(form);

        try {
            const response = await fetch(form.action, {
                method: "POST",
                body: formData
            });
            const html = await response.text();

            container.innerHTML = html;

            // Rebind after replacing form
            initBookForm();

            if(!html.includes('th:errors')){
                refreshBookTable();
            }
        } catch (err) {
            console.error("Error submitting form:", err);
        }
    });
}

document.addEventListener("DOMContentLoaded", () => initBookForm());



async function refreshBookTable(){
    $.ajax({
        type: "GET",
        url: "/get-book-list?function=refresh",
        timeout: 5000,
        success: function (data){
            $("#book-table").html(data);
        }
    });
}
$(document).ready(function(){
    $('#seriesSelector').select2({
        tags:true,
        placeholder:'Select a series or Add a new series'
    });
});