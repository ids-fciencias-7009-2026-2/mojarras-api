package com.mojarras.sys.mojarratores.infrastructure

import org.slf4j.LoggerFactory
import com.cloudinary.Cloudinary
import com.mojarras.sys.mojarratores.exception.BadRequestException
import com.mojarras.sys.mojarratores.photo.domain.CloudinaryUploadResult
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class CloudinaryService(
    @Value("\${cloudinary.cloud-name}") private val cloudName: String,
    @Value("\${cloudinary.api-key}") private val apiKey: String,
    @Value("\${cloudinary.api-secret}") private val apiSecret: String
) {

    private val logger = LoggerFactory.getLogger(CloudinaryService::class.java)

    private val cloudinary = Cloudinary(
        mapOf(
            "cloud_name" to cloudName,
            "api_key" to apiKey,
            "api_secret" to apiSecret
        )
    )

    fun upload(file: MultipartFile): CloudinaryUploadResult {

        if (file.isEmpty) {
            throw BadRequestException("File is empty")
        }

        val result = cloudinary.uploader().upload(file.bytes, emptyMap<String, Any>())

        return CloudinaryUploadResult(
            url = result["secure_url"] as String,
            publicId = result["public_id"] as String
        )
    }

    fun delete(publicId: String): Boolean {
        return try {
            val result = cloudinary.uploader().destroy(publicId, emptyMap<String, Any>())
            result["result"] == "ok"
        } catch (ex: Exception) {
            logger.error("Error deleting image from Cloudinary: $publicId", ex)
            false
        }
    }

}