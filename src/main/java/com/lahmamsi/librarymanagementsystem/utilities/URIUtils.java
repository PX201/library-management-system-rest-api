package com.lahmamsi.librarymanagementsystem.utilities;

import java.net.URI;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.servlet.http.HttpServletRequest;

public class URIUtils {
    public static URI buildResourceUri(HttpServletRequest request, long resourceId) {
        return ServletUriComponentsBuilder
            .fromRequestUri(request)
            .path("/{id}")
            .buildAndExpand(resourceId)
            .toUri();
    }
}
