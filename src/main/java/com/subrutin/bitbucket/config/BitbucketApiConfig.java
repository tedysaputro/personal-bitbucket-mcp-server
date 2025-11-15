package com.subrutin.bitbucket.config;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;

@ConfigMapping(prefix = "bitbucket.api")
public interface BitbucketApiConfig {

    /**
     * Bitbucket username, often the email or workspace handle associated with the
     * app password.
     */
    @WithDefault("")
    String email();

    /**
     * Bitbucket app password that pairs with the username for Basic auth.
     */
    @WithName("token")
    @WithDefault("")
    String token();

    /**
     * Default workspace/team whose repositories are queried when no override is
     * provided.
     */
    @WithDefault("")
    String workspace();
}
