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
        
        // =====================================================================
        // CASO 1: COMPARACIÓN SIMPLE (expr OP expr)
        // =====================================================================
        if (this.izquierda != null && this.derecha != null) {
            // 1. Resolvemos recursivamente los subárboles de las expresiones
            String idIzq = this.izquierda.generarASM(pw, gc);
            String idDer = this.derecha.generarASM(pw, gc);

            // 2. Cargamos y comparamos en la FPU
            pw.println("  fld " + idIzq + "\t\t; Carga operando izquierdo");
            pw.println("  fld " + idDer + "\t\t; Carga operando derecho");
            pw.println("  fxch\t\t\t; Intercambia ST(0) y ST(1) para restar correcto");
            pw.println("  fcom\t\t\t; Compara reales en FPU");
            pw.println("  fstsw ax\t\t; Copia registro de estado de FPU a AX");
            pw.println("  sahf\t\t\t; Mueve los bits de AX a FLAGS de CPU");
            pw.println("  ffree ST(0)\t\t; Libera espacio FPU");
            pw.println("  ffree ST(1)\t\t; Libera espacio FPU");

            // 3. Retornamos el operador de salto por FALSO (Inverso al pedido)
            switch (this.operador) {
                case "==": return "jne";  // Si pide igual, salta si es Distinto
                case "!=": return "je";   // Si pide distinto, salta si es Igual
                case ">":  return "jbe";  // Si pide mayor, salta si es Menor o Igual
                case ">=": return "jb";   // Si pide mayor o igual, salta si es Menor
                case "<":  return "jae";  // Si pide menor, salta si es Mayor o Igual
                case "<=": return "ja";   // Si pide menor o igual, salta si es Mayor
                default:   return "jmp";
            }
        }
        
        // =====================================================================
        // CASO 2: LÓGICA COMPUESTA CON CORTOCIRCUITO (AND / OR / NOT)
        // =====================================================================
        
        // CASO NOT: Invierte el salto lógico
        if ("NOT".equals(this.operador) && this.izqCondicion != null) {
            pw.println("; --- EVALUACION DE NOT ---");
            String saltoInterno = this.izqCondicion.generarSaltoFalso(pw, gc);
            
            // Invertimos el tipo de salto condicional resultante
            switch (saltoInterno) {
                case "jne":  return "je";
                case "je":   return "jne";
                case "jna":  return "ja";
                case "jnbe": return "jae";
                case "jae":  return "jnbe";
                case "ja":   return "jna";
                default:     return "jmp";
            }
        }

        // CASO AND: Si el primero es falso, salta directo por falso (Cortocircuito)
        if ("AND".equals(this.operador) && this.izqCondicion != null && this.segundaCondicion != null) {
            pw.println("; --- EVALUACION AND (PARTE 1) ---");
            String saltoFalso1 = this.izqCondicion.generarSaltoFalso(pw, gc);
            
            // Inventamos una etiqueta dinámica para este bloque IF que maneja el Generador
            String etiquetaDestinoFalso = gc.nuevoAuxiliar().replace("@aux", "Etiq_Falso_And_");
            pw.println("  " + saltoFalso1 + " " + etiquetaDestinoFalso);
            
            pw.println("; --- EVALUACION AND (PARTE 2) ---");
            String saltoFalso2 = this.segundaCondicion.generarSaltoFalso(pw, gc);
            pw.println("  " + saltoFalso2 + " " + etiquetaDestinoFalso);
            
            // Le avisamos al bloque IF contenedor que estampe la etiqueta donde caen los fallos
            pw.println(etiquetaDestinoFalso + ":");
            return "nop\t\t\t; Punto de salida del falso cortocircuito";
        }

        // CASO OR: Si el primero es verdadero, saltea el segundo y va al Then
        if ("OR".equals(this.operador) && this.izqCondicion != null && this.segundaCondicion != null) {
            String etiquetaThen = gc.nuevoAuxiliar().replace("@aux", "Etiq_True_Or_");
            String etiquetaSiguiente = gc.nuevoAuxiliar().replace("@aux", "Etiq_Sig_Or_");

            pw.println("; --- EVALUACION OR (PARTE 1) ---");
            String saltoFalso1 = this.izqCondicion.generarSaltoFalso(pw, gc);
            
            // Si NO salta por falso, significa que es VERDADERO -> Vamos directo al THEN
            // Invertimos el salto para ir al True directo
            String saltoVerdadero1 = invertirSalto(saltoFalso1);
            pw.println("  " + saltoVerdadero1 + " " + etiquetaThen);
            
            // Si falló el primero, evaluamos la segunda condición
            pw.println("; --- EVALUACION OR (PARTE 2) ---");
            String saltoFalso2 = this.segundaCondicion.generarSaltoFalso(pw, gc);
            pw.println("  " + saltoFalso2 + " " + etiquetaSiguiente);
            
            // Estampamos las salidas lógicas controladas del cortocircuito
            pw.println("  jmp " + etiquetaThen);
            pw.println(etiquetaSiguiente + ":");
            
            // Si llega acá, todo el OR falló, por ende debe saltar por falso
            return "jmp"; 
        }

        return "jmp";
    }

    // Método utilitario interno para invertir los saltos en cortocircuito del OR
    private String invertirSalto(String saltoFalso) {
        switch (saltoFalso) {
            case "jne":  return "je";
            case "je":   return "jne";
            case "jna":  return "ja";
            case "jnbe": return "jae";
            case "jae":  return "jnbe";
            case "ja":   return "jna";
            default:     return "je";
        }
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
