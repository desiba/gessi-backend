pipeline {
    agent any
    tools {
        maven 'maven'
    }
    stages {
        stage('Checkout Code') {
            steps {
                checkout scm
            }
        }
        stage('Build') {
            steps {
                bat "mvn clean install -Dmaven.test.skip=true"
            }
        }
        stage('Archive Artifact') {
            steps {
                archiveArtifacts artifacts: 'target/*.war'
            }
        }
        stage('deployment') {
            steps {
               deploy adapters: [tomcat9(alternativeDeploymentContext: '', credentialsId: 'deployer', path: '', url: 'http://52.207.116.101:8081/')], contextPath: null, war: '**/*.war'
            }
        }
        stage('Notification'){
            steps {
                emailText(
                    subject: "Job Completed",
                    body: "Jenkins pipeline job for maven build job completed",
                    to: "desiba_1@yahoo.com"
                )
            }
        }
    }
}
