def call (String cmd){
    // Steps that is replaced by the timedCommand
    timestamps{
        cmdout = (sh retrunStdout:true, script: "${cmd}").trim()
    }
    echo cmdout
    writFile
}
