package com.productions.esaf.cafe.common;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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

    public static boolean isConnectedToInternet (Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null)
        {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null)
            {
                for (int i=0;i<info.length;i++)
                {
                    if(info[i].getState()== NetworkInfo.State.CONNECTED);
                    return true;
                }
            }
        }
        return false;
    }

}
