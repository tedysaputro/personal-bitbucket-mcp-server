package com.subrutin.bitbucket.service;

import com.subrutin.bitbucket.dto.CommentListResponseDTO;
import com.subrutin.bitbucket.dto.CommentResponseDTO;

public interface PullRequestCommentService {

    public CommentResponseDTO createComment(String workspace, String reposlug, Integer pullRequestId, String commentText);

    public CommentResponseDTO createInlineComment(String workspace, 
        String reposlug, 
        Integer pullRequestId, 
        String filePath,
        Integer lineNumber,
        String commentText);

    public CommentResponseDTO findAComment(String workspace, String reposlug, Integer pullRequestId, Integer commentId);

    public CommentListResponseDTO findCommentList(String workspace, String reposlug, Integer pullRequestId, Integer page, Integer pageLength, Integer size);

    public CommentResponseDTO updateComment(String workspace, String reposlug, Integer pullRequestId, Integer commentId, String commentText);

}
