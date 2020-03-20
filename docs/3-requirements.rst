==================================
Supported Java and Gradle Versions
==================================

Java
  Java 8+ is required.

Gradle
  We guarantee backward-compatibility with the latest 3 major Gradle versions, and we provide best effort compatibility with prior versions. 
  
We daily `integrate <https://gitlab.com/zkovari/gradle-changelog-automation-plugin/pipelines>`_ with different Gradle and Java versions.
Currently, the following combinations are tested:

+----------------+---------+----------------------------------------+
|     Gradle     |   Java  |              Compatibility             |
+================+=========+========================================+
| latest version | 8/11/12 |     Guaranteed as soon as possible     |
+----------------+---------+----------------------------------------+
| 6.0            | 8/11    |               Guaranteed               |
+----------------+---------+----------------------------------------+
| 5.0            | 8/11    |               Guaranteed               |
+----------------+---------+----------------------------------------+
| 4.10.3         | 8/11    |               Guaranteed               |
+----------------+---------+----------------------------------------+
| 4.0            | 8       |               Guaranteed               |
+----------------+---------+----------------------------------------+
| 3.5            | 8       | Best effort, might break in the future |
+----------------+---------+----------------------------------------+
| 3.0            | 8       | Best effort, might break in the future |
+----------------+---------+----------------------------------------+
| 2.14.1         | 8       | Best effort, might break in the future |
+----------------+---------+----------------------------------------+