function toggleCart(button) {
    const icon = button.querySelector('i.main-icon');
    //const fill = button.querySelector('i.hover-icon');
    if (!icon) return;
    button.classList.add('clicked');
    if (icon.classList.contains("bi-cart-plus")) {
        icon.classList.remove("bi-cart-plus");
        //fill.classList.remove("bi-cat-plus-fill");
        icon.classList.add("bi-cart-check-fill");
        setTimeout(() => {
            icon.classList.remove("bi-cart-check-fill");
            icon.classList.add("bi-cart-plus");
            //fill.classList.add("bi-cat-plus-fill");
            button.classList.remove('clicked');
        }, 1000)
    }
}

$(document).ready(function () {
    $(".wishlist-button").submit(function () {
        event.preventDefault();
        $.ajax({
            type: "POST",
            url: "/wishlist/edit/add/" + $(this).data("isbn"),
            timeout: 5000,
            success: function () {
                $("#wishlist-alert").append(`
                    <div class="alert alert-success alert-dismissible" role="alert">
                        Book add to wishlist!
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                `)
            },
            error: function () {
                $("#wishlist-alert").append(`
                    <div class="alert alert-danger alert-dismissible" role="alert">
                        Please login to add books to wishlist!
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                `)
            }
        });
    })
});

