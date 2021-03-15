package voteit.libs.json;

import java.util.ArrayList;

public class JsonArray {
    ArrayList<Value> values;

    // Constructors

    public JsonArray() {
        this.values = new ArrayList<Value>();
    }

    protected JsonArray(ArrayList<Value> values) {
        this.values = values;
    }

    // Getter

    public String getString(int index) throws KeyNotFoundException, WrongTypeException {
        Value value = values.get(index);
        if (value == null) {
            throw new KeyNotFoundException("No value for key " + index);
        }
        try {
            return value.asString();
        } catch (WrongTypeException e) {
            throw new WrongTypeException(index + " : " + e.getMessage());
        }
    }

    public int getInteger(int index) throws KeyNotFoundException, WrongTypeException {
        Value value = values.get(index);
        if (value == null) {
            throw new KeyNotFoundException("No value for key " + index);
        }
        try {
            return value.asInt();
        } catch (WrongTypeException e) {
            throw new WrongTypeException(index + " : " + e.getMessage());
        }
    }

    public double getDouble(int index) throws KeyNotFoundException, WrongTypeException {
        Value value = values.get(index);
        if (value == null) {
            throw new KeyNotFoundException("No value for key " + index);
        }
        try {
            return value.asDouble();
        } catch (WrongTypeException e) {
            throw new WrongTypeException(index + " : " + e.getMessage());
        }
    }

    public boolean getBool(int index) throws KeyNotFoundException, WrongTypeException {
        Value value = values.get(index);
        if (value == null) {
            throw new KeyNotFoundException("No value for key " + index);
        }
        try {
            return value.asBool();
        } catch (WrongTypeException e) {
            throw new WrongTypeException(index + " : " + e.getMessage());
        }
    }

    public JsonObject getObject(int index) throws KeyNotFoundException, WrongTypeException {
        Value value = values.get(index);
        if (value == null) {
            throw new KeyNotFoundException("No value for key " + index);
        }
        try {
            return value.asObject();
        } catch (WrongTypeException e) {
            throw new WrongTypeException(index + " : " + e.getMessage());
        }
    }

    public JsonArray getArray(int index) throws KeyNotFoundException, WrongTypeException {
        Value value = values.get(index);
        if (value == null) {
            throw new KeyNotFoundException("No value for key " + index);
        }
        try {
            return value.asArray();
        } catch (WrongTypeException e) {
            throw new WrongTypeException(index + " : " + e.getMessage());
        }
    }

    public void add(String string) {
        values.add(new Value(string));
    }

    public void add(int integer) {
        values.add(new Value(integer));
    }

    public void add(double fp) {
        values.add(new Value(fp));
    }

    public void add(boolean bool) {
        values.add(new Value(bool));
    }

    public void add(JsonObject object) {
        values.add(new Value(object));
    }

    public void add(JsonArray array) {
        values.add(new Value(array));
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("[");
        values.forEach((v) -> {
            out.append(String.format("%s,", v.toString()));
        });
        if (out.length() > 1) {
            out.deleteCharAt(out.length() - 1); // Delete trailing comma
        }
        out.append("]");
        return out.toString();
    }

}
