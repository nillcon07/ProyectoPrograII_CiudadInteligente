package gestionDispositivos;

import gestionRutas.Interseccion;

public class AdministradorDispositivo {
    private final DiccionariosDeDispositivos dispositivos;

    public AdministradorDispositivo() {
        this.dispositivos = new DiccionariosDeDispositivos();
    }

    public void agregarDispositivo(String codigo, Dispositivo d) {
        dispositivos.insertar(codigo, d);
        System.out.println("Registrado en diccionario: " + d.getClass().getSimpleName() + " [" + codigo + "]");
    }

    // Registra una camara y la vincula directamente a una interseccion
    public void agregarCamara(Camara camara, Interseccion interseccion) {
        dispositivos.insertar(camara.getCodigo(), camara);
        interseccion.asignarCamara(camara);
        System.out.println("Camara [" + camara.getCodigo() + "] registrada y vinculada a interseccion: "
                + interseccion.getId());
    }

    public Dispositivo buscarDispositivo(String codigo) {
        return dispositivos.buscar(codigo);
    }

    public void actualizarEstado(String codigo, String nuevoEstado) {
        Dispositivo dispositivo = dispositivos.buscar(codigo);
        if (dispositivo != null) {
            dispositivo.setEstado(nuevoEstado);
            if (nuevoEstado.equalsIgnoreCase("ENCENDIDO")) {
                dispositivo.encender();
            } else {
                dispositivo.apagar();
            }
            System.out.println("Estado actualizado para [" + codigo + "] -> " + nuevoEstado);
        } else {
            System.out.println("No se encontró el dispositivo buscado con el codigo: " + codigo);
        }
    }

    public void eliminarDispositivo(String codigo) {
        dispositivos.eliminar(codigo);
    }
}
