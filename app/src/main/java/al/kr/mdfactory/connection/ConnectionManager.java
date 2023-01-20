package al.kr.mdfactory.connection;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public interface ConnectionManager {
    String BASE_URL = "http://192.168.1.107:8081/";

    void onMessage(String message);

    default Request buildDelete(@NonNull String urlApex) throws IOException {
        return new Request.Builder()
                .url(BASE_URL + urlApex)
                .delete()
                .build();
    }

    default Request buildGet(@NonNull String urlApex) throws IOException {
        return new Request.Builder()
                .url(BASE_URL + urlApex)
                .get()
                .build();
    }

    default Request buildPost(@NonNull String urlApex, @NonNull String json) throws IOException {
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), json);

        return new Request.Builder()
                .url(BASE_URL + urlApex)
                .post(body)
                .build();
    }

    default boolean isConnectedToServer() {
        try{
            URL myUrl = new URL(BASE_URL);
            URLConnection connection = myUrl.openConnection();
            connection.setConnectTimeout(10000);
            connection.connect();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    default void sendRequest(Request request, Callback<ResponseBody> onResponse) {
        if (isConnectedToServer()) {
            new OkHttpClient().newCall(request).enqueue(new okhttp3.Callback() {

                @Override
                public void onFailure(@NonNull final Call call, @NonNull final IOException e) {
                    e.printStackTrace();
                    onMessage("Соединение с сервером нарушено");
                }

                @Override
                public void onResponse(@NonNull final Call call, @NonNull final Response response) {
                    try (response; ResponseBody responseBody = response.body()) {
                        if (responseBody == null) {
                            onMessage("Ошибка");
                            return;
                        }

                        onResponse.accept(response.code(), responseBody);
                    }
                }
            });
        } else {
            onMessage("Соединение с сервером отсутствует");
        }
    }

    default void runAsync(Runnable runnable) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(runnable);
        executorService.shutdown();
    }
}
