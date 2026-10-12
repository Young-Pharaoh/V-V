package SegundoEjemplo;

public class main {
    public static boolean puedeMatricular(int numeroCreditos, boolean ultimoSemestre, boolean autorizacion) {
        if (ultimoSemestre && autorizacion) {
            return numeroCreditos <= 27;
        }
        return numeroCreditos <= 24;
    }
    public static void main(String[] args) {
        int numeroCreditos = 25;
        boolean ultimoSemestre = true;
        boolean autorizacion = true;

        if (puedeMatricular(numeroCreditos, ultimoSemestre, autorizacion)) {
            System.out.println("El estudiante puede matricularse.");
        } else {
            System.out.println("El estudiante no puede matricularse.");
        }
    }
}

/**
 * System requirements:
 * un estudiante puede matricular hasta 24 credigos
 * Los estudiantes del ultimo semestre pueden matricular hasta 27 creditos con autorizacion.
 */
