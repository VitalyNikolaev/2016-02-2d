package main;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.NewCookie;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class TokenManager {

    @NotNull
    public static String getNewRandomSessionID(Object... stringConvertibles) {
        final StringBuilder plainID = new StringBuilder(SALT);
        for (Object object : stringConvertibles)
            plainID.append(object.toString());

        return Base64.getEncoder().encodeToString(plainID.toString().getBytes(StandardCharsets.UTF_8));
    }

    @NotNull
    public static NewCookie getNewCookieWithSessionID(String sessionID) {
        return getNewCookie(sessionID, COOKIE_MAX_AGE);
    }

    @NotNull
    public static NewCookie getNewNullCookie() {
        return getNewCookie(null, 0);
    }

    @Nullable
    public static String getSIDStringFromHeaders(HttpHeaders headers) {
        if (headers.getCookies().containsKey(TokenManager.COOKIE_NAME))
            return headers.getCookies().get(COOKIE_NAME).toString();
        else return null;
    }

    private static NewCookie getNewCookie(@Nullable String sessionID, int maxAge) {
        String sessionID1 = sessionID;
        if (sessionID1 == null)
            sessionID1 = "";// TODO: Sure it always will be localhost?
        return new NewCookie(COOKIE_NAME, sessionID1, "/", "localhost", "This cookie is used to authenticate users in the Bomberman game.", maxAge, false);
    }

    public static final String COOKIE_NAME = "BOMBERMAN-SESSION-TOKEN";

    private static final int COOKIE_MAX_AGE = 3600;     // 1 hour by default; self-updating on some requests.
    private static final String SALT = "Just some words.";
}