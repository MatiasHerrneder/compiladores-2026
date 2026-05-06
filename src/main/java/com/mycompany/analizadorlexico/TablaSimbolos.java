/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.analizadorlexico;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class TablaSimbolos {
    private final int lenFormat = 100;
    private final List<Map<COLUMNA, String>> symtabla = new ArrayList<>();
    private final String FILENAME;
    private final String formato = "|%35s|%10s|%10s|%35s|%4s|";

    public TablaSimbolos(String filename){
        this.FILENAME = filename;
        this.inicializar();
    }

    public List<Map<COLUMNA, String>> getSymtabla() {
        return symtabla;
    }

    public String getFILENAME() {
        return FILENAME;
    }

    public String getFormato(){
        return this.formato;
    }

    public void addLine(Map<COLUMNA,String> line){
        this.symtabla.add(line);
    }

    public void inicializar(){
        // inicializar archivo
        this.escribirArchivo(null, false);
    }

    public void agregarSimbolo(String nombre, String token, String tipo){
        List<Map<COLUMNA, String>> tabla = getSymtabla();
        boolean encontrado = false;
        int i = 0;

        while (!encontrado && i < tabla.size()) {
            Map<COLUMNA, String> fila = tabla.get(i++);
            encontrado = Objects.equals(fila.get(COLUMNA.NOMBRE), nombre);
        }

        if (!encontrado) {
            Map<COLUMNA, String> sym = new HashMap<>();
            sym.put(COLUMNA.NOMBRE, nombre);
            sym.put(COLUMNA.TOKEN, token);
            sym.put(COLUMNA.TIPO, tipo);
            this.addLine(sym);
            this.escribirArchivo(Arrays.asList(sym), true);
        }
    }

    public void agregarSimbolo(String nombre, String token, String valor, Integer len){
        List<Map<COLUMNA, String>> tabla = getSymtabla();
        boolean encontrado = false;
        int i = 0;

        while (!encontrado && i < tabla.size()) {
            Map<COLUMNA, String> fila = tabla.get(i++);
            encontrado = Objects.equals(fila.get(COLUMNA.NOMBRE), nombre);
        }

        if (!encontrado) {
            Map<COLUMNA, String> sym = new HashMap<>();
            sym.put(COLUMNA.NOMBRE, nombre);
            sym.put(COLUMNA.TOKEN, token);
            sym.put(COLUMNA.VALOR, valor);
            if (len != null) sym.put(COLUMNA.LEN, String.valueOf(len));
            this.addLine(sym);
            this.escribirArchivo(Arrays.asList(sym), true);
        }
    }

    public boolean actualizarSimbolo(String nombre, String tipo) {
        List<Map<COLUMNA, String>> tabla = getSymtabla();

        for (Map<COLUMNA, String> linea : tabla) {
            if (linea.get(COLUMNA.NOMBRE).equals(nombre)) {
                linea.put(COLUMNA.TIPO, tipo);
                escribirArchivo(tabla, false); // Reescribe TODA la tabla
                return true;
            }
        }
        return false;
    }

    public boolean escribirArchivo(List<Map<COLUMNA, String>> filas, boolean append){
        try (PrintWriter out = new PrintWriter(new FileWriter(getFILENAME(), append))) {

            if (!append) {
                // CABECERA
                out.println(String.format(
                    this.getFormato(),
                    COLUMNA.NOMBRE, COLUMNA.TOKEN, COLUMNA.TIPO, COLUMNA.VALOR, COLUMNA.LEN
                ));
                out.println("-".repeat(this.lenFormat));
            }

            if (filas != null) {
                for (Map<COLUMNA, String> linea : filas) {
                    String nombre = linea.getOrDefault(COLUMNA.NOMBRE, "");
                    String token  = linea.getOrDefault(COLUMNA.TOKEN,  "");
                    String tipo   = linea.getOrDefault(COLUMNA.TIPO,   "");
                    String valor  = linea.getOrDefault(COLUMNA.VALOR,  "");
                    String len    = linea.getOrDefault(COLUMNA.LEN,    "");

                    out.println(String.format(
                        this.getFormato(), nombre, token, tipo, valor, len
                    ));
                }
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void leerArchivo() throws IOException {
        try (BufferedReader in = new BufferedReader(new FileReader(this.getFILENAME()))) {
            // Leemos las primeras dos líneas que son la cabecera.
            in.readLine();
            in.readLine();

            String linea;
            while ((linea = in.readLine()) != null) {
                String[] splitLine = linea.split("\\|");

                // Esperamos: "", NOMBRE, TOKEN, TIPO, VALOR, LEN, ""
                if (splitLine.length < 6) continue;

                Map<COLUMNA, String> fila = new HashMap<>();
                COLUMNA[] columnas = COLUMNA.values();

                for (int i = 0; i < columnas.length; i++) {
                    COLUMNA c = columnas[i];
                    String celda = (i + 1) < splitLine.length ? splitLine[i + 1].trim() : "";
                    fila.put(c, celda);
                }
                this.addLine(fila);
            }
        }
    }

    public String getTipo(String nombre) {
        for (Map<COLUMNA, String> fila : symtabla) {
            String nom = fila.get(COLUMNA.NOMBRE);
            if (nom != null && nom.equals(nombre)) {
                return fila.get(COLUMNA.TIPO);  // puede ser INT, FLOAT, STRING, etc.
            }
        }
        return null; // no encontrado
    }

}