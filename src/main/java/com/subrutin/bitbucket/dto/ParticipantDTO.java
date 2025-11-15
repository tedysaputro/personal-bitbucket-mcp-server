package com.subrutin.bitbucket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ParticipantDTO(
    String type,
    String uuid,
    @JsonProperty("display_name")
    String displayName,
    String nickname,
    @JsonProperty("account_id")
    String accountId,
    String role,
    Boolean approved,
    @JsonProperty("participated_on")
    String participatedOn
) {}
