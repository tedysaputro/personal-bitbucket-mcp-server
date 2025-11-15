package com.subrutin.bitbucket.service.impl;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.subrutin.bitbucket.client.BitbucketApiClient;
import com.subrutin.bitbucket.dto.CommentRequestDTO;
import com.subrutin.bitbucket.dto.CommentResponseDTO;
import com.subrutin.bitbucket.dto.ContentDTO;
import com.subrutin.bitbucket.dto.InlineDTO;
import com.subrutin.bitbucket.service.PullRequestCommentService;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PullRequestCommentServiceImpl implements PullRequestCommentService {

    private final BitbucketApiClient bitbucketApiClient;

    public PullRequestCommentServiceImpl(@RestClient BitbucketApiClient bitbucketApiClient) {
        this.bitbucketApiClient = bitbucketApiClient;
    }

    @Override
    public CommentResponseDTO createComment(String workspace, String reposlug, Integer pullRequestId,
            String commentText) {
        Log.infof("Creating comment on PR #%s in %s/%s: %s", pullRequestId, workspace, reposlug, commentText);
        CommentRequestDTO requestBody = new CommentRequestDTO(
                new ContentDTO(commentText));

        return bitbucketApiClient.createACommentOnAPullRequest(workspace, reposlug, pullRequestId, requestBody);
    }

    @Override
    public CommentResponseDTO createInlineComment(String workspace, String reposlug, Integer pullRequestId,
            String filePath, Integer lineNumber, String commentText) {
        Log.infof("Creating inline comment on PR #%s in %s/%s at %s:%d", pullRequestId, workspace, reposlug, filePath, lineNumber);
        InlineDTO inlineData = new InlineDTO(filePath, lineNumber);
        CommentRequestDTO requestBody = new CommentRequestDTO(
                new ContentDTO(commentText),
                inlineData);
        
        // Log the exact structure being sent
        Log.infof("Inline data - path: %s, to: %d", inlineData.path(), inlineData.to());
        Log.infof("Request body - content.raw: %s, inline: %s", requestBody.content().raw(), requestBody.inline());

        return bitbucketApiClient.createACommentOnAPullRequest(workspace, reposlug, pullRequestId, requestBody);
    }

}
