pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                // Get some code from a GitHub repository
                git url: 'https://github.com/TEST-HARVEY/jgsu-spring-petclinic.git',branch: 'main'
                
            }
        }
        stage('Build') {
            steps{
                sh './mvnw clean package'
            }
        
            post {
                always {
                    junit '**/target/surefire-reports/TEST-*.xml'
                    archiveArtifacts 'target/*.jar'
                }
            }
        }
    }
}
