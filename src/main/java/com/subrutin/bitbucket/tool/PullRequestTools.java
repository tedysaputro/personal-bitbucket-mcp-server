package com.subrutin.bitbucket.tool;

import com.subrutin.bitbucket.dto.PullRequestDTO;
import com.subrutin.bitbucket.dto.PullRequestListResponseDTO;
import com.subrutin.bitbucket.service.PullRequestService;

import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PullRequestTools {

    private final PullRequestService pullRequestService;

    

    public PullRequestTools(PullRequestService pullRequestService) {
        this.pullRequestService = pullRequestService;
    }



    @Tool(description = "Returns all pull requests on the specified repository. Use this to get list of pull requests from a Bitbucket repository.")
    public ToolResponse findAllPullRequest(
        @ToolArg(description = "The workspace ID or slug where the repository is located") 
        String workspace,
        @ToolArg(description = "The repository slug or name to get pull requests from") 
        String reposlug
    ){
        PullRequestListResponseDTO response = pullRequestService.findAllPullRequest(workspace, reposlug);
        return ToolResponse.structuredSuccess(response);
    }

    @Tool(description = "Returns a specific pull request by ID. Use this to get details of a single pull request from a Bitbucket repository.")
    public ToolResponse findAPullRequest(
        @ToolArg(description = "The workspace ID or slug where the repository is located") 
        String workspace,
        @ToolArg(description = "The repository slug or name to get pull requests from") 
        String reposlug,
        @ToolArg(description = "The pull request ID to get details from") 
        String pullRequestId
    ){
        PullRequestDTO response = pullRequestService.findApullRequest(workspace, reposlug, pullRequestId);
        return ToolResponse.structuredSuccess(response);
    }
        
    
}
