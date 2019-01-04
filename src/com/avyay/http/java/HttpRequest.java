package com.avyay.http.java;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class HttpRequest {
    public String type;
    public String url;

    public static HashMap parse(ArrayList<String> lines) {
        HashMap<String, String> args = new HashMap<>();
        String type = lines.get(0).substring(0, lines.get(0).indexOf(" "));
        String next = lines.get(0).substring(lines.get(0).indexOf(" ") + 1, lines.get(0).length());
        String url = next.substring(0, next.indexOf(" "));
        args.put("type", type);
        args.put("url", url);
        return args;
    }

    public HttpRequest(HashMap<String, String> fields) {
        type = fields.get("type");
        url = fields.get("url");
    }
}
