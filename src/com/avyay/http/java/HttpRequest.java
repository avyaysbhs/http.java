package com.avyay.http.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class HttpRequest {
    private HashMap<String, String> fields;

    private static HashMap parse(ArrayList<String> lines) {
        HashMap<String, String> args = new HashMap<>();
        String type = lines.get(0).substring(0, lines.get(0).indexOf(" "));
        String next = lines.get(0).substring(lines.get(0).indexOf(" ") + 1, lines.get(0).length());
        String url = next.substring(0, next.indexOf(" "));
        args.put("type", type);
        args.put("url", url);
        for (int i=1; i<lines.size(); i++) {
            String full = lines.get(i);
            if (!full.isEmpty()) {
                int colon = full.indexOf(": ");
                if (colon != -1) {
                    String field = full.substring(0, colon);
                    String value = full.substring(colon + 2, full.length());
                    args.put(field, value);
                }
            }
        }
        return args;
    }

    public HttpRequest(ArrayList<String> fields) {
        this.fields = HttpRequest.parse(fields);
    }

    public String field(String key) {
        return fields.get(key);
    }

    public Set<String> fields() {
        return fields.keySet();
    }
}
