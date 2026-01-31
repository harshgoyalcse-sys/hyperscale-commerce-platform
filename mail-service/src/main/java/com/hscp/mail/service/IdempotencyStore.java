package com.hscp.mail.service;

public interface IdempotencyStore {
    boolean isProcessed(String key);
    void markProcessed(String key);
}
