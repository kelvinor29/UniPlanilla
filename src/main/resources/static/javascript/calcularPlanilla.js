document.addEventListener("DOMContentLoaded", () => {
    const selectMes = document.getElementById("mes");
    const selectAnio = document.getElementById("anio");
    const selectTipoPlanilla = document.getElementById('tipoPlanilla');
    const mesActual = new Date().getMonth() + 1;
    const anioActual = new Date().getFullYear();

    // Funcione para cambiar los tipos de planillas permitidos en el mes y año selccionado
    const actualizarTiposPlanillas = async () => {
        const mes = selectMes.value;
        const anio = selectAnio.value;

        try {
            const res = await fetch(`/planillas/tiposPlanilla?mes=${mes}&anio=${anio}`);

            if (!res.ok) {
                console.error(`Error al obtener los tipos de planilla: ${res.status}`);
                selectTipoPlanilla.innerHTML = '<option value="" disabled>Error al cargar las opciones</option>';
                return;
            }

            const tiposPlanillas = await res.json();
            selectTipoPlanilla.innerHTML = ''; // Limpiar opciones

            const opcionFragmento = document.createDocumentFragment();
            tiposPlanillas.forEach(tp => {
                const opcionPlanilla = document.createElement('option');
                opcionPlanilla.value = tp.value;
                opcionPlanilla.textContent = tp.label;
                opcionFragmento.appendChild(opcionPlanilla);
            });

            selectTipoPlanilla.appendChild(opcionFragmento);
        } catch (error) {
            console.error("Error de red al obtener tipos de planilla:", error);
            selectTipoPlanilla.innerHTML = '<option value="" disabled>Error de conexión</option>';
        }
    };

    // Validar selección del mes
    const validarMes = () => {
        const mesSeleccionado = parseInt(selectMes.value);

        // Validar si el mes actual es enero y el mes seleccionado es diciembre, se cambia de año al anterior
        if (mesActual === 1 && mesSeleccionado === 12) {
            selectAnio.value = (anioActual - 1).toString();
        } else {
            selectAnio.value = anioActual.toString();
        }
        actualizarTiposPlanillas();
    };

    // Validar selección del año
    const validarAnio = () => {
        const anioSeleccionado = parseInt(selectAnio.value);

        // Validar si el mes actual es enero y el año seleccionado es el anterior, se cambia de mes a diciembre
        if (mesActual === 1 && anioSeleccionado === (anioActual - 1)) {
            selectMes.value = '12'; // Mes diciembre
        } else {
            selectMes.value = mesActual.toString();
        }
        actualizarTiposPlanillas();
    };

    selectMes.addEventListener('change', validarMes);
    selectAnio.addEventListener('change', validarAnio);

    // Llamados
    actualizarTiposPlanillas();
});