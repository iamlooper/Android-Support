package com.looper.android.support.util

import com.topjohnwu.superuser.Shell

object ShellUtils {

    /**
     * Executes a single shell command.
     * @param command, the shell command to execute.
     */
    fun execute(command: String) {
        Shell.cmd(command).exec()
    }
    
    /**
     * Executes a list of shell commands.
     * @param commands, the list of shell commands to execute.
     */
    fun execute(commands: List<String>) {
        commands.forEach {
            Shell.cmd(it).exec()
        }
    }
    
    /**
     * Executes a single shell command and returns the status and output.
     * @param command, the shell command to execute.
     * @return An array containing the status [Boolean] as the first element and the output [List<String>] as the second element.
     */
    fun executeWithOutput(command: String): Array<Any?> {
        val objectArray = arrayOfNulls<Any>(2)
        val shellJobResult: Shell.Result = Shell.cmd(command).exec()
        val shellJobStatus: Boolean = shellJobResult.isSuccess
        val shellJobOutput: List<String> = shellJobResult.out
        objectArray[0] = shellJobStatus
        objectArray[1] = shellJobOutput
        return objectArray
    }
}