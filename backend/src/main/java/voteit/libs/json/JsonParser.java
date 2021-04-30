package voteit.libs.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

import voteit.libs.libutils.Utils;

/**
 * JSON
 *
 * Creates a HashMap from a JSON string
 *
 * Only supports simple datatypes like string, integer, double, boolean
 *
 * (no arrays, inner objects, etc.)
 *
 */

public class JsonParser {

    private char c; // Current character
    private Iterator<Character> iter; // Iterator over the JSON String
    private STATE state; // Current parser state
    private boolean started = false; // Did parser already start (used for inner objects)

    public static JsonObject parseObject(String jsonString) throws JsonFormattingException, UnsupportedTypeException {
        return new JsonParser().buildObject(jsonString);
    }

    public static JsonArray parseArray(String jsonString) throws JsonFormattingException, UnsupportedTypeException {
        return new JsonParser().buildArray(jsonString);
    }

    public JsonObject buildObject(String jsonString) throws JsonFormattingException, UnsupportedTypeException {
        iter = Utils.stringToCharList(jsonString).iterator();
        JsonObject object = buildObjectWithIter();
        started = false; // Reset for reuse
        return object;
    }

    private JsonObject buildObjectWithIter() throws JsonFormattingException, UnsupportedTypeException {
        HashMap<String, Value> map = new HashMap<>();

        /**
         * State works like this:
         *
         * 1. START: read ({) and start parser
         *
         * 2. NEXTATTRIBUTE: read a character start reading the next attribute
         *
         * 2.1. ATTRIBUTE: read a character and start parsing the attribute name
         *
         * 2.2. BETWEEN: read (:) to switch from attribute name to value
         *
         * 2.3. VALUE: read a character and start parsing the value
         *
         * 3. ATTRIBUTEDONE: read a (,) to end the attribute and go to NEXTVALUE
         *
         * 4. FINISH: repeat 2 and 3 until (}) is found. Then stop and return
         */
        state = STATE.START;
        String attribute = "";
        Value value;

        if (!started) {
            started = true;
            next();
        }

        while (state != STATE.FINISH) {
            switch (c) {

            // Cases: Ignore
            case ' ': // Just ignore spaces outside of values
                next();
                break;
            case '\n': // Just ignore newlines outside of values
                next();
                break;

            // Cases: Syntax

            /**
             * Symbols:
             *
             * ({): Start object or inner object
             *
             * (:): Switch from attribute name to value
             *
             * (,): End attribute
             *
             * (}): End object
             */

            // Start parser or parse inner object
            case '{':
                if (state == STATE.START) { // Start parser
                    state = STATE.NEXTATTRIBUTE;
                } else if (state == STATE.VALUE) { // Parse inner object
                    value = parseValue();
                    map.put(attribute, value);
                    state = STATE.ATTRIBUTEDONE;

                    // Reset in case of error;
                    attribute = "";
                    value = null;

                    break;

                } else { // { at unexpected place
                    throw new JsonFormattingException("Started new Object at unsupported place");
                }

                next();
                break;

            // Switch from attribute to value
            case ':':
                if (state != STATE.BETWEEN) { // Error if (:) at unexpected place
                    throw new JsonFormattingException("Pair symbol (:) at wrong place");
                }

                state = STATE.VALUE;
                next();
                break;

            // Continue if , after value
            case ',':
                if (state != STATE.ATTRIBUTEDONE) { // Error if (,) at unexpected place
                    throw new JsonFormattingException("Starting new attribute (,) at wrong place");
                }

                state = STATE.NEXTATTRIBUTE;
                next();
                break;

            // Stop parser
            case '}':
                if (state != STATE.NEXTATTRIBUTE && state != STATE.ATTRIBUTEDONE) { // Error if (}) at unexpected
                                                                                    // place.
                    // Doesnt error on trailing comma.
                    throw new JsonFormattingException("Object ended before attribute is done");
                }
                if (iter.hasNext()) { // Inner object
                    next();
                }
                state = STATE.FINISH;
                break;

            // Cases: Parsing
            default:
                switch (state) {

                // Parse attribute
                case NEXTATTRIBUTE:
                    state = STATE.ATTRIBUTE;
                    attribute = parseAttribute();
                    state = STATE.BETWEEN;
                    break;

                // Parse value
                case VALUE:
                    value = parseValue();
                    map.put(attribute, value);
                    state = STATE.ATTRIBUTEDONE;

                    // Reset in case of error;
                    attribute = "";
                    value = null;

                    break;

                default: // Fallthrough case - throw error
                    throw new JsonFormattingException("Unexpected symbol: " + c);
                }
            }
        }

        return new JsonObject(map);
    }

    public JsonArray buildArray(String jsonString) throws JsonFormattingException, UnsupportedTypeException {
        iter = Utils.stringToCharList(jsonString).iterator();
        JsonArray array = buildArrayWithIter();
        started = false;
        return array;
    }

    private JsonArray buildArrayWithIter() throws JsonFormattingException, UnsupportedTypeException {
        ArrayList<Value> list = new ArrayList<Value>();

        /**
         * State works like this:
         *
         * 1. START: read ([) and start parser
         *
         * 2. VALUE: read a character and start parsing the value
         *
         * 3. VALUEDONE: read a (,) to end the value and go to VALUE
         *
         * 4. FINISH: repeat 2 and 3 until (]) is found. then stop and return
         */
        state = STATE.START;
        Value value;

        if (!started) {
            started = true;
            next();
        }

        while (state != STATE.FINISH) {
            switch (c) {

            // Cases: Ignore
            case ' ': // Just ignore spaces outside of values
                next();
                break;
            case '\n': // Just ignore newlines outside of values
                next();
                break;

            // Cases: Syntax

            /**
             * Symbols:
             *
             * ([): Start array or inner array
             *
             * (,): End value
             *
             * (]): End array
             */

            // Start parser or parse inner object
            case '[':
                if (state == STATE.START) { // Start parser
                    state = STATE.VALUE;
                } else if (state == STATE.VALUE) { // Parse inner array
                    value = parseValue();
                    list.add(value);
                    state = STATE.VALUEDONE;
                    value = null;
                    break;

                } else { // { at unexpected place
                    throw new JsonFormattingException("Started new array at unsupported place");
                }
                next();
                break;

            // Continue if , after value
            case ',':
                if (state != STATE.VALUEDONE) { // Error if (,) at unexpected place
                    throw new JsonFormattingException("Starting new value (,) at wrong place");
                }
                state = STATE.VALUE;
                next();
                break;

            // Stop parser
            case ']':
                if (state != STATE.VALUE && state != STATE.VALUEDONE) { // Error if (]) at unexpected place. Doesnt
                                                                        // error on trailing comma.
                    throw new JsonFormattingException("Array ended before value is done");
                }
                if (iter.hasNext()) { // Inner array
                    next();
                }
                state = STATE.FINISH;
                break;

            // Cases: Parsing
            default:
                switch (state) {

                // Parse value
                case VALUE:
                    value = parseValue();
                    list.add(value);
                    state = STATE.VALUEDONE;
                    value = null;
                    break;

                default: // Fallthrough case - throw error
                    throw new JsonFormattingException("Unexpected symbol: " + c);
                }
            }
        }

        return new JsonArray(list);
    }

    private String parseAttribute() throws JsonFormattingException {
        state = STATE.ATTRIBUTE; // Set incase of accidental usage
        try {
            return parse().asString();
        } catch (WrongTypeException | UnsupportedTypeException e) {
            // Shouldnt happen
            e.printStackTrace();
            System.out.println("Never should happen error happend: " + e.getMessage());
            return "";
        }
    }

    private Value parseValue() throws UnsupportedTypeException, JsonFormattingException {
        state = STATE.VALUE; // Set incase of accidental usage
        return parse();
    }

    private Value parse() throws UnsupportedTypeException, JsonFormattingException {

        // ({): Call parser for inner object
        if (c == '{') {
            Value object = new Value(buildObjectWithIter());
            return object;
        }

        if (c == '[') {
            Value array = new Value(buildArrayWithIter());
            return array;
        }

        // Read primitive value
        boolean fin = false;
        boolean onValue = false;
        boolean string = false;
        String value = "";

        while (!fin) {
            switch (c) {
            // Cases: Syntax

            /**
             * Symbols
             *
             * (:): End of attribute, but not of a value
             *
             * (,): End of value
             *
             * (}): End of entire object
             */

            // End of attribute - else just read to value
            case ':': {
                if (state == STATE.ATTRIBUTE) {
                    fin = true;
                } else {
                    value += c;
                    next();
                }
                break;
            }

            // End of value - else just read to attribute
            case ',':
                if (state == STATE.VALUE && !string) { // Do not end if in a string
                    fin = true;
                } else {
                    value += c;
                    next();
                }
                break;

            // End of entire object
            case '}':
                fin = true;
                break;

            // End of entire array
            case ']':
                fin = true;
                break;

            // Cases: String handling

            /**
             * Symbols
             *
             * ("): Starts and ends a String
             *
             * ( ): End value if not in String
             *
             * (\): Escape the next character
             */

            // Start and end string
            case '\"':
                if (!onValue) { // Begin String
                    string = true;
                    onValue = true;
                } else if (string) { // End String
                    fin = true;
                }
                next();
                break;

            // Read into string or end value
            case ' ':
                if (string) { // Is part of string?
                    value += c;
                    next();
                } else if (onValue) { // Else end if already on value
                    fin = true;
                } else { // Else is padding - ignore
                    next();
                }
                break;

            // Escape sequence - append next char to value
            case '\\':
                next();
                value += c;
                next();
                break;

            // Just append to value
            default:
                onValue = true;
                value += c;
                next();
            }
        }

        // Build Value object from primitives

        // String - always a String if parsing an attribute
        if (string || state == STATE.ATTRIBUTE) {
            return new Value(value);
        }

        // Boolean
        if (value.equalsIgnoreCase("true")) {
            return new Value(true);
        }
        if (value.equalsIgnoreCase("false")) {
            return new Value(false);
        }

        // Null
        if (value.equalsIgnoreCase("null")) {
            return new Value((JsonObject) null);
        }

        // Integer
        try {
            return new Value(Integer.parseInt(value));
        } catch (Exception e) {
        }

        try {
            return new Value(Long.parseLong(value));
        } catch (Exception e) {
        }

        // Double
        try {
            return new Value(Double.parseDouble(value));
        } catch (Exception e) {
        }

        // Falltrough - datatype unsupported
        throw new UnsupportedTypeException("value " + value + " doesnt match any datatype");
    }

    // Utils

    /**
     * Calls next on the iterator
     */
    private void next() throws JsonFormattingException {
        try {
            c = iter.next();
        } catch (NoSuchElementException e) {
            throw new JsonFormattingException("Json ended unexpectedly");
        }
    }

    private enum STATE {
        START, NEXTATTRIBUTE, ATTRIBUTE, BETWEEN, VALUE, ATTRIBUTEDONE, FINISH, VALUEDONE
    }
}
