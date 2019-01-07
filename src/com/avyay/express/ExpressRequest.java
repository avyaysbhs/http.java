package com.avyay.express;

import com.avyay.http.java.HttpRequest;

import java.util.HashMap;
import java.util.Set;

public class ExpressRequest {
    private HttpRequest request;
    private HashMap<String, String> _params;
    private HashMap<String, String> _query;

    public ExpressRequest(HttpRequest request, HashMap<String, String> params, String source) {
        _params = params;
        _query = new HashMap<>();
        if (source.contains("?")) {
            source = source.substring(source.indexOf("?") + 1, source.length());
            String[] args = source.split("&");
            for (String arg: args) {
                String key = arg.substring(0, arg.indexOf("="));
                String value = arg.substring(arg.indexOf("=") + 1, arg.length());
                _query.put(key, value);
            }
        }
    }

    public String param(String key) {
        return _params.get(key);
    }

    public Set<String> params() {
        return _params.keySet();
    }

    public String query(String key) {
        return _query.get(key);
    }

    public Set<String> query() {
        return _query.keySet();
    }
}
