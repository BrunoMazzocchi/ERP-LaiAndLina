

window.onload = function() {
    var orderState = document.getElementById("state").value
    console.log("Hola 1 ")
    switch (orderState){
        case 1:
            orderState.innerHtml = "En bodega"
            console.log("Hola 2 ")
            break;
        case 2:
            // code block
            break;
        default:
        // code block
    }

}
