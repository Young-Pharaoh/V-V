import PrimerEjemplo.CalculadoraDescuento;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
void main() {
    double compra = 100;
    boolean clientePremium = true;
    double total = CalculadoraDescuento.calcularTotal(compra, clientePremium);
    System.out.println("El total de compra antes del descuento es: " + compra);
    System.out.println("El cliente es premium: " + clientePremium);
    System.out.println("El descuento aplicado es: " + (compra - total));
    System.out.println("El total de la compra es: " + total);


}

/**
 * System requirements:
 * un estudiante puede matricular hasta 24 credigos
 */
