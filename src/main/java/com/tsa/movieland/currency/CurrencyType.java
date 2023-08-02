package com.tsa.movieland.currency;

public enum CurrencyType {
    EUR(978),
    USD(840);

    private final int code;

    CurrencyType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
