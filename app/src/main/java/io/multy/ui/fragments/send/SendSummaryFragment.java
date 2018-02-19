/*
 * Copyright 2017 Idealnaya rabota LLC
 * Licensed under Multy.io license.
 * See LICENSE for details
 */

package io.multy.ui.fragments.send;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ScrollView;
import android.widget.TextView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTouch;
import io.multy.R;
import io.multy.api.MultyApi;
import io.multy.api.socket.CurrenciesRate;
import io.multy.model.entities.wallet.CurrencyCode;
import io.multy.model.entities.wallet.RecentAddress;
import io.multy.model.requests.HdTransactionRequestEntity;
import io.multy.storage.RealmManager;
import io.multy.ui.fragments.BaseFragment;
import io.multy.ui.fragments.dialogs.CompleteDialogFragment;
import io.multy.util.Constants;
import io.multy.util.CryptoFormatUtils;
import io.multy.util.NativeDataHelper;
import io.multy.util.NumberFormatter;
import io.multy.util.analytics.Analytics;
import io.multy.util.analytics.AnalyticsConstants;
import io.multy.viewmodels.AssetSendViewModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class SendSummaryFragment extends BaseFragment {

    private static final String TAG = SendSummaryFragment.class.getSimpleName();
    public static final String TAG_SEND_SUCCESS = SendSummaryFragment.class.getSimpleName();

    @BindView(R.id.text_receiver_balance_original)
    TextView textReceiverBalanceOriginal;
    @BindView(R.id.text_receiver_balance_currency)
    TextView textReceiverBalanceCurrency;
    @BindView(R.id.text_receiver_address)
    TextView textReceiverAddress;
    @BindView(R.id.text_wallet_name)
    TextView textWalletName;
    @BindView(R.id.text_sender_balance_original)
    TextView textSenderBalanceOriginal;
    @BindView(R.id.text_sender_balance_currency)
    TextView textSenderBalanceCurrency;
    @BindView(R.id.text_fee_speed)
    TextView textFeeSpeed;
    @BindView(R.id.text_fee_amount)
    TextView textFeeAmount;
    @BindView(R.id.text_fee_speed_label)
    TextView textFeeSpeedLabel;
    @BindView(R.id.button_next)
    View buttonNext;
    @BindView(R.id.input_note)
    View inputNote;
    @BindView(R.id.image_slider)
    View slider;
    @BindView(R.id.scrollview)
    ScrollView scrollView;

    @BindString(R.string.donation_format_pattern)
    String formatPattern;
    @BindString(R.string.donation_format_pattern_bitcoin)
    String formatPatternBitcoin;

    private AssetSendViewModel viewModel;

    public static SendSummaryFragment newInstance() {
        return new SendSummaryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_summary, container, false);
        ButterKnife.bind(this, view);
        viewModel = ViewModelProviders.of(getActivity()).get(AssetSendViewModel.class);
        setBaseViewModel(viewModel);
        subscribeToErrors();
        setInfo();
        viewModel.setAmountScanned(false);
        inputNote.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                Analytics.getInstance(getActivity()).logSendSummary(AnalyticsConstants.SEND_SUMMARY_NOTE, viewModel.getChainId());
            }
        });
        Analytics.getInstance(getActivity()).logSendSummaryLaunch(viewModel.getChainId());
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        buttonNext.postDelayed(() -> {
            if (getActivity() != null) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null && imm.isActive())
                    imm.hideSoftInputFromWindow(buttonNext.getWindowToken(), 0);
            }
        }, 150);
    }

    @Override
    public void onResume() {
        super.onResume();
        startSlideAnimation();
    }

    @Override
    public void onPause() {
        stopSlideAnimation();
        super.onPause();
    }


    private void send() {
        viewModel.isLoading.setValue(true);
        String addressTo = viewModel.getReceiverAddress().getValue();

        try {
            viewModel.isLoading.setValue(true);
            byte[] seed = RealmManager.getSettingsDao().getSeed().getSeed();
            final int addressesSize = viewModel.getWallet().getAddresses().size();
            final String changeAddress = NativeDataHelper.makeAccountAddress(seed, viewModel.getWallet().getWalletIndex(), addressesSize, NativeDataHelper.Currency.BTC.getValue());
            final String hex = viewModel.transaction.getValue();
            Timber.i("hex=%s", hex);
            MultyApi.INSTANCE.sendHdTransaction(new HdTransactionRequestEntity(
                    NativeDataHelper.Currency.BTC.getValue(),
                    new HdTransactionRequestEntity.Payload(changeAddress, addressesSize, viewModel.getWallet().getWalletIndex(), hex))).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        viewModel.isLoading.postValue(false);
                        RealmManager.getAssetsDao().saveRecentAddress(new RecentAddress(NativeDataHelper.Currency.BTC.getValue(), addressTo));
                        CompleteDialogFragment.newInstance(viewModel.getChainId()).show(getActivity().getSupportFragmentManager(), TAG_SEND_SUCCESS);
                    } else {
                        Analytics.getInstance(getActivity()).logError(AnalyticsConstants.ERROR_TRANSACTION_API);
                        showError();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                    showError();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            showError();
        }
    }

    private void setInfo() {
        CurrenciesRate currenciesRate = RealmManager.getSettingsDao().getCurrenciesRate();
        textReceiverBalanceOriginal.setText(NumberFormatter.getInstance().format(viewModel.getAmount()));
        textReceiverBalanceOriginal.append(Constants.SPACE);
        textReceiverBalanceOriginal.append(CurrencyCode.BTC.name());
        textReceiverBalanceCurrency.setText(NumberFormatter.getFiatInstance().format(viewModel.getAmount() * currenciesRate.getBtcToUsd()));
        textReceiverBalanceCurrency.append(Constants.SPACE);
        textReceiverBalanceCurrency.append(CurrencyCode.USD.name());
//        textReceiverAddress.setText(viewModel.getReceiverAddress().getValue());
        textReceiverAddress.setText(viewModel.thoseAddress.getValue());
        textWalletName.setText(viewModel.getWallet().getName());
        double balance = viewModel.getWallet().getBalance();
        textSenderBalanceOriginal.setText(balance != 0 ? CryptoFormatUtils.satoshiToBtc(balance) + " BTC" : String.valueOf(balance));
        textSenderBalanceCurrency.setText(String.format("%s USD", NumberFormatter.getFiatInstance().format(viewModel.getAmount() * currenciesRate.getBtcToUsd())));
        textFeeSpeed.setText(viewModel.getFee().getName());
        textFeeSpeedLabel.setText(viewModel.getFee().getTime());

        MutableLiveData<Long> transactionPrice = AssetSendViewModel.transactionPrice;
        if (transactionPrice != null) {
            textFeeAmount.setText(String.format("%s BTC / %s USD", CryptoFormatUtils.satoshiToBtc(transactionPrice.getValue()), CryptoFormatUtils.satoshiToUsd(transactionPrice.getValue())));
        }
    }

    private void showError() {
        viewModel.isLoading.postValue(false);
        viewModel.errorMessage.postValue(getString(R.string.error_sending_tx));
    }

    private void startSlideAnimation() {
        slider.animate().cancel();
        slider.animate().setStartDelay(2000).withEndAction(() ->
                slider.animate().setStartDelay(0).withEndAction(this::slideAnimation).start())
                .start();
    }

    private void slideAnimation() {
        float dragRange = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
        int dragDuration = 700;
        slider.animate()
                .translationX(dragRange)
                .setDuration(dragDuration)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .withEndAction(() ->
                        slider.animate()
                                .translationX(0)
                                .setDuration(dragDuration)
                                .setInterpolator(new AccelerateDecelerateInterpolator())
                                .withEndAction(this::slideAnimation));
    }

    private void stopSlideAnimation() {
        slider.animate().cancel();
    }

    private void moveSliderToNextPoint(float rawX) {
        rawX -= slider.getWidth() / 2;
        slider.setTranslationX(rawX);
        if (slider.getX() + slider.getWidth() > buttonNext.getX() + buttonNext.getWidth()) {
            send();
        }
    }

    private void returnSliderOnStart() {
        slider.setTranslationX(0f);
        startSlideAnimation();
    }

    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for (byte b : a)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }

    @OnTouch(R.id.image_slider)
    boolean OnSliderTouch(View v, MotionEvent ev) {
        scrollView.requestDisallowInterceptTouchEvent(true);
        stopSlideAnimation();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_MOVE:
                moveSliderToNextPoint(ev.getRawX());
                return true;
            case MotionEvent.ACTION_UP:
                returnSliderOnStart();
            case MotionEvent.ACTION_CANCEL:
                return true;
                default: return false;
        }
    }
}
