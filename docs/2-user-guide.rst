==========
User Guide
==========


How to apply the plugin
-----------------------

Using the plugins DSL:

.. tabs::

   .. tab:: Groovy
   
      .. code-block:: groovy
         
         plugins {
             id 'org.zkovari.changelog' version '0.2.2'
         }
   .. tab:: Kotlin
   
      .. code-block:: kotlin
      
         plugins {
             id("org.zkovari.changelog") version "0.2.2"
         }

Using legacy plugin application:

.. tabs::

   .. tab:: Groovy

      .. code-block:: groovy
   
         buildscript {
             repositories {
                 jcenter()
             }
             dependencies {
                 classpath 'org.zkovari.changelog:changelog-automation-gradle-plugin:0.2.2'
             }
         }
         
         apply plugin: 'org.zkovari.changelog'
   .. tab:: Kotlin

      .. code-block:: kotlin
      
         buildscript {
             repositories {
                 jcenter()
             }
             dependencies {
                 classpath("org.zkovari.changelog:changelog-automation-gradle-plugin:0.2.2")
             }
         }
         
         apply(plugin = "org.zkovari.changelog")


How to generate unreleased changelog entries (YAML files)
---------------------------------------------------------

The easiest way if you generate new unreleased changelog entries with the plugin's script. It is already on the classpath, you just have fetch it
with the ``fetchChangelogScript`` task:

.. code-block:: bash
   
   gradle fetchChangelogScript
   # result: scripts/changelog.sh
   
   # also add permission to run it
   chmod +x scripts/changelog.sh

As a result, the script is present under ``scripts/changelog.sh``. To generate a new entry, run ``changelog.sh``:

.. code-block:: bash

   ./scripts/changelog.sh --type added "My new feature"
   
As a result, a new changelog entry is generated under ``changelogs/unreleased``.

See ``changelog.sh --help`` for more information.

How to process unreleased entries into CHANGELOG.md
---------------------------------------------------

If you already have unreleased YAML entries under ``changelogs/unreleased``, you can process them and combine them into your ``CHANGELOG.md``. To do so, run the 
``processChangelogEntries`` task:

.. code-block:: bash
   
   gradle processChangelogEntries


As a result, the unreleased entries are combined into ``CHANGELOG.md``. After this process, the directory ``changelogs/unreleased`` is automatically cleaned up.
