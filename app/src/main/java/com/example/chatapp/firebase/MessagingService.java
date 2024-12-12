package com.example.chatapp.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
/**
 * MessagingService is a FirebaseMessagingService that handles Firebase Cloud Messaging (FCM) events.
 * It receives notifications and manages FCM token updates.
 */


public class MessagingService extends FirebaseMessagingService {
    /**
     * Called when a new FCM token is generated for the device.
     * This method is invoked when:
     * - The app is installed for the first time.
     * - The token is refreshed (e.g., due to security reasons or manual refresh).
     *
     * @param token The newly generated FCM token.
     */
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d("FCM","Token: " +token);
    }
    /**
     * Called when an FCM message is received while the app is in the foreground or background.
     * This method handles both data messages and notification messages.
     *
     * @param message The RemoteMessage object containing the FCM message data.
     */

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        Log.d("FCM","460 Message: " + message.getNotification().getBody());
    }
}
