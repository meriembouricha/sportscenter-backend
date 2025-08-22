package com.ecomerce.sportscenter.alert;

import com.ecomerce.sportscenter.entity.AppUser;
import com.ecomerce.sportscenter.repository.ProductRepository;
import com.ecomerce.sportscenter.entity.Product;
import com.ecomerce.sportscenter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LowStockChecker {

    private final ProductRepository productRepository;
    private final UserRepository appUserRepository;
    private final JavaMailSender mailSender;

//    @Scheduled(fixedRate = 3600000) // Every hour
    //every 3 minutes
    @Scheduled(fixedDelay = 180000)
    public void checkLowStock() {
        List<Product> lowStockProducts = productRepository.findByQunatityLessThanEqual(100);
        if (lowStockProducts.isEmpty()) return;

        List<AppUser> admins = appUserRepository.findAllAdmins();
        if (admins.isEmpty()) return;

        StringBuilder body = new StringBuilder("⚠️ Produits en faible quantité:\n\n");
        for (Product p : lowStockProducts) {
            body.append("- ").append(p.getName())
                    .append(" (Stock: ").append(p.getQunatity()).append(")\n");
        }

        for (AppUser admin : admins) {
            sendEmail(admin.getEmail(), "Alerte: Stock Faible", body.toString());
        }
    }

    private void sendEmail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }
}
