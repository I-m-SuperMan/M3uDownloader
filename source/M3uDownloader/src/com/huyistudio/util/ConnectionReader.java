package com.huyistudio.util;

public interface ConnectionReader<T> {
    boolean open();
    T read();
    void close();
}
