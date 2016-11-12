package com.stehno.gradle.banner

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class BannerTask extends DefaultTask {

    @TaskAction
    void displayBanner(){
        BannerExtension extension = project.extensions.getByType(BannerExtension)
        
        boolean enabled = project.hasProperty('bannerEnabled') ? project.property('bannerEnabled').equalsIgnoreCase('true') : extension.enabled
        
        File bannerFile = project.hasProperty('bannerFile') ? new File(project.property('bannerFile')) : (extension.location ?: project.file('banner.txt'))
        
        if( enabled ){
            bannerFile.eachLine { line->
                logger.lifecycle line
            }
        }
    }
}


