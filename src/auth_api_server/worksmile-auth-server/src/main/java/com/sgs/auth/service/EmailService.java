package com.sgs.auth.service;

public interface EmailService {
    void sendMail(String to, String sub, String text);
}
