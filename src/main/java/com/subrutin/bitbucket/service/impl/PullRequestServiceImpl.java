package com.subrutin.bitbucket.service.impl;

import com.subrutin.bitbucket.client.BitbucketApiClient;
import com.subrutin.bitbucket.dto.PullRequestDTO;
import com.subrutin.bitbucket.dto.PullRequestListResponseDTO;
import com.subrutin.bitbucket.service.PullRequestService;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class PullRequestServiceImpl implements PullRequestService {

    private final BitbucketApiClient apiClient;

    public PullRequestServiceImpl(@RestClient BitbucketApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @Override
    public PullRequestListResponseDTO findAllPullRequest(String workspace, String reposlug) {
        return apiClient.listPullRequest(workspace, reposlug);
    }

    @Override
    public PullRequestDTO findApullRequest(String workspace, String reposlug, Integer pullRequestId) {
        return apiClient.getAPullRequest(workspace, reposlug, pullRequestId);
    }

    @Override
    public String findDiffStatForAPullRequest(String workspace, String reposlug, Integer pullRequestId) {
        return apiClient.getTheDiffStatForAPullRequest(workspace, reposlug, pullRequestId);
    }

    @Override
    public String getListChangesForAPullRequest(String workspace, String reposlug, Integer pullRequestId) {
        return apiClient.getListChangesForAPullRequest(workspace, reposlug, pullRequestId);
    }

}
