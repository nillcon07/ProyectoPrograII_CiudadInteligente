package ciudadInteligente;

import centroDeEmergencia.CentralDeEmergencia;
import ciudad.Ciudad;
import gestionDispositivos.AdministradorDispositivo;
import gestionRutas.GrafoCiudad;


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

    public void generarReporte() 
    {
        System.out.println("===== REPORTE =====");
        System.out.println("Ciudad: " + ciudad.getNombre());
        System.out.println("Modulo de emergencias: activo");
        centralEmergencias.mostrarCantidadEmergencias();
        System.out.println("Modulo de dispositivos: activo");
       
    }

    public GrafoCiudad getGrafoCiudad() {
        return grafoCiudad;
    }

    public void setGrafoCiudad(GrafoCiudad grafoCiudad) {
        this.grafoCiudad = grafoCiudad;
    }

    public CentralDeEmergencia getCentralEmergencias() {
        return centralEmergencias;
    }

    public void setCentralEmergencias(CentralDeEmergencia centralEmergencias) {
        this.centralEmergencias = centralEmergencias;
    }

    public AdministradorDispositivo getAdministradorDeDispositivos() {
        return administradorDeDispositivos;
    }

    public void setAdministradorDeDispositivos(
            AdministradorDispositivo administradorDeDispositivos) {
        this.administradorDeDispositivos = administradorDeDispositivos;
    }

    public Ciudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }
    public void gestionarEmergencia(String tipo, String ubicacion) {

        centralEmergencias.registrarEmergencia(tipo, ubicacion);

        System.out.println("Actualizando estado de calles via camaras...");
        actualizarEstadoCallesDesdeGrafo();

        System.out.println("Calculando ruta optima (respetando estados de calle)...");

        System.out.println("Liberando trafico...");

        System.out.println("Semaforos en VERDE.");

        System.out.println("Despachando unidad.");

        centralEmergencias.atenderEmergencia();
    }

    // Recorre todas las intersecciones del grafo y dispara la actualizacion de estado via camara
    private void actualizarEstadoCallesDesdeGrafo() {
        gestionRutas.NodoInterseccion actual = grafoCiudad.getVertices().getPrimero();
        while (actual != null) {
            actual.getInterseccion().actualizarEstadoCalles();
            actual = actual.getSiguiente();
        }
    }


}