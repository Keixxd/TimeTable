package com.example.timetable.data.repository

import android.content.Context
import java.io.File

class FileManager(private val context: Context?) {

    private fun findFileByName(name: String?): File? = File(context?.dataDir, "databases")
        .listFiles()
        ?.filter { it.name.equals(name) }
        ?.get(0)

    fun onRewriteFileName(oldName: String? ,newName: String?){
        File(context?.dataDir, "databases/${newName}").createNewFile()
        val file = findFileByName(oldName)
        val newFile = File(context?.dataDir, "databases/${newName}")
        file?.copyTo(newFile, true)
        file?.delete()
    }

    fun onDeleteFile(fileName: String?): Boolean?{
        val file = findFileByName(fileName)
        return file?.delete()
    }
}