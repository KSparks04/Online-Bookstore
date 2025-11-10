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

        const isbnInput = form.querySelector("#ISBN");
        const isbnErrorDiv = form.querySelector("#isbn-error"); // select once
        const isbn = isbnInput.value.trim();

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
    // Compute checksum
    let sum = 0;
    for (let i = 0; i < 12; i++) {
        const digit = parseInt(isbn[i]);
        sum += (i % 2 === 0) ? digit : digit * 3;
    }

    const checkDigit = (10 - (sum % 10)) % 10;

    // Valid if last digit matches checksum
    return checkDigit === Number(isbn[12]);
}