# Bitbucket MCP Server

A Model Context Protocol (MCP) server that provides AI assistants with tools to interact with Bitbucket Cloud repositories. Built with Quarkus, the Supersonic Subatomic Java Framework.

**Author**: [Tedy Saputro](https://tedy.saputro.dev) | **Contact**: [tedy@saputro.dev](mailto:tedy@saputro.dev)

## What is MCP?

The Model Context Protocol (MCP) is an open protocol that standardizes how applications provide context to Large Language Models (LLMs). This server implements MCP to expose Bitbucket operations as tools that AI assistants like Claude, ChatGPT, or other LLM-powered applications can use.

## Features

- 🔧 **11 MCP Tools** for Bitbucket operations
- 🚀 **Native Image Support** with GraalVM for fast startup and low memory footprint
- 🐳 **Multi-Architecture Docker Images** (AMD64 & ARM64)
- 🔐 **Secure Authentication** using Bitbucket App Passwords
- 📦 **RESTful API** for direct HTTP access
- ⚡ **SSE & HTTP Stream Transport** - Compatible with Cursor, VS Code, Cherry Studio

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Table of Contents

- [Quick Start](#quick-start)
- [MCP Tools Reference](#mcp-tools-reference)
- [Configuration](#configuration)
- [Running with Docker](#running-with-docker)
- [Development](#development)
- [Building](#building)
- [API Documentation](#api-documentation)
- [Use Cases](#use-cases)
- [Troubleshooting](#troubleshooting)
- [Roadmap](#roadmap)
- [Contributing](#contributing)

## Quick Start

### Prerequisites

1. **Bitbucket API Token**: Create an API token at https://bitbucket.org/account/settings/api-token/
   - Required permissions: `repository:read`, `pullrequest:read`, `pullrequest:write`
   - See [Creating a Bitbucket API Token](#creating-a-bitbucket-api-token) for detailed instructions
2. **Docker** (optional): For running the containerized version

### Using Docker (Recommended)

```bash
docker run -p 8080:8080 \
  -e BITBUCKET_EMAIL=your-email@example.com \
  -e BITBUCKET_API_TOKEN=your-api-token \
  -e BITBUCKET_WORKSPACE=your-workspace \
  subrutin/bitbucket-mcp-server:latest
```

### Using MCP with Supported Clients

This server uses **SSE (Server-Sent Events)** transport, which requires the server to be running and accessible via HTTP/HTTPS.

**Step 1:** Start the server using Docker:

```bash
docker run -d \
  --name bitbucket-mcp \
  -p 8080:8080 \
  -e BITBUCKET_EMAIL=your-email@example.com \
  -e BITBUCKET_API_TOKEN=your-api-token \
  -e BITBUCKET_WORKSPACE=your-workspace \
  subrutin/bitbucket-mcp-server:latest
```

**Step 2:** Configure your MCP client:

#### ✅ Cursor IDE

Add to your Cursor settings:

```json
{
  "mcpServers": {
    "bitbucket": {
      "url": "http://localhost:8080/mcp/sse"
    }
  }
}
```

#### ✅ VS Code

Install the MCP extension and add to your settings:

```json
{
  "mcp.servers": {
    "bitbucket": {
      "url": "http://localhost:8080/mcp/sse"
    }
  }
}
```

#### ✅ Cherry Studio

- Go to Settings
- Select MCP
- Click Button Create

```
  "Type": sse
  "url": "http://localhost:8080/mcp/sse",
  "name": "Bitbucket MCP"

```

#### ❌ Claude Desktop (Not Yet Supported)

Claude Desktop requires HTTPS for SSE connections, but this server currently runs on HTTP only. Support for Claude Desktop will be available once HTTPS/TLS support is added (see [Roadmap](#roadmap)).

**Workaround**: Use stdio transport (planned for future release) or set up a reverse proxy with HTTPS.

### Supported Transports

- ✅ **SSE (Server-Sent Events)** - Works with Cursor, VS Code, Cherry Studio
- ✅ **HTTP Stream** - For custom MCP clients
- ❌ **stdio** - Not yet supported (planned)
- ❌ **HTTPS/TLS** - Not yet supported (required for Claude Desktop)

## MCP Tools Reference

This server provides 11 tools for interacting with Bitbucket:

### Pull Request Tools

#### 1. `findAllPullRequest`
Returns all pull requests on the specified repository.

**Parameters:**
- `workspace` (string): The workspace ID or slug where the repository is located
- `reposlug` (string): The repository slug or name to get pull requests from

**Example:**
```
Use the findAllPullRequest tool with workspace "myteam" and reposlug "myrepo"
```

#### 2. `findAPullRequest`
Returns a specific pull request by ID with detailed information.

**Parameters:**
- `workspace` (string): The workspace ID or slug
- `reposlug` (string): The repository slug
- `pullRequestId` (integer): The pull request ID to get details from

**Example:**
```
Get details of pull request #42 from myteam/myrepo
```

#### 3. `findDiffStatForPullRequest`
Returns the diffstat (statistics about changes) for a pull request.

**Parameters:**
- `workspace` (string): The workspace ID or slug
- `reposlug` (string): The repository slug
- `id` (integer): The pull request ID

**Example:**
```
Show me the diffstat for PR #42
```

#### 4. `findListChangesInAPullRequest`
Returns the actual diff/changes in a pull request, showing added/removed lines.

**Parameters:**
- `workspace` (string): The workspace ID or slug
- `reposlug` (string): The repository slug
- `id` (integer): The pull request ID

**Example:**
```
Show me all the code changes in PR #42
```

### Pull Request Comment Tools

#### 5. `createComment`
Creates a general comment on a pull request.

**Parameters:**
- `workspace` (string): The workspace ID or slug
- `reposlug` (string): The repository slug
- `pullRequestId` (integer): The pull request ID to comment on
- `commentText` (string): The comment text content (supports Markdown)

**Example:**
```
Add a comment to PR #42 saying "LGTM! Great work on the refactoring."
```

#### 6. `updateComment`
Updates an existing comment on a pull request.

**Parameters:**
- `workspace` (string): The workspace ID or slug
- `reposlug` (string): The repository slug
- `pullRequestId` (integer): The pull request ID
- `commentId` (integer): The comment ID to update
- `commentText` (string): The updated comment text content

**Example:**
```
Update comment #123 on PR #42 with new text
```

#### 7. `createInlineComment`
Creates an inline comment on a specific line of code in a pull request diff.

**Parameters:**
- `workspace` (string): The workspace ID or slug
- `reposlug` (string): The repository slug
- `pullRequestId` (integer): The pull request ID
- `filePath` (string): The EXACT file path as shown in the PR diff (case-sensitive)
- `lineNumber` (integer): The line number in the NEW version of the file
- `commentText` (string): The comment text in Markdown format

**Important Notes:**
- The `filePath` must EXACTLY match the file path shown in the PR diff
- The `lineNumber` must be from the NEW/MODIFIED version (lines with '+' in diff)
- The line must exist in the PR diff - you cannot comment on unchanged lines
- **Workflow**: First call `findListChangesInAPullRequest` to get the diff, then identify the correct file path and line number

**Example:**
```
First, get the diff for PR #42, then add an inline comment on line 25 of src/main/java/Service.java
```

#### 8. `findAComment`
Returns a specific pull request comment with its details.

**Parameters:**
- `workspace` (string): The workspace ID or slug
- `reposlug` (string): The repository slug
- `pullRequestId` (integer): The pull request ID
- `commentId` (integer): The comment ID to retrieve

**Example:**
```
Get details of comment #123 from PR #42
```

#### 9. `findCommentList`
Returns a paginated list of comments for a specific pull request.

**Parameters:**
- `workspace` (string): The workspace ID or slug
- `reposlug` (string): The repository slug
- `pullRequestId` (integer): The pull request ID
- `page` (integer): The page number for pagination
- `pageLength` (integer): The number of items per page
- `size` (integer): The total number of items

**Example:**
```
Get the first 10 comments from PR #42
```

### User Profile Tools

#### 10. `Fetch user profile`
Returns the authenticated user's Bitbucket profile information.

**Parameters:** None

**Example:**
```
Show me my Bitbucket profile
```

## Configuration

### Environment Variables

This server requires the following environment variables for authentication with Bitbucket Cloud:

| Variable | Required | Description | Example |
|----------|----------|-------------|---------|
| `BITBUCKET_EMAIL` | Yes | Your Bitbucket account email | `user@example.com` |
| `BITBUCKET_API_TOKEN` | Yes | API token with repository and PR permissions | `ATBBxxx...` |
| `BITBUCKET_WORKSPACE` | Yes | Default workspace slug for operations | `myteam` |

### Creating a Bitbucket API Token

1. Go to https://bitbucket.org/account/settings/api-token/
2. Click "Create API token"
3. Give it a label (e.g., "MCP Server")
4. Select permissions:
   - **Repositories**: Read
   - **Pull requests**: Read, Write
5. Click "Create" and copy the generated token (use this as `BITBUCKET_API_TOKEN`)

**Note:** API tokens are different from app passwords. API tokens provide more granular permissions and are the recommended authentication method. For more information, see [Atlassian's API Token documentation](https://support.atlassian.com/bitbucket-cloud/docs/create-an-api-token/).

### Application Configuration

The server configuration is in `src/main/resources/application.yml`:

```yaml
bitbucket:
  api:
    email: ${BITBUCKET_EMAIL:}
    token: ${BITBUCKET_API_TOKEN:}
    workspace: ${BITBUCKET_WORKSPACE:}

quarkus:
  rest-client:
    bitbucket-api:
      url: https://api.bitbucket.org/2.0
```

### Supported Transports

This MCP server supports the following transport protocols:

- ✅ **SSE (Server-Sent Events)** - `GET /mcp/sse`
  - Works with: **Cursor IDE**, **VS Code**, **Cherry Studio**
  - Long-lived connections
  - Real-time updates
  - Currently HTTP only (HTTPS coming soon)
  
- ✅ **HTTP Stream** - `POST /mcp/stream`
  - For custom MCP clients
  - Request/response patterns
  - Programmatic access

- ❌ **stdio** - Not yet supported (in development)
  - Will enable direct process communication
  - Required for simplified Claude Desktop integration
  - See [Roadmap](#roadmap) for timeline

- ❌ **HTTPS/TLS** - Not yet supported (in development)
  - Required for Claude Desktop SSE connections
  - SSL certificate configuration needed
  - See [Roadmap](#roadmap) for timeline

## Running with Docker

### Pull and Run

```bash
# Pull the latest image
docker pull subrutin/bitbucket-mcp-server:latest

# Run the container
docker run -d \
  --name bitbucket-mcp \
  -p 8080:8080 \
  -e BITBUCKET_EMAIL=your-email@example.com \
  -e BITBUCKET_API_TOKEN=your-api-token \
  -e BITBUCKET_WORKSPACE=your-workspace \
  subrutin/bitbucket-mcp-server:latest
```

### Using Docker Compose

Create a `docker-compose.yml`:

```yaml
version: '3.8'

services:
  bitbucket-mcp:
    image: subrutin/bitbucket-mcp-server:latest
    ports:
      - "8080:8080"
    environment:
      BITBUCKET_EMAIL: ${BITBUCKET_EMAIL}
      BITBUCKET_API_TOKEN: ${BITBUCKET_API_TOKEN}
      BITBUCKET_WORKSPACE: ${BITBUCKET_WORKSPACE}
    restart: unless-stopped
```

Then run:
```bash
docker-compose up -d
```

### Health Check

Check if the server is running:
```bash
curl http://localhost:8080/q/health
```

## Development

### Running in Dev Mode

Run the application in development mode with live coding enabled:

```bash
./mvnw quarkus:dev
```

The Dev UI is available at <http://localhost:8080/q/dev/>

### Testing MCP Tools Locally

Once the server is running, you can test the MCP endpoints:

```bash
# Test SSE connection (Server-Sent Events)
curl -N http://localhost:8080/mcp/sse

# Test HTTP Stream connection
curl -N http://localhost:8080/mcp

# Test with MCP Inspector (if installed)
npx @modelcontextprotocol/inspector http://localhost:8080/mcp/sse
```

**Available MCP Endpoints:**
- `GET /mcp/sse` - SSE transport (recommended for Claude Desktop)
- `POST /mcp/` - HTTP Stream transport (for other MCP clients)

## Building

### Building Multi-Architecture Docker Images

This project includes a script to build native Docker images for both AMD64 and ARM64 architectures:

```bash
# Build and push multi-platform image
./build-multiplatform.sh subrutin/bitbucket-mcp-server 0.0.1

# The script will:
# 1. Use Docker buildx to create multi-arch images
# 2. Build native executables with GraalVM
# 3. Push to Docker Hub registry
```

### Building JVM Version

Package the application as a JVM application:

```bash
./mvnw package
```

This produces `quarkus-run.jar` in the `target/quarkus-app/` directory.

Run with:
```bash
java -jar target/quarkus-app/quarkus-run.jar
```

### Building Native Executable

Build a native executable with GraalVM:

```bash
# With local GraalVM installation
./mvnw package -Dnative

# Or using Docker (no GraalVM installation required)
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

Run the native executable:
```bash
./target/bitbucket-mcp-server-1.0.0-SNAPSHOT-runner
```

**Benefits of Native Image:**
- ⚡ Fast startup time (~0.01s vs ~1-2s for JVM)
- 💾 Low memory footprint (~20-30MB vs ~100-200MB for JVM)
- 📦 Small image size (~50-100MB vs ~300-400MB for JVM)

### Building Docker Image Manually

```bash
# Build native image
docker build -f src/main/docker/Dockerfile.multiplatform -t bitbucket-mcp-server:native .

# Build JVM image
docker build -f src/main/docker/Dockerfile.jvm -t bitbucket-mcp-server:jvm .
```

## API Documentation

### MCP Endpoints

The MCP server provides two transport options:

#### 1. SSE (Server-Sent Events) - Recommended
```
GET http://localhost:8080/mcp/sse
```

Best for:
- Long-lived connections
- Real-time updates

#### 2. HTTP Stream
```
POST http://localhost:8080/mcp/stream
Content-Type: application/json
```

Best for:
- Custom MCP clients
- Request/response patterns
- Programmatic access

**Note:** This server does **not support stdio transport**. Only SSE and HTTP Stream are available.

### REST API Endpoints

In addition to MCP tools, the server also exposes REST endpoints:

#### Get Repositories
```bash
GET /bitbucket/repositories?workspace={workspace}&page={page}&pagelen={pagelen}
```

Example:
```bash
curl "http://localhost:8080/bitbucket/repositories?workspace=myteam&page=1&pagelen=10"
```

### Health and Metrics

```bash
# Health check
curl http://localhost:8080/q/health

# Readiness check
curl http://localhost:8080/q/health/ready

# Liveness check
curl http://localhost:8080/q/health/live

# Metrics (if enabled)
curl http://localhost:8080/q/metrics
```

## Use Cases

### Code Review Automation

Use AI assistants to:
- Review pull requests and provide feedback
- Check for code quality issues
- Suggest improvements
- Add inline comments on specific lines

**Example prompt for Claude:**
```
Review pull request #42 in workspace "myteam" repository "myrepo". 
Check for:
1. Code quality issues
2. Potential bugs
3. Best practice violations
Add inline comments where improvements are needed.
```

### PR Management

- List all open pull requests
- Get detailed information about specific PRs
- View diffs and changes
- Manage comments and discussions

**Example prompt:**
```
Show me all open pull requests in myteam/myrepo and summarize what each one does
```

### Team Collaboration

- Fetch user profiles
- Track PR activity
- Monitor code changes
- Facilitate code review discussions

## Troubleshooting

### Common Issues

**Issue: "Authentication failed"**
- Verify your `BITBUCKET_EMAIL` and `BITBUCKET_API_TOKEN` are correct
- Ensure the API token has the required permissions (Repositories: Read, Pull requests: Read & Write)
- Check that the workspace exists and you have access
- Make sure you're using an API token, not an app password

**Issue: "Repository not found"**
- Verify the workspace and repository slug are correct
- Ensure you have read access to the repository
- Check that the repository exists in the specified workspace

**Issue: "Cannot create inline comment"**
- First call `findListChangesInAPullRequest` to get the exact file paths
- Ensure the file path matches exactly (case-sensitive)
- Verify the line number is from the NEW version of the file
- The line must be part of the PR changes (not unchanged lines)

**Issue: "Docker image won't start"**
- Check that all required environment variables are set
- Verify port 8080 is not already in use
- Check Docker logs: `docker logs bitbucket-mcp`

### Logs

View application logs:

```bash
# Docker logs
docker logs -f bitbucket-mcp

# Local development
# Logs are printed to console when running ./mvnw quarkus:dev
```

## Roadmap

We're actively working on improving the Bitbucket MCP Server. Here's what's planned:

### 🚧 In Development

- **stdio Transport Support** - Enable stdio-based MCP clients to connect directly without HTTP
  - Will allow simpler integration with Claude Desktop (no need to run separate server)
  - Direct process communication for better performance
  - Expected in next major release

- **HTTPS/TLS Support** - Enable secure connections for Claude Desktop compatibility
  - Required for Claude Desktop SSE connections
  - SSL certificate configuration
  - Automatic HTTP to HTTPS redirect
  - Expected in next major release

### 📋 Planned Features

- **Enhanced Authentication**
  - OAuth 2.0 support
  - Multiple workspace support
  - Token refresh mechanism



### 💡 Future Considerations

- GitHub integration (similar MCP server for GitHub)
- GitLab integration
- Webhook support for real-time updates
- Custom tool plugins system

Want to contribute? Check out our [Contributing](#contributing) section!

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

### How to Contribute

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Development Setup

See the [Development](#development) section for instructions on setting up your local environment.

## License

This project is open source. Please check the LICENSE file for details.

## Author

**Tedy Saputro**
- 📧 Email: [tedy@saputro.dev](mailto:tedy@saputro.dev)
- 🌐 Website: [tedy.saputro.dev](https://tedy.saputro.dev)

## Resources

- [Model Context Protocol Documentation](https://modelcontextprotocol.io/)
- [Quarkus Documentation](https://quarkus.io/)
- [Bitbucket Cloud REST API](https://developer.atlassian.com/cloud/bitbucket/rest/)
- [GraalVM Native Image](https://www.graalvm.org/native-image/)

## Support

For issues, questions, or contributions:

- 📧 **Email**: [tedy@saputro.dev](mailto:tedy@saputro.dev)
- 🌐 **Website**: [tedy.saputro.dev](https://tedy.saputro.dev)
- 💻 **GitHub**: Visit the project repository for issues and pull requests
