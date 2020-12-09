package _Self.buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.*

object Build : BuildType({
    name = "Build"

    vcs {
        root(HttpsGithubComLambotchiSpringBootRest1refsHeadsMaster)
    }

    steps {
        gradle {
            name = "Gradle build"
            tasks = "build"
            useGradleWrapper = false
            dockerImagePlatform = GradleBuildStep.ImagePlatform.Linux
            dockerImage = "dockerhub.wfgmb.com/gradle:latest"
        }
        script {
            name = "Test"
            scriptContent = """
                #!/usr/bin/env bash
                ls
            """.trimIndent()
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
