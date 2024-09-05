package uk.gov.hmcts.reform.laubackend.cases.utils;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.AttributeConverter;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Value;

public class StringCryptoConverter  implements AttributeConverter<String, String> {

    // Jasypt StringEncryptor for performing encryption and decryption
    private final StandardPBEStringEncryptor encryptor;

    @Value("${jasypt.encryptor.password}")
    private String encryptionKey;

    public StringCryptoConverter() {
        // Initialize the encryptor with the encryption password from the environment
        this.encryptor = new StandardPBEStringEncryptor();
    }

    @PostConstruct
    public void setup() {
        this.encryptor.setPassword(encryptionKey);
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return encryptor.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return encryptor.decrypt(dbData);
    }
}
