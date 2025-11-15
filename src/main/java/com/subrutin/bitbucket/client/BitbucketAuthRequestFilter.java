package com.subrutin.bitbucket.client;

import com.subrutin.bitbucket.config.BitbucketApiConfig;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Provider
@ApplicationScoped
@Priority(Priorities.AUTHENTICATION)
public class BitbucketAuthRequestFilter implements ClientRequestFilter {

    
    private final BitbucketApiConfig config;

    public BitbucketAuthRequestFilter(BitbucketApiConfig config) {
        this.config = config;
    }

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        var username = config.email();
        var appPassword = config.token();
        if (username == null || username.isBlank() || appPassword == null || appPassword.isBlank()) {
            return;
        }
        var basicToken = username + ":" + appPassword;
        var encoded = Base64.getEncoder().encodeToString(basicToken.getBytes(StandardCharsets.UTF_8));
        requestContext.getHeaders().putSingle("Authorization", "Basic " + encoded);
    }
}
