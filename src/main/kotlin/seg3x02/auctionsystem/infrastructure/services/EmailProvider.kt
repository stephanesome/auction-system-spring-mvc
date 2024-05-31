package seg3x02.auctionsystem.infrastructure.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailProvider {
    @Autowired
    private lateinit var mailSender: JavaMailSender

    fun sendEmail(from: String,to: String, subject: String, body: String) {
        try {
            val message = SimpleMailMessage()
            message.from = from
            message.setTo(to)
            message.subject = subject
            message.text = body
            mailSender.send(message)
        } catch (e: Exception) {
            println("Email sending failed: " + e.message)
        }
    }
}
