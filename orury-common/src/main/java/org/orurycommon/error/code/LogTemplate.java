package org.orurycommon.error.code;

import com.slack.api.model.block.composition.TextObject;

import java.util.List;

import static com.slack.api.model.block.composition.BlockCompositions.markdownText;

public record LogTemplate(
        String uri,
        String requestBody,
        String requestPart,
        String responseBody
) {
    public static LogTemplate of(
            String uri,
            String requestBody,
            String requestPart,
            String responseBody
    ) {
        return new LogTemplate(
                uri,
                requestBody,
                requestPart,
                responseBody
        );
    }

    public List<TextObject> toTextObjects() {
        return List.of(
                markdownText("requestBody : " + requestBody),
                markdownText("requestPart : " + requestPart),
                markdownText("responseBody : " + responseBody)
        );
    }
}
