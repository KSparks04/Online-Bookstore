function changeAmt(x){
    const input = document.getElementById("amount");
    let value = parseInt(input.value) || 0;
    value += x;
    if(value < 0){
        value = 0;
    }
    input.value = value;
}