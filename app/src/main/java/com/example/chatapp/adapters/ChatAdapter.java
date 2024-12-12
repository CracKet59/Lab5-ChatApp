package com.example.chatapp.adapters;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.databinding.ItemContainerReceivedMessageBinding;
import com.example.chatapp.databinding.ItemContainerSentMessageBinding;
import com.example.chatapp.models.ChatMessage;

import java.util.List;

/**
 * ChatAdapter is a RecyclerView.Adapter that handles displaying chat messages.
 * It supports two types of views: sent messages and received messages.
 */
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Bitmap receiverProfileImage;
    private final List<ChatMessage> chatMessages;

    private final String sendId;

    public static final int  VIEW_TYPE_SENT = 1;

    public static final int     VIEW_TYPE_RECEIVED = 2;


    /**
     * Constructor to initialize the ChatAdapter with chat messages, receiver's profile image, and sender ID.
     *
     * @param chatMessages       List of chat messages.
     * @param receiverProfileImage Bitmap of the receiver's profile image.
     * @param sendId             The ID of the sender (current user).
     */

    public ChatAdapter(List<ChatMessage>chatMessages,Bitmap receiverProfileImage, String sendId) {
        this.chatMessages = chatMessages;
        this.receiverProfileImage = receiverProfileImage;
        this.sendId = sendId;
    }
    /**
     * Inflates the appropriate view based on the view type (sent or received).
     *
     * @param parent   The parent ViewGroup into which the new view will be added.
     * @param viewType The type of the view to create.
     * @return A ViewHolder for the corresponding view type.
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_SENT) {
            return new SentMessageViewHolder(ItemContainerSentMessageBinding
                    .inflate(LayoutInflater.from(parent.getContext()),parent,false));
        }else {
            return new ReceiverMessageViewHolder(ItemContainerReceivedMessageBinding
                    .inflate(LayoutInflater.from(parent.getContext()),parent, false));
        }
    }
    /**
     * Binds data to the ViewHolder based on its position in the list.
     *
     * @param holder   The ViewHolder to bind data to.
     * @param position The position of the item within the adapter's data set.
     */

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_SENT) {
            ((SentMessageViewHolder)holder).setData(chatMessages.get(position));
        }else {
            ((ReceiverMessageViewHolder)holder).setData(chatMessages.get(position), receiverProfileImage);
        }
    }
    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items.
     */
    @Override
    public int getItemCount() {
        return chatMessages.size();
    }
    /**
     * Determines the view type for a given position in the list.
     *
     * @param position The position of the item.
     * @return The view type (VIEW_TYPE_SENT or VIEW_TYPE_RECEIVED).
     */
    @Override
    public int getItemViewType(int position) {
        if(chatMessages.get(position).senderId.equals(sendId)) {
            return VIEW_TYPE_SENT;
        }else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    static class SentMessageViewHolder extends RecyclerView.ViewHolder {

        private final ItemContainerSentMessageBinding binding;
        /**
         * Constructor to initialize the SentMessageViewHolder.
         *
         * @param itemContainerSentMessageBinding Binding for the sent message layout.
         */
        public SentMessageViewHolder(ItemContainerSentMessageBinding itemContainerSentMessageBinding) {
            super(itemContainerSentMessageBinding.getRoot());
            binding = itemContainerSentMessageBinding;
        }

        /**
         * Sets the data for the sent message view.
         *
         * @param chatMessage The ChatMessage object containing message details.
         */

        void setData(ChatMessage chatMessage) {
            binding.textMessage.setText(chatMessage.message);
            binding.textDateTime.setText(chatMessage.dateTime);

        }




    }


    static class ReceiverMessageViewHolder extends RecyclerView.ViewHolder {
        private final ItemContainerReceivedMessageBinding binding;
        public ReceiverMessageViewHolder(ItemContainerReceivedMessageBinding itemContainerReceivedMessageBinding) {
            super(itemContainerReceivedMessageBinding.getRoot());
            binding = itemContainerReceivedMessageBinding;
        }


        void setData(ChatMessage chatMessage, Bitmap receiverProfileImage ) {
            binding.textMessage.setText(chatMessage.message);
            binding.textDateTime.setText(chatMessage.dateTime);

            binding.imageProfile.setImageBitmap(receiverProfileImage);
        }
    }
}
