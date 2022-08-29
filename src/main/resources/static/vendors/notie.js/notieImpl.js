
function getParams(){
    const params = new Proxy(new URLSearchParams(window.location.search), {
        get: (searchParams, prop) => searchParams.get(prop),
    });
    let value = params.msg;

    switch(value){
        case "1":
            notie.alert({ type: 'success', text: 'Se ha agregado correctamente.', time: 2 })
            break;
        case "2":
            notie.alert({ type: 'error', text: 'Ocurrio un error al agregar.', time: 2 })
            break;
        case "3":
            notie.alert({ type: 'success', text: 'Se ha editado correctamente.', time: 2 })
            break;
        case "4":
            notie.alert({ type: 'error', text: 'Ocurrio un error al editar.', time: 2 })
            break;
        case "5":
            notie.alert({ type: 'success', text: 'Se ha eliminado correctamente.', time: 2 })
            break;
        case "6":
            notie.alert({ type: 'error', text: 'Ocurrio un error al eliminar.', time: 2 })
            break;
    }
}
