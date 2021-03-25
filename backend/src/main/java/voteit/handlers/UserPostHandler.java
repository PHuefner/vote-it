package voteit.handlers;

import java.sql.SQLException;

import voteit.Manager;
import voteit.libs.json.JsonFormattingException;
import voteit.libs.json.JsonObject;
import voteit.libs.json.JsonParser;
import voteit.libs.json.KeyNotFoundException;
import voteit.libs.json.UnsupportedTypeException;
import voteit.libs.json.WrongTypeException;
import voteit.libs.serverhttp.Context;
import voteit.libs.serverhttp.Handler;
import voteit.libs.serverhttp.Response;
import voteit.modules.Constants;
import voteit.modules.LoginNotFoundException;

public class UserPostHandler implements Handler {

    @Override
    public Response handle(Context context) {
        // TODO Auto-generated method stub

        JsonParser parser = new JsonParser();

        if (context.route.contains("login")) {
            JsonObject object = null;
            try {
                object = parser.buildObject(context.requestData);
            } catch (JsonFormattingException e) {
                System.out.println("Couldn't format JsonString.");
                System.out.println(e.getMessage());
                return new Response("An error occured. Please try again.");
            } catch (UnsupportedTypeException e) {
                System.out.println(e.getMessage());
                return new Response("An error occured. Please try again.");
            }

            try {
                int token = Manager.loginUser(object.getString("user"), object.getString("password"));
                Response res = new Response("");
                res.setStatus(200);
                res.addCookie(Constants.LOGINTOKENCOOKIEKEY, String.valueOf(token));
                return res;
            } catch (KeyNotFoundException | WrongTypeException | SQLException | NullPointerException
                    | LoginNotFoundException e) {
                System.out.println("Couldn't login user.");
                System.out.println(e.getMessage());
                return new Response("An error occured. Please try again.");
            } // TODO Nullpointer message not found
        }

        return null;
    }

}
