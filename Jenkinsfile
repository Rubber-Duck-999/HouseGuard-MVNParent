pipeline {
    agent any
    tools {
        maven 'Maven 3.6.3'
        jdk 'jdk8'
    }
    stages {
        stage('Compile') {
            steps {
                echo 'Building...'
                sh 'mvn -v'
                sh 'mvn compile'
            }
        }
    }
    post {
        failure {
            emailext body: 'Failed to build MavenParent', subject: 'Build Failure', to: '$DEFAULT_RECIPIENTS'
        }
    }
}
