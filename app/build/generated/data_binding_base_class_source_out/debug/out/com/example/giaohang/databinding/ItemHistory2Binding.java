// Generated by view binder compiler. Do not edit!
package com.example.giaohang.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import com.example.giaohang.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ItemHistory2Binding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final TextView rideId2;

  @NonNull
  public final TextView timestamp2;

  private ItemHistory2Binding(@NonNull LinearLayout rootView, @NonNull TextView rideId2,
      @NonNull TextView timestamp2) {
    this.rootView = rootView;
    this.rideId2 = rideId2;
    this.timestamp2 = timestamp2;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ItemHistory2Binding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ItemHistory2Binding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.item_history2, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ItemHistory2Binding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.rideId2;
      TextView rideId2 = rootView.findViewById(id);
      if (rideId2 == null) {
        break missingId;
      }

      id = R.id.timestamp2;
      TextView timestamp2 = rootView.findViewById(id);
      if (timestamp2 == null) {
        break missingId;
      }

      return new ItemHistory2Binding((LinearLayout) rootView, rideId2, timestamp2);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
