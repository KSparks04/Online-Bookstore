function initBookForm(retries = 10, delay = 100) {

    document.addEventListener("submit", async (e) => {
        if(!e.target.matches("#addBookForm form")) return;
        e.preventDefault();

        const form = e.target;
        const container = document.getElementById("addBookForm");
        const isbnInput = document.querySelector("#ISBN");
        const isbnErrorDiv = document.querySelector("#isbn-error"); // select once
        const isbn = isbnInput.value;

        if (!isValidISBN13(isbn)) {
            // Show unobtrusive inline error
            isbnErrorDiv.textContent = "Invalid ISBN-13";
            isbnErrorDiv.style.display = "block";
            isbnInput.classList.add("invalid"); // optional: highlight input
            return;
        } else {
            // Clear error if valid
            isbnErrorDiv.textContent = "";
            isbnErrorDiv.style.display = "none";
            isbnInput.classList.remove("invalid");
        }
        

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

function isValidISBN13(isbn) {
    //add every digit left to right alternating from just the value and the value*3
    //divide the whole sum by 10 (denote x)[floor math]
    //10-x = checksum bit

   let sum = 0;
   for(i=12; i > 0; i--){
    digit = Math.floor(isbn / (10 ** i)) % 10;
    sum += !(i%2==0) ? digit : digit * 3;
   }
   checksum_calc = (Math.floor(sum / 10)) - 10;
   return checksum_calc == Math.floor(isbn) % 10;

}
$(document).ready(function(){
    $('#seriesSelector').select2({
        tags:true,
        placeholder:'Select a series or Add a new series'
    });
});
