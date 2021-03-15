package voteit.libs.json;

/**
 * Value
 *
 * Wrapper class for a value of an unknown datatype. Throws an exception if
 * wrong datatype is requested
 */
class Value {
    String str;
    int integer;
    double fp;
    boolean bool;
    JsonObject object;
    JsonArray array;
    TYPE type;

    Value(String str) {
        this.str = str;
        type = TYPE.STRING;
    }

    Value(int integer) {
        this.integer = integer;
        type = TYPE.INTEGER;
    }

    Value(double fp) {
        this.fp = fp;
        type = TYPE.DOUBLE;
    }

    Value(boolean bool) {
        this.bool = bool;
        type = TYPE.BOOLEAN;
    }

    Value(JsonObject object) {
        this.object = object;
        type = TYPE.OBJECT;
    }

    Value(JsonArray array) {
        this.array = array;
        type = TYPE.ARRAY;
    }

    public String asString() throws WrongTypeException {
        if (str != null) {
            return str;
        } else {
            throw new WrongTypeException("Value is not a String");
        }
    }

    public int asInt() throws WrongTypeException {
        if (type == TYPE.INTEGER) {
            return integer;
        } else {
            throw new WrongTypeException("Value is not a Integer");
        }
    }

    public double asDouble() throws WrongTypeException {
        if (type == TYPE.DOUBLE) {
            return fp;
        } else {
            throw new WrongTypeException("Value is not a Double");
        }
    }

    public boolean asBool() throws WrongTypeException {
        if (type == TYPE.BOOLEAN) {
            return bool;
        } else {
            throw new WrongTypeException("Value is not a Boolean");
        }
    }

    public JsonObject asObject() throws WrongTypeException {
        if (type == TYPE.OBJECT) {
            return object;
        } else {
            throw new WrongTypeException("Value is not a JsonObject");
        }
    }

    public JsonArray asArray() throws WrongTypeException {
        if (type == TYPE.ARRAY) {
            return array;
        } else {
            throw new WrongTypeException("Value is not a JsonArray");
        }
    }

    @Override
    public String toString() {

        try {
            switch (type) {
                case STRING:
                    return "\"" + str + "\"";
                case INTEGER:
                    return Integer.toString(integer);
                case DOUBLE:
                    return Double.toString(fp);
                case BOOLEAN:
                    return Boolean.toString(bool);
                case OBJECT:
                    return object.toString();
                case ARRAY:
                    return array.toString();
                default:
                    return null;

            }
        } catch (NullPointerException e) {
            return "null";
        }
    }

    enum TYPE {
        STRING, INTEGER, DOUBLE, BOOLEAN, OBJECT, ARRAY
    }

}
