package gestionRutas;

import gestionDispositivos.Camara;
import unidadesDeEmergencia.Vehiculos;

public class Interseccion {

    private String id;
    private ColaVehiculos colaVehiculos;
    private ListaCalles callesConectadas;
    private Camara camara; // Camara que monitorea esta interseccion (puede ser null)

    public Interseccion(String id) {
        this.id = id;
        this.colaVehiculos = new ColaVehiculos();
        this.callesConectadas = new ListaCalles();
        this.camara = null;
    }

    // Cuenta cuantos vehiculos hay esperando en la interseccion
    public int contarVehiculos() {
        return colaVehiculos.contarElementos();
    }

    // La camara detecta el trafico y actualiza el estado de las calles salientes
    public void actualizarEstadoCalles() {
        if (camara == null) {
            return;
        }

        EstadoCalle estadoDetectado = camara.detectarEstado();

        NodoCalle actual = callesConectadas.getPrimero();
        while (actual != null) {
            actual.getCalle().setEstadoCalle(estadoDetectado);
            actual = actual.getSiguiente();
        }

        System.out.println("Camara [" + camara.getCodigo() + "] detecto estado "
                + estadoDetectado + " en interseccion " + id);
    }

    public void asignarCamara(Camara camara) {
        this.camara = camara;
        camara.setInterseccionMonitoreada(this);
    }

    public void agregarVehiculo(Vehiculos vehiculo) {
        colaVehiculos.encolar(vehiculo);
    }

    public Vehiculos liberarVehiculo() {
        return colaVehiculos.desencolar();
    }

    public void agregarCalle(Calle calle) {
        callesConectadas.agregar(calle);
    }
    public Calle buscarCalleHacia(Interseccion destino) 
    {
        NodoCalle actual = callesConectadas.getPrimero();

        while (actual != null) {

            Calle calle = actual.getCalle();

            if (calle.getDestino().getId().equals(destino.getId())) {
                return calle;
            }

            actual = actual.getSiguiente();
        }

        return null;

       
    }
    

    // Getters y Setters

    public String getId() {
        return id;
    }

    public ColaVehiculos getColaVehiculos() {
        return colaVehiculos;
    }

    public ListaCalles getCallesConectadas() {
        return callesConectadas;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setColaVehiculos(ColaVehiculos colaVehiculos) {
        this.colaVehiculos = colaVehiculos;
    }

    public void setCallesConectadas(ListaCalles callesConectadas) {
        this.callesConectadas = callesConectadas;
    }

    public Camara getCamara() {
        return camara;
    }

    public void setCamara(Camara camara) {
        this.camara = camara;
    }

    @Override
    public String toString() {
        return id;
    }
}