package com.mycompany.analizadorlexico.ast;

import java.util.List;

public class NodoRepeat extends NodoSentencia {
    private final List<NodoSentencia> sentencias;
    private final NodoCondicion condicion;

    public NodoRepeat(List<NodoSentencia> sentencias, NodoCondicion condicion) {
        super("REPEAT");
        this.sentencias = sentencias;
        this.condicion = condicion;
    }

    @Override
    protected String graficar(String idPadre) {
        final String miId = "nodo_repeat_" + System.identityHashCode(this);
        StringBuilder resultado = new StringBuilder();
        resultado.append(miId + " [label=\"REPEAT\"]\n");
        resultado.append(this.condicion.graficarRotado(miId));
        if (idPadre != null) {
            resultado.append(idPadre + " -- " + miId + "\n");
        }

        for (NodoSentencia s : sentencias) {
            resultado.append(s.graficar(miId));
        }

        return resultado.toString();
    }

    @Override
    public void generarAssembler(StringBuilder asm, GeneradorAssemblerContext contexto) {
        try {
            String etiquetaInicio = contexto.nuevaEtiqueta();
            String etiquetaFin = contexto.nuevaEtiqueta();

            asm.append(etiquetaInicio).append(":\n");
            for (NodoSentencia sentencia : sentencias) {
                sentencia.generarAssembler(asm, contexto);
            }
            condicion.generarSaltos(asm, contexto, etiquetaFin, etiquetaInicio);
            asm.append(etiquetaFin).append(":\n");
        } catch (UnsupportedOperationException ex) {
            asm.append("; ").append(ex.getMessage()).append("\n");
        }
    }
}

