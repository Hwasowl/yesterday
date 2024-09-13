package se.sowl.yesterdayai.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
@Getter
public class BingSearchResponse {
    Map<String, String> result;
}
