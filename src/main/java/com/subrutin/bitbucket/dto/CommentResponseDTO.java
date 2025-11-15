package com.subrutin.bitbucket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CommentResponseDTO(
    Integer id,
    @JsonProperty("created_on")
    String createdOn,
    @JsonProperty("updated_on")
    String updatedOn,
    ContentDTO content,
    UserDTO user,
    Boolean deleted,
    String type,
    PullRequestRefDTO pullrequest,
    InlineDTO inline
) {}
