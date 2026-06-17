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

    public void generarSaltos(StringBuilder asm,
                              GeneradorAssemblerContext contexto,
                              String etiquetaVerdadero,
                              String etiquetaFalso) {
        if (izquierda != null && derecha != null) {
            generarSaltosComparacion(asm, contexto, etiquetaVerdadero, etiquetaFalso);
            return;
        }

        if ("AND".equals(operador)) {
            String etiquetaIntermedia = contexto.nuevaEtiqueta();
            izqCondicion.generarSaltos(asm, contexto, etiquetaIntermedia, etiquetaFalso);
            asm.append(etiquetaIntermedia).append(":\n");
            segundaCondicion.generarSaltos(asm, contexto, etiquetaVerdadero, etiquetaFalso);
            return;
        }

        if ("OR".equals(operador)) {
            String etiquetaIntermedia = contexto.nuevaEtiqueta();
            izqCondicion.generarSaltos(asm, contexto, etiquetaVerdadero, etiquetaIntermedia);
            asm.append(etiquetaIntermedia).append(":\n");
            segundaCondicion.generarSaltos(asm, contexto, etiquetaVerdadero, etiquetaFalso);
            return;
        }

        if ("NOT".equals(operador) && izqCondicion != null) {
            izqCondicion.generarSaltos(asm, contexto, etiquetaFalso, etiquetaVerdadero);
            return;
        }

        throw new UnsupportedOperationException(
            "Condicion no soportada para assembler: " + operador
        );
    }

    private void generarSaltosComparacion(StringBuilder asm,
                                          GeneradorAssemblerContext contexto,
                                          String etiquetaVerdadero,
                                          String etiquetaFalso) {
        String tipoIzquierdo = izquierda.getTipoSemantico();
        String tipoDerecho = derecha.getTipoSemantico();

        if ("STRING".equals(tipoIzquierdo) || "STRING".equals(tipoDerecho)) {
            generarSaltosComparacionString(asm, contexto, etiquetaVerdadero, etiquetaFalso);
            return;
        }

        String nombreIzquierda = izquierda.generarAssembler(asm, contexto);
        String nombreDerecha = derecha.generarAssembler(asm, contexto);

        asm.append("FLD ").append(nombreIzquierda).append("\n");
        asm.append("FCOMP ").append(nombreDerecha).append("\n");
        asm.append("FSTSW AX\n");
        asm.append("SAHF\n");
        asm.append(obtenerSaltoVerdaderoNumerico())
           .append(" ")
           .append(etiquetaVerdadero)
           .append("\n");
        asm.append("JMP ").append(etiquetaFalso).append("\n");
    }

    private void generarSaltosComparacionString(StringBuilder asm,
                                                GeneradorAssemblerContext contexto,
                                                String etiquetaVerdadero,
                                                String etiquetaFalso) {
        if (!"STRING".equals(izquierda.getTipoSemantico())
                || !"STRING".equals(derecha.getTipoSemantico())) {
            throw new UnsupportedOperationException(
                "No se puede comparar STRING con tipos no STRING"
            );
        }

        if (!"==".equals(operador) && !"!=".equals(operador)) {
            throw new UnsupportedOperationException(
                "Solo se soportan comparaciones == y != para STRING"
            );
        }

        String nombreIzquierda = izquierda.generarAssembler(asm, contexto);
        String nombreDerecha = derecha.generarAssembler(asm, contexto);
        String etiquetaLoop = contexto.nuevaEtiqueta();

        asm.append("LEA SI, ").append(nombreIzquierda).append("\n");
        asm.append("LEA DI, ").append(nombreDerecha).append("\n");
        asm.append(etiquetaLoop).append(":\n");
        asm.append("MOV AL, [SI]\n");
        asm.append("MOV BL, [DI]\n");
        asm.append("CMP AL, BL\n");

        if ("==".equals(operador)) {
            asm.append("JNE ").append(etiquetaFalso).append("\n");
            asm.append("CMP AL, '$'\n");
            asm.append("JE ").append(etiquetaVerdadero).append("\n");
        } else {
            asm.append("JNE ").append(etiquetaVerdadero).append("\n");
            asm.append("CMP AL, '$'\n");
            asm.append("JE ").append(etiquetaFalso).append("\n");
        }

        asm.append("INC SI\n");
        asm.append("INC DI\n");
        asm.append("JMP ").append(etiquetaLoop).append("\n");
    }

    private String obtenerSaltoVerdaderoNumerico() {
        switch (operador) {
            case "==":
                return "JE";
            case "!=":
                return "JNE";
            case "<":
                return "JB";
            case "<=":
                return "JBE";
            case ">":
                return "JA";
            case ">=":
                return "JAE";
            default:
                throw new UnsupportedOperationException(
                    "Comparador no soportado para assembler: " + operador
                );
        }
    }

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
