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
        DOCKER_BUILDKIT = "1"
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
    stage('SonarQube Analysis') {
    steps {
        withSonarQubeEnv('MySonarServer') {
            sh """
                mvn sonar:sonar \
                -Dsonar.projectKey=DevOpsImage \
                -Dsonar.projectName=DevOpsImage
            """
        }
    }
}

    post {
        success {
            echo "Build & Docker image push with sonarqube completed: ${env.IMAGE_TAG}"
        }
        failure {
            echo "Build or Docker or sonarqube step failed!"
        }
    }
}
