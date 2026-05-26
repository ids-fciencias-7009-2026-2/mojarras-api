package com.mojarras.sys.mojarratores.infrastructure

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service
class EmailService(
    private val mailSender: JavaMailSender
) {

    @Value("\${sender_email}")
    private lateinit var senderEmail: String

    private val logger = LoggerFactory.getLogger(EmailService::class.java)

    fun sendInterestNotification(
        ownerEmail: String,
        ownerName: String,
        interestedName: String,
        interestedEmail: String,
        petName: String
    ) {

        val template = loadTemplate("templates/email/interest.html")

        val html = template.replaceVars(
            mapOf(
                "ownerName" to ownerName,
                "interestedName" to interestedName,
                "interestedEmail" to interestedEmail,
                "petName" to petName
            )
        )

        sendHtmlEmail(
            to = ownerEmail,
            subject = "🐾 Alguien quiere adoptar a $petName",
            html = html
        )
    }

    fun sendVerificationEmail(
        userEmail: String,
        userName: String,
        token: String
    ) {

        val template = loadTemplate("templates/email/verification.html")

        val html = template.replaceVars(
            mapOf(
                "userName" to userName,
                "verificationLink" to "http://localhost:3000/verify?token=$token"
            )
        )

        sendHtmlEmail(
            to = userEmail,
            subject = "Verifica tu cuenta 🐾",
            html = html
        )
    }

    private fun sendHtmlEmail(
        to: String,
        subject: String,
        html: String
    ) {
        try {
            val mimeMessage = mailSender.createMimeMessage()
            val helper = MimeMessageHelper(mimeMessage, true, "UTF-8")

            helper.setFrom(senderEmail)
            helper.setTo(to)
            helper.setSubject(subject)
            helper.setText(html, true)

            mailSender.send(mimeMessage)

        } catch (e: Exception) {
            logger.error("Error sending email", e)
        }
    }

    private fun loadTemplate(path: String): String {
        val resource = ClassPathResource(path)
        return resource.inputStream.bufferedReader().use { it.readText() }
    }

    private fun String.replaceVars(vars: Map<String, String>): String {
        var result = this
        vars.forEach { (key, value) ->
            result = result.replace("{{${key}}}", value)
        }
        return result
    }
}