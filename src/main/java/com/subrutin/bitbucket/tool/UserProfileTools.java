package com.subrutin.bitbucket.tool;

import com.subrutin.bitbucket.service.UserProfileService;

import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolResponse;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserProfileTools {


    private final UserProfileService userProfileService;

    public UserProfileTools(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @Tool(name = "Fetch user profile")
    public ToolResponse findUserProfile() {
        String response =  userProfileService.findUserProfile();
        return ToolResponse.success(response);
    }

}
