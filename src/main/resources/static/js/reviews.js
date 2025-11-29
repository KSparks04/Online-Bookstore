document.addEventListener("DOMContentLoaded", function() {
    const sortSelect = document.getElementById("sortReviews");
    const filterSelect = document.getElementById("filterRating");
    const reviewsContainer = document.getElementById("reviewsContainer");

    // keep a copy of all reviews
    const allReviews = Array.from(reviewsContainer.querySelectorAll(".review-item"));

    function renderReviews() {
        let filtered = allReviews;

        // filter by rating
        const selectedRating = filterSelect.value;
        if (selectedRating) {
            filtered = allReviews.filter(r => r.dataset.rating === selectedRating);
        }

        // sort
        const sortValue = sortSelect.value;
        filtered.sort((a, b) => {
            const dateA = new Date(a.dataset.date);
            const dateB = new Date(b.dataset.date);
            const ratingA = parseInt(a.dataset.rating);
            const ratingB = parseInt(b.dataset.rating);

            switch (sortValue) {
                case "mostRecent":
                    return dateB - dateA;
                case "oldest":
                    return dateA - dateB;
                case "highestRating":
                    return ratingB - ratingA;
                case "lowestRating":
                    return ratingA - ratingB;
                default:
                    return 0;
            }
        });

        reviewsContainer.innerHTML = "";
        filtered.forEach(r => reviewsContainer.appendChild(r));
    }

    sortSelect.addEventListener("change", renderReviews);
    filterSelect.addEventListener("change", renderReviews);

    renderReviews();
});