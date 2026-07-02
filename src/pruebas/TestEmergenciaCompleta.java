package pruebas;

import centroDeEmergencia.CentralDeEmergencia;
import ciudad.Ciudad;
import ciudadInteligente.CiudadInteligente;
import gestionDispositivos.AdministradorDispositivo;
import gestionDispositivos.Camara;
import gestionRutas.*;
import unidadesDeEmergencia.Vehiculos;

public class TestEmergenciaCompleta {

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println(" PRUEBA: FLUJO COMPLETO DE EMERGENCIA");
        System.out.println("====================================");

        GrafoCiudad grafo = new GrafoCiudad();
        CentralDeEmergencia central = new CentralDeEmergencia();
        AdministradorDispositivo admin = new AdministradorDispositivo();
        Ciudad ciudad = new Ciudad("Quilmes");

        Interseccion centralEmerg = new Interseccion("Central de Emergencias");
        Interseccion hospital     = new Interseccion("Hospital Municipal");
        Interseccion plaza        = new Interseccion("Plaza Central");

        grafo.agregarInterseccion(centralEmerg);
        grafo.agregarInterseccion(hospital);
        grafo.agregarInterseccion(plaza);

        grafo.conectarIntersecciones(centralEmerg, hospital, "Av. San Martin", 2.5, 8);
        grafo.conectarIntersecciones(centralEmerg, plaza,    "Av. Rivadavia",  5.0, 12);
        grafo.conectarIntersecciones(hospital,     plaza,    "Av. Belgrano",   1.8, 5);

        Camara cam = new Camara("CAM-H1", "ENCENDIDO", hospital);
        admin.agregarCamara(cam, hospital);

        CiudadInteligente sistema = new CiudadInteligente(grafo, central, admin, ciudad);

        // CASO A: calles libres, Dijkstra elige ruta mas corta
        System.out.println("\n--- CASO A: Calles libres ---");
        sistema.gestionarEmergencia("INCENDIO", centralEmerg, plaza);

        // CASO B: Hospital congestionado, Dijkstra elige Rivadavia directa
        System.out.println("\n--- CASO B: Hospital congestionado ---");
        hospital.agregarVehiculo(new Vehiculos("AAA111", "Auto", 60));
        hospital.agregarVehiculo(new Vehiculos("BBB222", "Auto", 60));
        sistema.gestionarEmergencia("MEDICA", centralEmerg, plaza);

        System.out.println("\n====================================");
        System.out.println(" FIN DE PRUEBA: FLUJO COMPLETO");
        System.out.println("====================================");
    }
}
