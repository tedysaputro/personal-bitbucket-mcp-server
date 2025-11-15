package com.subrutin.bitbucket.dto;

public record PullRequestRefDTO(
    String type,
    Integer id,
    String title,
    PullRequestLinksDTO links
) {}
