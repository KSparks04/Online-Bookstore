$(document).on("click", "#attempt-purchase", function() {
    attempt_purchase();
});

function attempt_purchase (){
    $.ajax({
        type: "POST",
        url: "/shopping-cart/attempt-purchase",
        timeout: 5000,
        dataType: "json",
        success: function(response){
            if(response.success){
                console.log("success");
                window.location.href = "/purchase-success";
            }else{
                showWarning(response.message);
            }
        },
        error: function(){
            showWarning("Something went wrong. Please try again.");
        }
    });
}

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