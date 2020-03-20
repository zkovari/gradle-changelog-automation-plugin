# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.4.0] - 2020-03-20
### Added
- Update Gradle to 6.2.2
- 36 Handle hashtag in references

## [0.3.0] - 2019-08-16
### Added
- 35 Accept shortened values for types in changelog.sh

### Changed
- 25 Do not display Help menu automatically in case of a wrong parameter

## [0.2.2] - 2019-07-21
### Fixed
- 15 Fix branch detection in changelog.sh and denote it in the generated filename

## [0.2.1] - 2019-07-20
### Changed
- Define project descriptions in the POM
- Define URL in the POM

## [0.2.0] - 2019-07-19
### Added
- Delete unreleased changelog entries after processing them into CHANGELOG.md

### Fixed
- Keep previous entries after updating changelog
- Close InputStream in changelog parsing

## [0.1.1] - 2019-07-18
### Fixed
- Handle empty String values in release entry generator
- Remove unnecessary quotes in generated YAML file

## [0.1.0] - 2019-07-18
### Added
- Initial implementation of changelog automation Gradle plugin
