package com.team2.hrbank.changelog.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@Embeddable
public record IPAddress(
        @Column(name = "ip_address", nullable = false)
        String ipAddress
) {

    private static final Pattern IPV4_PATTERN = Pattern.compile(
            "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$"
    );

    private static final Pattern IPV6_PATTERN = Pattern.compile(
            "^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$|^::1$|^::$"
    );

    public IPAddress {
        if (!IPV4_PATTERN.matcher(ipAddress).matches() || !IPV6_PATTERN.matcher(ipAddress).matches()) {
            throw new IllegalArgumentException("유효하지 않은 IP 주소입니다: " + ipAddress);
        }
    }

}
