async function sortBy(attribute){
            console.log("Called sortBy with attribute " + attribute);
            const response = await fetch(`/sortFragment/${attribute}`);
            const html = await response.text();
            document.getElementById("bookTableContainer").innerHTML = html;
        }