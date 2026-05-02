package com.mojarras.sys.mojarratores.infrastructure

import org.slf4j.LoggerFactory
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailService(
    private val mailSender: JavaMailSender
) {

    private val logger = LoggerFactory.getLogger(EmailService::class.java)

    fun sendInterestEmail(
        ownerEmail: String,
        ownerName: String,
        interestedName: String,
        interestedEmail: String,
        petName: String
    ) {
        val message = SimpleMailMessage()

        message.setTo(ownerEmail)
        message.subject = "Alguien está interesado en adoptar a $petName!"

        message.text = """
            Hola $ownerName,
            
            El usuario $interestedName ($interestedEmail) está interesado en adopar a "$petName".
            
            Por favor, ponte en contacto con esta persona para continuar con el proceso de adopción.
            
            Atentamente,
            El equipo de Mojarras.
        """.trimIndent()

        try {
            mailSender.send(message)
        } catch (e: Exception) {
            println("Error sending email: ${e.message}")
        }

        logger.info("Interest email sent to $ownerEmail for pet $petName")
    }
}