// Generated by view binder compiler. Do not edit!
package com.example.giaohang.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import com.example.giaohang.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentLoginBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final EditText email;

  @NonNull
  public final TextView forgotButton;

  @NonNull
  public final LinearLayout layout;

  @NonNull
  public final Button login;

  @NonNull
  public final EditText password;

  private FragmentLoginBinding(@NonNull LinearLayout rootView, @NonNull EditText email,
      @NonNull TextView forgotButton, @NonNull LinearLayout layout, @NonNull Button login,
      @NonNull EditText password) {
    this.rootView = rootView;
    this.email = email;
    this.forgotButton = forgotButton;
    this.layout = layout;
    this.login = login;
    this.password = password;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentLoginBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentLoginBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_login, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentLoginBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.email;
      EditText email = rootView.findViewById(id);
      if (email == null) {
        break missingId;
      }

      id = R.id.forgotButton;
      TextView forgotButton = rootView.findViewById(id);
      if (forgotButton == null) {
        break missingId;
      }

      LinearLayout layout = (LinearLayout) rootView;

      id = R.id.login;
      Button login = rootView.findViewById(id);
      if (login == null) {
        break missingId;
      }

      id = R.id.password;
      EditText password = rootView.findViewById(id);
      if (password == null) {
        break missingId;
      }

      return new FragmentLoginBinding((LinearLayout) rootView, email, forgotButton, layout, login,
          password);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
