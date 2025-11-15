package com.subrutin.bitbucket.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CommentResponseDTO(
    String type,
    Integer id,
    String createdOn,
    String updatedOn,
    ContentDTO content,
    UserDTO user,
    Boolean deleted,
    InlineDTO inline,
    CommentLinksDTO links,
    PullRequestRefDTO pullrequest,
    CommentResolutionDTO resolution,
    Boolean pending
) {}
