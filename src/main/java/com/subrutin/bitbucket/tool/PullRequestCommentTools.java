package com.subrutin.bitbucket.tool;

import com.subrutin.bitbucket.dto.CommentResponseDTO;
import com.subrutin.bitbucket.service.PullRequestCommentService;

import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PullRequestCommentTools {

    private final PullRequestCommentService pullRequestCommentService;

    public PullRequestCommentTools(PullRequestCommentService pullRequestCommentService) {
        this.pullRequestCommentService = pullRequestCommentService;
    }

    @Tool(description = "Creates a comment on a pull request. Use this to add a comment or feedback to a pull request.")
    public ToolResponse createComment(
        @ToolArg(description = "The workspace ID or slug where the repository is located") 
        String workspace,
        @ToolArg(description = "The repository slug or name") 
        String reposlug,
        @ToolArg(description = "The pull request ID to comment on") 
        Integer pullRequestId,
        @ToolArg(description = "The comment text content") 
        String commentText
    ){
        CommentResponseDTO response = pullRequestCommentService.createComment(workspace, reposlug, pullRequestId, commentText);
        return ToolResponse.structuredSuccess(response);
    }

    @Tool(description = "Creates an inline comment on a specific line of code in a pull request diff. " +
            "This will attach the comment to a specific line in the file changes. " +
            "CRITICAL REQUIREMENTS: " +
            "1. The filePath must EXACTLY match the file path shown in the PR diff (case-sensitive). " +
            "2. The lineNumber must be the line number from the NEW/MODIFIED version of the file (lines with '+' in diff). " +
            "3. The line must actually exist in the PR diff - you cannot comment on unchanged lines. " +
            "WORKFLOW: First call findListChangesInAPullRequest to get the diff, then identify the correct file path and line number from the diff output before creating the inline comment. " +
            "If the line number is wrong or the file path doesn't match exactly, the comment will be created as a general PR comment instead of an inline comment.")
    public ToolResponse createInlineComment(
        @ToolArg(description = "The workspace ID or slug (e.g., 'subrutin')") 
        String workspace,
        @ToolArg(description = "The repository slug (e.g., 'bitbucket-mcp-server')") 
        String reposlug,
        @ToolArg(description = "The pull request ID number") 
        Integer pullRequestId,
        @ToolArg(description = "The EXACT file path as shown in the PR diff. Must match exactly including directory separators. " +
                "Example: 'src/main/java/com/example/Service.java' - case sensitive!") 
        String filePath,
        @ToolArg(description = "The line number in the NEW version of the file (the version AFTER changes). " +
                "This is the line number shown next to lines with '+' prefix in the diff. " +
                "NOT the absolute line number from the original file. " +
                "The line must be part of the changes shown in the PR diff.") 
        Integer lineNumber,
        @ToolArg(description = "The comment text content in Markdown format") 
        String commentText
    ){
        CommentResponseDTO response = pullRequestCommentService.createInlineComment(workspace, reposlug, pullRequestId, filePath, lineNumber, commentText);
        return ToolResponse.structuredSuccess(response);
    }
}
