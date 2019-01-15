package com.productions.esaf.cafe.Service;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.productions.esaf.cafe.Model.Token;
import com.productions.esaf.cafe.common.Common;

public class MyFirebaseIdService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String tokenRefreshed = FirebaseInstanceId.getInstance().getToken();
        if(Common.atualUtilizador != null)
        {
            updateTokenToFirebase(tokenRefreshed);
        }
    }

    private void updateTokenToFirebase(String tokenRefreshed) {
        FirebaseDatabase db= FirebaseDatabase.getInstance();
        DatabaseReference tokens=db.getReference("Tokens");
        Token token = new Token(tokenRefreshed,false); //false cause this is from client side
        tokens.child(Common.atualUtilizador.getPhone()).setValue(token);
    }
}
