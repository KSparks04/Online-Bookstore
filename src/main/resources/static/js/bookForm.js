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
    const response = await fetch("/get-book-list?function=refresh");
    const html = await response.text();
    document.querySelector("table tbody").innerHTML = html;
}