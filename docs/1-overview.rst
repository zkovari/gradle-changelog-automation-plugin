========
Overview
========

Problem to solve
-----------------------------------

The *Gradle Changelog Automation Plugin* has one purpose: **to automate your CHANGELOG.md generation**.

The plugin addresses the following problems:

* frequent merge conflicts in your changelog file
   * even with only 2 developers, merge conflicts are very likely to happen
* time-consuming manual updates
   * any time you make a release, you need to be sure you also manually update your changelog file
* human errors
   * due to the manual maintenance, you might make mistakes (e.g. missing entry, wrong version/date, etc.)

Provided solution
-----------------

Generate your new unreleased changelog entries into separate **YAML** files. One YAML file represents one changelog entry. 
For example, for a new feature, you would create something like this:

.. code-block:: yaml
   
   title: My new feature
   reference: GH-1
   author: John Doe
   type: added

Then, during release time, use this plugin to automatically process your unreleased entries and combine them into your ``CHANGELOG.md`` file:

.. code-block:: html
  
  ## [1.0.0] - 2019-07-21
  ### Added
  - GH-1 My new feature (John Doe)


Followed conventions
---------------------

The plugin generates a ``CHANGELOG.md`` file in the root of the project. The content of the generated changelog is based on `Keep a Changelog <https://keepachangelog.com/en/1.0.0/>`_.

The unreleased changelog entries are expected to be under ``changelogs/unreleased`` directory in the root of the project.

The generator script that generates YAML files is expected under ``scripts/changelog.sh``.

Original source of idea
-----------------------

This plugin was inspired by `GitLab <https://gitlab.com/gitlab-org/gitlab-ce/>`_:

* https://about.gitlab.com/2018/07/03/solving-gitlabs-changelog-conflict-crisis/
