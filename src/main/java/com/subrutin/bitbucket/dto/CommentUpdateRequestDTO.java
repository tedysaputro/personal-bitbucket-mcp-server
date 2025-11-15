package com.subrutin.bitbucket.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CommentUpdateRequestDTO(
    ContentDTO content,
    InlineDTO inline,
    Boolean deleted,
    Boolean pending
) {
    // Constructor for updating only content
    public CommentUpdateRequestDTO(ContentDTO content) {
        this(content, null, null, null);
    }
    
    // Constructor for updating content and inline
    public CommentUpdateRequestDTO(ContentDTO content, InlineDTO inline) {
        this(content, inline, null, null);
    }
}
