package com.avyay.express;

import com.avyay.http.java.HttpRequest;

import java.util.HashMap;

public class ExpressRequest {
    private HttpRequest request;
    private HashMap<String, String> params;
    private HashMap<String, String> qArgs;

    public ExpressRequest(HttpRequest request, HashMap<String, String> params, String source) {
        this.params = params;
        qArgs = new HashMap<>();
        if (source.contains("?")) {
            source = source.substring(source.indexOf("?") + 1, source.length());
            String[] args = source.split("&");
            for (String arg: args) {
                String key = arg.substring(0, arg.indexOf("="));
                String value = arg.substring(arg.indexOf("=") + 1, arg.length());
                qArgs.put(key, value);
            }
        }
    }

    public String param(String key) {
        return params.get(key);
    }

    public String query(String key) {
        return qArgs.get(key);
    }
}
