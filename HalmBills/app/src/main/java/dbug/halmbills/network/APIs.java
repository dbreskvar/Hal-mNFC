package dbug.halmbills.network;

import dbug.halmbills.models.Sale;
import dbug.halmbills.models.SaleModel;
import dbug.halmbills.models.TestObject;
import okhttp3.MediaType;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIs {

    @GET("system/test")
    Call<TestObject> testEndpoint();

    @GET("transactions/{transactionid}/status")
    Call<?> getTransactionStatus(@Path("transactionid") String id);

    /*@POST("transaction/sale")
    Call<SaleModel> createSale(@Field("amount") Integer amount, @Field("currency") String currency,
                       @Field("paymentreference") String paymentreference, @Field("purpose") String purpose,
                       @Field("orderid") String orderid, @Field("channelid") String channelid);*/

    @POST("transaction/sale")
    Call<SaleModel> createSale(@Body Sale sale);

    @FormUrlEncoded
    @POST("transaction/invoice")
    Call<SaleModel> createInvoice(@Field("amount") Integer amount, @Field("currency") String currency,
                                  @Field("paymentreference") String paymentreference, @Field("purpose") String purpose,
                                  @Field("orderid") String orderid, @Field("channelid") String channelid);

    @GET("wallet/balance")
    Call<?> getWalletBalance();
}
