package org.mqstack.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.mqstack.plugin.Listener.BuildTimeListener
import org.mqstack.plugin.extension.DexPassExtension

/**
 * Created by mq on 2017/4/10.
 */
class DexPassPlugin implements Plugin<Project> {

    void apply(Project project) {

        project.gradle.addListener(new BuildTimeListener())
        project.extensions.create('dexpass', DexPassExtension)

        project.afterEvaluate {
            project.logger.error("====================dexpass plugin====================")

            def config = project.dexpass
            if (!config.dexPassEnable) {
                project.logger.error("====dexpass disabled====")
                return
            }

            project.logger.error(config.inputMessage)
            project.tasks.create('greetingTask', GreetingTask)

            //hook compile${variantName}JavaWithJavac copy

            def android = project.extensions.android
            android.applicationVariants.all { variant ->

                def variantOutput = variant.outputs.first()
                def variantName = variant.name.capitalize()
                String manifestPath = variant.outputs.first().processManifest.manifestOutputFile
                variant.getVariantData().getScope().getMainDexListFile()


                ParseManifestTask manifestTask = project.tasks.create(
                        "dexPass${variantName}Manifest", ParseManifestTask)
                manifestTask.channelName = config.channelName
                manifestTask.manifestPath = manifestPath

                manifestTask.mustRunAfter variantOutput.processManifest
                variantOutput.processResources.dependsOn manifestTask
            };
        }
    }
}
