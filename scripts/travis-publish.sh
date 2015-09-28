#!/bin/bash

# Example setting to use at command line for testing:
# export TRAVIS_SCALA_VERSION=2.10.5;export TRAVIS_PULL_REQUEST="false";export TRAVIS_BRANCH="master"

export publish_cmd="publishLocal"

if [[ $TRAVIS_PULL_REQUEST == "false" && $TRAVIS_BRANCH == "master" && $(cat version.sbt) =~ "-SNAPSHOT" ]]; then
  export publish_cmd="publish gitSnapshots publish"
fi

sbt_cmd="sbt ++$TRAVIS_SCALA_VERSION"

coverage="$sbt_cmd coverage validateJVM coverageReport && bash <(curl -s https://codecov.io/bash)"

run_cmd="$coverage && $sbt_cmd clean validate $publish_cmd"
eval $run_cmd
