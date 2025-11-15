package com.subrutin.bitbucket.dto;

public record InlineCommentRequestDTO(
    ContentDTO content,
    InlineDTO inline
) {}
