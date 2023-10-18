package org.demo

class buildUtils {
    static def timedGradleBuild(script, tasks) {
        def gradleHome = script.tool 'gradle3.2'
        script.sh "Echo building ${script.env.BUILD_TAG}"
        script.timestamps{
            script.sh "${gradleHome}/bin/gradle ${tasks}"
        }
    }
  
}
