package com.daniel.app.airbnb.backend.util;

import java.util.UUID;

public class TokenUtil {
    public static String confirmationToken() {
        return UUID.randomUUID().toString();
    }


}
