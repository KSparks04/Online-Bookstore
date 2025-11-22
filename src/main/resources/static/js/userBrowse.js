function toggleCart(button) {
    const icon = button.querySelector('i.main-icon');
    //const fill = button.querySelector('i.hover-icon');
    if(!icon)return;
    button.classList.add('clicked');
    if (icon.classList.contains("bi-cart-plus")) {
        icon.classList.remove("bi-cart-plus");
        //fill.classList.remove("bi-cat-plus-fill");
        icon.classList.add("bi-cart-check-fill");
        setTimeout(()=>{
            icon.classList.remove("bi-cart-check-fill");
            icon.classList.add("bi-cart-plus");
            //fill.classList.add("bi-cat-plus-fill");
            button.classList.remove('clicked');
        },1000)
    }


}