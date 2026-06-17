package com.mycompany.analizadorlexico.ast;

import java.util.List;

public class NodoIf extends NodoSentencia {
    private final NodoCondicion condicion;
    private final List<NodoSentencia> sentenciasThen;
    private final List<NodoSentencia> sentenciasElse; // puede ser null

    public NodoIf(NodoCondicion condicion,
                  List<NodoSentencia> sentenciasThen,
                  List<NodoSentencia> sentenciasElse) {
        super("IF");
        this.condicion = condicion;
        this.sentenciasThen = sentenciasThen;
        this.sentenciasElse = sentenciasElse;
    }

    @Override
    protected String graficar(String idPadre) {
        final String miId = this.getIdNodo();
        StringBuilder resultado = new StringBuilder();

        // Grafica el nodo IF
        resultado.append(super.graficar(idPadre));

        // Grafica la condición colgando directamente del nodo IF
        resultado.append(condicion.graficar(miId));

        // Agrega un nodo ficticio THEN colgando del nodo IF
        Nodo nodoThen = new Nodo("Then");
        resultado.append(nodoThen.graficar(miId));

        // Grafica las sentencias asociadas al "then" colgando del nodo ficticio THEN
        String idNodoThen = nodoThen.getIdNodo();
        for (NodoSentencia sentencia: sentenciasThen) {
            resultado.append(sentencia.graficar(idNodoThen));
        }

        // Si hay sentencias asociadas al "else"...
        if (sentenciasElse != null) {
            // Agrega un nodo ficticio "ELSE" colgando del nodo IF
            Nodo nodoElse = new Nodo("Else");
            resultado.append(nodoElse.graficar(miId));

            // Grafica las sentencias asociadas al "else" colgando del nodo ficticio ELSE
            String idNodoElse = nodoElse.getIdNodo();
            for (NodoSentencia sentencia: sentenciasElse) {
                resultado.append(sentencia.graficar(idNodoElse));
            }
        }

        return resultado.toString();
    }

    @Override
    public void generarAssembler(StringBuilder asm, GeneradorAssemblerContext contexto) {
        try {
            String etiquetaThen = contexto.nuevaEtiqueta();
            String etiquetaFin = contexto.nuevaEtiqueta();

            if (sentenciasElse == null || sentenciasElse.isEmpty()) {
                condicion.generarSaltos(asm, contexto, etiquetaThen, etiquetaFin);
                asm.append(etiquetaThen).append(":\n");
                for (NodoSentencia sentencia : sentenciasThen) {
                    sentencia.generarAssembler(asm, contexto);
                }
                asm.append(etiquetaFin).append(":\n");
                return;
            }

            String etiquetaElse = contexto.nuevaEtiqueta();
            condicion.generarSaltos(asm, contexto, etiquetaThen, etiquetaElse);
            asm.append(etiquetaThen).append(":\n");
            for (NodoSentencia sentencia : sentenciasThen) {
                sentencia.generarAssembler(asm, contexto);
            }
            asm.append("JMP ").append(etiquetaFin).append("\n");
            asm.append(etiquetaElse).append(":\n");
            for (NodoSentencia sentencia : sentenciasElse) {
                sentencia.generarAssembler(asm, contexto);
            }
            asm.append(etiquetaFin).append(":\n");
        } catch (UnsupportedOperationException ex) {
            asm.append("; ").append(ex.getMessage()).append("\n");
        }
    }
}
