package com.github.etcmanila.kepniah.chainbuilder.sender;

public class ChainMessage<T> {
    private String address;
    private T body;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

}
