package com.subrutin.bitbucket.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CommentListResponseDTO(
    Integer size,
    Integer page,
    Integer pagelen,
    String next,
    String previous,
    List<CommentResponseDTO> values
) {}
