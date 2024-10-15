/* © 2024 Beans Labo Co., Ltd. */
package jp.co.beanslabo.sample.gradle.oas.generator.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.TimeZone;

/**
 * JSON の Serialize / Deserialize に関する設定を行うクラス.
 */
@Configuration
public class JsonConfig {

    /**
     * JSON の Serialize / Deserialize に 利用する {@link ObjectMapper} を生成する. <br>
     * JSON と Object の変換については基本的にこの ObjectMapper をDIして利用すること.
     *
     * <p>ここで生成する ObjectMapper はデフォルトのObjectMapperに加えて、以下のような機能を提供する.
     *
     * <ul>
     *   <li>拡張ISO8601形式の日付文字列の相互変換.
     *   <li>{@link org.openapitools.jackson.nullable.JsonNullable} の相互変換.
     * </ul>
     *
     * @return {@link ObjectMapper}
     */
    @Primary
    @Bean
    public ObjectMapper objectMapper() {

        return JsonMapper.builder()
            // 日付のOFFSETにJVMのTimeZoneを指定
            .defaultTimeZone(TimeZone.getDefault())
            // Date and Time APIを利用するためのModule
            .addModule(new JavaTimeModule())
            // OAS Generatorで生成されるJsonNullable型との変換Module
            .addModule(new JsonNullableModule())
            // Serialize時に Unix Timestamp として出力するのを無効化
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            // Deserialize時にJSONの整数値からENUMへのindexを利用したマッピングを禁止
            .enable(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS)
            .build();
    }
}
