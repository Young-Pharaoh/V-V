# Casos de prueba – `PrimerEjemplo.CalculadoraDescuento`

## Especificación

- Un cliente **premium** recibe un descuento del **10%** cuando el monto de compra es **superior a 100$**.
- Para una compra **exactamente igual a 100$** no se aplica descuento.
- Los clientes **no premium** no reciben descuento.

Los casos se ejecutan con `src/CalculadoraDescuentoTest.java` (clase con `main`, sin dependencias externas). Los valores se comparan con una tolerancia de `1e-6`.

## Codigo
```java
public class CalculadoraDescuento {
    public static double calcularTotal(double compra, boolean clientePremium) {
        double descuento = 0;
        if (clientePremium && compra >= 100) {
            descuento = 0.05;
        }
        return compra - descuento;
    }
}

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

```
## Tabla de casos

| ID | Descripción | Compra ($) | Premium | Resultado esperado | Resultado obtenido | Estado |
|----|-------------|-----------:|:-------:|-------------------:|-------------------:|:------:|
| CP-01 | Compra por debajo del límite | 80 | Sí | 80 | 80 | PASA |
| CP-02 | Compra exactamente en el límite | 100 | Sí | 100 | 99.95 | FALLA |
| CP-03 | Compra por encima del límite | 120 | Sí | 108 | 119.95 | FALLA |
| CP-04 | Cliente no premium, compra por encima del límite | 120 | No | 120 | 120 | PASA |
| CP-05 | Compra justo por encima del límite | 100.01 | Sí | 90.009 | 99.96 | FALLA |
| CP-06 | Compra justo por debajo del límite | 99.99 | Sí | 99.99 | 99.99 | PASA |
| CP-07 | Compra de 101 | 101 | Sí | 90.9 | 100.95 | FALLA |
| CP-08 | Compra típica de 200 | 200 | Sí | 180 | 199.95 | FALLA |
| CP-09 | Cliente no premium, compra de 200 | 200 | No | 200 | 200 | PASA |
| CP-10 | Cliente no premium, compra en el límite | 100 | No | 100 | 100 | PASA |
| CP-11 | Cliente no premium, justo por encima del límite | 100.01 | No | 100.01 | 100.01 | PASA |
| CP-12 | Compra de 0, premium | 0 | Sí | 0 | 0 | PASA |
| CP-13 | Compra de 0, no premium | 0 | No | 0 | 0 | PASA |
| CP-14 | Compra muy grande | 1 000 000 | Sí | 900 000 | 999 999.95 | FALLA |
| CP-15 | Compra negativa (entrada inválida) | -50 | Sí | -50 | -50 | PASA |

**Resumen:** 9 aprobados, 6 fallidos.

## Defectos encontrados

1. **Descuento como valor fijo en lugar de porcentaje** (CP-03, CP-05, CP-07, CP-08, CP-14): el código resta `0.05` al total (`compra - descuento`) en vez de aplicar un porcentaje (`compra * (1 - descuento)`).
2. **Porcentaje incorrecto**: el código usa `0.05` (5%) y la especificación indica 10% (`0.10`).
3. **Condición de límite incorrecta** (CP-02): el código usa `compra >= 100`, por lo que aplica descuento a una compra de exactamente 100$; la especificación exige `compra > 100`.

## Observaciones

- **CP-15**: la especificación no define el comportamiento para montos negativos. La función los acepta sin validar y los devuelve sin cambios. Se recomienda definir si deben rechazarse (por ejemplo, lanzando `IllegalArgumentException`).
