pipeline {
    agent any

    tools {
        jdk 'JAVA_HOME'
        maven 'M2_HOME'
    }

    environment {
        // Docker registry credentials (configured in Jenkins)
        DOCKER_CREDENTIALS = 'docker-registry'  
        REGISTRY = "docker.io"                     // Docker Hub or your registry
        IMAGE_NAME = "aminebichiou/dockerimage"   // Image name in your registry
    }

    stages {

        stage('GIT Checkout') {
            steps {
                git branch: 'master',
                    url: 'https://github.com/AmineBichiou/DockerImage.git'
            }
        }

        stage('Compile') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Docker Build') {
            steps {
                script {
                    // Tag image with commit SHA short version
                    def shortCommit = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
                    def imageTag = "${env.REGISTRY}/${env.IMAGE_NAME}:${shortCommit}"
                    env.IMAGE_TAG = imageTag

                    echo "Building Docker image: ${imageTag}"

                    // Build Docker image
                    sh """
                        if [ ! -f Dockerfile ]; then
                            echo "FROM openjdk:17-jdk-slim" > Dockerfile
                            echo "COPY target/*.jar app.jar" >> Dockerfile
                            echo 'ENTRYPOINT ["java","-jar","/app.jar"]' >> Dockerfile
                        fi

                        docker build -t ${imageTag} .
                    """
                }
            }
        }

        stage('Docker Push') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: "${DOCKER_CREDENTIALS}", usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        sh """
                            echo "${DOCKER_PASS}" | docker login ${env.REGISTRY} --username "${DOCKER_USER}" --password-stdin
                            docker push ${env.IMAGE_TAG}
                            docker logout ${env.REGISTRY}
                        """
                    }
                }
            }
        }
    }

    post {
        success {
            echo "Build & Docker image push completed: ${env.IMAGE_TAG}"
        }
        failure {
            echo "Build or Docker step failed!"
        }
    }
}
