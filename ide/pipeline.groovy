pipeline {
    agent any
    triggers { pollSCM('H/5 * * * *') }
    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/TEST-HARVEY/jgsu-spring-petclinic.git', branch: 'main'
            }            
        }
        stage('Build') {
            steps {
                sh './mvnw clean compile'
                //sh 'false' // true
            }
        
            post {
                always {
                    junit '**/target/surefire-reports/TEST-*.xml'
                    archiveArtifacts 'target/*.jar'
                }
                changed {
                    emailext subject: "Job \'${JOB_NAME}\' (build ${BUILD_NUMBER}) ${currentBuild.result}",
                        body: "Please go to ${BUILD_URL} and verify the build", 
                        attachLog: true, 
                        compressLog: true, 
                        to: "test@jenkins",
                        recipientProviders: [upstreamDevelopers(), requestor()]
                }
            }
        }
    }
}
