$(document).on("click", "#attempt-purchase", function() {
    $("#purchaseModal").modal("show");
});

$(document).on("submit", "#purchaseForm", function(e) {
    e.preventDefault();

    const firstName = $("#firstNameInput").val();
    const lastName = $("#lastNameInput").val();
    const country = $("#countryInput").val();
    const address = $("#addressInput").val();
    const city = $("#cityInput").val();
    const postalCode = $("#postalInput").val();
    const phone = $("#phoneInput").val();
    const paymentMethod = $("#paymentMethod").val();

    $.ajax({
        type: "POST",
        url: "/shopping-cart/attempt-purchase",
        contentType: "application/json",
        data: JSON.stringify({
            firstName: firstName,
            lastName: lastName,
            country: country,
            address: address,
            city: city,
            postal: postalCode,
            phone: phone,
            paymentMethod: paymentMethod
        }),
        dataType: "json",
        timeout: 5000,
        success: function(response) {
            if (response.success) {
                console.log("success");
                $("#purchaseModal").modal("hide");
                window.location.href = "/purchase-success";
            } else {
                showWarning(response.message);
            }
        },
        error: function() {
            showWarning("Something went wrong. Please try again.");
        }
    });
})

function showWarning(message){
    let container = $("#warning-container");

    if(container.length == 0){
        container = $(`<div id="warning-container"></div>`)
            .css({
                position: "fixed",
                bottom:"10px",
                right:"10px",
                background:"#f8d7a",
                color:"#721c24",
                border:"1px solid #f5c6cb",
                padding:"10px 20px",
                "border-radius": "5px",
                "z-index": 9999,
                "box-shadow": "0 2px 6px rgba(0,0,0,0.2)"
            })
            .appendTo("body");
    }
    container.text(message).fadeIn();
    setTimeout(() => container.fadeOut(), 4000);
}