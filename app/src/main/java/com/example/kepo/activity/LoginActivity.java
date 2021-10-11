package com.example.kepo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kepo.R;
import com.example.kepo.databinding.ActivityLoginBinding;
import com.example.kepo.model.LoginResponse;
import com.example.kepo.model.LoginResponseData;
import com.example.kepo.model.SharedPref;
import com.example.kepo.model.UserLoginCredentials;
import com.example.kepo.network.APIService;
import com.example.kepo.network.ApiUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private SharedPref pref;
    private APIService mAPIService;
    private TextView tvBottomDialogTitle;
    private TextView tvBottomDialogMessage;
    private Button btnBottomDialogOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        binding.btnLogin.setClickable(true);

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.btnLogin.setClickable(false);
                login();
            }
        });

        binding.setUser(new UserLoginCredentials());
        pref = new SharedPref(this);
        LoginResponseData user = pref.load();
        if(!user.getUserID().isEmpty()){
            goToHomeActivity();
        }


    }

    private void login() {
        UserLoginCredentials user = binding.getUser();
        mAPIService = ApiUtils.getAPIService();
        if (user.getUsername() == null || user.getPassword() == null) {
            final BottomSheetDialog dialog = new BottomSheetDialog(LoginActivity.this);
            dialog.setCancelable(false);
            View view = LayoutInflater.from(getApplicationContext()).inflate(
                    R.layout.bottom_dialog, (RelativeLayout)findViewById(R.id.bottom_dialog)
            );
            tvBottomDialogTitle = view.findViewById(R.id.tv_bottom_dialog_title);
            tvBottomDialogMessage = view.findViewById(R.id.tv_bottom_dialog_message);
            btnBottomDialogOk = view.findViewById(R.id.btn_bottom_dialog_ok);

            btnBottomDialogOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            tvBottomDialogTitle.setText("Error");
            tvBottomDialogMessage.setText("Please input username and password");

            dialog.setContentView(view);
            dialog.show();
            binding.btnLogin.setClickable(true);
            return;
        }

        binding.progressBar.bringToFront();
        binding.progressBar.setVisibility(View.VISIBLE);
        sendLoginPost(user.getUsername(), user.getPassword());

    }

    public void sendLoginPost(String username, String password) {
        try {
            JsonObject temp = new JsonObject();
            temp.addProperty("username", username);
            temp.addProperty("password", password);
            Call<LoginResponse> call = mAPIService.userLogin(temp);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if(response.isSuccessful()){
                        binding.progressBar.setVisibility(View.GONE);
                        LoginResponse resp = response.body();
                        if(!resp.getMessage().equals("Login success")){
                            final BottomSheetDialog dialog = new BottomSheetDialog(LoginActivity.this);
                            dialog.setCancelable(false);
                            View view = LayoutInflater.from(getApplicationContext()).inflate(
                                    R.layout.bottom_dialog, (RelativeLayout)findViewById(R.id.bottom_dialog)
                            );
                            tvBottomDialogTitle = view.findViewById(R.id.tv_bottom_dialog_title);
                            tvBottomDialogMessage = view.findViewById(R.id.tv_bottom_dialog_message);
                            btnBottomDialogOk = view.findViewById(R.id.btn_bottom_dialog_ok);

                            btnBottomDialogOk.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });

                            tvBottomDialogTitle.setText("Error");
                            tvBottomDialogMessage.setText(resp.getMessage());

                            dialog.setContentView(view);
                            dialog.show();
                            binding.btnLogin.setClickable(true);
                            return;
                        }
                        LoginResponseData user = new LoginResponseData();
                        user.setUserID(resp.getData().getUserID());
                        user.setUsername(resp.getData().getUsername());
                        user.setName(resp.getData().getName());
                        pref.save(user);
                        goToHomeActivity();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    binding.progressBar.setVisibility(View.GONE);
                    final BottomSheetDialog dialog = new BottomSheetDialog(LoginActivity.this);
                    dialog.setCancelable(false);
                    View view = LayoutInflater.from(getApplicationContext()).inflate(
                            R.layout.bottom_dialog, (RelativeLayout)findViewById(R.id.bottom_dialog)
                    );
                    tvBottomDialogTitle = view.findViewById(R.id.tv_bottom_dialog_title);
                    tvBottomDialogMessage = view.findViewById(R.id.tv_bottom_dialog_message);
                    btnBottomDialogOk = view.findViewById(R.id.btn_bottom_dialog_ok);

                    btnBottomDialogOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    tvBottomDialogTitle.setText("Error");
                    tvBottomDialogMessage.setText("Something wrong occured when logging in");

                    dialog.setContentView(view);
                    dialog.show();
                    binding.btnLogin.setClickable(true);
                    return;
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void goToHomeActivity(){
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

}