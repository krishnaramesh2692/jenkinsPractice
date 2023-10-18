def call(body){
  def settings = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = settings
  body()
  timestamps{
    res =  (sh script: "${settings.cmd}" , returnStdout:true).trim()
  }
  echo res
   readFile file: '<path to huge datafile>' 
  def content = readFile file:"path to the data file"
  List myKeys = extractLines(content)
  //mykeys.collection {}
  writeFile file: "${settings.logFilePah}", text: "${cmdOutput}" 
}

@NonCPS
List extractLines(final String Content){
  List mykeys = []
  content.eachLine { line -> 
    myKeys << line
  }
  return myKeys
}
