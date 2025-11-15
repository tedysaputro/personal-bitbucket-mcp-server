package com.subrutin.bitbucket.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BranchDTO(
    String name,
    @JsonProperty("merge_strategies")
    List<String> mergeStrategies,
    @JsonProperty("default_merge_strategy")
    String defaultMergeStrategy
) {}
