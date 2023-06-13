package com.elielbatiston.wishlist;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class JsonUtil<T> {

    private Class<T> clazz;

    public JsonUtil(final Class<T> clazz) {
        this.clazz = clazz;
    }

    public T read(final String path) {
        try {
            final URI uri = getURI(path);
            final File file = new File(uri);
            final ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(file, clazz);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    private URI getURI(final String path) throws URISyntaxException {
        return JsonUtil.class.getClassLoader()
            .getResource(path)
            .toURI();
    }
}
