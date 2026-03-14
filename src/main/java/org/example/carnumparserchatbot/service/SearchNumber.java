package org.example.carnumparserchatbot.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class SearchNumber {
    public String numParser(String text) {
        // ищем в тексте валидный номер, можно заменить на Pattern regex
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == 'х' || text.charAt(i) == 'Х' && (i + 7) < text.length()) {

                String str = text.substring(i, i + 8);

                if (str.charAt(1) >= '0' && str.charAt(1) <= '9'
                        && str.charAt(2) >= '0' && str.charAt(2) <= '9'
                        && str.charAt(3) > '0' && str.charAt(3) <= '9'
                        && str.charAt(4) == 'А' || str.charAt(4) == 'а'
                        && str.charAt(5) == 'М' || str.charAt(5) == 'м'
                        && str.charAt(6) >= '0' && str.charAt(6) <= '9'
                        && str.charAt(7) >= '0' && str.charAt(7) <= '9') {

                    StringBuilder stringBuilder = new StringBuilder();

                    if ((i + 8) < text.length() && text.charAt(i + 8) > '0' && text.charAt(i + 8) <= '9') {
                        stringBuilder.append(text, i, i + 9);
                    } else stringBuilder.append(text, i, i + 8);

                    stringBuilder.setCharAt(0, 'Х');
                    stringBuilder.setCharAt(4, 'А');
                    stringBuilder.setCharAt(5, 'М');

                    return stringBuilder.toString();
                }
            }
        }
        return "";
    }
}
