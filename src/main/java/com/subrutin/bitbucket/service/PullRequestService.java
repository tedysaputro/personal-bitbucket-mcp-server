package com.subrutin.bitbucket.service;

import com.subrutin.bitbucket.dto.PullRequestDTO;
import com.subrutin.bitbucket.dto.PullRequestListResponseDTO;

public interface PullRequestService {
    public PullRequestListResponseDTO findAllPullRequest(
        String workspace,
        String reposlug
    );

    public PullRequestDTO findApullRequest(
        String workspace,
        String reposlug,
        Integer pullRequestId
    );

    public String findDiffStatForAPullRequest(
        String workspace,
        String reposlug,
        Integer id
    );

    public String getListChangesForAPullRequest(
        String workspace,
        String reposlug,
        Integer pullRequestId
    );

}
