package com.productions.esaf.cafe.common;


import com.productions.esaf.cafe.Model.Utilizador;

public class Common {
    public static Utilizador atualUtilizador;

    public static String convertCodeToStatus(String status) {
        if("0".equals(status))
            return "Encomendado";
        else if(status.equals("1"))
            return "A Preparar";
        else
            return "Enviado";
    }
}
