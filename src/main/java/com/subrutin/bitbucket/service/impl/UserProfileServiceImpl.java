package com.subrutin.bitbucket.service.impl;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.subrutin.bitbucket.client.BitbucketApiClient;
import com.subrutin.bitbucket.service.UserProfileService;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserProfileServiceImpl implements UserProfileService{

    private final BitbucketApiClient apiClient;

    public UserProfileServiceImpl(@RestClient BitbucketApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @Override
    public String findUserProfile() {
        return apiClient.findUserProfile();
    }
}
