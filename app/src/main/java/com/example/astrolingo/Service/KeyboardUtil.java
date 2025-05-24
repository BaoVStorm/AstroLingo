package com.example.astrolingo.Service;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

public class KeyboardUtil {

    // Gọi để ẩn bàn phím thủ công
    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                view.clearFocus();
            }
        }
    }

    // Lắng nghe sự kiện bàn phím mở hoặc đóng
    public static void setKeyboardVisibilityListener(View rootView, KeyboardVisibilityListener listener) {
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect r = new Rect();
            rootView.getWindowVisibleDisplayFrame(r);
            int screenHeight = rootView.getRootView().getHeight();
            int heightDiff = screenHeight - r.height();

            boolean isKeyboardVisible = heightDiff > screenHeight * 0.15;

            if (listener != null) {
                listener.onKeyboardVisibilityChanged(isKeyboardVisible);
            }
        });
    }

    // Giao diện callback
    public interface KeyboardVisibilityListener {
        void onKeyboardVisibilityChanged(boolean isVisible);
    }
}
