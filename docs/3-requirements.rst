==================================
Supported Java and Gradle Versions
==================================

Java
  JDK 8+ is required. We daily `integrate <https://gitlab.com/zkovari/gradle-changelog-automation-plugin/pipelines>`_ with Java LTS versions, currently Java 8 and Java 11 are supported.

Gradle
  We guarantee backward-compatibility with the latest 3 major Gradle versions, and we provide best effort compatibility with prior versions.
  We daily `integrate <https://gitlab.com/zkovari/gradle-changelog-automation-plugin/pipelines>`_ with the following Gradle distributions:
  
+----------------+------+-------------+----------------------------------------+
|     Gradle     | Java | Integration |              Compatibility             |
+================+======+=============+========================================+
| latest version | 8/11 |      OK     |     Guaranteed as soon as possible     |
+----------------+------+-------------+----------------------------------------+
| 5.0            | 8    |      OK     |               Guaranteed               |
+----------------+------+-------------+----------------------------------------+
| 4.10.3         | 8    |      OK     |               Guaranteed               |
+----------------+------+-------------+----------------------------------------+
| 4.0            | 8    |      OK     |               Guaranteed               |
+----------------+------+-------------+----------------------------------------+
| 3.5            | 8    |      OK     |               Guaranteed               |
+----------------+------+-------------+----------------------------------------+
| 3.0            | 8    |      OK     |               Guaranteed               |
+----------------+------+-------------+----------------------------------------+
| 2.14.1         | 8    |      OK     | Best effort, might break in the future |
+----------------+------+-------------+----------------------------------------+
| 2.0            | 8    |      OK     | Best effort, might break in the future |
+----------------+------+-------------+----------------------------------------+
| 1.12           | 8    |      OK     | Best effort, might break in the future |
+----------------+------+-------------+----------------------------------------+