package com.mojarras.sys.mojarratores.infrastructure

import com.cloudinary.Cloudinary
import com.mojarras.sys.mojarratores.exception.BadRequestException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class CloudinaryService(
    @Value("\${cloudinary.cloud-name}") private val cloudName: String,
    @Value("\${cloudinary.api-key}") private val apiKey: String,
    @Value("\${cloudinary.api-secret}") private val apiSecret: String
) {

    private val cloudinary = Cloudinary(
        mapOf(
            "cloud_name" to cloudName,
            "api_key" to apiKey,
            "api_secret" to apiSecret
        )
    )

    fun upload(file: MultipartFile): String {

        if (file.isEmpty) {
            throw BadRequestException("File is empty")
        }

        val result = cloudinary.uploader().upload(file.bytes, emptyMap<String, Any>())

        return result["secure_url"] as String
    }
}