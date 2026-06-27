package gestionDispositivos;

import gestionRutas.EstadoCalle;
import gestionRutas.Interseccion;

public class Camara extends Dispositivo {

    private boolean grabando;
    private Interseccion interseccionMonitoreada;

    // Constructor sin interseccion (para compatibilidad con codigo existente)
    public Camara(String codigo, String estado) {
        super(codigo, estado);
        this.grabando = true; // Las camaras graban siempre al crearse
        this.interseccionMonitoreada = null;
    }

    // Constructor con interseccion asignada
    public Camara(String codigo, String estado, Interseccion interseccion) {
        super(codigo, estado);
        this.grabando = true;
        this.interseccionMonitoreada = interseccion;
    }

    // Detecta el estado de la calle segun la cantidad de vehiculos en la interseccion
    // 0 vehiculos -> LIBRE
    // 1 a 3       -> CONGESTIONADA
    // 4+          -> BLOQUEADA
    public EstadoCalle detectarEstado() {
        if (interseccionMonitoreada == null) {
            return EstadoCalle.LIBRE;
        }

        int cantidadVehiculos = interseccionMonitoreada.contarVehiculos();

        if (cantidadVehiculos == 0) {
            return EstadoCalle.LIBRE;
        } else if (cantidadVehiculos <= 3) {
            return EstadoCalle.CONGESTIONADA;
        } else {
            return EstadoCalle.BLOQUEADA;
        }
    }

    public void iniciarGrabacion() {
        this.grabando = true;
    }

    public void detenerGrabacion() {
        this.grabando = false;
    }

    public boolean isGrabando() {
        return grabando;
    }

    public void setGrabando(boolean grabando) {
        this.grabando = grabando;
    }

    public Interseccion getInterseccionMonitoreada() {
        return interseccionMonitoreada;
    }

    public void setInterseccionMonitoreada(Interseccion interseccion) {
        this.interseccionMonitoreada = interseccion;
    }

    @Override
    public String toString() {
        String interseccionInfo = (interseccionMonitoreada != null)
                ? interseccionMonitoreada.getId()
                : "Sin asignar";
        return "Camara [" + getCodigo() + "] | Estado: " + getEstado()
                + " | Grabando: " + (grabando ? "Si" : "No")
                + " | Monitoreando: " + interseccionInfo;
    }
}
