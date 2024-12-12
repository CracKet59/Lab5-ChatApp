package com.example.chatapp.listeners;

import com.example.chatapp.models.User;
/**
 * UserListener is an interface that defines a callback for user interaction events.
 * It is used to handle the selection of a user from a list, typically in a RecyclerView.
 */
public interface UserListener {
    /**
     * Called when a user is clicked.
     *
     * @param user The User object representing the clicked user.
     */
    void onUserClicked(User user);
}
