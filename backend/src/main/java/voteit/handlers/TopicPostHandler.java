package voteit.handlers;

import voteit.Manager;
import voteit.libs.json.JsonFormattingException;
import voteit.libs.json.JsonObject;
import voteit.libs.json.JsonParser;
import voteit.libs.json.KeyNotFoundException;
import voteit.libs.json.UnsupportedTypeException;
import voteit.libs.json.WrongTypeException;
import voteit.libs.serverhttp.*;

public class TopicPostHandler implements Handler {

    @Override
    public Response handle(Context context) {

        JsonParser parser = new JsonParser();

        if (context.route.contains("edit")) {
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

            Manager.updateTopic(object);

            Response res = new Response("");
            res.setStatus(200);
            return res;

        } else if (context.route.contains("delete")) {
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
                Manager.deleteTopic(object.getInteger("topicId"));
            } catch (NullPointerException | KeyNotFoundException | WrongTypeException e) {
                System.out.println("Couldn't delete topic.");
                System.out.println(e.getMessage());
                return new Response("An error occured. Please try again.");
            }

            Response res = new Response("");
            res.setStatus(200);
            return res;

        } else if (context.route.contains("vote")) {
            return null;

        } else if (context.route.contains("submit")) {
            JsonObject object = null;
            try {
                object = parser.buildObject(context.requestData);
            } catch (JsonFormattingException e) {
                System.out.println("Couldn't format JsonString.");
                System.out.println(e.getMessage());
                return new Response("An error occured. Please try again.");
            } catch (UnsupportedTypeException e) {
                e.printStackTrace();
                return new Response("An error occured. Please try again.");
            }

            Manager.addTopic(object);

            Response res = new Response("");
            res.setStatus(200);
            return res;

        } else {
            return new Response("Wrong url path.");
        }
    }

}
