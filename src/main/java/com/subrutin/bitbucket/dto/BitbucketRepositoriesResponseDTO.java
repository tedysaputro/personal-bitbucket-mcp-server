package com.subrutin.bitbucket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record BitbucketRepositoriesResponseDTO(
        int pagelen,
        int size,
        int page,
        String next,
        @JsonProperty("values") List<BitbucketRepositoryDTO> repositories) {
}
