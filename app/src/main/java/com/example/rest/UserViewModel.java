package com.example.rest;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
 interface UserService {
    @GET("/public/v2/users")
    Call<List<User>> fetchAllUsers();
}
public class UserViewModel extends ViewModel {
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private MutableLiveData<List<User>> users = new MutableLiveData<>();
    private UserService userService;
    private UserAdapter adapter;

    public UserViewModel() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://gorest.co.in")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userService = retrofit.create(UserService.class);
        adapter = new UserAdapter(new ArrayList<>());

    }

    public LiveData<List<User>> getUsers() {
        return users;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void fetchUsers() {
        isLoading.setValue(true);
        userService.fetchAllUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    users.setValue(response.body());
                    adapter.setUsers(response.body());
                }
                isLoading.setValue(false);
            }
            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.i("RTR", "Error response: " + t);
                isLoading.setValue(false);
            }
        });
    }

    public UserAdapter getAdapter() {
        return adapter;
    }
}


