==========
User Guide
==========


How to apply the plugin
-----------------------

.. tabs::

   .. tab:: Groovy
   
      Using the `plugins DSL: <https://docs.gradle.org/current/userguide/plugins.html#sec:plugins_block>`_
      
      .. code-block:: groovy
         
         plugins {
             id 'org.zkovari.changelog' version '0.4.0'
         }
         
         
      Using legacy `plugin application <https://docs.gradle.org/current/userguide/plugins.html#sec:old_plugin_application>`_:

      .. code-block:: groovy
   
         buildscript {
             repositories {
                 jcenter()
             }
             dependencies {
                 classpath 'org.zkovari.changelog:changelog-automation-gradle-plugin:0.4.0'
             }
         }
         
         apply plugin: 'org.zkovari.changelog'
   
   .. tab:: Kotlin
     
      Using the `plugins DSL: <https://docs.gradle.org/current/userguide/plugins.html#sec:plugins_block>`_
   
      .. code-block:: kotlin
      
         plugins {
             id("org.zkovari.changelog") version "0.4.0"
         }
         
      Using legacy `plugin application <https://docs.gradle.org/current/userguide/plugins.html#sec:old_plugin_application>`_:
      
      .. code-block:: kotlin
      
         buildscript {
             repositories {
                 jcenter()
             }
             dependencies {
                 classpath("org.zkovari.changelog:changelog-automation-gradle-plugin:0.4.0")
             }
         }
         
         apply(plugin = "org.zkovari.changelog")




How to generate unreleased changelog entries (YAML files)
---------------------------------------------------------

To create new unreleased changelog entries, the easiest way if you use the plugin's built-in generator script. It is already on the classpath, you just have fetch it
with the ``fetchChangelogScript`` task:

.. code-block:: bash
   
   gradle fetchChangelogScript
   # result: scripts/changelog.sh
   # result: scripts/changelog.bat
   
   # also add permission
   chmod +x scripts/changelog.sh

As a result, the script is present under ``scripts/changelog.sh``. If you're using Windows replace ``changelog.sh`` by ``changelog.bat`` on the examples below.. To generate a new entry, run ``changelog.sh``:

.. code-block:: bash

   ./scripts/changelog.sh --type added "My new feature"
   
As a result, a new changelog entry is generated under ``changelogs/unreleased``.

For type (``-t|--type``) the following values are accepted: ``added``, ``changed``, ``deprecated``, ``fixed``, ``removed``, ``security``.
Shortened values can be also specified, for example, ``a`` corresponds to ``added``, ``secu`` to ``security``, etc.

See ``changelog.sh --help`` for more information.

Optionally specify reference and author
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Optionally a reference (``-r|--reference``) or the author (``-u|--git-username``) can be also specified in the unreleased entry. 
The reference could typically refer an issue or a pull/merge-request number. For author, the Git username is used (from Git config).
E.g. running:

.. code-block:: bash

   ./scripts/changelog.sh --type fixed -u -r "13" "Fix bug"
   
...would create:

.. code-block:: yaml

   title: Fix bug
   reference: 13
   author: zkovari
   type: fixed

How to process unreleased entries into CHANGELOG.md
---------------------------------------------------

If you already have unreleased YAML entries under ``changelogs/unreleased``, you can combine them into your ``CHANGELOG.md``. 
The generated changelog is based on `Keep a Changelog <https://keepachangelog.com/en/1.0.0/>`_.

To process the unreleased entries, run the task ``processChangelogEntries``:

.. code-block:: bash
   
   gradle processChangelogEntries


Result is ``CHANGELOG.md``. The unreleased entries are also automatically removed from ``changelogs/unreleased``.

``CHANGELOG.md``
  .. code-block:: html
    
    # Changelog
    All notable changes to this project will be documented in this file.
  
    The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
    and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).
    
    ## [1.0.0] - 2019-07-21
    ### Added
    - My new feature

For version, always the project's version, while for date, `ISO standard <https://www.iso.org/iso-8601-date-and-time-format.html>`_ format it used: ``YYYY-MM-DD``.

Continuous processing
^^^^^^^^^^^^^^^^^^^^^

New release entries can be continuously generated. In that case, the previous ``CHANGELOG.md`` will be updated with a new released changelog.

As an example, see this project's `changelog <https://github.com/zkovari/gradle-changelog-automation-plugin/blob/feature/docs/CHANGELOG.md>`_.





