package com.subrutin.bitbucket.client;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import com.subrutin.bitbucket.dto.BitbucketRepositoriesResponseDTO;
import com.subrutin.bitbucket.dto.CommentRequestDTO;
import com.subrutin.bitbucket.dto.CommentResponseDTO;
import com.subrutin.bitbucket.dto.InlineCommentRequestDTO;
import com.subrutin.bitbucket.dto.PullRequestDTO;
import com.subrutin.bitbucket.dto.PullRequestDetailDTO;
import com.subrutin.bitbucket.dto.PullRequestListResponseDTO;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@RegisterRestClient(configKey = "bitbucket-api")
@RegisterProvider(BitbucketAuthRequestFilter.class)
public interface BitbucketApiClient {

    @GET
    @Path("/repositories/{workspace}")
    BitbucketRepositoriesResponseDTO listRepositories(
            @PathParam("workspace") String workspace,
            @QueryParam("page") Integer page,
            @QueryParam("pagelen") Integer pageLength);

    @GET
    @Path("/user")
    String findUserProfile();


    @GET
    @Path("/repositories/{workspace}/{reposlug}/pullrequests")
    PullRequestListResponseDTO listPullRequest(
            @PathParam("workspace") String workspace,
            @PathParam("reposlug") String reposlug);

    @GET
    @Path("/repositories/{workspace}/{reposlug}/pullrequests/{pullRequestId}")
    PullRequestDTO getAPullRequest(
        @PathParam("workspace") String workspace,
        @PathParam("reposlug") String reposlug,
        @PathParam("pullRequestId") String pullRequestId
    );

    @GET
    @Path("/repositories/{workspace}/{reposlug}/pullrequests/{pullRequestId}/diffstat")
    public String getTheDiffStatForAPullRequest(
        @PathParam("workspace") String workspace,
        @PathParam("reposlug") String reposlug,
        @PathParam("pullRequestId") String pullRequestId
    );


    @GET
    @Path("/repositories/{workspace}/{reposlug}/pullrequests/{pullRequestId}/diff")
    public String getListChangesForAPullRequest(
        @PathParam("workspace") String workspace,
        @PathParam("reposlug") String reposlug,
        @PathParam("pullRequestId") String pullRequestId
    );


    @POST
    @Path("/repositories/{workspace}/{reposlug}/pullrequests/{pullRequestId}/comments")
    public CommentResponseDTO createACommentOnAPullRequest(
        @PathParam("workspace") String workspace,
        @PathParam("reposlug") String reposlug,
        @PathParam("pullRequestId") String pullRequestId,
        CommentRequestDTO commentRequestDTO
    );


    @POST
    @Path("/repositories/{workspace}/{reposlug}/pullrequests/{pullRequestId}/comments")
    public CommentResponseDTO createAInlineCommentOnAPullRequest(
        @PathParam("workspace") String workspace,
        @PathParam("reposlug") String reposlug,
        @PathParam("pullRequestId") String pullRequestId,
        InlineCommentRequestDTO inlineCommentRequestDTO
    );
}
