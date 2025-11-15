package com.subrutin.bitbucket.resource;

import com.subrutin.bitbucket.dto.BitbucketRepositoriesResponseDTO;
import com.subrutin.bitbucket.service.BitbucketRepositoryService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/bitbucket/repositories")
@Produces(MediaType.APPLICATION_JSON)
public class BitbucketRepositoryResource {

    @Inject
    BitbucketRepositoryService repositoryService;

    @GET
    public BitbucketRepositoriesResponseDTO listRepositories(
            @QueryParam("workspace") String workspace,
            @QueryParam("page") Integer page,
            @QueryParam("pagelen") Integer pageLength) {
        return repositoryService.listRepositories(workspace, page, pageLength);
    }
}
