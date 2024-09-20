package se.sowl.yesterdaynews.service.gpt;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GPTResponseParserService {
    public List<String> parseBatchResult(String batchResult) {
        List<String> parsed = new ArrayList<>();
        String[] items = batchResult.split("---");
        for (String item : items) {
            String trimmed = item.trim();
            if (trimmed.startsWith("요약:") && trimmed.contains("태그:")) {
                parsed.add(trimmed);
            } else {
                parsed.add("요약: 처리 실패\n태그: 오류");
            }
        }
        return parsed;
    }
}
