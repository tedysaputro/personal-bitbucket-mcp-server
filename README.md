# Personal Bitbucket MCP Server

[![MCP Badge](https://lobehub.com/badge/mcp/tedysaputro-personal-bitbucket-mcp-server?style=for-the-badge)](https://lobehub.com/mcp/tedysaputro-personal-bitbucket-mcp-server)

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
- ⚡ **Multiple Transport Options** - stdio (universal), SSE, and HTTP Stream
- 🎯 **Universal Client Support** - stdio works with all MCP clients (Claude Desktop, Cursor, VS Code, Cherry Studio, and more)

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

This server supports multiple transport protocols. Choose the method that works best for your client:

#### 🎯 Method 1: stdio Transport (Recommended - Works with All Clients)

The stdio transport allows direct process communication without needing a running HTTP server. This is the **universal method** that works with all MCP clients.

**For Claude Desktop**, add this to your config file:

**macOS**: `~/Library/Application Support/Claude/claude_desktop_config.json`  
**Windows**: `%APPDATA%\Claude\claude_desktop_config.json`

```json
{
  "mcpServers": {
    "bitbucket": {
      "command": "docker",
      "args": [
        "run",
        "-i",
        "--rm",
        "-e", "BITBUCKET_EMAIL=your-email@example.com",
        "-e", "BITBUCKET_API_TOKEN=your-api-token",
        "-e", "BITBUCKET_WORKSPACE=your-workspace",
        "subrutin/bitbucket-mcp-server:stdio-0.0.2"
      ]
    }
  }
}
```

Restart Claude Desktop, and you'll see the Bitbucket tools available in the 🔨 tools menu.

**For Cursor IDE**, add to your Cursor settings (same format as Claude Desktop):

```json
{
  "mcpServers": {
    "bitbucket": {
      "command": "docker",
      "args": [
        "run", "-i", "--rm",
        "-e", "BITBUCKET_EMAIL=your-email@example.com",
        "-e", "BITBUCKET_API_TOKEN=your-api-token",
        "-e", "BITBUCKET_WORKSPACE=your-workspace",
        "subrutin/bitbucket-mcp-server:stdio-0.0.2"
      ]
    }
  }
}
```

**For VS Code** with MCP extension (same format):

```json
{
  "mcp.servers": {
    "bitbucket": {
      "command": "docker",
      "args": [
        "run", "-i", "--rm",
        "-e", "BITBUCKET_EMAIL=your-email@example.com",
        "-e", "BITBUCKET_API_TOKEN=your-api-token",
        "-e", "BITBUCKET_WORKSPACE=your-workspace",
        "subrutin/bitbucket-mcp-server:stdio-0.0.2"
      ]
    }
  }
}
```

**For other MCP clients**, use the same Docker command pattern with stdio transport.

#### 🌐 Method 2: SSE Transport (Alternative for Web-Based Clients)

The SSE transport requires the server to be running and accessible via HTTP.

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

### Supported Transports

- ✅ **stdio** - Direct process communication (Universal - Recommended)
  - **Works with: ALL MCP clients** (Claude Desktop, Cursor, VS Code, Cherry Studio, etc.)
  - No HTTP server needed
  - Simplest setup
  - Use image: `subrutin/bitbucket-mcp-server:stdio-0.0.2`
  
- ✅ **SSE (Server-Sent Events)** - HTTP-based transport
  - **Alternative option for web-based clients**
  - Requires running HTTP server
  - Long-lived connections
  - Real-time updates
  - Use image: `subrutin/bitbucket-mcp-server:latest`
  
- ✅ **HTTP Stream** - For custom MCP clients
  - Request/response patterns
  - Programmatic access
  - Use image: `subrutin/bitbucket-mcp-server:latest`

- ⚠️ **HTTPS/TLS** - Not yet supported
  - Currently only HTTP available for SSE
  - HTTPS support planned for future release

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

- ✅ **stdio** - Direct process communication (Universal - Recommended)
  - **Works with: ALL MCP clients** (Claude Desktop, Cursor, VS Code, Cherry Studio, and more)
  - No HTTP server needed
  - Simplest setup and integration
  - **Docker image**: `subrutin/bitbucket-mcp-server:stdio-0.0.2`
  
- ✅ **SSE (Server-Sent Events)** - `GET /mcp/sse`
  - **Alternative for web-based clients**
  - Requires running HTTP server
  - Long-lived connections
  - Real-time updates
  - Currently HTTP only (HTTPS coming soon)
  - **Docker image**: `subrutin/bitbucket-mcp-server:latest`
  
- ✅ **HTTP Stream** - `POST /mcp/stream`
  - For custom MCP clients
  - Request/response patterns
  - Programmatic access
  - **Docker image**: `subrutin/bitbucket-mcp-server:latest`

- ⚠️ **HTTPS/TLS** - Not yet supported (in development)
  - Will enable secure SSE connections
  - SSL certificate configuration needed
  - See [Roadmap](#roadmap) for timeline

## Running with Docker

### Available Docker Images

This project provides two Docker images for different use cases:

1. **`subrutin/bitbucket-mcp-server:latest`** - SSE/HTTP transport
   - For Cursor, VS Code, Cherry Studio
   - Requires running HTTP server
   - Supports SSE and HTTP Stream

2. **`subrutin/bitbucket-mcp-server:stdio-0.0.2`** - stdio transport (Recommended)
   - For ALL MCP clients (Claude Desktop, Cursor, VS Code, Cherry Studio, etc.)
   - Direct process communication
   - No HTTP server needed
   - Simplest setup

### Pull and Run (SSE/HTTP Transport)

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

### Run with stdio Transport (All MCP Clients - Recommended)

```bash
# Pull the stdio image
docker pull subrutin/bitbucket-mcp-server:stdio-0.0.2

# Run interactively (for testing)
docker run -i --rm \
  -e BITBUCKET_EMAIL=your-email@example.com \
  -e BITBUCKET_API_TOKEN=your-api-token \
  -e BITBUCKET_WORKSPACE=your-workspace \
  subrutin/bitbucket-mcp-server:stdio-0.0.2
```

**Note**: For MCP clients (Claude Desktop, Cursor, VS Code, etc.), use the configuration shown in [Method 1: stdio Transport](#-method-1-stdio-transport-recommended---works-with-all-clients) instead of running manually.

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

**Available MCP Transports:**
- `stdio` - Direct process communication (Universal - works with ALL MCP clients - use `stdio-0.0.2` image) ⭐ Recommended
- `GET /mcp/sse` - SSE transport (Alternative - requires HTTP server)
- `POST /mcp/stream` - HTTP Stream transport (for custom MCP clients)

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

The MCP server provides three transport options:

#### 1. stdio - Direct Process Communication ✨ New! (Universal - Recommended)
```bash
# Use with Docker
docker run -i --rm \
  -e BITBUCKET_EMAIL=your-email@example.com \
  -e BITBUCKET_API_TOKEN=your-api-token \
  -e BITBUCKET_WORKSPACE=your-workspace \
  subrutin/bitbucket-mcp-server:stdio-0.0.2
```

Best for:
- **ALL MCP clients** (Claude Desktop, Cursor, VS Code, Cherry Studio, etc.)
- No HTTP server needed
- Direct process communication
- Simplest setup
- Recommended for all use cases

#### 2. SSE (Server-Sent Events)
```
GET http://localhost:8080/mcp/sse
```

Best for:
- **Alternative option** when stdio is not preferred
- Web-based clients
- Long-lived connections
- Real-time updates

#### 3. HTTP Stream
```
POST http://localhost:8080/mcp/stream
Content-Type: application/json
```

Best for:
- Custom MCP clients
- Request/response patterns
- Programmatic access

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

### ✅ Recently Completed

- **stdio Transport Support** - ✨ Now available!
  - Universal transport for ALL MCP clients
  - Direct process communication without HTTP server
  - Works with Claude Desktop, Cursor, VS Code, Cherry Studio, and more
  - Use image: `subrutin/bitbucket-mcp-server:stdio-0.0.2`
  - Released in version 0.0.2

### 🚧 In Development

- **HTTPS/TLS Support** - Enable secure connections for SSE transport
  - Will enable Claude Desktop to use SSE transport
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
