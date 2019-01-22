package com.productions.esaf.cafe.common;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.productions.esaf.cafe.Model.Utilizador;
import com.productions.esaf.cafe.Remote.APIService;
import com.productions.esaf.cafe.Remote.RetrofitClient;

public class Common {
    public static Utilizador atualUtilizador;
    public static String PHONE_TEXT= "userPhone";
    private static final String BASE_URL = "https://fcm.googleapis.com/";
    public static APIService getFCMService()
    {
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }

    public static String convertCodeToStatus(String status) {
        if("0".equals(status))
            return "Encomendado";
        else if(status.equals("1"))
            return "A Preparar";
        else
            return "Enviado";
    }

    public static final String DELETE = "Delete";
    public static final String USER_KEY = "User";
    public static final String PWD_KEY = "Password";
    public static boolean isConnectedToInternet (Context context) {
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
