package com.mycompany.analizadorlexico.ast;

public class NodoCondicion extends Nodo {
    // Para comparaciones simples: expr OP expr
    private final NodoExpresion izquierda;
    private final NodoExpresion derecha;

    // Para condiciones compuestas: cond AND/OR/NOT cond
    private final NodoCondicion izqCondicion;
    private final NodoCondicion segundaCondicion;

    private final String operador;

    // expr OP expr
    public NodoCondicion(NodoExpresion izquierda, String operador, NodoExpresion derecha) {
        super("COND");
        this.izquierda     = izquierda;
        this.operador      = operador;
        this.derecha       = derecha;
        this.izqCondicion  = null;
        this.segundaCondicion = null;
    }

    // cond AND/OR cond
    public NodoCondicion(String operador, NodoCondicion izqCond, NodoCondicion derCond) {
        super("COND");
        this.izquierda     = null;
        this.derecha       = null;
        this.operador      = operador;
        this.izqCondicion  = izqCond;
        this.segundaCondicion = derCond;
    }

    // NOT cond
    public NodoCondicion(String operador, NodoCondicion cond) {
        super("COND");
        this.izquierda     = null;
        this.derecha       = null;
        this.operador      = operador;
        this.izqCondicion  = cond;
        this.segundaCondicion = null;
    }

    public NodoExpresion getIzquierda() { return izquierda; }
    public NodoExpresion getDerecha()   { return derecha; }
    public String getOperador()         { return operador; }

    public String graficarRotado(String idPadre) {
        String operadorInvertido = invertirOperador(operador);
        NodoCondicion nodoRotado;

        if (izquierda != null || derecha != null) {
            nodoRotado = new NodoCondicion(izquierda, operadorInvertido, derecha);
        } else if (segundaCondicion != null) {
            nodoRotado = new NodoCondicion(operadorInvertido, izqCondicion, segundaCondicion);
        } else if (izqCondicion != null) {
            nodoRotado = new NodoCondicion(operadorInvertido, izqCondicion);
        } else {
            return graficar(idPadre);
        }

        return nodoRotado.graficar(idPadre);
    }

    private String invertirOperador(String op) {
        switch (op) {
            case "!=":
                return "==";
            case "==":
                return "!=";
            case ">=":
                return "<";
            case "<":
                return ">=";
            case "<=":
                return ">";
            case ">":
                return "<=";
            case "AND":
                return "OR";
            case "OR":
                return "AND";
            default:
                return op;
        }
    }

    public String generarSaltoFalso(java.io.PrintWriter pw, com.mycompany.analizadorlexico.GeneradorCodigo gc) {
        // Caso de comparación simple: expr OP expr
        if (this.izquierda != null && this.derecha != null) {
            // 1. Resolvemos los subárboles de las expresiones izquierda y derecha
            String idIzq = this.izquierda.generarASM(pw, gc);
            String idDer = this.derecha.generarASM(pw, gc);

            // 2. Cargamos y comparamos usando la FPU siguiendo las pautas de clase
            pw.println("  fld " + idIzq + "\t\t; Carga operando izquierdo de la condicion");
            pw.println("  fld " + idDer + "\t\t; Carga operando derecho de la condicion");
            pw.println("  fxch\t\t\t; Intercambia para comparar ST(1) con ST(0)");
            pw.println("  fcom\t\t\t; Compara los dos reales en la FPU");
            pw.println("  fstsw ax\t\t; Copia los bits de estado de la FPU a AX");
            pw.println("  sahf\t\t\t; Traslada los bits a FLAGS de la CPU");
            pw.println("  ffree\t\t\t; Libera espacio de la pila FPU");

            // 3. Mapeamos cuál es el salto que esquiva el bloque (SALTO POR FALSO)
            switch (this.operador) {
                case "==":
                    return "jne"; // Si se pedía igual, salta si es Distinto
                case "!=":
                    return "je";  // Si se pedía distinto, salta si es Igual
                case ">":
                    return "jna"; // Si se pedía mayor, salta si es Menor o Igual (JNA/JBE)
                case ">=":
                    return "jnbe"; // Si se pedía mayor o igual, salta si es Menor (JB/JNAE)
                case "<":
                    return "jae"; // Si se pedía menor, salta si es Mayor o Igual (JAE/JNB)
                case "<=":
                    return "ja";  // Si se pedía menor o igual, salta si es Mayor (JA/JNBE)
                default:
                    return "jmp";
            }
        }
        
        // Espacio para lógica compuesta (AND/OR/NOT) si tu compilador lo requiere más adelante.
        return "jmp";
    }

    @Override
    protected String graficar(String idPadre) {
        final String miId = "nodo_cond_" + System.identityHashCode(this);
        StringBuilder resultado = new StringBuilder();

        resultado.append(miId + " [label=\"" + operador + "\"]\n");
        if (idPadre != null) {
            resultado.append(idPadre + " -- " + miId + "\n");
        }

        // Condición simple
        if (izquierda != null) resultado.append(izquierda.graficar(miId));
        if (derecha   != null) resultado.append(derecha.graficar(miId));

        // Condición compuesta
        if (izqCondicion     != null) resultado.append(izqCondicion.graficar(miId));
        if (segundaCondicion != null) resultado.append(segundaCondicion.graficar(miId));

        return resultado.toString();
    }
}
