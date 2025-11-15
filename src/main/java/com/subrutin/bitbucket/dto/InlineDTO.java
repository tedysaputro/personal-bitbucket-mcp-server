package com.subrutin.bitbucket.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record InlineDTO(
    String path,
    Integer from,
    Integer to,
    Integer startFrom,
    Integer startTo
) {
    // Constructor for simple inline comment (only path and to line)
    public InlineDTO(String path, Integer to) {
        this(path, null, to, null, null);
    }
    
    // Constructor with from and to (for modified lines)
    public InlineDTO(String path, Integer from, Integer to) {
        this(path, from, to, null, null);
    }
}
