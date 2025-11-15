package com.subrutin.bitbucket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthorDTO(
    String type,
    String uuid,
    @JsonProperty("display_name")
    String displayName,
    String nickname,
    @JsonProperty("account_id")
    String accountId
) {}
