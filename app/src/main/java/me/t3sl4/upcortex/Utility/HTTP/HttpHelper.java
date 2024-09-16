package me.t3sl4.upcortex.Utility.HTTP;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.t3sl4.upcortex.BuildConfig;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public class HttpHelper {

    public static final String BASE_URL = BuildConfig.BASE_URL;
    private static Retrofit retrofit;

    // Singleton Retrofit instance
    private static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    // API Service Interface
    public interface ApiService {
        @GET
        Call<ResponseBody> getRequest(@Url String url, @QueryMap Map<String, String> params);

        @POST
        Call<ResponseBody> postRequest(@Url String url, @Body RequestBody body);

        @PUT
        Call<ResponseBody> putRequest(@Url String url, @Body RequestBody body);

        @DELETE
        Call<ResponseBody> deleteRequest(@Url String url, @QueryMap Map<String, String> params);

        @Multipart
        @POST
        Call<ResponseBody> uploadFiles(@Url String url, @PartMap Map<String, RequestBody> partMap, @Part List<MultipartBody.Part> files);

        @Streaming
        @GET
        Call<ResponseBody> downloadFile(@Url String url);
    }

    public static ApiService getApiService() {
        return getRetrofitInstance().create(ApiService.class);
    }

    // Helper methods for different request types
    public static Call<ResponseBody> makeRequest(String method, String url, Map<String, String> params, String jsonBody, String authToken) {
        ApiService service;
        if (authToken != null && !authToken.isEmpty()) {
            service = getApiServiceWithAuth(authToken);
        } else {
            service = getApiService();
        }

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(jsonBody != null ? jsonBody : "", mediaType);
        Call<ResponseBody> call = null;

        if (params == null) {
            params = new HashMap<>();
        }

        switch (method.toUpperCase()) {
            case "GET":
                call = service.getRequest(url, params);
                break;
            case "POST":
                call = service.postRequest(url, body);
                break;
            case "PUT":
                call = service.putRequest(url, body);
                break;
            case "DELETE":
                call = service.deleteRequest(url, params);
                break;
        }

        return call;
    }

    public static Call<ResponseBody> uploadFiles(String url, Map<String, RequestBody> partMap, List<MultipartBody.Part> files, String authToken) {
        ApiService service;
        if (authToken != null && !authToken.isEmpty()) {
            service = getApiServiceWithAuth(authToken);
        } else {
            service = getApiService();
        }
        return service.uploadFiles(url, partMap, files);
    }

    public static Call<ResponseBody> downloadFile(String url) {
        ApiService service = getApiService();
        return service.downloadFile(url);
    }

    // Helper methods for authentication
    private static OkHttpClient getHttpClientWithAuth(String authToken) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        Interceptor authInterceptor = chain -> {
            Request original = chain.request();
            Request.Builder builder = original.newBuilder()
                    .header("authorization", "Bearer " + authToken)
                    .method(original.method(), original.body());
            Request request = builder.build();
            return chain.proceed(request);
        };

        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(authInterceptor)
                .build();
    }

    private static Retrofit getRetrofitInstanceWithAuth(String authToken) {
        OkHttpClient client = getHttpClientWithAuth(authToken);

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ApiService getApiServiceWithAuth(String authToken) {
        return getRetrofitInstanceWithAuth(authToken).create(ApiService.class);
    }

    public static Call<ResponseBody> makeRequestWithAuth(String method, String url, Map<String, String> params, String jsonBody, String authToken) {
        ApiService service = getApiServiceWithAuth(authToken);

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(jsonBody != null ? jsonBody : "", mediaType);
        Call<ResponseBody> call = null;

        if (params == null) {
            params = new HashMap<>();
        }

        switch (method.toUpperCase()) {
            case "GET":
                call = service.getRequest(url, params);
                break;
            case "POST":
                call = service.postRequest(url, body);
                break;
            case "PUT":
                call = service.putRequest(url, body);
                break;
            case "DELETE":
                call = service.deleteRequest(url, params);
                break;
        }

        return call;
    }

    public static Call<ResponseBody> uploadFilesWithAuth(String url, Map<String, RequestBody> partMap, List<MultipartBody.Part> files, String authToken) {
        ApiService service = getApiServiceWithAuth(authToken);
        return service.uploadFiles(url, partMap, files);
    }

    public static Call<ResponseBody> downloadFileWithAuth(String url, String authToken) {
        ApiService service = getApiServiceWithAuth(authToken);
        return service.downloadFile(url);
    }

    // Helper method to create RequestBody for text parts
    public static RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(MultipartBody.FORM, descriptionString);
    }

    // Helper method to create MultipartBody.Part for file parts
    public static MultipartBody.Part prepareFilePart(String partName, File file) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }
}