package com.mycompany.analizadorlexico.ast;

import com.mycompany.analizadorlexico.TablaSimbolos;

public class GeneradorAssemblerContext {
    private final TablaSimbolos tablaSimbolos;
    private int contadorTemporales = 0;
    private int contadorEtiquetas = 0;

    public GeneradorAssemblerContext(TablaSimbolos tablaSimbolos) {
        this.tablaSimbolos = tablaSimbolos;
    }

    public TablaSimbolos getTablaSimbolos() {
        return tablaSimbolos;
    }

    public String nuevoTemporal(String tipo) {
        String nombre = "_aux_" + (++contadorTemporales);
        tablaSimbolos.agregarSimbolo(nombre, "ASM_AUX", tipo != null ? tipo : "FLOAT");
        return nombre;
    }

    public String nuevaEtiqueta() {
        return "ETIQ_" + (++contadorEtiquetas);
    }

    public String nombreConstanteInt(int valor) {
        return "cte_int_" + normalizar(String.valueOf(valor));
    }

    public String nombreConstanteFloat(float valor) {
        return "cte_float_" + normalizar(Float.toString(valor));
    }

    public String nombreConstanteHex(String valorHex) {
        return "cte_hex_" + normalizar(valorHex);
    }

    public String nombreConstanteString(String valor) {
        return "cte_str_" + Integer.toUnsignedString(valor.hashCode());
    }

    public String normalizar(String valor) {
        String base = valor.replace('-', 'm').replace('.', '_');
        StringBuilder resultado = new StringBuilder();

        for (int i = 0; i < base.length(); i++) {
            char actual = base.charAt(i);
            if (Character.isLetterOrDigit(actual) || actual == '_') {
                resultado.append(actual);
            } else {
                resultado.append('_');
            }
        }

        if (resultado.isEmpty()) {
            resultado.append("valor");
        }

        return resultado.toString();
    }
}
