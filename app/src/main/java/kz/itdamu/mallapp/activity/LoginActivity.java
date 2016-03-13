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

public class LoginActivity extends BaseActivity {

    private Toolbar toolbar;
    private EditText txtEmail;
    private EditText txtPassword;
    private Button btnLogin;
    private Button btnRegisterLink;
    private SessionManager session;
    private SQLiteHandler db;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Войти");
            setSupportActionBar(toolbar);
            initToolbar(toolbar);
            makeNavigationBackButton();
        }
        txtEmail = (EditText)findViewById(R.id.email);
        txtPassword = (EditText)findViewById(R.id.password);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnRegisterLink = (Button)findViewById(R.id.btnLinkToRegisterScreen);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());
        // Session manager
        session = new SessionManager(getApplicationContext());
        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = txtEmail.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();
                // Check for empty data in the form
                if (!email.isEmpty() && !password.isEmpty()) {
                    // login user
                    checkLogin(email, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        // Link to Register Screen
        btnRegisterLink.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        pDialog.setMessage("Logging in ...");
        showDialog();
        UserApi api = ServiceGenerator.createService(UserApi.class, Helper.API_URL);
        api.login(email, password, new Callback<User>() {
            @Override
            public void success(User user, retrofit.client.Response response) {
                hideDialog();
                if (user != null) {
                    session.setLogin(true);
                    db.addUser(user);
                    Intent intent = new Intent(
                            LoginActivity.this,
                            MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
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
