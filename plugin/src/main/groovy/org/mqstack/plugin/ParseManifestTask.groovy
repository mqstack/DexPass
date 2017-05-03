package org.mqstack.plugin

import groovy.xml.Namespace
import groovy.xml.QName;
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction;

/**
 * Created by mq on 2017/5/2.
 */

public class ParseManifestTask extends DefaultTask {

    static final String MANIFEST_NAME = "AndroidManifest.xml"
    static final String CHANNEL_NODE_NAME = "Channel"

    String channelName;
    String manifestPath;

    ParseManifestTask() {
        group = 'dexpass'
    }

    @TaskAction
    def parseManifest() {
        def ns = new Namespace("http://schemas.android.com/apk/res/android", "android")

        def xml = new XmlParser().parse(new InputStreamReader(new FileInputStream(manifestPath), "utf-8"))

        def application = xml.application[0]

        if (application) {
            QName nameAttr = new QName('http://schemas.android.com/apk/res/android', 'name', 'android')
            def applicationName = application.attribute(nameAttr)
            if (applicationName != null && !applicationName.isEmpty()) {
                printf "====application name %s====\n", applicationName
            } else {
                printf "====application name empty====\n"
            }

            def metaDataTags = application['meta-data']

            def originApplicationName = metaDataTags.findAll {
                (it.attributes()[ns.name] == CHANNEL_NODE_NAME)
            }.each {
                it.parent().remove(it)
            }

            application.appendNode('meta-data', [(ns.name): CHANNEL_NODE_NAME, (ns.value): channelName])
            project.logger.error("====application add meta-data ${CHANNEL_NODE_NAME}: ${channelName}====")

            def printer = new XmlNodePrinter(new PrintWriter(manifestPath, "utf-8"))
            printer.preserveWhitespace = true
            printer.print(xml)
        }
    }
}
