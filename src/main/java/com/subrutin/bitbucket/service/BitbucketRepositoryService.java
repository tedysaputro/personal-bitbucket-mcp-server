package com.subrutin.bitbucket.service;

import com.subrutin.bitbucket.client.BitbucketApiClient;
import com.subrutin.bitbucket.config.BitbucketApiConfig;
import com.subrutin.bitbucket.dto.BitbucketRepositoriesResponseDTO;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class BitbucketRepositoryService {

    @Inject
    @RestClient
    BitbucketApiClient apiClient;

    @Inject
    BitbucketApiConfig config;

    public BitbucketRepositoriesResponseDTO listRepositories(String workspace, Integer page, Integer pageLength) {
        var resolvedWorkspace = (workspace == null || workspace.isBlank()) ? config.workspace() : workspace;
        if (resolvedWorkspace == null || resolvedWorkspace.isBlank()) {
            throw new IllegalStateException("Bitbucket workspace must be configured via bitbucket.api.workspace or provided per request");
        }
        return apiClient.listRepositories(resolvedWorkspace, page, pageLength);
    }
}
