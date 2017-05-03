package org.mqstack.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Created by mq on 2017/4/10.
 */
class GreetingTask extends DefaultTask {
    String greeting = 'hello from Greeting Task'

    GreetingTask(){
        group = 'greeting'
    }

    @TaskAction
    def greet() {
        println greeting
    }
}
