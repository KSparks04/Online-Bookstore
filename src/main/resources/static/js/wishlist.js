$(document).ready(function () {
    $(".wishlist-remove-book").submit(function () {
        event.preventDefault();
        removeWishListBook(this);
    })
});

function removeWishListBook (element){
    $.ajax({
        type: "POST",
        url: "/wishlist/edit/remove/" + $(element).data("isbn"),
        timeout: 5000,
        success: getWishlist
    });
}

function getWishlist(){
    event.preventDefault();
    $.ajax({
        type: "GET",
        url: "/wishlist/fragment",
        timeout: 5000,
        success: function(data){
            $("#wishlist-body").html(data);
        }
    });
}