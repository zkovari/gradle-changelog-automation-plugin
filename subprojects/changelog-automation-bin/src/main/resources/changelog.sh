#!/usr/bin/env bash
#
# Copyright 2019 Zsolt Kovari
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
# 
#     http://www.apache.org/licenses/LICENSE-2.0
# 
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
# 
#-------------------------------------------------------------------------
# Authors:
#     Zsolt Kovari (zsolt@kovaridev.com)
#------------------------------------------------------------------------- 
# Script: changelog.sh
# Version: 0.2.0
# Last updated: 2019-08-06
# URL: https://github.com/zkovari/gradle-changelog-automation-plugin
# Issue tracker: https://github.com/zkovari/gradle-changelog-automation-plugin/issues
#-------------------------------------------------------------------------
# Usage: changelog.sh [OPTION]... --type [TYPE] [TITLE]
#        For the available options and examples, see changelog.sh --help 
#

# abort on error
set -e

# if you update it, update the header comment too as well as the date
VERSION=0.2.0

function display_help {
    cat <<Help
Usage: $(basename "$0") [OPTION]... --type [TYPE] [TITLE]

Generate a .yml file that corresponds to a new changelog entry.
The .yml file is generated under 'changelogs/unreleased' directory
relative to the current working directory where this script is executed.

Example: $(basename "$0") --type added "My new feature"

------------------------------------------------------------------------------

Obligatory:

  -t| --type [TYPE]     the type of the changelog entry. Available values:
                        added, changed, deprecated, fixed, removed, security.
                        The given value can be also shortened, e.g. '-t a'
                        corresponds to 'added', '-t secu' to 'security', etc.

Options for changelog content generation:

  -r| --reference [REF] write an extra reference to the changelog.
                        Typically reference of an issue or a merge/pull request
  -u| --git-username    write the current git user.name to the changelog

Miscellaneous:

  -h| --help            display this help text and exit
  -v| --version         display version information and exit
  --dry-run             display the .yml changelog entry without 
                        generating the file

------------------------------------------------------------------------------

Examples:

# 'added' changelog entry
$(basename "$0") --type added "Add new feature"

# 'removed' changelog entry
$(basename "$0") -t removed "Removed legacy behaviour"

# 'added' changelog entry with extra reference and git user name.
# Reference '18' could point to an issue or a merge/pull request
$(basename "$0") -t added -r 18 -u "Add new feature"

------------------------------------------------------------------------------

Author: Zsolt Kovari
Reference: https://github.com/zkovari/gradle-changelog-automation-plugin

Help
}

function checkEmptyArg {
    if [[ -z $1 ]]; then
        echo "$2 must be specified."
        echo "See options and examples in --help"
        exit 1
    fi
}

function getType {
    if [[ "added" =~ ^$1 ]]; then
        echo "added"
    elif [[ "changed" =~ ^$1 ]]; then
        echo "changed"
    elif [[ "deprecated" =~ ^$1 ]]; then
        echo "deprecated"
    elif [[ "fixed" =~ ^$1 ]]; then
        echo "fixed"
    elif [[ "removed" =~ ^$1 ]]; then
        echo "removed"
    elif [[ "security" =~ ^$1 ]]; then
        echo "security"
    else
        echo ""
    fi
}

function checkType {
	if [[ -z $(getType $1) ]]; then
        echo "Invalid value was specified for --type: '$1'. Accepted values are: added, changed, deprecated, fixed, removed, security"
        echo "See options and examples in --help"
        exit 1
    fi		
    #if [[ ! "added" =~ $1 && ! "changed" =~ $1 && ! "deprecated" =~ $1 && ! "fixed" =~ $1 && ! "removed" =~ $1 && ! "security" =~ $1 ]]; then
    #fi
}

OUTPUT_DIR="changelogs/unreleased"
TITLE=""
REFERENCE=""
AUTHOR=""
TYPE=""
USE_GIT_USERNAME=false
DRY_RUN=false

while [[ $# -gt 0 ]]
do
    key="${1}"
    case ${key} in
    -t|--type)
        TYPE="${2}"
        shift # past argument
        shift # past value
        ;;
    -r|--reference)
        REFERENCE="${2}"
        shift # past argument
        shift # past value
        ;;
    -u|--git-username)
        USE_GIT_USERNAME=true
        shift # past argument
        ;;
    -v| --version)
        echo "Version: $VERSION"
        exit 0
        shift # past argument
        ;;
    --dry-run)
        DRY_RUN=true
        shift # past argument
        ;;
    -h|--help)
        display_help
        exit 0
        shift # past argument
        ;;
    *)    # unknown option
        TITLE=$1        
        shift # past argument
        ;;
    esac
done

checkEmptyArg "$TITLE" "Title"
checkEmptyArg "$TYPE" "Type"
checkType "$TYPE"

if hash git 2>/dev/null; then
    if [[ "$USE_GIT_USERNAME" == true ]]; then
        AUTHOR=$(git config --get user.name)
    fi
fi

PARSED_TYPE=$(getType $TYPE)
CHANGELOG_ENTRY="# Auto-generated by $(basename "$0") script. Version: $VERSION
---
title: $TITLE
reference: $REFERENCE
author: $AUTHOR
type: $PARSED_TYPE"

if [[ "$DRY_RUN" = true ]]; then
    echo "$CHANGELOG_ENTRY"
    exit 0
fi

if [[ ! -d $OUTPUT_DIR ]]; then
    echo "Output directory is created: $OUTPUT_DIR"
    mkdir -p $OUTPUT_DIR 
fi

if hash git 2>/dev/null; then
    # get current branch name
    if git rev-parse --abbrev-ref HEAD >/dev/null 2>/dev/null; then
        FILENAME=$(git rev-parse --abbrev-ref HEAD 2>&1)
    else
        echo "WARN: Not a git repository: $(pwd)"
        FILENAME=""
    fi
else
    FILENAME="$(date +"%Y-%m-%d_%H-%M-%S")_changelog_entry"
fi
if [[ $FILENAME == "" || $FILENAME =~ "fatal: not a git" ]]; then
    FILENAME="$(date +"%Y-%m-%d_%H-%M-%S")_changelog_entry" 
fi

# replace unwanted characters to _
FILENAME=${FILENAME//[^a-zA-Z0-9_-]/_}
FILENAME="$FILENAME.yml"

FILEPATH="$OUTPUT_DIR/$FILENAME"
if [[ -f "$FILEPATH" ]]; then
    FILENAME="$(date +"%Y-%m-%d_%H-%M-%S")_$FILENAME"
    FILEPATH="$OUTPUT_DIR/$FILENAME"
fi

echo "$CHANGELOG_ENTRY" > "$FILEPATH"
echo "New changelog was generated to: $FILEPATH"
