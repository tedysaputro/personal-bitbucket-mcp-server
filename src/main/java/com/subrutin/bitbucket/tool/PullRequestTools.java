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



    @Tool(name = "find_all_pull_request", description = "Returns all pull requests on the specified repository. Use this to get list of pull requests from a Bitbucket repository.")
    public ToolResponse findAllPullRequest(
        @ToolArg(description = "The workspace ID or slug where the repository is located") 
        String workspace,
        @ToolArg(description = "The repository slug or name to get pull requests from") 
        String reposlug
    ){
        PullRequestListResponseDTO response = pullRequestService.findAllPullRequest(workspace, reposlug);
        return ToolResponse.structuredSuccess(response);
    }

    @Tool(name = "find_a_pull_request", description = "Returns a specific pull request by ID. Use this to get details of a single pull request from a Bitbucket repository.")
    public ToolResponse findAPullRequest(
        @ToolArg(description = "The workspace ID or slug where the repository is located") 
        String workspace,
        @ToolArg(description = "The repository slug or name to get pull requests from") 
        String reposlug,
        @ToolArg(description = "The pull request ID to get details from") 
        Integer pullRequestId
    ){
        PullRequestDTO response = pullRequestService.findApullRequest(workspace, reposlug, pullRequestId);
        return ToolResponse.structuredSuccess(response);
    }


    @Tool(name = "find_diff_stat_for_pull_request", description = "Redirects to the repository diffstat with the revspec that corresponds to the pull request. Use this to get the diffstat of a pull request.")
    public ToolResponse findDiffStatForPullRequest(
        @ToolArg(description = "The workspace ID or slug where the repository is located") 
        String workspace,
        @ToolArg(description = "The repository slug or name to get pull requests from") 
        String reposlug,
        @ToolArg(description = "The pull request ID to get") 
        Integer id
    ){
        String response = pullRequestService.findDiffStatForAPullRequest(workspace, reposlug, id);
        return ToolResponse.success(response);
    }

    @Tool(name = "find_list_changes_in_pull_request", description = "Redirects to the repository diff with the revspec that corresponds to the pull request. Use this to get the list of changes in a pull request.")
    public ToolResponse findListChangesInAPullRequest(
        @ToolArg(description = "The workspace ID or slug where the repository is located") 
        String workspace,
        @ToolArg(description = "The repository slug or name to get pull requests from") 
        String reposlug,
        @ToolArg(description = "The pull request ID to get") 
        Integer id
    ) {
        String response = pullRequestService.getListChangesForAPullRequest(workspace, reposlug, id);
        return ToolResponse.success(response);
    }
        
    
}
