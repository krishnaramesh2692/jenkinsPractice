@Library("Utilities") _ 
    pipeline{
        agent any
        stages{
            stage ("Input"){
                steps{
                    getUser 'Enter USer1', 'Enter User2'
                }
            }
        }
    }
