package org.zkovari.gradle.changelog.api;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.testfixtures.ProjectBuilder;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Test;
import org.zkovari.gradle.changelog.api.ChangelogAutomationPlugin;
import org.zkovari.gradle.changelog.api.tasks.ProcessChangelogEntries;

public class ChangelogAutomationPluginIntegrationTest {

	private Project project;

	@Before
	public void setUp() {
		project = ProjectBuilder.builder().withName("test-project").build();
	}

	@Test
	public void testTasksAreInitialized() {
		project.getPluginManager().apply(ChangelogAutomationPlugin.class);

		Task processChangelogEntriesTask = project.getTasks().findByName("processChangelogEntries");
		assertNotNull("Expected task processChangelogEntries", processChangelogEntriesTask);
		assertThat(processChangelogEntriesTask, IsInstanceOf.instanceOf(ProcessChangelogEntries.class));
	}

}
