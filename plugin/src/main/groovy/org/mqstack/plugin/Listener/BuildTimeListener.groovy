package org.mqstack.plugin.Listener;

import org.gradle.BuildListener;
import org.gradle.BuildResult;
import org.gradle.api.Task;
import org.gradle.api.execution.TaskExecutionListener;
import org.gradle.api.initialization.Settings;
import org.gradle.api.invocation.Gradle;
import org.gradle.api.tasks.TaskState;
import org.gradle.util.Clock;

/**
 * Created by mq on 2017/4/26.
 */

public class BuildTimeListener implements TaskExecutionListener, BuildListener {

    private Clock clock
    private times = []


    @Override
    public void buildStarted(Gradle gradle) {

    }

    @Override
    public void settingsEvaluated(Settings settings) {

    }

    @Override
    public void projectsLoaded(Gradle gradle) {

    }

    @Override
    public void projectsEvaluated(Gradle gradle) {

    }

    @Override
    public void buildFinished(BuildResult buildResult) {
        println "Task spend time:"
        for (time in times) {
            if (time[0] >= 50) {
                printf "%6sms %s\n", time
            }
        }
    }

    @Override
    public void beforeExecute(Task task) {
        clock = new Clock()
    }

    @Override
    public void afterExecute(Task task, TaskState taskState) {
        def ms = clock.timeInMs
        times.add([ms, task.path])
    }
}
