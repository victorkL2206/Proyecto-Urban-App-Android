package com.victordev.urban.urbanpty;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.victordev.urban.urbanpty.persistencia.DatosIniciosesion;

public class Main2Activity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private EditText _emailText;
    private EditText _passwordText;
    private Button _loginButton;
    private TextView _signupLink;
    private FirebaseAuth firebaseAuth;
    private String nombre,correo,password;
    private FirebaseAuth.AuthStateListener authStateListener;
    private Bundle parametros;
    private DatosIniciosesion iniciosesion;
    boolean validador = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Vistas();
        parametros = this.getIntent().getExtras();



            if(parametros != null){
                if(!iniciosesion.ini() ){
                    Log.w("prueba456",parametros.getString("nombre"));
                    nombre = parametros.getString("nombre");
                    password = parametros.getString("password");
                    correo = parametros.getString("email");
                    createAccount(correo,password);

                }else {
                    cambiarActividad();
                }

        }



        _loginButton.setOnClickListener(new View.OnClickListener() {


            @Override

            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    private void createAccount(String correo, String password) {
        Log.w("prueba456",correo);
        Log.w("prueba456",password);
        firebaseAuth.createUserWithEmailAndPassword(correo,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Usuario Registrado exitosamente",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(),"Error en la creacion de la cuenta"+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }

            }
        });
    }



    private void Vistas() {
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null){
                    Log.w("prueba045","Usuario logiado"+firebaseUser.getEmail());
                }else{
                    Log.w("prueba045","Usuario cerro sesion");
                }

            }
        };
        iniciosesion = new DatosIniciosesion(this);
        _emailText =(EditText)findViewById(R.id.input_email);
        _passwordText =(EditText)findViewById(R.id.input_password);
        _loginButton = (Button)findViewById(R.id.btn_login);
        _signupLink =(TextView) findViewById(R.id.link_signup);
    }


    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(Main2Activity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        signIn(email,password);
        // TODO: Implement your own authentication logic here.
        if(validador){
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onLoginSuccess or onLoginFailed
                            onLoginSuccess();
                            // onLoginFailed();
                            progressDialog.dismiss();
                            iniciosesion.guardarSescion(true);
                            cambiarActividad();
                        }

                    }, 3000);


        }else {
            progressDialog.dismiss();
            _loginButton.setEnabled(true);
        }


    }

    private void cambiarActividad() {
        Intent i = new Intent(Main2Activity.this,MainActivity.class);
        startActivity(i);
    }

    private boolean signIn(String email, String password) {

        Log.w("prueba456",email);
        Log.w("prueba456",password);
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Authentication Success",Toast.LENGTH_LONG).show();
                    validador = true;

                }else {
                    Toast.makeText(getApplicationContext(),"Authentication  Unsucces"+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    validador = false;
                    iniciosesion.guardarSescion(validador);
                }

            }
        });
        return validador;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically

            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();


        _loginButton.setEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
        if(parametros != null){
            parametros.clear();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
