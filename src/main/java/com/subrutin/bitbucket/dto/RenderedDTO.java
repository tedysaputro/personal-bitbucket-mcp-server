package com.subrutin.bitbucket.dto;

public record RenderedDTO(
    MarkupDTO title,
    MarkupDTO description,
    MarkupDTO reason
) {}
