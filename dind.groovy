
pipeline{
    agent{
      kubernetes{
        yamlFile 'k8sPods.yaml'
        yaml '''
        apiVersion: v1
        kind: pod
        metadata:
          labels:
            app_name: app_value
        spec:
          volumes:
            -name: docker-socket
            emptyDir {}
          contianer:
            -name: docker
            image: docker:19.0.3
            readinessProbe:
              exec:
                command: [sh, -c, 'ls -S /var/run/docker.sock']
            commad:
              -sleep
            args:
              - 99d
            volumeMounts:
              -name: docker-sokcet
              mountpath: /var/run
             -name: docker-daemon
             image: docker:19.03.1-dind
             securityContext: 
               privileged: true
             volumeNounts:
              -name: docker-socket
              mountPath: /var/run
          
        '''
        customWorkspace 'some/other/path'
        defaultcontainer 'maven'
      }
    }
    stages{
      stage ('Run Maven'){
          container('docker'){
          steps{
            sh 'mvn -version'
          }
        }
      }
    }
}

