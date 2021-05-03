package voteit.modules;

import voteit.libs.serverhttp.Response;

public class Constants {

    public static final String LOGINTOKENCOOKIEKEY = "LoginTokenUserIdName";
    public static final String SQLUNIQUEVIOLATION = "23505";

    public static Response genericServerError() {
        return new Response("Internal server error").setStatus(500);
    }

    public static Response genericNotFound() {
        return new Response("Not Found").setStatus(404);
    }

    public static Response genericNotImplemented() {
        return new Response("Not Implemented").setStatus(501);
    }

    public static Response genericJsonError(Exception e) {
        return new Response("JSON Invalid: " + e.getMessage()).setStatus(400);
    }
}
