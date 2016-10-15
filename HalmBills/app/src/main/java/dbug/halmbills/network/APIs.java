package dbug.halmbills.network;

import dbug.halmbills.models.TestObject;
import retrofit2.Call;
import retrofit2.http.GET;

public interface APIs {

    @GET("system/test")
    Call<TestObject> testEndpoint();
}
