package com.subrutin.bitbucket.dto;

import java.util.List;

public record PullRequestListResponseDTO(
    Integer size,
    Integer page,
    Integer pagelen,
    String next,
    String previous,
    List<PullRequestDTO> values
) {}
