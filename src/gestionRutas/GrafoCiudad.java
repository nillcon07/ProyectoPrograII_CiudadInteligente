package gestionRutas;

public class GrafoCiudad {

    private ListaIntersecciones vertices;
    private ListaCalles aristas;

    public GrafoCiudad() {
        this.vertices = new ListaIntersecciones();
        this.aristas = new ListaCalles();
    }

    public void agregarInterseccion(Interseccion interseccion) {
        vertices.agregar(interseccion);
    }

    public void agregarCalle(Calle calle) {
        aristas.agregar(calle);
    }

    public void conectarIntersecciones(Interseccion origen,Interseccion destino,String nombre,double distancia,double tiempoEstimado) {

        Calle calle = new Calle(nombre,distancia,tiempoEstimado,origen,destino);

        origen.agregarCalle(calle);
        aristas.agregar(calle);
    }

    //IMPLEMENTACION ALGORITMO DIJKSTRA Para buscar ruta optima
    public ListaCalles calcularRutaMinima(Interseccion origen, Interseccion destino) {
        ListaCalles rutaMinima = new ListaCalles();
        
        if (origen == null || destino == null) {
            System.out.println("Error: Origen o destino nulos.");
            return rutaMinima;
        }

        int n = vertices.tamanio();
        if (n == 0) return rutaMinima;

        // Arreglos Para ver el estado de Dijkstra
        Interseccion[] nodos = new Interseccion[n];
        double[] distancias = new double[n];
        Calle[] callePrevia = new Calle[n];
        boolean[] visitados = new boolean[n];

        //inicializar
        NodoInterseccion actualNodo = vertices.getPrimero();
        int indice = 0;
        int indiceOrigen = -1;
        int indiceDestino = -1;

        while (actualNodo != null) {
            nodos[indice] = actualNodo.getInterseccion();
            distancias[indice] = 99999.0; //representa infinto se inicializa alli para ir encontrando caminos cada vez mas cortos
            visitados[indice] = false;

            if (nodos[indice].getId().equals(origen.getId())) indiceOrigen = indice;
            if (nodos[indice].getId().equals(destino.getId())) indiceDestino = indice;

            indice++;
            actualNodo = actualNodo.getSiguiente();
        }

        // Validación de existencia en el grafo
        if (indiceOrigen == -1 || indiceDestino == -1) {
            System.out.println("Error: El origen o destino no se encuentran registrados en el grafo.");
            return rutaMinima;
        }

        distancias[indiceOrigen] = 0.0;

        for (int i = 0; i < n; i++) {
            //Extraer el nodo no visitado con la distancia mínima
            int u = -1;
            double minDistancia = 99999.0;
            
            for (int j = 0; j < n; j++) {
                if (!visitados[j] && distancias[j] < minDistancia) {
                    minDistancia = distancias[j];
                    u = j;
                }
            }

            // Si no hay más nodos alcanzables o ya se extrajo el destino se corta
            if (u == -1 || u == indiceDestino) break;

            visitados[u] = true;

            NodoCalle actualCalle = nodos[u].getCallesConectadas().getPrimero();
            while (actualCalle != null) {
                Calle calle = actualCalle.getCalle();
                Interseccion vecino = calle.getDestino();

                // Buscar el índice del vecino en el arreglo
                int v = -1;
                for (int j = 0; j < n; j++) {
                    if (nodos[j].getId().equals(vecino.getId())) {
                        v = j;
                        break;
                    }
                }

                // Si el vecino existe, no fue visitado, y se encuentra un camino más corto
                if (v != -1 && !visitados[v]) {

                    // Verificar si la calle es transitable
                    EstadoCalle estado = calle.getEstadoCalle();

                    if (estado == EstadoCalle.CORTADA || estado == EstadoCalle.BLOQUEADA) {
                        // La calle no es transitable, se omite
                        actualCalle = actualCalle.getSiguiente();
                        continue;
                    }

                    double pesoCalle = calle.getDistancia();

                    // Si esta congestionada se penaliza el costo (doble de distancia)
                    if (estado == EstadoCalle.CONGESTIONADA) {
                        pesoCalle *= 2.0;
                    }

                    double nuevaDistancia = distancias[u] + pesoCalle;
                    if (nuevaDistancia < distancias[v]) {
                        distancias[v] = nuevaDistancia;
                        callePrevia[v] = calle;
                    }
                }
                actualCalle = actualCalle.getSiguiente();
            }
        }

        //Reconstruir la ruta óptima
        if (distancias[indiceDestino] == 99999.0) {
            System.out.println("No existe un camino posible entre " + origen.getId() + " y " + destino.getId());
            return rutaMinima;
        }

        int actual = indiceDestino;
        while (actual != indiceOrigen) {
            Calle calleLlegada = callePrevia[actual];
            //Origen -> Destino
            rutaMinima.agregarAlFrente(calleLlegada); 

            // Retroceder al nodo origen de la calle que se acanba de agregar
            Interseccion nodoAnterior = calleLlegada.getOrigen();
            for (int j = 0; j < n; j++) {
                if (nodos[j].getId().equals(nodoAnterior.getId())) {
                    actual = j;
                    break;
                }
            }
        }

        return rutaMinima;
    }
    // Cambia el estado de una calle por nombre
    public boolean actualizarEstadoCalle(String nombreCalle, EstadoCalle nuevoEstado) {
        NodoCalle actual = aristas.getPrimero();
        while (actual != null) {
            if (actual.getCalle().getNombre().equals(nombreCalle)) {
                actual.getCalle().setEstadoCalle(nuevoEstado);
                System.out.println("Estado de calle '" + nombreCalle + "' actualizado a: " + nuevoEstado);
                return true;
            }
            actual = actual.getSiguiente();
        }
        System.out.println("No se encontro la calle: " + nombreCalle);
        return false;
    }

    // Bloquea todas las calles que llegan a una interseccion (para aislar zona de emergencia)
    public void bloquearCallesHaciaInterseccion(Interseccion destino) {
        NodoCalle actual = aristas.getPrimero();
        while (actual != null) {
            Calle calle = actual.getCalle();
            if (calle.getDestino().getId().equals(destino.getId())) {
                calle.setEstadoCalle(EstadoCalle.BLOQUEADA);
                System.out.println("Calle bloqueada: " + calle.getNombre());
            }
            actual = actual.getSiguiente();
        }
    }

    // Libera todas las calles bloqueadas hacia una interseccion (fin de emergencia)
    public void liberarCallesHaciaInterseccion(Interseccion destino) {
        NodoCalle actual = aristas.getPrimero();
        while (actual != null) {
            Calle calle = actual.getCalle();
            if (calle.getDestino().getId().equals(destino.getId())
                    && calle.getEstadoCalle() == EstadoCalle.BLOQUEADA) {
                calle.setEstadoCalle(EstadoCalle.LIBRE);
                System.out.println("Calle liberada: " + calle.getNombre());
            }
            actual = actual.getSiguiente();
        }
    }

    public ListaIntersecciones getVertices() {
        return vertices;
    }

    public void setVertices(ListaIntersecciones vertices) {
        this.vertices = vertices;
    }

    public ListaCalles getAristas() {
        return aristas;
    }

    public void setAristas(ListaCalles aristas) {
        this.aristas = aristas;
    }
   
}
