pipeline {
    agent any

    tools {
        jdk 'JAVA_HOME'
        maven 'M2_HOME'
    }

    environment {
        DOCKER_CREDENTIALS = 'docker-registry'  
        REGISTRY = "docker.io"                     
        IMAGE_NAME = "aminebichiou/devopsimage"   
    }

    stages {

        stage('GIT Checkout') {
            steps {
                deleteDir()
                git branch: 'master',
                    url: 'https://github.com/AmineBichiou/DevopsImage.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Docker Build') {
            steps {
                script {
                    def shortCommit = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
                    def imageTag = "${env.REGISTRY}/${env.IMAGE_NAME}:${shortCommit}"
                    env.IMAGE_TAG = imageTag

                    echo "Building Docker image: ${imageTag}"

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
