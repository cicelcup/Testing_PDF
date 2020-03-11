package com.example.testing_pdf

import android.content.Context
import java.io.File

object Common {
    fun getFilePath(context: Context):String{
        val dir = File(context.filesDir.toString() +
                File.separator +
                context.resources.getString(R.string.app_name) +
                File.separator)
        if(!dir.exists()){
         dir.mkdir()
        }
        return dir.path + File.separator
    }
}