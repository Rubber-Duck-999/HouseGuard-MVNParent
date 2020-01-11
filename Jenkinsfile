pipeline {
    agent any
    
    stages {
        stage('Compile') {
            steps {
                echo 'Building...'
                sh 'mvn -v'
                sh 'mvn compile'
            }
        }
        stage('Test') {
            steps {
                sh 'echo "Test"'
                sh 'mvn test'
            }
        }
    }
}
