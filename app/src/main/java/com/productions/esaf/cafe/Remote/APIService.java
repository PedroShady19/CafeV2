package com.productions.esaf.cafe.Remote;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import com.productions.esaf.cafe.Model.MyResponse;
import com.productions.esaf.cafe.Model.Sender;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA9aSlEEY:APA91bFgjjStY4RBE-3TmPAVCGQXVQ-PXA_u3ZkkMkJlEXl-lVJIIbsMEIvKhdhWOjEfNt4_Xbni345RzY5fsjp_JKqWsi-jM5RpxqPtWIWa7MoBokvjtsyZzeRn_Bo3gBC9j-eCryw3"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
