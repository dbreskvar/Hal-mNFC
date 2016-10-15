package dbug.halmbills.network;

import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

    private static Retrofit retrofit;

    //TODO Change after test mode has completed
    private static String API_BASE_URL = "https://hack.halcom.com/MBillsWS/API/v1/";

    private static final String API_KEY = "d6d64630-8e4c-46d3-a7a9-56e5db61761a";
    private static /*final*/ String secretKey = "HalmNFC42Secret";
    protected static final String clientID = "1147645198395222651";
    protected static final String clientName = "MERCHANT 008";

    static class MyDeserializer implements JsonDeserializer<SyncResponse> {
        @Override
        public SyncResponse deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
            // Get the "content" element from the parsed JSON
            JsonElement content = je.getAsJsonObject().get("data");

            // Deserialize it. You use a new instance of Gson to avoid infinite recursion
            // to this deserializer
            return new Gson().fromJson(content, SyncResponse.class);
        }
    }

    public static <S> S setupRestClient(Class<S> apiClass, final String url) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(SyncResponse.class, new MyDeserializer())
                .create();
        OkHttpClient.Builder httpClient = new OkHttpClient().newBuilder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                //If additional API KEY is requested, add it here under headers
                Request.Builder builder = request.newBuilder()
                        .header("Content-Type", "application/json")
                        .method(request.method(), request.body());

                String token;
                long nonce = (long) (Math.random() * 1000000000000000L);

                String username = String.format(Locale.getDefault(), "%s.%d.%d",
                        API_KEY, 123456, (System.currentTimeMillis() / 1000L));

                /*username = "79f68986-6f6b-40a0-98b6-417d302dec85.21312312.1462271771";
                secretKey = "mysecretkey";*/
                Log.e("Rest", "Nonce: " + "123456" + " url: " + url + " username: " + username);

                String passwordToHash = username + "a";

                token = new String(Hex.encodeHex(DigestUtils.sha256(passwordToHash)));
                String authHeader = username + ":" + token;
                String base64authHeader = Base64.encodeToString(authHeader.getBytes(), Base64.NO_WRAP);
                token = "Basic " + base64authHeader;
                Log.e("Token", "Authorization: " + token);

                if (token != null) builder.header("Authorization", token);

                Log.e("Headers", "where ma headers at: " + builder);
                Request main = builder.build();

                Log.e("Headers", "where ma headers at: " + main.headers());

                return chain.proceed(main);
            }
        });

        OkHttpClient client = httpClient.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        setRetrofit(retrofit);

        return retrofit.create(apiClass);
    }

    private static void setRetrofit(Retrofit mRetro) {
        retrofit = mRetro;
    }

    public static Retrofit retrofit() {
        if (retrofit != null) return retrofit;
        else return null;
    }

}
