package com.ceramica.mensaje.aplicacion.casosdeuso.patron.estado.enums;

public enum Opciones {
    VER_PRODUCTOS("Ver productos"), DIAS_ABIERTOS("Días abiertos"), SOBRE_NOSOTROS("Sobre nosotros"), VOLVER_ATRAS("Ir al menu anterior"),
    COMPRAR_PRODUCTO("Comprar producto"), VER_STOCK_PRODUCTO("Ver stock");

    Opciones(String valor){
        this.valor = valor;
    }
    private String valor;

    public String getValor() {
        return valor;
    }



}
