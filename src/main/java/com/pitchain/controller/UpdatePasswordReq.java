package com.pitchain.controller;

public record UpdatePasswordReq(
        String originPassword,
        String newPassword
) {
}
