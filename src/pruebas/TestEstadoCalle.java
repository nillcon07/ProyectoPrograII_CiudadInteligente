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

        grafo.conectarIntersecciones(central, hospital, "Av. San Martin", 2.5, 8);
        grafo.conectarIntersecciones(central, plaza, "Av. Rivadavia", 7.0, 18);
        grafo.conectarIntersecciones(hospital, plaza, "Av. Belgrano", 1.8, 5);

        Camara camHospital = new Camara("CAM-H1", "ENCENDIDO", hospital);
        admin.agregarCamara(camHospital, hospital);

        System.out.println("\n=== CASO 1: Ruta normal (calles LIBRES) ===");
        ListaCalles ruta = grafo.calcularRutaMinima(central, plaza);
        imprimirRuta(ruta);
        System.out.println("Evalucacion del algoritmo Dijkstra:");
        System.out.println("   Por Hospital:  2.5 + 1.8 = 4.3 km");
        System.out.println("   Por Rivadavia: 7.0 km (directo)");
        System.out.println("   -> Eligio Hospital porque 4.3 < 7.0");

        System.out.println("\n=== CASO 2: Congestion detectada por camara en Hospital ===");
        hospital.agregarVehiculo(new Vehiculos("AAA111", "Auto", 60));
        hospital.agregarVehiculo(new Vehiculos("BBB222", "Camion", 40));

        hospital.actualizarEstadoCalles();
        ruta = grafo.calcularRutaMinima(central, plaza);
        imprimirRuta(ruta);
        System.out.println("Evalucacion del algoritmo Dijkstra:");
        System.out.println("   Por Hospital:  2.5 + (1.8 x 2) = 6.1 km (Calle Belgrano de 1.8de peso penalizada por CONGESTIONADA)");
        System.out.println("   Por Rivadavia: 7.0 km (directo)");
        System.out.println("   -> Eligio Hospital de todas formas porque 6.1 < 7.0");
        System.out.println("   -> Nota: la penalizacion existe por congestion pero no alcanza para cambiar la ruta en este grafo");

        System.out.println("\n=== CASO 3: Calle CORTADA manualmente ===");
        hospital.liberarVehiculo();
        hospital.liberarVehiculo();
        grafo.actualizarEstadoCalle("Av. Rivadavia", EstadoCalle.LIBRE);
        grafo.actualizarEstadoCalle("Av. San Martin", EstadoCalle.CORTADA);
        grafo.actualizarEstadoCalle("Av. Belgrano", EstadoCalle.CORTADA);

        ruta = grafo.calcularRutaMinima(central, plaza);
        imprimirRuta(ruta);
        System.out.println("Evalucacion del algoritmo Dijkstra:");
        System.out.println("   Por Rivadavia: 7.0km (Directo)(unica opcion transitable)");
        System.out.println("   Por Hospital:  CORTADA -> se encuentra cortada manualmente Av. San Martin y Av. Belgrano");
        System.out.println("   -> Eligio Rivadavia porque mediante el Hosptial (que seria de 4.3km) esta cortada");

        System.out.println("\n=== CASO 4: Todas las rutas bloqueadas ===");
        grafo.actualizarEstadoCalle("Av. San Martin", EstadoCalle.BLOQUEADA);
        grafo.actualizarEstadoCalle("Av. Belgrano", EstadoCalle.BLOQUEADA);
        grafo.actualizarEstadoCalle("Av. Rivadavia", EstadoCalle.CORTADA);

        ruta = grafo.calcularRutaMinima(central, plaza);
        System.out.println("Evalucacion del algoritmo Dijkstra:");
        System.out.println("   Av. San Martin: BLOQUEADA -> descartada");
        System.out.println("   Av. Belgrano:   BLOQUEADA -> descartada");
        System.out.println("   Av. Rivadavia:  CORTADA   -> descartada");
        if (ruta.getPrimero() == null) {
            System.out.println("   -> Sin ruta disponible, todas las calles estan bloqueadas o cortadas.");
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
