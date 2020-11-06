package com.example.communitygardenscheduler.activities.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.communitygardenscheduler.activities.MainActivity;
import com.example.communitygardenscheduler.classes.PseudoServer;
import com.example.communitygardenscheduler.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class SignUpActivity extends AppCompatActivity {

    // Class Constants.
    public static final String PASSWORD_DOESN_T_MATCH = "Password doesn't match";
    public static final String IS_REQUIRED = "Email or Password is required";
    public static final String FAILURE = "Failure to create account";
    public static final String SUCCESS = "Success";
    public static final String USERNAME = "Failure to set username";
    public static final String FAILURE_TO_SIGN_IN = "Failure to sign in";

    // Adding Views and DB stuff.
    public EditText emailField, passwordField, confirmPasswordField, usernameField;
    public Button signUpButton;
    public CheckBox isExperienced;
    private FirebaseAuth mAuth;

    // Class Fields.
    final String fileName = "ExpGardenerList.txt";
    private String email, password, username;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Getting file
        file = new File(getFilesDir(), fileName);

        // Authentication
        mAuth = FirebaseAuth.getInstance();

        // Setting views
        usernameField = findViewById(R.id.usernameInputField);
        emailField = findViewById(R.id.emailInputField);
        passwordField = findViewById(R.id.passwordInputField);
        signUpButton = findViewById(R.id.signUpButton);
        confirmPasswordField = findViewById(R.id.confirmPasswordInputField);
        isExperienced = findViewById(R.id.checkBox);

        // SignUp Session
        clickSignUpButton();
    }

    /**
     * This method is used to handle errors that can occur during this Activity.
     */
    private void clickSignUpButton() {

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = emailField.getText().toString();
                password = passwordField.getText().toString();
                username = usernameField.getText().toString();
                String confirmPassword = confirmPasswordField.getText().toString();

                // Error Handling.
                if (email.equals("") || password.equals("")) {

                    Toast toast = Toast.makeText(getApplicationContext(), IS_REQUIRED
                            , Toast.LENGTH_SHORT);
                    toast.show();

                } else {

                    if (!password.equals(confirmPassword)) {
                        // Set Error
                        confirmPasswordField.setError(PASSWORD_DOESN_T_MATCH);
                    } else {

                        // Call to method that does the actual signup.
                        createAccount();

                    }
                }
            }
        });
    }

    /**
     * This method checks whether a User can create an Account with the provided Information
     */
    private void createAccount() {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    try {
                        setUserName(email, password);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } else {
                    // Display a message when Authentication has failed.
                    Toast toast = Toast.makeText(getApplicationContext(),
                            FAILURE,
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    /**
     * This method add a User to the ExperiencedGardeners File.
     *
     * @param uid - UserID of this User.
     */
    private void addExpGardener(String uid) {

        try {
            PseudoServer server = new PseudoServer(file);
            server.appendString(uid);

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method sets the userInformation that is related to that User.s
     *
     * @param email    - Email Address of this User
     * @param password - Password of this User
     */

    private void storeCredentials(String email, String password) throws IOException {
        File Keystore = new File(this.getApplicationContext().getFilesDir(), "verySecure.txt");
        if(!Keystore.exists()) {
            Keystore.createNewFile();
        }
        FileWriter writer = new FileWriter(Keystore, false);
        writer.append(email);
        writer.append("\n");
        writer.append(password);
        writer.close();
    }
    private void setUserName(final String email, final String password) throws InterruptedException {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    // Add a User to the ExperiencedGardeners File.
                    if (isExperienced.isChecked()) {
                        addExpGardener(user.getUid());
                    }

                    // Build a userProfile.
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(username)
                            .setPhotoUri(Uri.parse("http://experienced.gardener.com"))
                            .build();

                    // Listening for a change on a User's Profile.
                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {

                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {

                                        Toast toast = Toast.makeText(getApplicationContext(),
                                                SUCCESS,
                                                Toast.LENGTH_SHORT);
                                        toast.show();
                                        try {
                                            storeCredentials(email, password);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        // Launch MainActivity when an Authentication is complete.
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);

                                    } else {

                                        Toast toast = Toast.makeText(getApplicationContext(),
                                                USERNAME,
                                                Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                }
                            });

                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            FAILURE_TO_SIGN_IN,
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }
}
