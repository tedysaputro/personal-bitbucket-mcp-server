package com.subrutin.bitbucket.dto;

public record PullRequestLinksDTO(
    LinkDTO self,
    LinkDTO html,
    LinkDTO commits,
    LinkDTO approve,
    LinkDTO diff,
    LinkDTO diffstat,
    LinkDTO comments,
    LinkDTO activity,
    LinkDTO merge,
    LinkDTO decline
) {}
