#!/bin/bash
set -ex

dir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
version=$(grep version= "$dir/../gradle.properties" | sed s/version=//)

git tag "v$version" -a