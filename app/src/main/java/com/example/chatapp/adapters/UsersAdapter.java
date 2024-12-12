package com.example.chatapp.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.databinding.ItemContainerUserBinding;
import com.example.chatapp.listeners.UserListener;
import com.example.chatapp.models.User;

import java.util.List;
/**
 * UsersAdapter is a RecyclerView.Adapter that displays a list of users.
 * It uses a `UserListener` interface to handle click events when a user is selected.
 */
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder>{

    private final List<User> users;
    private final UserListener userListener;
    /**
     * Constructor to initialize the UsersAdapter with a list of users and a listener for click events.
     *
     * @param users        List of User objects to display.
     * @param userListener Listener to handle user click events.
     */
    public UsersAdapter (List<User> users, UserListener userListener) {
        this.users = users;
        this.userListener = userListener;
    }

    /**
     * Inflates the layout for a single user item and creates a UserViewHolder.
     *
     * @param parent   The parent ViewGroup into which the new view will be added.
     * @param viewType The view type of the new view.
     * @return A UserViewHolder that holds the layout for a single user item.
     */

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemContainerUserBinding itemContainerUserBinding = ItemContainerUserBinding
                .inflate(LayoutInflater.from(parent.getContext()),parent,false);

        return new UserViewHolder(itemContainerUserBinding);
    }
    /**
     * Binds data to the UserViewHolder at the specified position.
     *
     * @param holder   The UserViewHolder to bind data to.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(users.get(position));
    }
    /**
     * Returns the total number of users in the adapter.
     *
     * @return The total number of users.
     */
    @Override
    public int getItemCount() {
        return users.size();
    }

    /**
     * ViewHolder class for displaying individual user items in the RecyclerView.
     */
    class UserViewHolder extends RecyclerView.ViewHolder {
        ItemContainerUserBinding binding;
        /**
         * Constructor to initialize the UserViewHolder.
         *
         * @param itemContainerUserBinding Binding for the user item layout.
         */
        public UserViewHolder(ItemContainerUserBinding itemContainerUserBinding) {
            super(itemContainerUserBinding.getRoot());
            binding = itemContainerUserBinding;
        }

        /**
         * Sets the user data to the corresponding views in the layout.
         * Handles the click event for selecting a user.
         *
         * @param user The User object containing user details.
         */
        void setUserData(User user) {
            binding.inputFirstName.setText(user.firstName);
            binding.textEmail.setText(user.email);
            binding.imageProfile.setImageBitmap(getUserImage(user.image));
            binding.getRoot().setOnClickListener(v -> userListener.onUserClicked(user));
        }
    }
    /**
     * Decodes a Base64-encoded string into a Bitmap image.
     *
     * @param encodedImage The Base64 string representing the encoded image.
     * @return A Bitmap object representing the decoded image.
     */
    private Bitmap getUserImage(String encodedImage){
        byte [] bytes = Base64.decode(encodedImage, Base64.DEFAULT);


        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
