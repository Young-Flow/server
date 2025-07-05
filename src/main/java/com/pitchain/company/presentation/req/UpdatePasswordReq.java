package com.pitchain.company.presentation.req;

public record UpdatePasswordReq(
        String originPassword,
        String newPassword
) {
}
