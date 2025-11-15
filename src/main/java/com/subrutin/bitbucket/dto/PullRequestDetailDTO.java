package com.subrutin.bitbucket.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO for single pull request detail response.
 * This is the same structure as PullRequestDTO but used for single PR endpoints.
 */
public record PullRequestDetailDTO(
    String type,
    PullRequestLinksDTO links,
    Integer id,
    String title,
    RenderedDTO rendered,
    SummaryDTO summary,
    String state,
    AuthorDTO author,
    BranchSourceDTO source,
    BranchSourceDTO destination,
    @JsonProperty("merge_commit")
    MergeCommitDTO mergeCommit,
    @JsonProperty("comment_count")
    Integer commentCount,
    @JsonProperty("task_count")
    Integer taskCount,
    @JsonProperty("close_source_branch")
    Boolean closeSourceBranch,
    @JsonProperty("closed_by")
    UserDTO closedBy,
    String reason,
    @JsonProperty("created_on")
    String createdOn,
    @JsonProperty("updated_on")
    String updatedOn,
    List<ReviewerDTO> reviewers,
    List<ParticipantDTO> participants,
    Boolean draft,
    Boolean queued
) {}
