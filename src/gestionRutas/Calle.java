package gestionRutas;

public class Calle {

    private String nombre;
    private double distancia;
    private double tiempoEstimado;
    private Interseccion origen;
    private Interseccion destino;
    private EstadoCalle estadoCalle;

    // Constructor
    public Calle(String nombre, double distancia, double tiempoEstimado,
                  Interseccion origen, Interseccion destino) {
        this.nombre = nombre;
        this.distancia = distancia;
        this.tiempoEstimado = tiempoEstimado;
        this.origen = origen;
        this.destino = destino;
        this.estadoCalle = EstadoCalle.LIBRE; // Por defecto libre
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public double getTiempoEstimado() {
        return tiempoEstimado;
    }

    public void setTiempoEstimado(double tiempoEstimado) {
        this.tiempoEstimado = tiempoEstimado;
    }

    public Interseccion getOrigen() {
        return origen;
    }

    public void setOrigen(Interseccion origen) {
        this.origen = origen;
    }

    public Interseccion getDestino() {
        return destino;
    }

    public void setDestino(Interseccion destino) {
        this.destino = destino;
    }

    public EstadoCalle getEstadoCalle() {
        return estadoCalle;
    }

    public void setEstadoCalle(EstadoCalle estadoCalle) {
        this.estadoCalle = estadoCalle;
    }

    @Override
    public String toString() {
        return "Calle{" +
                "nombre='" + nombre + '\'' +
                ", distancia=" + distancia +
                ", tiempoEstimado=" + tiempoEstimado +
                ", origen=" + origen +
                ", destino=" + destino +
                ", estado=" + estadoCalle +
                '}';
    }
}