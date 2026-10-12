package PrimerEjemplo;

public class CalculadoraDescuentoTest {
    private static final double TOLERANCIA = 1e-6;
    private static int aprobados = 0;
    private static int fallidos = 0;

    public static void main(String[] args) {
        probar("CP-01", "Premium, compra de 80", 80, true, 80);
        probar("CP-02", "Premium, compra exactamente 100 (límite)", 100, true, 100);
        probar("CP-03", "Premium, compra de 120", 120, true, 108);
        probar("CP-04", "No premium, compra de 120", 120, false, 120);
        probar("CP-05", "Premium, compra justo por encima del límite", 100.01, true, 90.009);
        probar("CP-06", "Premium, compra justo por debajo del límite", 99.99, true, 99.99);
        probar("CP-07", "Premium, compra de 101", 101, true, 90.9);
        probar("CP-08", "Premium, compra típica de 200", 200, true, 180);
        probar("CP-09", "No premium, compra de 200", 200, false, 200);
        probar("CP-10", "No premium, compra exactamente 100", 100, false, 100);
        probar("CP-11", "No premium, compra justo por encima del límite", 100.01, false, 100.01);
        probar("CP-12", "Premium, compra de 0", 0, true, 0);
        probar("CP-13", "No premium, compra de 0", 0, false, 0);
        probar("CP-14", "Premium, compra muy grande", 1_000_000, true, 900_000);
        probar("CP-15", "Premium, compra negativa (entrada inválida)", -50, true, -50);

        System.out.println();
        System.out.println("Aprobados: " + aprobados + " | Fallidos: " + fallidos);
    }

    private static void probar(String id, String descripcion, double compra, boolean premium, double esperado) {
        double obtenido = CalculadoraDescuento.calcularTotal(compra, premium);
        boolean ok = Math.abs(obtenido - esperado) < TOLERANCIA;
        if (ok) {
            aprobados++;
        } else {
            fallidos++;
        }
        System.out.printf("%s [%s] %s -> esperado: %s, obtenido: %s%n",
                id, ok ? "PASA" : "FALLA", descripcion, esperado, obtenido);
    }
}
