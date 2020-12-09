import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.dockerSupport
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.GradleBuildStep
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.ScriptBuildStep
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2019_2.projectFeatures.dockerRegistry
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs
import jetbrains.buildServer.configs.kotlin.v2019_2.vcs.GitVcsRoot

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2020.2"

project {

    vcsRoot(HttpsGithubComLambotchiSpringBootRest1refsHeadsMaster)

    buildType(Build)

    features {
        dockerRegistry {
            id = "PROJECT_EXT_2"
            name = "Portus"
            url = "https://dockerhub.wfgmb.com"
            userName = "teamcity_devkube"
            password = "credentialsJSON:79dd1475-18c0-43f7-b7c8-9ed9f8015a75"
        }
    }
}

object Build : BuildType({
    name = "Build"

    artifactRules = "build/libs/*.jar"

    vcs {
        root(HttpsGithubComLambotchiSpringBootRest1refsHeadsMaster)
    }

    steps {
        script {
            name = "Debug"
            scriptContent = """
                #!/usr/bin/env bash
                echo "one"
                pwd
                ls
                echo "two"
            """.trimIndent()
            dockerImagePlatform = ScriptBuildStep.ImagePlatform.Linux
            dockerImage = "dockerhub.wfgmb.com/gradle:latest"
        }
        gradle {
            name = "Gradle build"
            enabled = false
            tasks = "build"
            useGradleWrapper = false
            dockerImagePlatform = GradleBuildStep.ImagePlatform.Linux
            dockerImage = "dockerhub.wfgmb.com/gradle:latest"
        }
    }

    triggers {
        vcs {
        }
    }

    features {
        dockerSupport {
            loginToRegistry = on {
                dockerRegistryId = "PROJECT_EXT_2"
            }
        }
    }
})

object HttpsGithubComLambotchiSpringBootRest1refsHeadsMaster : GitVcsRoot({
    name = "https://github.com/lambotchi/spring-boot-rest1#refs/heads/master"
    url = "https://github.com/lambotchi/spring-boot-rest1"
    branch = "refs/heads/master"
})
