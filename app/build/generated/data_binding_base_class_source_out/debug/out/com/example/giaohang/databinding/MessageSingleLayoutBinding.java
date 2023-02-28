// Generated by view binder compiler. Do not edit!
package com.example.giaohang.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import com.example.giaohang.R;
import de.hdodenhof.circleimageview.CircleImageView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class MessageSingleLayoutBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final ImageView messageImageLayout;

  @NonNull
  public final CircleImageView messageProfileLayout;

  @NonNull
  public final RelativeLayout messageSingleLayout;

  @NonNull
  public final TextView messageTextLayout;

  @NonNull
  public final TextView nameTextLayout;

  @NonNull
  public final TextView timeTextLayout;

  private MessageSingleLayoutBinding(@NonNull RelativeLayout rootView,
      @NonNull ImageView messageImageLayout, @NonNull CircleImageView messageProfileLayout,
      @NonNull RelativeLayout messageSingleLayout, @NonNull TextView messageTextLayout,
      @NonNull TextView nameTextLayout, @NonNull TextView timeTextLayout) {
    this.rootView = rootView;
    this.messageImageLayout = messageImageLayout;
    this.messageProfileLayout = messageProfileLayout;
    this.messageSingleLayout = messageSingleLayout;
    this.messageTextLayout = messageTextLayout;
    this.nameTextLayout = nameTextLayout;
    this.timeTextLayout = timeTextLayout;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static MessageSingleLayoutBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static MessageSingleLayoutBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.message_single_layout, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static MessageSingleLayoutBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.message_image_layout;
      ImageView messageImageLayout = rootView.findViewById(id);
      if (messageImageLayout == null) {
        break missingId;
      }

      id = R.id.message_profile_layout;
      CircleImageView messageProfileLayout = rootView.findViewById(id);
      if (messageProfileLayout == null) {
        break missingId;
      }

      RelativeLayout messageSingleLayout = (RelativeLayout) rootView;

      id = R.id.message_text_layout;
      TextView messageTextLayout = rootView.findViewById(id);
      if (messageTextLayout == null) {
        break missingId;
      }

      id = R.id.name_text_layout;
      TextView nameTextLayout = rootView.findViewById(id);
      if (nameTextLayout == null) {
        break missingId;
      }

      id = R.id.time_text_layout;
      TextView timeTextLayout = rootView.findViewById(id);
      if (timeTextLayout == null) {
        break missingId;
      }

      return new MessageSingleLayoutBinding((RelativeLayout) rootView, messageImageLayout,
          messageProfileLayout, messageSingleLayout, messageTextLayout, nameTextLayout,
          timeTextLayout);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
