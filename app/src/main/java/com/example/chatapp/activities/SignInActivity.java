package com.example.chatapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chatapp.R;
import com.example.chatapp.databinding.ActivitySignInBinding;
import com.example.chatapp.utilities.Constants;
import com.example.chatapp.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
/**
 * SignInActivity handles the user sign-in process.
 * It validates user input, authenticates users against Firebase Firestore,
 * and redirects them to the MainActivity upon successful login.
 */

public class SignInActivity extends AppCompatActivity {

    private ActivitySignInBinding binding;
    private PreferenceManager preferenceManager;
    /**
     * Called when the activity is starting. Initializes the activity's UI
     * and sets up event listeners for user interactions.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down, this Bundle contains
     *                           the most recent data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setListener();
    }
    /**
     * Sets click listeners for UI elements such as the "Create New Account" text
     * and the "Sign In" button.
     */
    private void setListener() {
        binding.textCreateNewAccount.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(),SignUpActivity.class)));


       binding.buttonSignIn.setOnClickListener(v -> {
           if(isValidateSignInDetails()) {
               SignIn();
           }
       });
    }
    /**
     * Displays a short Toast message on the screen.
     *
     * @param message The message to be displayed.
     */

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    /**
     * Authenticates the user by validating the provided email and password
     * against the Firebase Firestore database. On success, it saves user details
     * in shared preferences and navigates to MainActivity.
     */
    private void SignIn(){
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_EMAIL,binding.inputEmail.getText().toString())
                .whereEqualTo(Constants.KEY_PASSWORD,binding.inputPassowrd.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                   if(task.isSuccessful() && task.getResult()!= null && task.getResult().getDocuments().size()>0) {
                       DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);

                       preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                       preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                       preferenceManager.putString(Constants.KEY_FIRST_NAME, documentSnapshot.getString(Constants.KEY_FIRST_NAME));
                       preferenceManager.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE));

                       Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                       intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                       startActivity(intent);




                   }else {
                       loading(false);
                       showToast("Unable to Sign in");
                   }
                });
    }

    /**
     * Toggles the visibility of the progress bar and the "Sign In" button
     * during the sign-in process.
     *
     * @param isLoading True if the process is loading, false otherwise.
     */
    private void loading(Boolean isLoading) {
        if(isLoading) {
            binding.buttonSignIn.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.buttonSignIn.setVisibility(View.VISIBLE);

        }
    }

    /**
     * Validates the user's sign-in details such as email and password.
     * Displays appropriate error messages if validation fails.
     *
     * @return True if all details are valid, false otherwise.
     */
    private boolean isValidateSignInDetails() {
        if (binding.inputEmail.getText().toString().trim().isEmpty()) {
            showToast(("Please enter your Email"));
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()) {
            showToast(("Please enter Valid Email"));
            return false;
        } else if (binding.inputPassowrd.getText().toString().trim().isEmpty()) {
            showToast("Please enter your Password");
            return false;
        } else {
            return true;
        }

    }




}