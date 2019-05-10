package com.example.parkingsystem;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDialog;
import android.widget.ImageView;
/*
로딩-프래그먼트에 사용할
 */
public class BaseFragment extends Fragment {
    private AppCompatDialog loading_dialog;

    public void show_loading() {
        if ( loading_dialog == null) {
            loading_dialog = new AppCompatDialog(getActivity());
            loading_dialog.setCancelable(false);
        }

        loading_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading_dialog.setContentView(R.layout.progress_loading);
        loading_dialog.show();

        ImageView frame_loading = (ImageView) loading_dialog.findViewById(R.id.loading_view);
        AnimationDrawable animationDrawable = (AnimationDrawable) frame_loading.getDrawable();
        animationDrawable.start();
    }

    public void hide_loading() {
        if ( loading_dialog != null && loading_dialog.isShowing() ) {
            loading_dialog.dismiss();
        }
    }
}
