package voteit.handlers;

import java.sql.SQLException;

import voteit.Manager;
import voteit.libs.json.*;
import voteit.libs.serverhttp.Context;
import voteit.libs.serverhttp.Handler;
import voteit.libs.serverhttp.Response;
import voteit.modules.Constants;
import voteit.modules.LoginNotFoundException;

public class UserLoginHandler implements Handler {

    @Override
    public Response handle(Context context) {

        JsonParser parser = new JsonParser();
        JsonObject object = null;

        try {
            object = parser.buildObject(context.requestData);
        } catch (UnsupportedTypeException | JsonFormattingException e) {
            System.out.println("Json Error:");
            System.out.println(e.getMessage());
            return Constants.genericJsonError(e);
        }

        try {
            int token = Manager.loginUser(object.getString("user"), object.getString("password"));
            Response res = new Response("Logged in");
            res.setStatus(200);
            res.addCookie(Constants.LOGINTOKENCOOKIEKEY, String.valueOf(token));
            return res;
        } catch (SQLException | NullPointerException e) {
            System.out.println("Couldn't login user.");
            System.out.println(e.getMessage());
            return Constants.genericServerError();
        } catch (KeyNotFoundException | WrongTypeException e) {
            System.out.println("Login failed on json");
            System.out.println(e.getMessage());
            return Constants.genericJsonError(e);
        } catch (LoginNotFoundException e) {
            return new Response("Wrong username or password").setStatus(401);
        } // TODO Nullpointer message not found
    }

}
