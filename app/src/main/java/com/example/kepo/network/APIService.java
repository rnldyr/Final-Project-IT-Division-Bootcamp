package com.example.kepo.network;

import com.example.kepo.model.CreateTodoResponse;
import com.example.kepo.model.GetTodoDetailResponse;
import com.example.kepo.model.GetUserTodoResponse;
import com.example.kepo.model.LoginResponse;
import com.example.kepo.model.SearchTodoResponse;
import com.example.kepo.model.SearchUserResponse;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIService {

    // POST
    @POST("/login")
    Call<LoginResponse> userLogin (@Body JsonObject body);

    @POST("/user/{user_id}/todo")
    Call<CreateTodoResponse> createTodo (@Body JsonObject body, @Path("user_id") String user_id);

    @POST("searchUser")
    Call<SearchUserResponse> searchUser (@Body JsonObject body);

    @POST("/searchTodos")
    Call<SearchTodoResponse> searchTodo (@Body JsonObject body);

    @POST("/user/{user_id}/deleteTodo")
    Call<CreateTodoResponse> deleteTodo (@Body JsonObject body, @Path("user_id") String user_id);

    // PUT
    @PUT("/user/{user_id}/todo/{todo_id}")
    Call<CreateTodoResponse> updateTodo (@Body JsonObject body, @Path("user_id") String user_id, @Path("todo_id") String todo_id);

    // GET
    @GET("/user/{user_id}/todo")
    Call<GetUserTodoResponse> userToDo (@Path("user_id") String user_id);

    @GET("user/{user_id}/todo/{todo_id}")
    Call<GetTodoDetailResponse> getTodoDetail (@Path("user_id") String user_id, @Path("todo_id") String todo_id);

}
