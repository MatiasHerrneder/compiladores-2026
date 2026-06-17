package com.mycompany.analizadorlexico;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

import com.mycompany.analizadorlexico.ast.*;

public class GeneradorCodigo {
    private NodoPrograma ast;
    private TablaSimbolos tablaSimbolos; 
    private int contadorAuxiliares = 0;

    public GeneradorCodigo(NodoPrograma ast, TablaSimbolos tablaSimbolos) {
        this.ast = ast;
        this.tablaSimbolos = tablaSimbolos;
    }

    public void generar(String nombreArchivo) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(nombreArchivo))) {
            
            // 1. Buffer temporal para las instrucciones .CODE
            java.io.StringWriter swCode = new java.io.StringWriter();
            PrintWriter pwCode = new PrintWriter(swCode);

            // El recorrido del AST va a llenar 'contadorAuxiliares'
            this.ast.generarASM(pwCode, this); 

            // 2. Volcado del archivo final estructurado
            pw.println("include macros2.asm\t; Inclusión de macros de la cátedra");
            pw.println("include number.asm\t; Inclusión de macros de la cátedra");
            pw.println(".MODEL LARGE\t\t; Modelo de Memoria");
            pw.println(".386\t\t\t; Tipo de Procesador");
            pw.println("STACK 200h\t\t; Bytes en el Stack");
            pw.println();

            // =================================================================
            // SECCIÓN .DATA 100% DINÁMICA
            // =================================================================
            pw.println(".DATA");
            
            // Set para evitar duplicados en la impresión de constantes en .DATA
            Set<String> idsDeclarados = new HashSet<>();

            // Recorremos la estructura interna de tu tabla de símbolos real
            for (Map<COLUMNA, String> fila : this.tablaSimbolos.getSymtabla()) {
                String nombre = fila.get(COLUMNA.NOMBRE);
                String token = fila.get(COLUMNA.TOKEN);
                String valor = fila.get(COLUMNA.VALOR);

                if (nombre == null) continue;

                // CASO A: Es una variable declarada por el usuario (Token es "ID")
                if ("ID".equals(token)) {
                    String idVariable = "_" + nombre.toLowerCase();
                    if (!idsDeclarados.contains(idVariable)) {
                        pw.println("  " + idVariable + " \tdd \t?");
                        idsDeclarados.add(idVariable);
                    }
                } 
                
                // CASO B: Es una constante de tipo STRING
                else if ("STRING_CONST".equals(token)) {
                    String valConst = (valor != null && !valor.isEmpty()) ? valor : nombre;
                    
                    // 1. Quitamos comillas y pasamos a minúscula
                    String textoBase = valConst.replace("\"", "").toLowerCase().trim();
                    
                    // 2. Reemplazamos espacios, tildes y caracteres raros por UN SOLO guion bajo
                    String idAsm = "_" + textoBase.replaceAll("[áéíóúÁÉÍÓÚñÑ]", "n") // evita la ? de la ó
                                                .replaceAll("[^a-z0-9_]", "_")
                                                .replaceAll("_+", "_");
                    
                    // 3. Si termina en guion bajo se lo removemos
                    if (idAsm.endsWith("_") && idAsm.length() > 1) {
                        idAsm = idAsm.substring(0, idAsm.length() - 1);
                    }
                    
                    if (!idsDeclarados.contains(idAsm)) {
                        pw.println("  " + idAsm + " \tdb \t\"" + textoBase + "\", '$'");
                        idsDeclarados.add(idAsm);
                    }
                }

                // CASO C: Es una constante numérica (INT_CONST, FLOAT_CONST, HEX_CONST)
                else if (token != null && token.contains("CONST")) {
                    String valConst = (valor != null && !valor.isEmpty()) ? valor : nombre;
                    
                    // Adecuamos el formato para flotantes en assembler (Intel exige punto decimal)
                    String formatoVal = valConst.contains(".") ? valConst : valConst + ".0";
                    
                    // El identificador no debe llevar puntos en el nombre
                    String idAsm = "_" + valConst.replace(".", "_");
                    
                    if (!idsDeclarados.contains(idAsm)) {
                        pw.println("  " + idAsm + " \tdd \t" + formatoVal);
                        idsDeclarados.add(idAsm);
                    }
                }
            }
            
            // Registramos de manera segura las variables fijas emuladas para #Iguales
            if (!idsDeclarados.contains("_comparador")) pw.println("  _comparador \tdd \t?");
            if (!idsDeclarados.contains("_contador"))   pw.println("  _contador   \tdd \t?");
            
            // Constantes de control por defecto para la lógica interna
            if (!idsDeclarados.contains("_0")) pw.println("  _0          \tdd \t0.0");
            if (!idsDeclarados.contains("_1")) pw.println("  _1          \tdd \t1.0");
            // D. Declarar exactamente la cantidad de auxiliares calculados en el recorrido
            for (int i = 1; i <= this.contadorAuxiliares; i++) {
                pw.println("  @aux" + i + " \tdd \t?");
            }
            pw.println();
            // =================================================================

            // Sección .CODE
            pw.println(".CODE");
            pw.println("START:");
            pw.println("  mov AX, @DATA\t\t; Inicializa el segmento de datos");
            pw.println("  mov DS, AX");
            pw.println("  mov ES, AX");
            pw.println();

            // Volcamos las instrucciones generadas por los nodos
            pw.print(swCode.toString());

            // Cierre estándar del programa
            pw.println();
            pw.println("  mov ax, 4c00h\t\t; Finaliza la ejecucion");
            pw.println("  int 21h");
            pw.println("END START");

        } catch (IOException e) {
            System.err.println("Error al escribir el archivo ASM: " + e.getMessage());
        }
    }

    public String nuevoAuxiliar() {
        this.contadorAuxiliares++;
        return "@aux" + this.contadorAuxiliares;
    }
}