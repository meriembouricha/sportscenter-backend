pipeline {
    agent any
    
    environment {
        SONAR_TOKEN = credentials('sonar-token')
        GITHUB_TOKEN = credentials('github-pat')
        ACR = credentials('acr-user')
        NEXUS = credentials('nexus-creds')
        DOCKER_IMAGE = 'sportscenter.azurecr.io/sportscenter-backend'
    }
    
    stages {
        stage('Checkout') {
            steps {
                script {
                    // Clean workspace
                    cleanWs()
                    
                    // Checkout from GitHub
                    checkout([
                        $class: 'GitSCM',
                        branches: [[name: '*/prod']],
                        userRemoteConfigs: [[
                            url: 'https://github.com/meriembouricha/sportscenter-backend.git',
                            credentialsId: 'github-pat'
                        ]]
                    ])
                }
            }
        }
        
        stage('Compile') {
            steps {
                sh 'mvn clean compile'
            }
        }
        
        stage('Run Unit Tests & Coverage') {
            steps {
                sh 'mvn clean test jacoco:report -Dspring.profiles.active=test'
            }
            post {
                always {
                    // Publish test results
                    junit 'target/surefire-reports/*.xml'
                    
                    // Publish JaCoCo coverage report
                    publishHTML([
                        allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'target/site/jacoco',
                        reportFiles: 'index.html',
                        reportName: 'JaCoCo Coverage Report'
                    ])
                }
            }
        }
        
        stage('Trivy File System Scan') {
            steps {
                sh 'trivy fs --exit-code 0 --severity HIGH,CRITICAL .'
            }
        }
        
        stage('SonarQube Analysis') {
            steps {
                // Note: 'SonarQubeServer' must match the Jenkins SonarQube server configuration name
                withSonarQubeEnv('SonarQubeServer') {
                    sh 'mvn verify sonar:sonar -Dsonar.token=$SONAR_TOKEN -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml'
                }
            }
        }
        
        stage('Wait for Quality Gate') {
            steps {
                script {
                    timeout(time: 5, unit: 'MINUTES') {
                        def qg = waitForQualityGate()
                        if (qg.status != 'OK') {
                            echo "Quality Gate status: ${qg.status}"
                        }
                    }
                }
            }
        }
        
        stage('Build JAR') {
            steps {
                sh 'mvn package -DskipTests'
            }
        }
        
        stage('Publish JAR to Nexus') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'nexus-creds', usernameVariable: 'NEXUS_USR', passwordVariable: 'NEXUS_PSW')]) {
                    sh "mvn deploy -s jenkins-settings.xml -DskipTests"
                }
            }
        }
        
        stage('Publish Reports to Nexus (raw repo)') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'nexus-creds', usernameVariable: 'NEXUS_USR', passwordVariable: 'NEXUS_PSW')]) {
                    script {
                        // Create archive of JaCoCo reports
                        sh "cd target/site && tar -czf jacoco-reports-${BUILD_NUMBER}.tar.gz jacoco/"
                        
                        // Upload to Nexus raw repository
                        sh """
                        curl -v -u $NEXUS_USR:$NEXUS_PSW \
                          --upload-file target/site/jacoco-reports-${BUILD_NUMBER}.tar.gz \
                          http://20.0.136.5:8081/repository/nexus-reports/jacoco-reports-${BUILD_NUMBER}.tar.gz
                        """
                        
                        // Also upload individual HTML report
                        sh """
                        curl -v -u $NEXUS_USR:$NEXUS_PSW \
                          --upload-file target/site/jacoco/index.html \
                          http://20.0.136.5:8081/repository/nexus-reports/jacoco-report-${BUILD_NUMBER}.html
                        """
                    }
                }
            }
        }
        
        stage('Build & Tag Docker Image') {
            steps {
                script {
                    // Build Docker image
                    sh "docker build -t sportscenter-backend ."
                    
                    // Tag the image
                    sh "docker tag sportscenter-backend ${DOCKER_IMAGE}"
                }
            }
        }
        
        stage('Trivy Docker Image Scan') {
            steps {
                sh "trivy image --exit-code 0 --severity HIGH,CRITICAL ${DOCKER_IMAGE}"
            }
        }
        
        stage('Push Docker Image to ACR') {
            steps {
                script {
                    // Login to Azure Container Registry
                    sh "echo $ACR_PSW | docker login sportscenter.azurecr.io --username $ACR_USR --password-stdin"
                    
                    // Push the image
                    sh "docker push ${DOCKER_IMAGE}"
                }
            }
        }
    }
    
    post {
        failure {
            echo "Pipeline failed."
        }
        success {
            echo "Pipeline completed successfully."
        }
        always {
            // Clean up Docker images to save space
            // Using || true to ensure commands succeed even without Docker permissions
            sh 'docker rmi sportscenter-backend || true'
            sh "docker rmi ${DOCKER_IMAGE} || true"
        }
    }
}