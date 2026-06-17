package com.mycompany.analizadorlexico.ast;

public class NodoAsignacion extends NodoSentencia {
    private final NodoVariable variable;
    private final NodoExpresion expresion;

    public NodoAsignacion(NodoVariable variable, NodoExpresion expresion) {
        super("ASIG");
        this.variable = variable;
        this.expresion = expresion;
    }

    public NodoVariable getVariable() { return variable; }
    public NodoExpresion getExpresion() { return expresion; }

    @Override
    protected String graficar(String idPadre) {
        final String miId = "nodo_asig_" + System.identityHashCode(this);
        StringBuilder resultado = new StringBuilder();
        //resultado.append(this.variable.graficar(miId));
        resultado.append(this.expresion.graficarConVariable(this.variable, miId));
        resultado.append(miId + " [label=\"=\"]\n");
        if (idPadre != null) {
            resultado.append(idPadre + " -- " + miId + "\n");
        }
        return resultado.toString();
    }

    @Override
    public void generarAssembler(StringBuilder asm, GeneradorAssemblerContext contexto) {
        try {
            if ("STRING".equals(expresion.getTipoSemantico())) {
                asm.append("; TODO asignacion STRING no soportada todavia: ")
                   .append(variable.getNombre())
                   .append("\n");
                return;
            }

            String resultadoExpresion = expresion.generarAssembler(asm, contexto);
            asm.append("FLD ").append(resultadoExpresion).append("\n");
            asm.append("FSTP ").append(variable.getNombre()).append("\n");
        } catch (UnsupportedOperationException ex) {
            asm.append("; ").append(ex.getMessage()).append("\n");
        }
    }
}
