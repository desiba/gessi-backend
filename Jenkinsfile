pipeline {
    agent any
	tools {
		maven 'maven-3.9.10'
	}
    stages {
        stage('Checkout Code') {
            steps {
                checkout scm
            }
        }
        stage('Build') {
            steps {
                sh "mvn clean install -Dmaven.test.skip=true"
            }
        }
        stage('Archive Artifact') {
            steps {
                archiveArtifacts artifacts: '**/*.war'
            }
        }
        stage('deployment') {
            steps {
               deploy adapters: [tomcat9(alternativeDeploymentContext: '', credentialsId: 'admin', path: '', url: 'http://54.234.59.160:8081/')], contextPath: null, war: '**/*.war'
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
