/*
 * Copyright 2017 Idealnaya rabota LLC
 * Licensed under Multy.io license.
 * See LICENSE for details
 */

package io.multy.model.entities.wallet;


import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import io.multy.R;
import io.multy.api.socket.CurrenciesRate;
import io.multy.storage.RealmManager;
import io.multy.util.NativeDataHelper;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Wallet extends RealmObject implements WalletBalanceInterface {

    @SerializedName("currencyid")
    private int currencyId; //chain id
    @SerializedName("networkid")
    private int networkId; //id of network testnet, mainnet etc.
    @SerializedName("walletindex")
    private int index;
    @SerializedName("walletname")
    private String walletName;
    @SerializedName("lastactiontime")
    private long lastActionTime;
    @PrimaryKey
    @SerializedName("dateofcreation")
    private long dateOfCreation;
    @SerializedName("pending")
    private boolean pending;
    @SerializedName("address")
    private String creationAddress;

    private int fiatId; //id of chosen fiat currency for this walelt

    private String balance = "0"; //satoshi for btc,
    private String availableBalance = "0";

    @Nullable
    private EthWallet ethWallet;
    @Nullable
    private BtcWallet btcWallet;

    private BigInteger convertBalance(BigInteger divisor) {
        BigInteger value = new BigInteger(balance);
        if (value.longValue() == 0) {
            return value;
        } else {
            return new BigInteger(balance).divide(divisor);
        }
    }

    public WalletAddress getActiveAddress() {
        RealmList<WalletAddress> addresses = new RealmList<>();
        switch (NativeDataHelper.Blockchain.valueOf(currencyId)) {
            case BTC:
                addresses = getBtcWallet().getAddresses();
                break;
            case ETH:
                addresses = getEthWallet().getAddresses();
                break;
        }

        return addresses.get(addresses.size() - 1);
    }

    public List<WalletAddress> getAddresses() {
        switch (NativeDataHelper.Blockchain.valueOf(currencyId)) {
            case BTC:
                return getBtcWallet().getAddresses();
            case ETH:
                return getEthWallet().getAddresses();
        }

        return new ArrayList<>();
    }

    @Override
    public String getBalanceLabel() {
        switch (NativeDataHelper.Blockchain.valueOf(currencyId)) {
            case BTC:
                return convertBalance(BtcWallet.DIVISOR) + " BTC"; // not sure this is good decision //TODO investigate
            case ETH:
                return convertBalance(EthWallet.DIVISOR) + " ETH";
            default:
                return "unsupported";
        }
    }

    @Override
    public String getFiatBalanceLabel() {
        CurrenciesRate currenciesRate = RealmManager.getSettingsDao().getCurrenciesRate();
        //TODO support different fiat currencies here
        switch (NativeDataHelper.Blockchain.valueOf(currencyId)) {
            case BTC:
                return String.valueOf(convertBalance(BtcWallet.DIVISOR).doubleValue() * currenciesRate.getBtcToUsd() + getFiatString()); //convert from satoshi
            case ETH:
                return String.valueOf(convertBalance(EthWallet.DIVISOR).doubleValue() * currenciesRate.getEthToUsd() + getFiatString()); //convert from wev
            default:
                return "unsupported";
        }
    }

    public String getFiatString() {
        switch (fiatId) {
            default: return "$";
        }
    }

    @Override
    public int getIconResourceId() {
        switch (NativeDataHelper.Blockchain.valueOf(currencyId)) {
            case BTC:
                return networkId == NativeDataHelper.NetworkId.MAIN_NET.getValue() ? R.drawable.ic_btc : R.drawable.ic_chain_btc_test;
            case ETH:
                return networkId == NativeDataHelper.NetworkId.MAIN_NET.getValue() ? R.drawable.ic_eth_medium_icon : R.drawable.ic_chain_eth_test;
            default:
                return 0;
        }
    }

    public boolean isPayable() {
        switch (NativeDataHelper.Blockchain.valueOf(currencyId)) {
            case BTC:
                return getAvailableBalanceNumeric().longValue() > 150;
            case ETH:
                return true; //TODO change for positive balance ETH
        }
        return false;
    }

    public BigInteger getBalanceNumeric() {
        return new BigInteger(balance);
    }

    public BigInteger getPendingBalance() {
        return new BigInteger(balance).subtract(new BigInteger(availableBalance));
    }

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    public int getNetworkId() {
        return networkId;
    }

    public void setNetworkId(int networkId) {
        this.networkId = networkId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public long getLastActionTime() {
        return lastActionTime;
    }

    public void setLastActionTime(long lastActionTime) {
        this.lastActionTime = lastActionTime;
    }

    public long getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(long dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public int getFiatId() {
        return fiatId;
    }

    public void setFiatId(int fiatId) {
        this.fiatId = fiatId;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getAvailableBalance() {
        return availableBalance;
    }

    public BigInteger getAvailableBalanceNumeric() {
        return new BigInteger(availableBalance);
    }

    public void setAvailableBalance(String availableBalance) {
        this.availableBalance = availableBalance;
    }

    @Nullable
    public EthWallet getEthWallet() {
        return ethWallet;
    }

    public void setEthWallet(@Nullable EthWallet ethWallet) {
        this.ethWallet = ethWallet;
    }

    @Nullable
    public BtcWallet getBtcWallet() {
        return btcWallet;
    }

    public void setBtcWallet(@Nullable BtcWallet btcWallet) {
        this.btcWallet = btcWallet;
    }

    public String getCreationAddress() {
        return creationAddress;
    }

    public void setCreationAddress(String creationAddress) {
        this.creationAddress = creationAddress;
    }

    public long getId() {
        return dateOfCreation;
    }
}
