pipeline {
  agent any
  options {
    timestamps()
    disableConcurrentBuilds()
    buildDiscarder(logRotator(numToKeepStr: '20'))
  }
  environment {
    DOCKER_BUILDKIT = '1'
  }
  stages {
    stage('Checkout') { steps { checkout scm } }
    stage('Frontend Quality') {
      steps {
        dir('frontend') {
          sh 'npm ci'
          sh 'npm run lint'
          sh 'npm run build'
        }
      }
    }
    stage('Backend Test') {
      steps {
        dir('backend') {
          sh 'mvn --batch-mode clean verify'
        }
      }
      post {
        always { junit allowEmptyResults: true, testResults: 'backend/**/target/surefire-reports/*.xml' }
      }
    }
    stage('Container Build') {
      steps {
        sh 'docker compose build'
      }
    }
    stage('Smoke Test') {
      steps {
        sh 'docker compose up -d postgres kafka account-service'
        sh 'sleep 15'
        sh 'curl --fail http://localhost:8083/actuator/health'
      }
      post { always { sh 'docker compose down -v' } }
    }
  }
  post {
    always { archiveArtifacts allowEmptyArchive: true, artifacts: 'frontend/dist/**,backend/**/target/*.jar' }
  }
}

