package voteit.handlers;

import java.sql.SQLException;

import voteit.VoteitDB;
import voteit.libs.json.JsonArray;
import voteit.libs.json.JsonFormattingException;
import voteit.libs.json.JsonObject;
import voteit.libs.json.JsonParser;
import voteit.libs.json.KeyNotFoundException;
import voteit.libs.json.UnsupportedTypeException;
import voteit.libs.json.WrongTypeException;
import voteit.libs.serverhttp.*;
import voteit.modules.Constants;

public class TopicPostHandler implements Handler {

    @Override
    public Response handle(Context context) {

        JsonParser parser = new JsonParser();

        if (context.route.contains("edit")) {
            JsonObject object = null;
            try {
                object = parser.buildObject(context.requestData);
                VoteitDB.updateTopic(object);
            } catch (KeyNotFoundException | WrongTypeException | UnsupportedTypeException | JsonFormattingException e) {
                System.out.println("Json error on edit topic");
                System.out.println(e.getMessage());
                return Constants.genericJsonError(e);
            } catch (SQLException e) {
                return Constants.genericServerError();
            }

            return new Response("Topic updated");

        } else if (context.route.contains("delete")) {
            JsonObject object = null;

            try {
                object = parser.buildObject(context.requestData);
                VoteitDB.deleteTopic(object.getInteger("topicId"));
            } catch (KeyNotFoundException | WrongTypeException | UnsupportedTypeException | JsonFormattingException e) {
                System.out.println("Json error on delete topic");
                System.out.println(e.getMessage());
                return Constants.genericJsonError(e);
            } catch (SQLException e) {
                return Constants.genericServerError();
            }

            return new Response("Topic deleted");

        } else if (context.route.contains("vote")) {
            
            try {
                JsonArray array = parser.buildArray(context.requestData);
                VoteitDB.voteForTopic(array.getObject(0).getInteger("topicId"), array.getObject(1).getInteger("userId"));
            } catch (JsonFormattingException | KeyNotFoundException | WrongTypeException | UnsupportedTypeException e) {
                System.out.println(e.getMessage());
                return Constants.genericJsonError(e);
            } catch (SQLException e) {
                return Constants.genericServerError();
            }

            return new Response("Voted succesfully");
        } else if (context.route.contains("submit")) {
            JsonObject object = null;

            try {
                object = parser.buildObject(context.requestData);
                VoteitDB.addTopic(object);
            } catch (SQLException e) {
                return Constants.genericServerError();
            } catch (KeyNotFoundException | WrongTypeException | UnsupportedTypeException | JsonFormattingException e) {
                System.out.println("Json error on submit topic");
                System.out.println(e.getMessage());
                return Constants.genericJsonError(e);
            }

            return new Response("Topic submitted");
        } else {
            return Constants.genericNotFound();
        }
    }

}
