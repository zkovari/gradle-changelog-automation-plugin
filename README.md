[![GitLab pipeline status](https://gitlab.com/zkovari/gradle-changelog-automation-plugin/badges/master/pipeline.svg)](https://gitlab.com/zkovari/gradle-changelog-automation-plugin/pipelines)
[![Maven central](https://img.shields.io/maven-central/v/org.zkovari.changelog/changelog-automation-gradle-plugin.svg)](https://search.maven.org/search?q=g:%20org.zkovari.changelog%20a:changelog-automation-gradle-plugin)
[![Documentation Status](https://readthedocs.org/projects/gradle-changelog-automation-plugin/badge/?version=latest)](https://gradle-changelog-automation-plugin.readthedocs.io/en/latest/?badge=latest)
[![codecov](https://codecov.io/gl/zkovari/gradle-changelog-automation-plugin/branch/master/graph/badge.svg)](https://codecov.io/gl/zkovari/gradle-changelog-automation-plugin)
[![Maintainability](https://api.codeclimate.com/v1/badges/4560682da831d5bdeb6f/maintainability)](https://codeclimate.com/github/zkovari/gradle-changelog-automation-plugin/maintainability)
[![Gitter](https://img.shields.io/gitter/room/zkovari/gradle-changelog-automation-plugin)](https://gitter.im/gradle-changelog-automation-plugin)
# Gradle Changelog Automation Plugin

Inspired by [GitLab](https://gitlab.com/gitlab-org/gitlab-ce/): [https://about.gitlab.com/2018/07/03/solving-gitlabs-changelog-conflict-crisis/](https://about.gitlab.com/2018/07/03/solving-gitlabs-changelog-conflict-crisis/)

## What does this plugin do

This plugin is able to generate `CHANGELOG.md` from YAML files.
One YAML file represents one unreleased changelog entry.
For example, a new added feature's entry would look like this:

```yaml
title: My new feature
reference: GH-1
author: John Doe
type: added
```

Then the plugin can process the unreleased YAML entries and combine them into `CHANGELOG.md`.
The changelog is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

As an example, see this project's changelog which is automatically generated by this plugin:
[CHANGELOG.md](CHANGELOG.md).

## Getting started

### How to apply the plugin

Using the [plugins DSL](https://docs.gradle.org/current/userguide/plugins.html#sec:plugins_block):

```gradle
plugins {
    id 'org.zkovari.changelog' version '0.4.0'
}
```

Or using legacy [plugin application](https://docs.gradle.org/current/userguide/plugins.html#sec:old_plugin_application):

```gradle
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'org.zkovari.changelog:changelog-automation-gradle-plugin:0.4.0'
    }
}

apply plugin: 'org.zkovari.changelog'
```

### How to generate YAML entries

The plugin contains a generator script that you can fetch with the `fetchChangelogScript` task:

```bash
gradle fetchChangelogScript
# result: scripts/changelog.sh

# add permission
chmod +x scripts/changelog.sh
```

To generate a new unreleased changelog entry (YAML), run `changelog.sh`:

```bash
./scripts/changelog.sh --type added "My new feature"
```

As a result, a new changelog entry is generated under `changelogs/unreleased`,
in this case the following one:

```yaml
title: My new feature
reference:
author:
type: added
```

See `changelog.sh --help` or the [detailed documentation](https://gradle-changelog-automation-plugin.readthedocs.io/en/latest/2-user-guide.html#how-to-generate-unreleased-changelog-entries-yaml-files) for further information.

### How to process unreleased changelog entries

If you already have unreleased YAML entries under `changelogs/unreleased`, you can combine them into your `CHANGELOG.md` with the task `processChangelogEntries`:

```bash
gradle processChangelogEntries
```

As a result, your `CHANGELOG.md` is updated with a new release entry.
As an exmaple, see this project's changelog which is automatically generated by this plugin: [CHANGELOG.md](CHANGELOG.md).

The task `processChangelogEntries` also removes your unreleased entries from `changelogs/unreleased`.

## Further documentation

Please visit our [detailed documentation](https://gradle-changelog-automation-plugin.readthedocs.io/en/latest/) for further information.

## License

![GitHub](https://img.shields.io/github/license/zkovari/gradle-changelog-automation-plugin)
