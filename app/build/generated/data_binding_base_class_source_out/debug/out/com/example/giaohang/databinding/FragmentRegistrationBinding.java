// Generated by view binder compiler. Do not edit!
package com.example.giaohang.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import com.example.giaohang.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentRegistrationBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final EditText email;

  @NonNull
  public final LinearLayout layout;

  @NonNull
  public final EditText password;

  @NonNull
  public final Button register;

  private FragmentRegistrationBinding(@NonNull LinearLayout rootView, @NonNull EditText email,
      @NonNull LinearLayout layout, @NonNull EditText password, @NonNull Button register) {
    this.rootView = rootView;
    this.email = email;
    this.layout = layout;
    this.password = password;
    this.register = register;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentRegistrationBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentRegistrationBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_registration, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentRegistrationBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.email;
      EditText email = rootView.findViewById(id);
      if (email == null) {
        break missingId;
      }

      LinearLayout layout = (LinearLayout) rootView;

      id = R.id.password;
      EditText password = rootView.findViewById(id);
      if (password == null) {
        break missingId;
      }

      id = R.id.register;
      Button register = rootView.findViewById(id);
      if (register == null) {
        break missingId;
      }

      return new FragmentRegistrationBinding((LinearLayout) rootView, email, layout, password,
          register);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
