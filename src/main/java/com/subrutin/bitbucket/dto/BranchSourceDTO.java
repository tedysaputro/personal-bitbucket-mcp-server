package com.subrutin.bitbucket.dto;

public record BranchSourceDTO(
    RepositoryRefDTO repository,
    BranchDTO branch,
    CommitDTO commit
) {}
