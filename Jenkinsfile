pipeline {
    agent any
    tools {
        gradle 'Gradle 6.2.1'
        jdk 'OracleJDK_1.8.0'
    }

    stages {
        stage ('Clean') {
            steps {
                sh 'gradle clean'
            }
        }
        stage('Unit Tests') {
            steps {
                script {
                    try {
                        sh 'gradle test'
                    } finally {
                        junit '**/build/test-results/test/*.xml'
                    }
                }
            }
        }
        stage('Coverage') {
            steps {
                jacoco(
                    execPattern: 'build/jacoco/*.exec',
                    classPattern: 'build/classes',
                    sourcePattern: 'src/main/java',
                    exclusionPattern: 'src/test*'
                )
            }
        }
        stage ('Build JAR') {
            steps {
                sh 'gradle jar'
                archiveArtifacts artifacts: 'build/libs/*.jar', fingerprint: true
            }
        }
    }
}