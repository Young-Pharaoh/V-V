package PrimerEjemplo;

/**
 * un clinete premimun recive un descuentodel 10% cuand el monto de compra es superrior a 100$
 * para una compre exactamente igual a 100$ no se aplica descuento
 * Los clientes no premium no reciben descuento
 */

public class CalculadoraDescuento {
    public static double calcularTotal(double compra, boolean clientePremium) {
        double descuento = 0;
        if (clientePremium && compra >= 100) {
            descuento = 0.05;
        }
        return compra - descuento;
    }
}