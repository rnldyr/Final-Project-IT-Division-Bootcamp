package com.example.kepo.network;

public class ApiUtils {

    private ApiUtils() {}

    public static final String BASE_URL = "https://it-division-kepo.herokuapp.com";

    public static APIService getAPIService() {

        return RetrofitClientInstance.getRetrofitInstance(BASE_URL).create(APIService.class);
    }
}
