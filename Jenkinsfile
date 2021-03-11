pipeline {
    agent {
        docker { 
            image 'maven:3.3.3' 
        }
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
