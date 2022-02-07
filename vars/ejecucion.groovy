import pipeline.*

def call(varTOOLS,varStages){

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

                // println  ('ACA LLEGAMOS NO MAS ' + varTOOLS)
                // println  ('ACA LLEGAMOS NO MAS ' + varStages)
                // figlet  varTOOLS
                script{
                  switch(varTOOLS)
                    {
                        case 'Maven':
                        figlet  "corriendo Maven"
                            maven.call(varStages);
                        break;
                        case 'Gradle':
                        figlet  "corriendo Gradlet"
                            gradle.call(varStages);
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