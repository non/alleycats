addCommandAlias("gitSnapshots", ";set version in ThisBuild := git.gitDescribedVersion.value.get + \"-SNAPSHOT\"")
enablePlugins(GitBranchPrompt)
