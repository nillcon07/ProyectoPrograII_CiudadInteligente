package pruebas;

import gestionDispositivos.AdministradorDispositivo;
import gestionDispositivos.Camara;
import gestionRutas.*;
import unidadesDeEmergencia.Vehiculos;

public class TestEstadoCalle {

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println(" PRUEBA: ESTADOS DE CALLE");
        System.out.println("====================================");

        GrafoCiudad grafo = new GrafoCiudad();
        AdministradorDispositivo admin = new AdministradorDispositivo();

        Interseccion central = new Interseccion("Central de Emergencias");
        Interseccion hospital = new Interseccion("Hospital Municipal");
        Interseccion plaza = new Interseccion("Plaza Central");

        grafo.agregarInterseccion(central);
        grafo.agregarInterseccion(hospital);
        grafo.agregarInterseccion(plaza);

        // Central -> Hospital (directo, 2.5 km)
        // Central -> Plaza (directo, 7.0 km)
        // Hospital -> Plaza (1.8 km)
        grafo.conectarIntersecciones(central, hospital, "Av. San Martin", 2.5, 8);
        grafo.conectarIntersecciones(central, plaza, "Av. Rivadavia", 7.0, 18);
        grafo.conectarIntersecciones(hospital, plaza, "Av. Belgrano", 1.8, 5);

        // === CAMARA en hospital monitoreando trafico ===
        Camara camHospital = new Camara("CAM-H1", "ENCENDIDO", hospital);
        admin.agregarCamara(camHospital, hospital);

        System.out.println("\n=== CASO 1: Ruta normal (calles LIBRES) ===");
        ListaCalles ruta = grafo.calcularRutaMinima(central, plaza);
        imprimirRuta(ruta);

        System.out.println("\n=== CASO 2: Congestion detectada por camara en Hospital ===");
        // Simulamos 2 vehiculos esperando en Hospital -> CONGESTIONADA
        hospital.agregarVehiculo(new Vehiculos("AAA111", "Auto", 60));
        hospital.agregarVehiculo(new Vehiculos("BBB222", "Camion", 40));

        hospital.actualizarEstadoCalles(); // la camara detecta CONGESTIONADA
        ruta = grafo.calcularRutaMinima(central, plaza);
        imprimirRuta(ruta);
        // Dijkstra penaliza Av. San Martin + Av. Belgrano (4.3 * 2 = 8.6 > 7.0)
        // deberia elegir Av. Rivadavia directo

        System.out.println("\n=== CASO 3: Calle CORTADA manualmente ===");
        // Limpiamos congestion
        hospital.liberarVehiculo();
        hospital.liberarVehiculo();
        // Cortamos Av. Rivadavia
        grafo.actualizarEstadoCalle("Av. Rivadavia", EstadoCalle.CORTADA);
        // Restauramos San Martin a LIBRE
        grafo.actualizarEstadoCalle("Av. San Martin", EstadoCalle.LIBRE);
        grafo.actualizarEstadoCalle("Av. Belgrano", EstadoCalle.LIBRE);

        ruta = grafo.calcularRutaMinima(central, plaza);
        imprimirRuta(ruta);
        // Debe ir Central -> Hospital -> Plaza porque Rivadavia esta cortada

        System.out.println("\n=== CASO 4: Todas las rutas bloqueadas ===");
        grafo.actualizarEstadoCalle("Av. San Martin", EstadoCalle.BLOQUEADA);
        grafo.actualizarEstadoCalle("Av. Belgrano", EstadoCalle.BLOQUEADA);
        // Rivadavia ya estaba CORTADA

        ruta = grafo.calcularRutaMinima(central, plaza);
        if (ruta.getPrimero() == null) {
            System.out.println("No hay ruta disponible - todas las calles estan bloqueadas o cortadas.");
        } else {
            imprimirRuta(ruta);
        }

        System.out.println("\n====================================");
        System.out.println(" FIN DE PRUEBA: ESTADOS DE CALLE");
        System.out.println("====================================");
    }

    private static void imprimirRuta(ListaCalles ruta) {
        NodoCalle actual = ruta.getPrimero();
        if (actual == null) {
            System.out.println("Sin ruta disponible.");
            return;
        }
        while (actual != null) {
            Calle c = actual.getCalle();
            System.out.println("  " + c.getOrigen().getId() + " -> " + c.getDestino().getId()
                    + " por " + c.getNombre()
                    + " | " + c.getDistancia() + " km"
                    + " | Estado: " + c.getEstadoCalle());
            actual = actual.getSiguiente();
        }
    }
}
