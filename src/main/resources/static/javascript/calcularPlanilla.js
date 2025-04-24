document.addEventListener("DOMContentLoaded", function () {
    const selectMes = document.getElementById("mes");
    const selectAnio = document.getElementById("anio");
    const mesActual = new Date().getMonth() + 1;
    const anioActual = new Date().getFullYear();

    // Funciones
    // Utilizar AJAX para cambiar los tipos de planillas permitidos en el mes y año selccionado
    function actualizarTiposPlanillas() {
        const mes = selectMes.value;
        const anio = selectAnio.value;

        fetch(`/planillas/tiposPlanilla?mes=${mes}&anio=${anio}`)
            .then(res => res.json())
            .then(tiposPlanillas => {
                const selectTipoPlanilla = document.getElementById('tipoPlanilla');
                selectTipoPlanilla.innerHTML = ''; // Limpiar opciones

                // Generar opciones
                tiposPlanillas.forEach(tp => {
                    const opcionPlanilla = document.createElement('option');
                    opcionPlanilla.value = tp.value;
                    opcionPlanilla.textContent = tp.label;
                    selectTipoPlanilla.appendChild(opcionPlanilla);
                });
            });
    }

    // Validar selección del mes
    selectMes.addEventListener('change', function () {
        const mesSeleccionado = parseInt(selectMes.value);

        // Validar si el mes actual es enero y se selecciona el mes diciembre para cambiar el input del año
        if (mesActual === 1 && mesSeleccionado === 12) {
            selectAnio.value = (anioActual - 1).toString();
        }
        else selectAnio.value = anioActual.toString();

        actualizarTiposPlanillas();
    });

    // Validar selección del año
    selectAnio.addEventListener('change', function () {
        const anioSeleccionado = parseInt(selectAnio.value);

        // Validar si el mes actual es enero y el año seleccionado es el anterior, se cambia de mes a diciembre
        if (mesActual === 1 && anioSeleccionado === (anioActual - 1)) {
            selectMes.value = '12'; // Mes diciembre
        }
        else selectMes.value = mesActual.toString();

        actualizarTiposPlanillas();
    });


    // Llamados
    actualizarTiposPlanillas();
});