// Generated by view binder compiler. Do not edit!
package com.example.giaohang.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.viewbinding.ViewBinding;
import com.example.giaohang.R;
import java.lang.NullPointerException;
import java.lang.Override;

public final class AppBarLayoutCustBinding implements ViewBinding {
  @NonNull
  private final Toolbar rootView;

  private AppBarLayoutCustBinding(@NonNull Toolbar rootView) {
    this.rootView = rootView;
  }

  @Override
  @NonNull
  public Toolbar getRoot() {
    return rootView;
  }

  @NonNull
  public static AppBarLayoutCustBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static AppBarLayoutCustBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.app_bar_layout_cust, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static AppBarLayoutCustBinding bind(@NonNull View rootView) {
    if (rootView == null) {
      throw new NullPointerException("rootView");
    }

    return new AppBarLayoutCustBinding((Toolbar) rootView);
  }
}
