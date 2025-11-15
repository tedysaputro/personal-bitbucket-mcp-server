package com.subrutin.bitbucket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BitbucketRepositoryDTO(
        String uuid,
        String name,
        String slug,
        @JsonProperty("full_name") String fullName,
        @JsonProperty("is_private") boolean isPrivate) {
}
