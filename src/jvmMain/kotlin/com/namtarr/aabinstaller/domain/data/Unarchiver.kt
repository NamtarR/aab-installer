package com.namtarr.aabinstaller.domain.data

import com.namtarr.aabinstaller.model.Result
interface Unarchiver {

    suspend fun getFileFromArchive(path: String, fileName: String): Result<String>
}