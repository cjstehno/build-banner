package com.stehno.gradle.banner

import spock.lang.Specification
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.BuildTask
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome

class BannerTaskSpec extends Specification {

    @Rule TemporaryFolder projectRoot = new TemporaryFolder()
    
    private File buildFile
    
    def setup(){
        buildFile = projectRoot.newFile('build.gradle')
        buildFile.text = '''
            plugins { 
                id 'groovy'
                id 'com.stehno.gradle.build-banner'
            }       
        '''.stripIndent()
        
        projectRoot.newFile('banner.txt').text = 'Awesome Banner!'
        projectRoot.newFile('other-banner.txt').text = 'Awesome-er Banner!'
    }
 
    def 'simple run'(){     
        when:
        BuildResult result = GradleRunner.create()
            .withPluginClasspath()
            .withProjectDir(projectRoot.root)
            .withArguments('clean build'.split(' '))
            .build()

        then:
        buildPassed result
        
        result.output.contains('Awesome Banner!')
    }
    
    def 'extension run'(){     
        setup:
        buildFile.text = '''
            plugins { 
                id 'groovy'
                id 'com.stehno.gradle.build-banner'
            }   
            
            banner {
                location = file('other-banner.txt')
            }
        '''.stripIndent()
        
        when:
        BuildResult result = GradleRunner.create()
            .withPluginClasspath()
            .withProjectDir(projectRoot.root)
            .withArguments('clean build'.split(' '))
            .build()

        then:
        buildPassed result
        
        result.output.contains('Awesome-er Banner!')
    }
    
    def 'simple run with status hidden'(){      
        when:
        BuildResult result = GradleRunner.create()
            .withPluginClasspath()
            .withProjectDir(projectRoot.root)
            .withArguments('clean build -PbannerEnabled=false'.split(' '))
            .build()

        then:
        buildPassed result
        
        !result.output.contains('Awesome Banner!')
    }
    
    private boolean buildPassed(final BuildResult result){
        result.tasks.every { BuildTask task ->
            task.outcome == TaskOutcome.SUCCESS || task.outcome == TaskOutcome.UP_TO_DATE
        }
    }
}
