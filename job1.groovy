@Library("Utilities") _ 
    pipeline{
        agent any
        options{
            //multibranch pipeline
            disableConcurrentBuilds()
        }
        libraries{
            lib("mylib@masther")
            lib("alib")
        }
        
        stages{
            stage ("Input"){
                retry(5){
                    timeout(time:50, unit: "SECONDS"){
                        steps{
                            getUser 'Enter USer1', 'Enter User2'
                        }
                    }
                }
            }
            stage ("MarkerFile"){
                script{
                    timeout(time:5, unit:"SECONDS"){
                        waitUntil{
                            def ret = sh returnStatus:true, script: "test -e /home/file.txt"
                            return ( ret == 0 )
                        }
                    }
                }
            }
            stage("Source_Stash"){
                git branch: master, url:'git@'
                stash name:'test-source', includes:'build/gradle, src, test/'
            }
            prallel{
                stage("BUILD") {
                    when{
                        allOf{
                            expression {params.BRANCH_NAME == "master"}
                            expression {params.BUILD_TYPE == "Release"}
                        }
                    }
                    steps{
                        lock("worker_node1") {
                            sh 'gradle clean build -x test'
                        }
                    }
                }
                stage("Test"){
                    parallel(
                        master: {node('master'){unstash 'test-sources' sh ''}},
                        workwer: { node('master'){unstash 'test-sources' sh }}
                        failFast: true
                    )
                    
                }
            }
            
        }
        post   {
            always{ mail to: body: subject: }
            changed{}
            success{ mail boldy: 'build failed', subject: 'BUILD failed', to:""}
            failure{}
        }
    }
