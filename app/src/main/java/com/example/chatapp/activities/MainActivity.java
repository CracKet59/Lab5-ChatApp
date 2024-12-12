package com.example.chatapp.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chatapp.R;
import com.example.chatapp.databinding.ActivityMainBinding;
import com.example.chatapp.utilities.Constants;
import com.example.chatapp.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
/**
 * MainActivity is the primary activity that displays user details
 * and provides options to sign out or start a new chat.
 * It handles user authentication and Firebase token management.
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private PreferenceManager preferenceManager;
    /**
     * Called when the activity is starting. Initializes the activity's UI,
     * loads user details, retrieves the FCM token, and sets event listeners.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down, this Bundle contains
     *                           the most recent data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        loadUserDetails();
        getToken();
        setListeners();


    }
    /**
     * Sets click listeners for UI elements including the sign-out button
     * and the floating action button (FAB) for starting a new chat.
     */
    private void setListeners() {

        binding.imageSignOut.setOnClickListener(v -> SignOut());

        binding.fabNewChat.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), userActivity.class)));
    }
    /**
     * Loads the user's details (name and profile image) from shared preferences
     * and displays them on the UI.
     */

    private void loadUserDetails() {
        binding.inputFirstName.setText(preferenceManager.getString(Constants.KEY_FIRST_NAME));


        byte [] bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        binding.imageProfile.setImageBitmap(bitmap);
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
     * Retrieves the Firebase Cloud Messaging (FCM) token for the device
     * and updates it in the Firestore database.
     */
    private void getToken() {
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }


    /**
     * Updates the FCM token in the Firestore database for the current user.
     *
     * @param token The FCM token to be updated in the database.
     */
    private void updateToken(String token) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_USERS)
                .document(preferenceManager.getString(Constants.KEY_USER_ID));

        documentReference.update(Constants.KEY_FCM_TOKEN, token)
                .addOnSuccessListener(unused -> showToast("Token updated successfully"))
                .addOnFailureListener(e -> showToast("Unable to update token"));

    }
    /**
     * Signs the user out by deleting their FCM token from the Firestore database,
     * clearing shared preferences, and redirecting to the SignInActivity.
     */

    private void SignOut() {
        showToast("Signing out ...");
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_USERS)
                .document(preferenceManager.getString(Constants.KEY_USER_ID));
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(unused -> {
                    preferenceManager.clear();
                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                    finish();
                }).addOnFailureListener(e -> showToast("Unable to Sign Out"));

    }



}