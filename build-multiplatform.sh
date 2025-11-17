#!/bin/bash

# Build dan push Docker image untuk multiple platforms (ARM64 dan AMD64)
# Usage: ./build-multiplatform.sh [image-name] [tag]

# Set default values
IMAGE_NAME=${1:-"subrutin/bitbucket-mcp-server"}
TAG=${2:-"latest"}
FULL_IMAGE_NAME="$IMAGE_NAME:$TAG"

echo "🏗️  Building multi-platform Docker image: $FULL_IMAGE_NAME"
echo "📦  Platforms: linux/amd64, linux/arm64"

# Ensure we're using the multiarch builder
docker buildx use multiarch

# Build and push multi-platform image
docker buildx build \
    --platform linux/amd64,linux/arm64 \
    --file src/main/docker/Dockerfile.multiplatform \
    --tag $FULL_IMAGE_NAME \
    --push \
    .

if [ $? -eq 0 ]; then
    echo "✅  Multi-platform build completed successfully!"
    echo "📋  Image: $FULL_IMAGE_NAME"
    echo "🔍  To verify platforms, run: docker manifest inspect $FULL_IMAGE_NAME"
else
    echo "❌  Build failed!"
    exit 1
fi
