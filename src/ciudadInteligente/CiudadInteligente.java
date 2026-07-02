package ciudadInteligente;

import centroDeEmergencia.CentralDeEmergencia;
import ciudad.Ciudad;
import gestionDispositivos.AdministradorDispositivo;
import gestionRutas.Calle;
import gestionRutas.GrafoCiudad;
import gestionRutas.Interseccion;
import gestionRutas.ListaCalles;
import gestionRutas.NodoCalle;


public class CiudadInteligente {

    private GrafoCiudad grafoCiudad;
    private CentralDeEmergencia centralEmergencias;
    private AdministradorDispositivo administradorDeDispositivos;
    private Ciudad ciudad;

    public CiudadInteligente(
            GrafoCiudad grafoCiudad,
            CentralDeEmergencia centralEmergencias,
            AdministradorDispositivo administradorDeDispositivos,
            Ciudad ciudad) {

        this.grafoCiudad = grafoCiudad;
        this.centralEmergencias = centralEmergencias;
        this.administradorDeDispositivos = administradorDeDispositivos;
        this.ciudad = ciudad;
    }

    public void iniciarSistema() {
        System.out.println("Sistema iniciado");
    }

    public void generarReporte() {
        System.out.println("===== REPORTE =====");
        System.out.println("Ciudad: " + ciudad.getNombre());
        System.out.println("Modulo de emergencias: activo");
        centralEmergencias.mostrarCantidadEmergencias();
        System.out.println("Modulo de dispositivos: activo");
    }

    public void gestionarEmergencia(String tipo, Interseccion origen, Interseccion destino) {

        System.out.println("\n>>> EMERGENCIA: " + tipo + " en " + destino.getId());

        // 1. Registrar la emergencia
        centralEmergencias.registrarEmergencia(tipo, destino.getId());

        // 2. Actualizar estado de calles via camaras
        System.out.println("\n[1] Actualizando estado de calles via camaras...");
        actualizarEstadoCallesDesdeGrafo();

        // 3. Calcular ruta optima respetando estados actuales
        System.out.println("\n[2] Calculando ruta optima...");
        ListaCalles ruta = grafoCiudad.calcularRutaMinima(origen, destino);

        if (ruta.getPrimero() == null) {
            System.out.println("No hay ruta disponible hacia " + destino.getId() + ". Emergencia no puede ser atendida.");
            return;
        }

        // Imprimir ruta elegida
        NodoCalle actualCalle = ruta.getPrimero();
        while (actualCalle != null) {
            Calle c = actualCalle.getCalle();
            System.out.println("   " + c.getOrigen().getId() + " -> " + c.getDestino().getId()
                    + " por " + c.getNombre() + " | " + c.getDistancia() + " km | " + c.getEstadoCalle());
            actualCalle = actualCalle.getSiguiente();
        }

        // 4. Bloquear la ruta para que la unidad pase sin obstaculos
        System.out.println("\n[3] Bloqueando calles de la ruta para paso de emergencia...");
        grafoCiudad.bloquearRuta(ruta);

        // 5. Despachar unidad
        System.out.println("\n[4] Despachando unidad de emergencia...");
        centralEmergencias.atenderEmergencia();

        // 6. Liberar calles al finalizar
        System.out.println("\n[5] Emergencia atendida. Liberando calles...");
        grafoCiudad.liberarRuta(ruta);

        System.out.println("\n>>> Emergencia finalizada. Trafico normalizado.");
    }

    private void actualizarEstadoCallesDesdeGrafo() {
        gestionRutas.NodoInterseccion actual = grafoCiudad.getVertices().getPrimero();
        while (actual != null) {
            actual.getInterseccion().actualizarEstadoCalles();
            actual = actual.getSiguiente();
        }
    }

    public GrafoCiudad getGrafoCiudad() { return grafoCiudad; }
    public void setGrafoCiudad(GrafoCiudad grafoCiudad) { this.grafoCiudad = grafoCiudad; }
    public CentralDeEmergencia getCentralEmergencias() { return centralEmergencias; }
    public void setCentralEmergencias(CentralDeEmergencia centralEmergencias) { this.centralEmergencias = centralEmergencias; }
    public AdministradorDispositivo getAdministradorDeDispositivos() { return administradorDeDispositivos; }
    public void setAdministradorDeDispositivos(AdministradorDispositivo administradorDeDispositivos) { this.administradorDeDispositivos = administradorDeDispositivos; }
    public Ciudad getCiudad() { return ciudad; }
    public void setCiudad(Ciudad ciudad) { this.ciudad = ciudad; }
}
