package com.example.chatapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.adapters.UsersAdapter;
import com.example.chatapp.databinding.ActivityUserBinding;
import com.example.chatapp.listeners.UserListener;
import com.example.chatapp.models.User;
import com.example.chatapp.utilities.Constants;
import com.example.chatapp.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
/**
 * userActivity displays a list of available users retrieved from Firebase Firestore.
 * It allows the current user to select a user to initiate a chat, which redirects them
 * to the ChatActivity.
 */
public class  userActivity extends AppCompatActivity implements UserListener {

    private ActivityUserBinding binding;
    private PreferenceManager preferenceManager;
    /**
     * Called when the activity is starting. Initializes the activity's UI,
     * sets up event listeners, and retrieves the list of users from Firestore.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down, this Bundle contains
     *                           the most recent data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserBinding.inflate(getLayoutInflater());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setContentView(binding.getRoot());
        setListeners();
        getUser();
    }

    /**
     * Sets up click listeners for UI elements such as the back button.
     */

    private void setListeners() {
        binding.imageBack.setOnClickListener(v -> onBackPressed());
    }

    /**
     * Retrieves the list of users from Firebase Firestore, excluding the current user.
     * Displays the list in a RecyclerView using the `UsersAdapter`. Shows an error message
     * if no users are available.
     */
    private void getUser() {
    loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS).get()
                .addOnCompleteListener(task -> {
                    loading(false);
                    String currentUserId= preferenceManager.getString(Constants.KEY_USER_ID);
                    if(task.isSuccessful() && task.getResult()!= null) {
                        List<User> users = new ArrayList<>();
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            if(currentUserId.equals(queryDocumentSnapshot.getId())) {
                                continue;
                            }
                           User user = new User();
                            user.firstName = queryDocumentSnapshot.getString(Constants.KEY_FIRST_NAME);
                            user.email = queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
                            user.image = queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
                            user.token = queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                            user.id = queryDocumentSnapshot.getId();
                            users.add(user);




                        }

                        if(users.size() > 0) {
                            UsersAdapter usersAdapter = new UsersAdapter(users, this);
                            binding.userRecyclerView.setAdapter(usersAdapter);
                            binding.userRecyclerView.setVisibility(View.VISIBLE);

                        }else {
                            showErrorMesssage();
                        }
                    }else {
                        showErrorMesssage();
                    }
                });
    }
    /**
     * Displays an error message on the screen when no users are available.
     */

    private void showErrorMesssage() {
        binding.textErrorMessage.setText(String.format("%s","No user available"));
        binding.textErrorMessage.setVisibility(View.VISIBLE);

    }

    /**
     * Toggles the visibility of the progress bar during the loading process.
     *
     * @param isLoading True if the process is loading, false otherwise.
     */
    private void loading(Boolean isLoading) {
        if(isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
        }else {
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }
    /**
     * Handles the click event on a user item in the RecyclerView.
     * Redirects to ChatActivity, passing the selected user's details.
     *
     * @param user The user object that was clicked.
     */
    @Override
    public void onUserClicked(User user) {
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra(Constants.KEY_USER, user);
        startActivity(intent);
        finish();
    }
}