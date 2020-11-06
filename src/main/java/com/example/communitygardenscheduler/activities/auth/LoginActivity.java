package com.example.communitygardenscheduler.activities.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.FragmentActivity;

import com.example.communitygardenscheduler.activities.MainActivity;
import com.example.communitygardenscheduler.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    // Class Constants.
    private static final String TAG = LoginActivity.class.getName();
    private static final String REQUIRED = "Email or Password is required";
    private static final String SUCCESS = "Success";
    private static final String FAILURE = "Failure";
    private static final String NOT_LOGGED_IN = "Not Logged In";
    private static final String MY_APP_TAG = "MY_APP_TAG";
    private static final String AUTHENTICATE_USING_BIOMETRICS = "App can authenticate using biometrics.";
    private static final String CREDENTIALS = "User does not have saved credentials.";
    private static final String AVAILABLE_ON_THIS_DEVICE = "No biometric features available on this device.";
    private static final String UNAVAILABLE = "Biometric features are currently unavailable.";
    private static final String NO_REGISTERED_CREDENTIALS = "The user hasn't associated any biometric credentials with their account.";
    private static final String TITLE = "Login with Fingerprint";
    private static final String SUBTITLE = "Scan FingerPrint below";
    private static final String CANCEL = "Cancel";
    private static final String RECOGNISED_SUCCESSFULLY = "Fingerprint recognised successfully";
    private static final String FINGERPRINT_NOT_RECOGNISED = "Fingerprint not recognised";
    private static final String UNRECOVERABLE_ERROR_OCCURRED = "An unrecoverable error occurred";
    private static final String TXT = "verySecure.txt";

    // Class Fields
    private File Keystore;
    public EditText emailView, passwordView;
    public Button login, signUp, bioAuth;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Setting Views and Firebase Stuff
        mAuth = FirebaseAuth.getInstance();
        emailView = findViewById(R.id.emailField);
        passwordView = findViewById(R.id.passwordField);
        login = findViewById(R.id.loginButton);
        signUp = findViewById(R.id.signUpButton);
        bioAuth = findViewById((R.id.bioAuthButton));

        // Setting Listeners.
        bioAuth.setOnClickListener(this);
        login.setOnClickListener(this);
        signUp.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bioAuthButton:
                checkDeviceBiometricStatus();
                final BiometricPrompt.PromptInfo promptInfo = buildPromptInfo();
                final BiometricPrompt myBiometricPrompt = authenticateBiometric();
                myBiometricPrompt.authenticate(promptInfo);
                break;

            case R.id.loginButton:
                preSignIn();
                break;

            case R.id.signUpButton:
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
                break;

            default:
                break;
        }

    }

    private void preSignIn() {
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();

        if (email.equals("") || password.equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(), REQUIRED
                    , Toast.LENGTH_SHORT);
            toast.show();
        } else {
            try {
                signIn(email, password);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private BiometricPrompt authenticateBiometric() {

        FragmentActivity activity = this;
        Executor newExecutor = Executors.newSingleThreadExecutor();
        return new BiometricPrompt(activity, newExecutor, new BiometricPrompt.AuthenticationCallback() {

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Log.d(TAG, RECOGNISED_SUCCESSFULLY);
                try {
                    String[] credentials = fetchCredentials();
                    signIn(credentials[0], credentials[1]);
                } catch (FileNotFoundException | InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Log.d(TAG, FINGERPRINT_NOT_RECOGNISED);
            }

            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                if (errorCode != BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                    Log.d(TAG, UNRECOVERABLE_ERROR_OCCURRED);
                }
            }

        });
    }

    private BiometricPrompt.PromptInfo buildPromptInfo() {
        return new BiometricPrompt.PromptInfo.Builder()
                .setTitle(TITLE)
                .setSubtitle(SUBTITLE)
                .setDescription("More text")
                .setNegativeButtonText(CANCEL)
                .build();
    }

    private void checkDeviceBiometricStatus() {
        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Log.d(MY_APP_TAG, AUTHENTICATE_USING_BIOMETRICS);
                if (!hasSavedCredentials()) {
                    bioAuth.setVisibility(View.GONE);
                    Log.d(MY_APP_TAG, CREDENTIALS);
                }
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                bioAuth.setVisibility(View.GONE);
                Log.e(MY_APP_TAG, AVAILABLE_ON_THIS_DEVICE);
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Log.e(MY_APP_TAG, UNAVAILABLE);
                bioAuth.setVisibility(View.GONE);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Log.e(MY_APP_TAG, NO_REGISTERED_CREDENTIALS);
                bioAuth.setVisibility(View.GONE);
                break;
        }
    }

    private void storeCredentials(String email, String password) throws IOException {
        Keystore = new File(this.getApplicationContext().getFilesDir(), TXT);
        if (!Keystore.exists()) {
            Keystore.createNewFile();
        }
        FileWriter writer = new FileWriter(Keystore, false);
        writer.append(email);
        writer.append("\n");
        writer.append(password);
        writer.close();
    }

    private String[] fetchCredentials() throws FileNotFoundException {
        String[] credentials = new String[2];
        Keystore = new File(this.getApplicationContext().getFilesDir(), TXT);
        Scanner scan = new Scanner(Keystore);
        credentials[0] = scan.nextLine();
        credentials[1] = scan.nextLine();
        return credentials;
    }

    private boolean hasSavedCredentials() {
        Keystore = new File(this.getApplicationContext().getFilesDir(), TXT);
        return Keystore.exists();
    }

    public void signIn(final String email, final String password) throws InterruptedException {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    try {
                        storeCredentials(email, password);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast toast = Toast.makeText(getApplicationContext(),
                            SUCCESS,
                            Toast.LENGTH_SHORT);
                    toast.show();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);

                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            FAILURE,
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    // to be deleted
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String test = currentUser.getEmail();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            Toast toast = Toast.makeText(getApplicationContext(),
                    test,
                    Toast.LENGTH_SHORT);
            toast.show();
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    NOT_LOGGED_IN,
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    // Prevents back key after user has logged out.
    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
