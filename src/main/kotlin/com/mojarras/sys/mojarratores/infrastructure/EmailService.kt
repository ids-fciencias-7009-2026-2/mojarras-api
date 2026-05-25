package com.mojarras.sys.mojarratores.infrastructure

import jdk.internal.joptsimple.internal.Messages.message
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailService(
    private val mailSender: JavaMailSender
) {
    @Value("\${sender_email}") private lateinit var senderEmail: String

    private val logger = LoggerFactory.getLogger(EmailService::class.java)

    fun sendInterestNotification(
        ownerEmail: String,
        ownerName: String,
        interestedName: String,
        interestedEmail: String,
        petName: String
    ) {
        val message = SimpleMailMessage()
        message.from = senderEmail
        message.setTo(ownerEmail)
        message.subject = "Alguien está interesado en adoptar a $petName!"

        message.text = """
            Hola $ownerName,
            
            El usuario $interestedName ($interestedEmail) está interesado en adoptar a "$petName".
            
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

    fun sendVerificationEmail(
        userEmail: String,
        userName: String,
        token: String
    ){
        val message = SimpleMailMessage()
        message.from = senderEmail
        message.setTo(userEmail)
        message.subject = "Verifica tu cuenta"

        message.text =  """
            Hola, $userName,
            
            Verifica tu cuenta entrando al siguiente enlace:
            http://localhost:3000/verify?token=$token
            
            Atentamente,
            El equipo de Mojarras.
            
            """.trimIndent()

        try {
            mailSender.send(message)
        } catch (e: Exception) {
            println("Error sending email: ${e.message}")
        }
        logger.info("Verification email sent to $userEmail")
    }
}