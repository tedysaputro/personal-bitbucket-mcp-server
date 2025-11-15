package com.subrutin.bitbucket.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CommentRequestDTO(
    ContentDTO content,
    InlineDTO inline
) {
    // Constructor for regular comment (without inline)
    public CommentRequestDTO(ContentDTO content) {
        this(content, null);
    }
}
