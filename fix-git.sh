#!/bin/bash

# Check current branch
echo "Current branch:"
git branch

# If no branch exists yet, create and commit to main branch
if [ $? -ne 0 ] || [ -z "$(git branch)" ]; then
  echo "No branches exist. Creating main branch..."
  git checkout -b main
else
  # If we're not on main branch, create it and switch to it
  if [ "$(git branch --show-current)" != "main" ]; then
    echo "Creating and switching to main branch..."
    git checkout -b main
  fi
fi

# Add all files if not already added
git add .

# Commit if there are changes to commit
git diff --cached --quiet || git commit -m "Initial commit for Netflix Support Chat bot"

# Push to GitHub with the correct branch name
echo "Pushing to GitHub..."
git push -u origin main

echo "Done! Check GitHub repository to verify the push."
