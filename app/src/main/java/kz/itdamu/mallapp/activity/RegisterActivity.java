package kz.itdamu.mallapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import kz.itdamu.mallapp.R;
import kz.itdamu.mallapp.entity.Message;
import kz.itdamu.mallapp.entity.User;
import kz.itdamu.mallapp.helper.Helper;
import kz.itdamu.mallapp.helper.SQLiteHandler;
import kz.itdamu.mallapp.helper.SessionManager;
import kz.itdamu.mallapp.rest.ServiceGenerator;
import kz.itdamu.mallapp.rest.UserApi;
import retrofit.Callback;
import retrofit.RetrofitError;

public class RegisterActivity extends BaseActivity {

    private Toolbar toolbar;
    private EditText txtName;
    private EditText txtEmail;
    private EditText txtPhone;
    private EditText txtPassword;
    private Button btnRegister;
    private SessionManager session;
    private SQLiteHandler db;
    private ProgressDialog pDialog;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Регистрация");
            setSupportActionBar(toolbar);
            initToolbar(toolbar);
            makeNavigationBackButton();
        }
        txtName = (EditText)findViewById(R.id.name);
        txtEmail = (EditText)findViewById(R.id.email);
        txtPhone = (EditText)findViewById(R.id.phone);
        txtPassword = (EditText)findViewById(R.id.password);
        btnRegister = (Button)findViewById(R.id.btnRegister);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        // Session manager
        session = new SessionManager(getApplicationContext());
        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());
        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(RegisterActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }
        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = txtName.getText().toString().trim();
                String email = txtEmail.getText().toString().trim();
                String phone = txtPhone.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && !phone.isEmpty()) {
                    user = new User();
                    user.setName(name);
                    user.setEmail(email);
                    user.setPhone(phone);
                    user.setPassword(password);
                    user.setDevice_id(session.getDeviceId());
                    registerUser(user);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

    }

    private void registerUser(final User user) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";
        pDialog.setMessage("Registering ...");
        showDialog();
        UserApi api = ServiceGenerator.createService(UserApi.class, Helper.API_URL);
        api.register(user.getName(), user.getPhone(), user.getEmail(), user.getPassword(), user.getDevice_id(), new Callback<Message>() {
            @Override
            public void success(Message message, retrofit.client.Response response) {
                Log.e("message", message.getMessage());
                hideDialog();
                if (!message.isError()) {
                    db.addUser(user);
                    Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();
                    // Launch login activity
                    Intent intent = new Intent(
                            RegisterActivity.this,
                            LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), message.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
