def call(){
pipeline {
    agent any
    environment {
        NEXUS_USER         = credentials('NEXUS-USER')
        NEXUS_PASSWORD     = credentials('NEXUS-PASS')
    }
    parameters {
        choice choices: ['Maven','Gradle'], description: 'Seleccione una herramienta para preceder a compilar', name: 'compileTool'
        text description: 'intradusca o enviar los stages separados por " ; " ( punto y coma) vacío si necesita dodo los stages.', name: 'stages'

    }
    stages {
        stage("Pipeline"){
            steps {
                script{
                  switch(params.compileTool)
                    {
                        case 'Maven':
                            maven.call(params.stages);
                        break;
                        case 'Gradle':
                            gradle.call(params.stages);
                        break;
                    }
                }
            }

            post{
                success{
                    slackSend color: 'good', message: "[Daniel] [${JOB_NAME}] [${BUILD_TAG}] Ejecucion Exitosa", teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: 'token-slack'
                }
                failure{
                    slackSend color: 'danger', message: "[Daniel] [${env.JOB_NAME}] [${BUILD_TAG}] Ejecucion fallida en stage [${env.DESCRTIPTION_STAGE}]", teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: 'token-slack'
                }
            }
        }
    }
}


}

return this;