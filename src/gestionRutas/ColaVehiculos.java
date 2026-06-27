package gestionRutas;

import unidadesDeEmergencia.NodoVehiculos;
import unidadesDeEmergencia.Vehiculos;

public class ColaVehiculos {
    private NodoVehiculos frente;
    private NodoVehiculos fin;

    // Constructor: la cola arranca vacía
    public ColaVehiculos() {
        this.frente = null;
        this.fin = null;
    }

    // Verifica si la cola no tiene elementos
    public boolean estaVacia() {
        return frente == null;
    }

    // RF07: Agregar vehículo al final de la cola (Enqueue)
    public void encolar(Vehiculos vehiculo) {
        NodoVehiculos nuevoNodo = new NodoVehiculos(vehiculo);
        
        if (estaVacia()) {
            // Si está vacía, el nuevo nodo es el frente y también el fin
            this.frente = nuevoNodo;
            this.fin = nuevoNodo;
        } else {
            // Si ya hay elementos, el actual 'fin' apunta al nuevo, y el nuevo pasa a ser el 'fin'
            this.fin.setSiguiente(nuevoNodo);
            this.fin = nuevoNodo;
        }
    }

    // RF07: Liberar el vehículo del frente (Dequeue)
    public Vehiculos desencolar() {
        if (estaVacia()) {
            System.out.println("Alerta (RNF02): Intento de desencolar en una intersección vacía.");
            return null; 
        }

        // Guardamos el vehículo que está al frente para devolverlo
        Vehiculos vehiculoLiberado = this.frente.getVehiculo();

        // Movemos el frente al siguiente nodo de la lista
        this.frente = this.frente.getSiguiente();

        // Si al mover el frente la cola quedó vacía, el 'fin' también debe ser null
        if (this.frente == null) {
            this.fin = null;
        }

        return vehiculoLiberado;
    }

    // Método auxiliar para ver quién está primero sin sacarlo
    public Vehiculos verPrimero() {
        if (estaVacia()) return null;
        return frente.getVehiculo();
    }

    // Cuenta los vehiculos en la cola sin modificarla
    public int contarElementos() {
        int contador = 0;
        NodoVehiculos actual = frente;
        while (actual != null) {
            contador++;
            actual = actual.getSiguiente();
        }
        return contador;
    }


}
