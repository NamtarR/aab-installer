package com.namtarr.aabinstaller.data

import com.namtarr.aabinstaller.domain.data.Unarchiver
import com.namtarr.aabinstaller.model.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FileOutputStream
import java.util.zip.ZipFile

class ApksUnarchiver: Unarchiver {

    override suspend fun getFileFromArchive(path: String, fileName: String): Result<String> = withContext(Dispatchers.IO) {
        val outputPath = fileOutputPath(path, fileName)

        ZipFile(path).use { zip ->
            val entry = zip.entries().asSequence().find { it.name == fileName }
                ?: return@withContext Result.serviceFailure<String>("Zip")

            val outputStream = FileOutputStream(outputPath)

            zip.getInputStream(entry).use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
        }

        Result.Success(outputPath)
    }

    private fun fileOutputPath(inputPath: String, fileName: String): String {
        val fileExtension = fileName.split(".").last()
        val inputExtension = inputPath.split(".").last()

        return inputPath.removeSuffix(inputExtension).plus(fileExtension)
    }
}