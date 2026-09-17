package com.haitac.manager.service;

import java.util.*;

/**
 * Bộ xử lý JSON thuần Java (Zero-Dependency JSON Parser & Serializer)
 * Tương thích Java 8+ trở lên, an toàn, hỗ trợ Map, List, String, Number, Boolean, null.
 */
public class JsonHelper {

    public static String toJson(Object obj) {
        return toJson(obj, 0);
    }

    @SuppressWarnings("unchecked")
    private static String toJson(Object obj, int indent) {
        if (obj == null) {
            return "null";
        }
        if (obj instanceof String) {
            return quote((String) obj);
        }
        if (obj instanceof Number || obj instanceof Boolean) {
            return obj.toString();
        }
        if (obj instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) obj;
            if (map.isEmpty()) return "{}";
            StringBuilder sb = new StringBuilder();
            sb.append("{\n");
            int i = 0;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                indent(sb, indent + 1);
                sb.append(quote(entry.getKey())).append(": ");
                sb.append(toJson(entry.getValue(), indent + 1));
                if (++i < map.size()) {
                    sb.append(",");
                }
                sb.append("\n");
            }
            indent(sb, indent);
            sb.append("}");
            return sb.toString();
        }
        if (obj instanceof List) {
            List<Object> list = (List<Object>) obj;
            if (list.isEmpty()) return "[]";
            StringBuilder sb = new StringBuilder();
            sb.append("[\n");
            for (int i = 0; i < list.size(); i++) {
                indent(sb, indent + 1);
                sb.append(toJson(list.get(i), indent + 1));
                if (i < list.size() - 1) {
                    sb.append(",");
                }
                sb.append("\n");
            }
            indent(sb, indent);
            sb.append("]");
            return sb.toString();
        }
        return quote(obj.toString());
    }

    private static void indent(StringBuilder sb, int count) {
        for (int i = 0; i < count; i++) {
            sb.append("  ");
        }
    }

    private static String quote(String string) {
        if (string == null || string.length() == 0) {
            return "\"\"";
        }
        char c;
        int len = string.length();
        StringBuilder sb = new StringBuilder(len + 4);
        sb.append('"');
        for (int i = 0; i < len; i += 1) {
            c = string.charAt(i);
            switch (c) {
                case '\\':
                case '"':
                    sb.append('\\');
                    sb.append(c);
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                default:
                    if (c < ' ') {
                        String t = "000" + Integer.toHexString(c);
                        sb.append("\\u").append(t.substring(t.length() - 4));
                    } else {
                        sb.append(c);
                    }
            }
        }
        sb.append('"');
        return sb.toString();
    }

    // ==================== PARSER ====================

    public static Object parse(String jsonStr) throws Exception {
        if (jsonStr == null) return null;
        jsonStr = jsonStr.trim();
        if (jsonStr.isEmpty()) return null;
        Tokenizer tokenizer = new Tokenizer(jsonStr);
        return parseValue(tokenizer);
    }

    private static Object parseValue(Tokenizer t) throws Exception {
        t.skipWhitespace();
        char c = t.peek();
        if (c == '{') return parseObject(t);
        if (c == '[') return parseArray(t);
        if (c == '"') return parseString(t);
        if (c == 't' || c == 'f') return parseBoolean(t);
        if (c == 'n') return parseNull(t);
        return parseNumber(t);
    }

    private static Map<String, Object> parseObject(Tokenizer t) throws Exception {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        t.consume('{');
        t.skipWhitespace();
        if (t.peek() == '}') {
            t.consume('}');
            return map;
        }
        while (true) {
            t.skipWhitespace();
            String key = parseString(t);
            t.skipWhitespace();
            t.consume(':');
            Object val = parseValue(t);
            map.put(key, val);
            t.skipWhitespace();
            char next = t.peek();
            if (next == '}') {
                t.consume('}');
                break;
            } else if (next == ',') {
                t.consume(',');
            } else {
                throw new Exception("Ký tự bất thường trong JSON object: " + next + " tại vị trí " + t.pos);
            }
        }
        return map;
    }

    private static List<Object> parseArray(Tokenizer t) throws Exception {
        List<Object> list = new ArrayList<Object>();
        t.consume('[');
        t.skipWhitespace();
        if (t.peek() == ']') {
            t.consume(']');
            return list;
        }
        while (true) {
            Object val = parseValue(t);
            list.add(val);
            t.skipWhitespace();
            char next = t.peek();
            if (next == ']') {
                t.consume(']');
                break;
            } else if (next == ',') {
                t.consume(',');
            } else {
                throw new Exception("Ký tự bất thường trong JSON array: " + next + " tại vị trí " + t.pos);
            }
        }
        return list;
    }

    private static String parseString(Tokenizer t) throws Exception {
        t.consume('"');
        StringBuilder sb = new StringBuilder();
        while (t.hasMore()) {
            char c = t.next();
            if (c == '"') {
                return sb.toString();
            }
            if (c == '\\') {
                if (!t.hasMore()) throw new Exception("Chuỗi JSON kết thúc bất ngờ sau dấu gạch chéo ngược");
                char esc = t.next();
                switch (esc) {
                    case '"': sb.append('"'); break;
                    case '\\': sb.append('\\'); break;
                    case '/': sb.append('/'); break;
                    case 'b': sb.append('\b'); break;
                    case 'f': sb.append('\f'); break;
                    case 'n': sb.append('\n'); break;
                    case 'r': sb.append('\r'); break;
                    case 't': sb.append('\t'); break;
                    case 'u':
                        if (t.pos + 4 > t.src.length()) throw new Exception("Unicode escape không hợp lệ");
                        String hex = t.src.substring(t.pos, t.pos + 4);
                        t.pos += 4;
                        sb.append((char) Integer.parseInt(hex, 16));
                        break;
                    default:
                        sb.append(esc);
                }
            } else {
                sb.append(c);
            }
        }
        throw new Exception("Chuỗi JSON không được đóng ngoặc kép");
    }

    private static Boolean parseBoolean(Tokenizer t) throws Exception {
        if (t.peek() == 't') {
            t.expect("true");
            return Boolean.TRUE;
        } else {
            t.expect("false");
            return Boolean.FALSE;
        }
    }

    private static Object parseNull(Tokenizer t) throws Exception {
        t.expect("null");
        return null;
    }

    private static Number parseNumber(Tokenizer t) throws Exception {
        t.skipWhitespace();
        int start = t.pos;
        if (t.peek() == '-') t.next();
        while (t.hasMore() && (Character.isDigit(t.peek()) || t.peek() == '.' || t.peek() == 'e' || t.peek() == 'E' || t.peek() == '+' || t.peek() == '-')) {
            t.next();
        }
        String numStr = t.src.substring(start, t.pos);
        if (numStr.contains(".") || numStr.contains("e") || numStr.contains("E")) {
            return Double.parseDouble(numStr);
        }
        long val = Long.parseLong(numStr);
        if (val >= Integer.MIN_VALUE && val <= Integer.MAX_VALUE) {
            return (int) val;
        }
        return val;
    }

    private static class Tokenizer {
        final String src;
        int pos = 0;

        Tokenizer(String src) {
            this.src = src;
        }

        boolean hasMore() {
            return pos < src.length();
        }

        char peek() {
            if (!hasMore()) return '\0';
            return src.charAt(pos);
        }

        char next() {
            return src.charAt(pos++);
        }

        void consume(char expected) throws Exception {
            skipWhitespace();
            if (!hasMore() || src.charAt(pos) != expected) {
                throw new Exception("Kỳ vọng ký tự '" + expected + "' nhưng nhận '" + (hasMore() ? src.charAt(pos) : "EOF") + "' tại vị trí " + pos);
            }
            pos++;
        }

        void expect(String str) throws Exception {
            skipWhitespace();
            if (pos + str.length() > src.length() || !src.substring(pos, pos + str.length()).equals(str)) {
                throw new Exception("Kỳ vọng từ khóa '" + str + "' tại vị trí " + pos);
            }
            pos += str.length();
        }

        void skipWhitespace() {
            while (hasMore()) {
                char c = src.charAt(pos);
                if (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
                    pos++;
                } else {
                    break;
                }
            }
        }
    }
}
