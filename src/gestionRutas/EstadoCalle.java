package gestionRutas;

public enum EstadoCalle {
    LIBRE,        // Transitable sin restricciones
    CONGESTIONADA, // Transitable pero con demora (penaliza el costo en Dijkstra)
    CORTADA,      // No transitable (obra, manifestacion, etc.)
    BLOQUEADA     // No transitable (emergencia activa en esa via)
}
