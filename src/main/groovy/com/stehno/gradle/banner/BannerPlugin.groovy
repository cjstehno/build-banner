package com.stehno.gradle.banner

import org.gradle.api.Plugin
import org.gradle.api.Project

class BannerPlugin implements Plugin<Project> {

    @Override void apply(final Project project) {
        project.extensions.create('banner', BannerExtension)
        
        project.task 'banner', type:BannerTask
        project.tasks.build.dependsOn = ['banner']
    }
}
