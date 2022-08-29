const checkboxes = document.getElementsByName('state')
function onlyOne(checkbox) {
    checkboxes.forEach((item) => {
        if (item !== checkbox) item.checked = false
    })
}

function checkIfCompleted(){
    const completedCheckBox = document.getElementById("completedCheckbox");
    const formToSubmit = document.getElementById("orderForm");
    const salePriceValue = document.getElementById("salePrice");
    const endDate = document.getElementById("endDate");
    const today = new Date().toISOString().slice(0, 10)

    if(completedCheckBox.checked === true){
        notie.input({
            text: 'Por favor agrega el precio vendido:',
            cancelCallback: function (value) {
                notie.alert({ type: 3, text: 'Por favor cambia el estado de la orden e intenta nuevamente'})
            },
            submitCallback: function (value) {
                notie.alert({ type: 1, text: 'Se vendió por: ' + value })
                salePriceValue.value = value;
                endDate.value = today;
                formToSubmit.submit();
            },
            type: 'text',
            placeholder: '$0',
            allowed: new RegExp('[^0-9]', 'g')
        })
    } else if(completedCheckBox.checked === false){
        formToSubmit.submit();
    }
}

$("#noteForm").submit(function (e) {
    e.preventDefault();
});

$("#orderForm").submit(function (e) {
    e.preventDefault();
});