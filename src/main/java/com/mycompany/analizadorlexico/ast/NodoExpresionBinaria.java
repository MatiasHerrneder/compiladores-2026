package com.mycompany.analizadorlexico.ast;

public class NodoExpresionBinaria extends NodoExpresion {
    private final NodoExpresion izquierda;
    private final String operador;
    private final NodoExpresion derecha;

    public NodoExpresionBinaria(NodoExpresion izquierda, String operador, NodoExpresion derecha) {
        super("EXP_BIN");
        this.izquierda = izquierda;
        this.operador = operador;
        this.derecha = derecha;
    }

    @Override
    public String getTipoSemantico() {
        String ti = izquierda.getTipoSemantico();
        String td = derecha.getTipoSemantico();
        if (ti == null || td == null) return null;
        if (ti.equals("FLOAT") || td.equals("FLOAT")) return "FLOAT";
        return "INT";
    }

    public NodoExpresion getIzquierda() {
        return izquierda;
    }

    public String getOperador() {
        return operador;
    }

    public NodoExpresion getDerecha() {
        return derecha;
    }

    @Override
    public String generarAssembler(StringBuilder asm, GeneradorAssemblerContext contexto) {
        if ("STRING".equals(getTipoSemantico())) {
            throw new UnsupportedOperationException("No se soportan operaciones aritmeticas sobre STRING");
        }

        String nombreIzquierda = izquierda.generarAssembler(asm, contexto);
        String nombreDerecha = derecha.generarAssembler(asm, contexto);
        String temporal = contexto.nuevoTemporal(getTipoSemantico());

        asm.append("FLD ").append(nombreIzquierda).append("\n");

        switch (operador) {
            case "+":
                asm.append("FADD ").append(nombreDerecha).append("\n");
                break;
            case "-":
                asm.append("FSUB ").append(nombreDerecha).append("\n");
                break;
            case "*":
                asm.append("FMUL ").append(nombreDerecha).append("\n");
                break;
            case "/":
                asm.append("FDIV ").append(nombreDerecha).append("\n");
                break;
            default:
                throw new UnsupportedOperationException("Operador no soportado: " + operador);
        }

        asm.append("FSTP ").append(temporal).append("\n");
        return temporal;
    }

    @Override
    protected String graficar(String idPadre) {
        final String miId = "nodo_expbin_" + System.identityHashCode(this);
        StringBuilder resultado = new StringBuilder();
        resultado.append(miId + " [label=\"" + operador + "\"]\n");
        if (idPadre != null) resultado.append(idPadre + " -- " + miId + "\n");
        resultado.append(izquierda.graficar(miId));
        resultado.append(derecha.graficar(miId));
        return resultado.toString();
    }
}
