package com.mycompany.analizadorlexico.ast;

import java.util.List;
import java.util.ArrayList;
/**
 * Representa la función especial #Iguales del Grupo 1.
 *
 * Sintaxis:
 *   #Iguales(expresión_pivot, [lista1], [lista2], ..., [listaN])
 *
 * Semántica: devuelve la cantidad de elementos iguales al pivot
 * encontrados en todas las listas combinadas. Si una lista es vacía,
 * contribuye con cero.
 *
 * Ejemplo:
 *   result := #Iguales(a+w/b, [(d-3)*2, e, f])
 */
public class NodoIguales extends NodoExpresion {

    private final NodoExpresion pivot;
    private final NodoVariable comparador;
    private final NodoVariable contador;

    private final ArrayList<ArrayList<NodoExpresion>> listas;

    public NodoIguales(NodoExpresion pivot, ArrayList<ArrayList<NodoExpresion>> listas) {
        super("IGUALES");
        this.pivot = pivot;
        this.listas = listas;
        this.comparador = new NodoVariable("comparador");
        this.contador = new NodoVariable("contador");
        comparador.setTipoSemantico("INT");
        contador.setTipoSemantico("INT");
    }

    public NodoExpresion getPivot() {
        return pivot;
    }

    public ArrayList<ArrayList<NodoExpresion>> getListas() {
        return listas;
    }

    @Override
    public String getTipoSemantico() { return "INT"; }

    @Override
    public String generarAssembler(StringBuilder asm, GeneradorAssemblerContext contexto) {
        String tipoPivot = pivot.getTipoSemantico();
        if (tipoPivot == null) {
            throw new UnsupportedOperationException(
                "#Iguales no puede evaluarse sin tipo semantico para el pivote"
            );
        }

        // El pivote se evalua una sola vez y se reutiliza para todas las comparaciones.
        String nombrePivot = pivot.generarAssembler(asm, contexto);

        // El contador vive en un temporal para que el resultado pueda usarse como expresion.
        String temporalContador = contexto.nuevoTemporal("INT");
        asm.append("FLDZ\n");
        asm.append("FSTP ").append(temporalContador).append("\n");

        for (ArrayList<NodoExpresion> lista : listas) {
            for (NodoExpresion elemento : lista) {
                validarComparacion(tipoPivot, elemento.getTipoSemantico());

                String etiquetaIgual = contexto.nuevaEtiqueta();
                String etiquetaFinComparacion = contexto.nuevaEtiqueta();

                // Reutilizamos la misma logica de comparacion que ya usan IF y REPEAT.
                NodoVariable refPivot = new NodoVariable(nombrePivot);
                refPivot.setTipoSemantico(tipoPivot);
                NodoCondicion comparacion = new NodoCondicion(refPivot, "==", elemento);

                comparacion.generarSaltos(asm, contexto, etiquetaIgual, etiquetaFinComparacion);
                asm.append(etiquetaIgual).append(":\n");
                asm.append("FLD1\n");
                asm.append("FADD ").append(temporalContador).append("\n");
                asm.append("FSTP ").append(temporalContador).append("\n");
                asm.append(etiquetaFinComparacion).append(":\n");
            }
        }

        return temporalContador;
    }

    @Override
    public String graficarConVariable (NodoVariable variable, String id) {
        StringBuilder resultado = this.graficarSentencias();
        resultado.append(variable.graficar(id));
        resultado.append(this.graficarContador(id));
        return resultado.toString();
    }

    @Override
    protected String graficar(String idPadre) {
        StringBuilder resultado = this.graficarSentencias();
        resultado.append(this.graficarContador(idPadre));

        return resultado.toString();
    }

    private StringBuilder graficarSentencias () {
        String idPrograma = "nodo_programa";
        //final String miId = "nodo_iguales_" + System.identityHashCode(this);
        

        //resultado.append(miId + " [label=\"#Iguales\"]\n");
        //if (idPadre != null) {
        //    resultado.append(idPadre + " -- " + miId + "\n");
        //}

        StringBuilder resultado = new StringBuilder();
        // comp = pivot  → NodoAsignacion
        NodoAsignacion asigComp = new NodoAsignacion(comparador, pivot);
        resultado.append(asigComp.graficar(idPrograma));

        // contador = 0  → NodoAsignacion
        NodoConstanteInt cero = new NodoConstanteInt(0);
        NodoAsignacion asigContador = new NodoAsignacion(contador, cero);
        resultado.append(asigContador.graficar(idPrograma));

        // IF por cada elemento de cada lista
        for (List<NodoExpresion> lista : listas) {
            for (NodoExpresion elemento : lista) {
                
                // Condicion: comp == elemento  → NodoCondicion
                NodoVariable comp = new NodoVariable("comparador");
                comp.setTipoSemantico("INT");
                NodoCondicion condicion = new NodoCondicion(comp, "==", elemento);
                
                NodoConstanteInt uno = new NodoConstanteInt(1);
                // contador = contador + 1  → NodoAsignacion
                NodoVariable contRef1 = new NodoVariable("contador");
                contRef1.setTipoSemantico("INT");
                NodoVariable contRef2 = new NodoVariable("contador");
                contRef2.setTipoSemantico("INT");
                NodoExpresionBinaria suma = new NodoExpresionBinaria(contRef1, "+", uno);
                NodoAsignacion asigInc = new NodoAsignacion(contRef2, suma);

                // Then: lista con la asignacion
                List<NodoSentencia> sentenciasThen = new ArrayList<>();
                sentenciasThen.add(asigInc);

                // IF → NodoIf
                NodoIf ifNodo = new NodoIf(condicion, sentenciasThen, null);
                resultado.append(ifNodo.graficar(idPrograma));
            }
        }
        return resultado;
    }

    private String graficarContador(String idPadre) {
        NodoVariable contador = new NodoVariable("contador");
        contador.setTipoSemantico("INT");
        return contador.graficar(idPadre);
    }

    private void validarComparacion(String tipoPivot, String tipoElemento) {
        if (tipoElemento == null) {
            throw new UnsupportedOperationException(
                "#Iguales no puede comparar un elemento sin tipo semantico"
            );
        }

        if ("STRING".equals(tipoPivot) || "STRING".equals(tipoElemento)) {
            if (!"STRING".equals(tipoPivot) || !"STRING".equals(tipoElemento)) {
                throw new UnsupportedOperationException(
                    "#Iguales no puede mezclar STRING con tipos numericos"
                );
            }
            return;
        }

        if (!"INT".equals(tipoPivot) && !"FLOAT".equals(tipoPivot)) {
            throw new UnsupportedOperationException(
                "#Iguales no soporta pivotes del tipo " + tipoPivot
            );
        }

        if (!"INT".equals(tipoElemento) && !"FLOAT".equals(tipoElemento)) {
            throw new UnsupportedOperationException(
                "#Iguales no soporta elementos del tipo " + tipoElemento
            );
        }
    }
}
