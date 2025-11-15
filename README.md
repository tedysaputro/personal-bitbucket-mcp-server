# bitbucket-mcp-server

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/bitbucket-mcp-server-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Bitbucket Cloud REST client

This project now exposes a REST endpoint that proxies to the Bitbucket Cloud v2 API using the Quarkus REST Client with Jackson serialization. The client relies on [Atlassian's app password authentication](https://developer.atlassian.com/cloud/bitbucket/rest/intro/#authentication).

### Configuration

Set the following properties (see `src/main/resources/application.yml`) either directly or through environment variables:

```yaml
bitbucket:
  api:
    username: ${BITBUCKET_USERNAME:}
    app-password: ${BITBUCKET_APP_PASSWORD:}
    workspace: ${BITBUCKET_WORKSPACE:}

quarkus:
  rest-client:
    bitbucket-api:
      url: https://api.bitbucket.org/2.0
```

- `BITBUCKET_USERNAME`: the Bitbucket account (email/workspace handle) tied to the app password.
- `BITBUCKET_APP_PASSWORD`: app password granting API access.
- `BITBUCKET_WORKSPACE`: default workspace slug when the API consumer does not pass one.

### Usage

The HTTP endpoint `/bitbucket/repositories` returns Bitbucket repositories in JSON:

```
GET /bitbucket/repositories?workspace=<optional>&page=<optional>&pagelen=<optional>
```

Behind the scenes `BitbucketRepositoryService` delegates to `BitbucketApiClient`, which automatically adds the `Authorization: Basic <token>` header via `BitbucketAuthRequestFilter`.
