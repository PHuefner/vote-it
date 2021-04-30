package voteit.libs.json;

import java.util.HashMap;

public class JsonObject {
    private HashMap<String, Value> map;

    // Constructors

    public JsonObject() {
        this.map = new HashMap<String, Value>();
    }

    protected JsonObject(HashMap<String, Value> map) {
        this.map = map;
    }

    // Getter

    public String getString(String key) throws KeyNotFoundException, WrongTypeException {
        Value value = map.get(key);
        if (value == null) {
            throw new KeyNotFoundException("No value for key " + key);
        }
        try {
            return value.asString();
        } catch (WrongTypeException e) {
            throw new WrongTypeException(key + " : " + e.getMessage());
        }
    }

    public int getInteger(String key) throws KeyNotFoundException, WrongTypeException {
        Value value = map.get(key);
        if (value == null) {
            throw new KeyNotFoundException("No value for key " + key);
        }
        try {
            return value.asInt();
        } catch (WrongTypeException e) {
            throw new WrongTypeException(key + " : " + e.getMessage());
        }
    }

    public long getLong(String key) throws KeyNotFoundException, WrongTypeException {
        Value value = map.get(key);
        if (value == null) {
            throw new KeyNotFoundException("No value for key " + key);
        }
        try {
            return value.asLong();
        } catch (WrongTypeException e) {
            throw new WrongTypeException(key + " : " + e.getMessage());
        }
    }

    public double getDouble(String key) throws KeyNotFoundException, WrongTypeException {
        Value value = map.get(key);
        if (value == null) {
            throw new KeyNotFoundException("No value for key " + key);
        }
        try {
            return value.asDouble();
        } catch (WrongTypeException e) {
            throw new WrongTypeException(key + " : " + e.getMessage());
        }
    }

    public boolean getBool(String key) throws KeyNotFoundException, WrongTypeException {
        Value value = map.get(key);
        if (value == null) {
            throw new KeyNotFoundException("No value for key " + key);
        }
        try {
            return value.asBool();
        } catch (WrongTypeException e) {
            throw new WrongTypeException(key + " : " + e.getMessage());
        }
    }

    public JsonObject getObject(String key) throws KeyNotFoundException, WrongTypeException {
        Value value = map.get(key);
        if (value == null) {
            throw new KeyNotFoundException("No value for key " + key);
        }
        try {
            return value.asObject();
        } catch (WrongTypeException e) {
            throw new WrongTypeException(key + " : " + e.getMessage());
        }
    }

    public JsonArray getArray(String key) throws KeyNotFoundException, WrongTypeException {
        Value value = map.get(key);
        if (value == null) {
            throw new KeyNotFoundException("No value for key " + key);
        }
        try {
            return value.asArray();
        } catch (WrongTypeException e) {
            throw new WrongTypeException(key + " : " + e.getMessage());
        }
    }

    // Setter

    public void put(String key, String str) {
        map.put(key, new Value(str));
    }

    public void put(String key, int integer) {
        map.put(key, new Value(integer));
    }

    public void put(String key, double fp) {
        map.put(key, new Value(fp));
    }

    public void put(String key, boolean bool) {
        map.put(key, new Value(bool));
    }

    public void put(String key, JsonObject object) {
        map.put(key, new Value(object));
    }

    // Return as JSON

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("{");
        map.forEach((k, v) -> {
            out.append(String.format("\"%s\":%s,", k, v.toString()));
        });
        if (out.length() > 1) {
            out.deleteCharAt(out.length() - 1); // Delete trailing comma
        }
        out.append("}");
        return out.toString();
    }
}
