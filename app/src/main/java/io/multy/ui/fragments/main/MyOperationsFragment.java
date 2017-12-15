/*
 * Copyright 2017 Idealnaya rabota LLC
 * Licensed under Multy.io license.
 * See LICENSE for details
 */

package io.multy.ui.fragments.main;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.multy.R;
import io.multy.ui.activities.AssetRequestActivity;
import io.multy.ui.activities.AssetSendActivity;
import io.multy.ui.activities.MainActivity;
import io.multy.ui.fragments.BaseFragment;
import io.multy.viewmodels.FeedViewModel;

/**
 * Created by Ihar Paliashchuk on 02.11.2017.
 * ihar.paliashchuk@gmail.com
 */

public class MyOperationsFragment extends BaseFragment {

    public static final String TAG = MyOperationsFragment.class.getSimpleName();

    private FeedViewModel viewModel;

    public static MyOperationsFragment newInstance() {
        return new MyOperationsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setVisibilityContainer(View.VISIBLE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        DataBindingUtil.inflate(inflater, R.layout.fragment_feed, container, false);
        View view = inflater.inflate(R.layout.fragment_fast_operations, container, false);
        ButterKnife.bind(this, view);
        viewModel = ViewModelProviders.of(this).get(FeedViewModel.class);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        setVisibilityContainer(View.GONE);
    }

    private void setVisibilityContainer(int visibility) {
        View fullContainer = getActivity().findViewById(R.id.full_container);
        if (fullContainer != null) {
            fullContainer.setVisibility(visibility);
        }
    }

    @OnClick(R.id.button_send)
    void onSendClick() {
        startActivity(new Intent(getContext(), AssetSendActivity.class));
    }

    @OnClick(R.id.button_receive)
    void onReceiveClick() {
        startActivity(new Intent(getContext(), AssetRequestActivity.class));
    }

    @OnClick(R.id.button_nfc)
    void onNfcClick() {
    }

    @OnClick(R.id.button_scan_qr)
    void onScanClick() {
        ((MainActivity) getActivity()).showScanScreen();
    }

    @OnClick(R.id.button_cancel)
    void onCancelClick() {
        getActivity().onBackPressed();
    }


//    public void showScanScreen() {
//        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, Constants.CAMERA_REQUEST_CODE);
//        } else {
//            startActivityForResult(new Intent(getContext(), ScanActivity.class), Constants.CAMERA_REQUEST_CODE);
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == Constants.CAMERA_REQUEST_CODE) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                showScanScreen();
//            }
//        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == Constants.CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//            if (data.hasExtra(Constants.EXTRA_QR_CONTENTS)) {
//                startActivity(new Intent(getContext(), AssetSendActivity.class)
//                        .putExtra(Constants.EXTRA_ADDRESS, data.getStringExtra(Constants.EXTRA_QR_CONTENTS))
//                        .putExtra(Constants.EXTRA_AMOUNT, data.getStringExtra(Constants.EXTRA_AMOUNT)));
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
}