# Casos de prueba

- [Primer ejemplo – `PrimerEjemplo.CalculadoraDescuento`](#primer-ejemplo--primerejemplocalculadoradescuento)
- [Segundo ejemplo – `SegundoEjemplo.main.puedeMatricular`](#segundo-ejemplo--segundoejemplomainpuedematricular)

---

# Primer ejemplo – `PrimerEjemplo.CalculadoraDescuento`

## Especificación

- Un cliente **premium** recibe un descuento del **10%** cuando el monto de compra es **superior a 100$**.
- Para una compra **exactamente igual a 100$** no se aplica descuento.
- Los clientes **no premium** no reciben descuento.

Los casos se ejecutan con `src/PrimerEjemplo/CalculadoraDescuentoTest.java` (clase con `main`, sin dependencias externas). Los valores se comparan con una tolerancia de `1e-6`.

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

---

# Segundo ejemplo – `SegundoEjemplo.main.puedeMatricular`

## Requisitos

Tomados del comentario al final de `src/SegundoEjemplo/main.java`:

```java
/**
 * System requirements:
 * un estudiante puede matricular hasta 24 credigos
 * Los estudiantes del ultimo semestre pueden matricular hasta 27 creditos con autorizacion.
 */
```

Interpretación usada para diseñar los casos:

- Un estudiante puede matricular **hasta 24 créditos** (24 inclusive).
- Un estudiante de **último semestre** que además tenga **autorización** puede matricular **hasta 27 créditos** (27 inclusive).
- Si falta cualquiera de las dos condiciones (último semestre **o** autorización), el límite sigue siendo 24 créditos.

## Codigo

Función bajo prueba (`src/SegundoEjemplo/main.java`):

```java
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
```

Clase de prueba (mismo estilo que `CalculadoraDescuentoTest`, sin dependencias externas):

```java
package SegundoEjemplo;

public class MatriculaTest {
    private static int aprobados = 0;
    private static int fallidos = 0;

    public static void main(String[] args) {
        probar("CP-01", "Estudiante regular, justo en el límite", 24, false, false, true);
        probar("CP-02", "Estudiante regular, justo por encima del límite", 25, false, false, false);
        probar("CP-03", "Estudiante regular, justo por debajo del límite", 23, false, false, true);
        probar("CP-04", "Último semestre sin autorización, 24 créditos", 24, true, false, true);
        probar("CP-05", "Último semestre sin autorización, 25 créditos", 25, true, false, false);
        probar("CP-06", "Autorización sin ser último semestre, 25 créditos", 25, false, true, false);
        probar("CP-07", "Último semestre con autorización, 25 créditos", 25, true, true, true);
        probar("CP-08", "Último semestre con autorización, justo en el límite", 27, true, true, true);
        probar("CP-09", "Último semestre con autorización, justo por encima del límite", 28, true, true, false);
        probar("CP-10", "Último semestre con autorización, 24 créditos", 24, true, true, true);
        probar("CP-11", "Cero créditos", 0, false, false, true);
        probar("CP-12", "Créditos negativos (entrada inválida)", -5, false, false, true);

        System.out.println();
        System.out.println("Aprobados: " + aprobados + " | Fallidos: " + fallidos);
    }

    private static void probar(String id, String descripcion, int creditos, boolean ultimoSemestre,
                               boolean autorizacion, boolean esperado) {
        boolean obtenido = main.puedeMatricular(creditos, ultimoSemestre, autorizacion);
        boolean ok = obtenido == esperado;
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

Los casos combinan **valores límite** (23/24/25 y 26/27/28) con una **tabla de decisión** sobre las dos condiciones booleanas.

| ID | Descripción | Créditos | Último semestre | Autorización | Resultado esperado | Resultado obtenido | Estado |
|----|-------------|---------:|:---------------:|:------------:|:------------------:|:------------------:|:------:|
| CP-01 | Estudiante regular, justo en el límite | 24 | No | No | Puede | Puede | PASA |
| CP-02 | Estudiante regular, justo por encima del límite | 25 | No | No | No puede | No puede | PASA |
| CP-03 | Estudiante regular, justo por debajo del límite | 23 | No | No | Puede | Puede | PASA |
| CP-04 | Último semestre sin autorización, 24 créditos | 24 | Sí | No | Puede | Puede | PASA |
| CP-05 | Último semestre sin autorización, 25 créditos | 25 | Sí | No | No puede | No puede | PASA |
| CP-06 | Autorización sin ser último semestre, 25 créditos | 25 | No | Sí | No puede | No puede | PASA |
| CP-07 | Último semestre con autorización, 25 créditos (caso del `main`) | 25 | Sí | Sí | Puede | Puede | PASA |
| CP-08 | Último semestre con autorización, justo en el límite | 27 | Sí | Sí | Puede | Puede | PASA |
| CP-09 | Último semestre con autorización, justo por encima del límite | 28 | Sí | Sí | No puede | No puede | PASA |
| CP-10 | Último semestre con autorización, 24 créditos | 24 | Sí | Sí | Puede | Puede | PASA |
| CP-11 | Cero créditos | 0 | No | No | Puede | Puede | PASA |
| CP-12 | Créditos negativos (entrada inválida) | -5 | No | No | Puede | Puede | PASA |

**Resumen:** 12 aprobados, 0 fallidos.

## Defectos encontrados

No se encontraron defectos funcionales: la función cumple los dos requisitos en todos los valores límite y en todas las combinaciones de último semestre y autorización.

## Observaciones

- **CP-12**: los requisitos no definen el comportamiento para un número de créditos negativo. La función lo acepta sin validar y devuelve `true` (puede matricular). Se recomienda definir si debe rechazarse (por ejemplo, lanzando `IllegalArgumentException` cuando `numeroCreditos < 0`).
- **CP-11**: tampoco se especifica si matricular 0 créditos es válido; la función lo permite.
- **Requisitos**: el texto contiene un error tipográfico ("credigos" en lugar de "créditos") y no aclara explícitamente que un estudiante de último semestre **sin** autorización conserva el límite de 24 créditos; se asumió que sí.
- **Calidad de código**: la clase se llama `main` (en minúscula), lo que va contra la convención de Java de nombrar clases en `PascalCase` y se confunde con el método `main`. Se recomienda renombrarla (por ejemplo, `Matricula`).
