/* © 2024 Beans Labo Co., Ltd. */
package jp.co.beanslabo.sample.gradle.oas.generator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.beanslabo.sample.gradle.oas.generator.SampleGradleOasGeneratorApplication;
import jp.co.beanslabo.sample.gradle.oas.generator.model.PostV1SampleGradleOasGeneratorRequest;
import jp.co.beanslabo.sample.gradle.oas.generator.model.PostV1SampleGradleOasGeneratorRequestObjectArrayFieldInner;
import jp.co.beanslabo.sample.gradle.oas.generator.model.PostV1SampleGradleOasGeneratorRequestObjectField;
import lombok.val;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.net.URI;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * {@link SampleGradleOasGeneratorApi} の Integration Test.
 */
@SpringBootTest(classes = SampleGradleOasGeneratorApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class SampleGradleOasGeneratorApiTest {

    /**
     * 全てのフィールドに以下を付与.
     *
     * <ul>
     *   <li>nullable: true
     * </ul>
     */
    private static final String API_PATH = "/api/sample-gradle-oas-generator";

    /**
     * 全てのフィールドに以下を付与.
     *
     * <ul>
     *   <li>nullable: true
     *   <li>required
     * </ul>
     */
    private static final String REQUIRED_API_PATH = "/api/sample-gradle-oas-generator-required";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    static Stream<Arguments> argumentsStream() {

        val paths = List.of(API_PATH, REQUIRED_API_PATH);
        val jsons =
            List.of(
                "boolean.json",
                "double.json",
                "float.json",
                "int32.json",
                "int64.json",
                "intArray.json",
                "intArrayItem.json",
                "integer.json",
                "number.json",
                "object.json",
                "objectArray.json",
                "objectArrayItem.json",
                "objectArrayItemField.json",
                "objectField1.json",
                "objectField2.json",
                "objectFieldBoth.json",
                "string.json",
                "stringArray.json",
                "stringArrayItem.json",
                "stringDateTimeFormat.json",
                "stringDateFormat.json",
                "stringToEnum.json",
                "stringBinaryFormat.json",
                "stringByteFormat.json",
                "stringEmailFormat.json",
                "stringHostnameFormat.json",
                "stringIpv4Format.json",
                "stringIpv6Format.json",
                "stringPasswordFormat.json",
                "stringUriFormat.json",
                "stringUuidFormat.json");

        return paths.stream().flatMap(path -> jsons.stream().map(json -> arguments(path, json)));
    }

    @SuppressWarnings("java:S5961")
    @ParameterizedTest
    @ValueSource(strings = {API_PATH, REQUIRED_API_PATH})
    @DisplayName("全パラメータ値あり")
    void okTest(String apiPath) throws Exception {

        val jsonPath = "/json/ok.json";
        String jsonStr;
        try (val is = SampleGradleOasGeneratorApiTest.class.getResourceAsStream(jsonPath)) {
            jsonStr = IOUtils.toString(Objects.requireNonNull(is), Charset.defaultCharset());
        }

        mockMvc
            .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
            .andDo(print())
            .andExpect(status().is2xxSuccessful())
            .andExpect(mvcResult -> {
                val responseBody = mvcResult.getResponse().getContentAsString();
                assertThat(responseBody).isNotBlank();
                assertThat(responseBody).isEqualTo("{\"status\":\"OK\"}");
            });

        val request = objectMapper.readValue(jsonStr, PostV1SampleGradleOasGeneratorRequest.class);

        // リクエストの設定値が想定したものかチェックする
        assertThat(request.getInt32Field()).isExactlyInstanceOf(JsonNullable.class);
        assertThat(request.getInt32Field().get())
            .isExactlyInstanceOf(Integer.class)
            .isEqualTo(2147483647);
        assertThat(request.getInt32Field()).isEqualTo(JsonNullable.of(2147483647));
        assertThat(request.getInt32Field().isPresent()).isTrue();

        assertThat(request.getInt64Field()).isExactlyInstanceOf(JsonNullable.class);
        assertThat(request.getInt64Field().get())
            .isExactlyInstanceOf(Long.class)
            .isEqualTo(9223372036854775807L);
        assertThat(request.getInt64Field()).isEqualTo(JsonNullable.of(9223372036854775807L));
        assertThat(request.getInt64Field().isPresent()).isTrue();

        assertThat(request.getIntegerNoFormat()).isExactlyInstanceOf(JsonNullable.class);
        assertThat(request.getIntegerNoFormat().get()).isExactlyInstanceOf(Integer.class).isEqualTo(1);
        assertThat(request.getIntegerNoFormat().isPresent()).isTrue();

        assertThat(request.getFloatField()).isExactlyInstanceOf(JsonNullable.class);
        assertThat(request.getFloatField().get()).isExactlyInstanceOf(Float.class).isEqualTo(1.2f);
        assertThat(request.getFloatField().isPresent()).isTrue();

        assertThat(request.getDoubleField()).isExactlyInstanceOf(JsonNullable.class);
        assertThat(request.getDoubleField().get()).isExactlyInstanceOf(Double.class).isEqualTo(3.4d);
        assertThat(request.getDoubleField().isPresent()).isTrue();

        assertThat(request.getNumberNoFormat()).isExactlyInstanceOf(JsonNullable.class);
        assertThat(request.getNumberNoFormat().get())
            .isExactlyInstanceOf(BigDecimal.class)
            .isEqualTo("5.6");
        assertThat(request.getNumberNoFormat().isPresent()).isTrue();

        assertThat(request.getBooleanField()).isExactlyInstanceOf(JsonNullable.class);
        assertThat(request.getBooleanField().get()).isExactlyInstanceOf(Boolean.class).isFalse();
        assertThat(request.getBooleanField().isPresent()).isTrue();

        assertThat(request.getIntArrayField()).isExactlyInstanceOf(JsonNullable.class);
        assertThat(request.getIntArrayField().isPresent()).isTrue();
        assertThat(request.getIntArrayField().get())
            .isExactlyInstanceOf(ArrayList.class)
            .hasOnlyElementsOfType(Integer.class)
            .hasSize(2);
        assertThat(request.getIntArrayField().get().get(0))
            .isExactlyInstanceOf(Integer.class)
            .isEqualTo(2147483647);
        assertThat(request.getIntArrayField().get().get(1))
            .isExactlyInstanceOf(Integer.class)
            .isEqualTo(-2147483648);

        assertThat(request.getObjectField()).isExactlyInstanceOf(JsonNullable.class);
        assertThat(request.getObjectField().isPresent()).isTrue();
        assertThat(request.getObjectField().get())
            .isExactlyInstanceOf(PostV1SampleGradleOasGeneratorRequestObjectField.class);
        assertThat(request.getObjectField().get().getId())
            .isExactlyInstanceOf(JsonNullable.class)
            .isEqualTo(JsonNullable.of(100));
        assertThat(request.getObjectField().get().getId().get()).isEqualTo(100);
        assertThat(request.getObjectField().get().getId().isPresent()).isTrue();
        assertThat(request.getObjectField().get().getName())
            .isExactlyInstanceOf(JsonNullable.class)
            .isEqualTo(JsonNullable.of("hoge"));
        assertThat(request.getObjectField().get().getName().get())
            .isExactlyInstanceOf(String.class)
            .isEqualTo("hoge");
        assertThat(request.getObjectField().get().getName().isPresent()).isTrue();

        assertThat(request.getObjectArrayField()).isExactlyInstanceOf(JsonNullable.class);
        assertThat(request.getObjectArrayField().isPresent()).isTrue();
        assertThat(request.getObjectArrayField().get()).isExactlyInstanceOf(ArrayList.class).hasSize(1);
        assertThat(request.getObjectArrayField().get().getFirst())
            .isExactlyInstanceOf(PostV1SampleGradleOasGeneratorRequestObjectArrayFieldInner.class);
        assertThat(request.getObjectArrayField().get().getFirst().getInnerId())
            .isExactlyInstanceOf(JsonNullable.class)
            .isEqualTo(JsonNullable.of(1));
        assertThat(request.getObjectArrayField().get().getFirst().getInnerId().get())
            .isExactlyInstanceOf(Integer.class)
            .isEqualTo(1);
        assertThat(request.getObjectArrayField().get().getFirst().getInnerId().isPresent()).isTrue();
        assertThat(request.getObjectArrayField().get().getFirst().getInnerName().get())
            .isExactlyInstanceOf(String.class)
            .isEqualTo("foo");
        assertThat(request.getObjectArrayField().get().getFirst().getInnerName().isPresent()).isTrue();

        assertThat(request.getStringField())
            .isExactlyInstanceOf(JsonNullable.class)
            .isEqualTo(JsonNullable.of("abc123XYZ"));
        assertThat(request.getStringField().isPresent()).isTrue();
        assertThat(request.getStringField().get())
            .isExactlyInstanceOf(String.class)
            .isEqualTo("abc123XYZ");

        assertThat(request.getStringArrayField()).isExactlyInstanceOf(JsonNullable.class);
        assertThat(request.getStringArrayField().isPresent()).isTrue();
        assertThat(request.getStringArrayField().get())
            .isExactlyInstanceOf(ArrayList.class)
            .hasOnlyElementsOfType(String.class)
            .hasSize(2);
        assertThat(request.getStringArrayField().get().get(0)).isEqualTo("str");
        assertThat(request.getStringArrayField().get().get(1)).isEqualTo("にほんご");

        assertThat(request.getStringDateFormat())
            .isExactlyInstanceOf(JsonNullable.class)
            .isEqualTo(JsonNullable.of(LocalDate.of(2023, 9, 1)));
        assertThat(request.getStringDateFormat().isPresent()).isTrue();
        assertThat(request.getStringDateFormat().get())
            .isExactlyInstanceOf(LocalDate.class)
            .hasToString("2023-09-01");

        assertThat(request.getStringDateTimeFormat())
            .isExactlyInstanceOf(JsonNullable.class)
            .isEqualTo(
                JsonNullable.of(OffsetDateTime.of(2023, 9, 1, 8, 45, 0, 0, ZoneOffset.ofHours(9))));
        assertThat(request.getStringDateTimeFormat().isPresent()).isTrue();
        assertThat(request.getStringDateTimeFormat().get())
            .isExactlyInstanceOf(OffsetDateTime.class)
            .hasToString("2023-09-01T08:45+09:00");

        assertThat(request.getStringPasswordFormat())
            .isExactlyInstanceOf(JsonNullable.class)
            .isEqualTo(JsonNullable.of("password"));
        assertThat(request.getStringPasswordFormat().isPresent()).isTrue();
        assertThat(request.getStringPasswordFormat().get())
            .isExactlyInstanceOf(String.class)
            .isEqualTo("password");

        assertThat(request.getStringByteFormat()).isExactlyInstanceOf(JsonNullable.class);
        assertThat(request.getStringByteFormat().isPresent()).isTrue();
        assertThat(request.getStringByteFormat().get())
            .containsExactly(83, 119, 97, 103, 103, 101, 114, 32, 114, 111, 99, 107, 115);

        assertThat(request.getStringEmailFormat())
            .isExactlyInstanceOf(JsonNullable.class)
            .isEqualTo(JsonNullable.of("test@example.com"));
        assertThat(request.getStringEmailFormat().isPresent()).isTrue();
        assertThat(request.getStringEmailFormat().get())
            .isExactlyInstanceOf(String.class)
            .isEqualTo("test@example.com");

        assertThat(request.getStringUuidFormat())
            .isExactlyInstanceOf(JsonNullable.class)
            .isEqualTo(JsonNullable.of(UUID.fromString("12345678-468b-4ae0-a065-7d7ac70b37a8")));
        assertThat(request.getStringUuidFormat().isPresent()).isTrue();
        assertThat(request.getStringUuidFormat().get())
            .isExactlyInstanceOf(UUID.class)
            .hasToString("12345678-468b-4ae0-a065-7d7ac70b37a8");

        assertThat(request.getStringUriFormat())
            .isExactlyInstanceOf(JsonNullable.class)
            .isEqualTo(JsonNullable.of(URI.create("http://localhost/example")));
        assertThat(request.getStringUriFormat().isPresent()).isTrue();
        assertThat(request.getStringUriFormat().get())
            .isExactlyInstanceOf(URI.class)
            .hasToString("http://localhost/example");

        assertThat(request.getStringHostnameFormat())
            .isExactlyInstanceOf(JsonNullable.class)
            .isEqualTo(JsonNullable.of("hostname"));
        assertThat(request.getStringHostnameFormat().isPresent()).isTrue();
        assertThat(request.getStringHostnameFormat().get())
            .isExactlyInstanceOf(String.class)
            .isEqualTo("hostname");

        assertThat(request.getStringIpv4Format())
            .isExactlyInstanceOf(JsonNullable.class)
            .isEqualTo(JsonNullable.of("256.1.1.1"));
        assertThat(request.getStringIpv4Format().isPresent()).isTrue();
        assertThat(request.getStringIpv4Format().get())
            .isExactlyInstanceOf(String.class)
            .isEqualTo("256.1.1.1");

        assertThat(request.getStringIpv6Format())
            .isExactlyInstanceOf(JsonNullable.class)
            .isEqualTo(JsonNullable.of("192.0.2.1"));
        assertThat(request.getStringIpv6Format().isPresent()).isTrue();
        assertThat(request.getStringIpv6Format().get())
            .isExactlyInstanceOf(String.class)
            .isEqualTo("192.0.2.1");

        assertThat(request.getStringToEnum())
            .isExactlyInstanceOf(JsonNullable.class)
            .isEqualTo(JsonNullable.of(PostV1SampleGradleOasGeneratorRequest.StringToEnumEnum._1));
        assertThat(request.getStringToEnum().isPresent()).isTrue();
        assertThat(request.getStringToEnum().get())
            .isExactlyInstanceOf(PostV1SampleGradleOasGeneratorRequest.StringToEnumEnum.class)
            .isEqualTo(PostV1SampleGradleOasGeneratorRequest.StringToEnumEnum._1);
    }

    @SuppressWarnings("java:S5961")
    @ParameterizedTest
    @MethodSource(value = "argumentsStream")
    @DisplayName("値がNullのフィールドあり")
    void fieldIsNull(String apiPath, String jsonName) throws Exception {
        String jsonStr;
        var jsonPath = String.join("/", "/json/fieldIsNull", jsonName);
        try (var is = SampleGradleOasGeneratorApiTest.class.getResourceAsStream(jsonPath)) {
            jsonStr = IOUtils.toString(Objects.requireNonNull(is), Charset.defaultCharset());
        }

        var request = objectMapper.readValue(jsonStr, PostV1SampleGradleOasGeneratorRequest.class);

        switch (jsonName) {
            /*
             * Jsonのフィールドが定義されているがnullが設定されている場合
             * JsonNullable.value: null
             * JsonNullable.isPresent: true
             * となること
             */
            case "boolean.json" -> {
                switch (apiPath) {
                    case API_PATH -> mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(mvcResult -> {
                            val responseBody = mvcResult.getResponse().getContentAsString();
                            assertThat(responseBody).isNotBlank();
                            assertThat(responseBody).isEqualTo("{\"status\":\"OK\"}");
                        });
                    case REQUIRED_API_PATH -> mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isBadRequest());
                    default -> fail("API Path が異常");
                }

                assertThat(request.getBooleanField()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getBooleanField()).isEqualTo(JsonNullable.of(null));
                assertThat(request.getBooleanField().isPresent()).isTrue();
                assertThat(request.getBooleanField().get()).isNull();
            }
            case "double.json" -> {
                switch (apiPath) {
                    case API_PATH -> mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(mvcResult -> {
                            val responseBody = mvcResult.getResponse().getContentAsString();
                            assertThat(responseBody).isNotBlank();
                            assertThat(responseBody).isEqualTo("{\"status\":\"OK\"}");
                        });
                    case REQUIRED_API_PATH -> mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isBadRequest());
                    default -> fail("API Path が異常");
                }

                assertThat(request.getDoubleField()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getDoubleField()).isEqualTo(JsonNullable.of(null));
                assertThat(request.getDoubleField().isPresent()).isTrue();
                assertThat(request.getDoubleField().get()).isNull();
            }
            case "float.json" -> {
                switch (apiPath) {
                    case API_PATH -> mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(mvcResult -> {
                            val responseBody = mvcResult.getResponse().getContentAsString();
                            assertThat(responseBody).isNotBlank();
                            assertThat(responseBody).isEqualTo("{\"status\":\"OK\"}");
                        });
                    case REQUIRED_API_PATH -> mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isBadRequest());
                    default -> fail("API Path が異常");
                }

                assertThat(request.getFloatField()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getFloatField()).isEqualTo(JsonNullable.of(null));
                assertThat(request.getFloatField().isPresent()).isTrue();
                assertThat(request.getFloatField().get()).isNull();
            }
            case "int32.json" -> {
                switch (apiPath) {
                    case API_PATH -> mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(mvcResult -> {
                            val responseBody = mvcResult.getResponse().getContentAsString();
                            assertThat(responseBody).isNotBlank();
                            assertThat(responseBody).isEqualTo("{\"status\":\"OK\"}");
                        });
                    case REQUIRED_API_PATH -> mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isBadRequest());
                    default -> fail("API Path が異常");
                }

                assertThat(request.getInt32Field()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getInt32Field()).isEqualTo(JsonNullable.of(null));
                assertThat(request.getInt32Field().isPresent()).isTrue();
                assertThat(request.getInt32Field().get()).isNull();
            }
            case "int64.json" -> {
                switch (apiPath) {
                    case API_PATH -> mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(mvcResult -> {
                            val responseBody = mvcResult.getResponse().getContentAsString();
                            assertThat(responseBody).isNotBlank();
                            assertThat(responseBody).isEqualTo("{\"status\":\"OK\"}");
                        });
                    case REQUIRED_API_PATH -> mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isBadRequest());
                    default -> fail("API Path が異常");
                }

                assertThat(request.getInt64Field()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getInt64Field()).isEqualTo(JsonNullable.of(null));
                assertThat(request.getInt64Field().isPresent()).isTrue();
                assertThat(request.getInt64Field().get()).isNull();
            }
            case "intArray.json" -> {
                switch (apiPath) {
                    case API_PATH -> mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(mvcResult -> {
                            val responseBody = mvcResult.getResponse().getContentAsString();
                            assertThat(responseBody).isNotBlank();
                            assertThat(responseBody).isEqualTo("{\"status\":\"OK\"}");
                        });
                    case REQUIRED_API_PATH -> mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isBadRequest());
                    default -> fail("API Path が異常");
                }

                assertThat(request.getIntArrayField()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getIntArrayField()).isEqualTo(JsonNullable.of(null));
                assertThat(request.getIntArrayField().isPresent()).isTrue();
                assertThat(request.getIntArrayField().get()).isNull();
            }
            case "intArrayItem.json" -> {

                // 要素が null の場合はバリデーションエラーとならない
                mockMvc
                    .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(mvcResult -> {
                        val responseBody = mvcResult.getResponse().getContentAsString();
                        assertThat(responseBody).isNotBlank();
                        assertThat(responseBody).isEqualTo("{\"status\":\"OK\"}");
                    });

                assertThat(request.getIntArrayField()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getIntArrayField().isPresent()).isTrue();
                assertThat(request.getIntArrayField().get())
                    .hasSize(1)
                    .isEqualTo(Collections.singletonList(null));
                assertThat(request.getIntArrayField().get().getFirst()).isNull();
            }
            case "integer.json" -> {
                switch (apiPath) {
                    case API_PATH -> mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(mvcResult -> {
                            val responseBody = mvcResult.getResponse().getContentAsString();
                            assertThat(responseBody).isNotBlank();
                            assertThat(responseBody).isEqualTo("{\"status\":\"OK\"}");
                        });
                    case REQUIRED_API_PATH -> mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isBadRequest());
                    default -> fail("API Path が異常");
                }

                assertThat(request.getIntegerNoFormat()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getIntegerNoFormat()).isEqualTo(JsonNullable.of(null));
                assertThat(request.getIntegerNoFormat().isPresent()).isTrue();
                assertThat(request.getIntegerNoFormat().get()).isNull();
            }
            case "number.json" -> {
                switch (apiPath) {
                    case API_PATH -> mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(mvcResult -> {
                            val responseBody = mvcResult.getResponse().getContentAsString();
                            assertThat(responseBody).isNotBlank();
                            assertThat(responseBody).isEqualTo("{\"status\":\"OK\"}");
                        });
                    case REQUIRED_API_PATH -> mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isBadRequest());
                    default -> fail("API Path が異常");
                }

                assertThat(request.getNumberNoFormat()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getNumberNoFormat()).isEqualTo(JsonNullable.of(null));
                assertThat(request.getNumberNoFormat().isPresent()).isTrue();
                assertThat(request.getNumberNoFormat().get()).isNull();
            }
            case "object.json" -> {
                switch (apiPath) {
                    case API_PATH -> mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(mvcResult -> {
                            val responseBody = mvcResult.getResponse().getContentAsString();
                            assertThat(responseBody).isNotBlank();
                            assertThat(responseBody).isEqualTo("{\"status\":\"OK\"}");
                        });
                    case REQUIRED_API_PATH -> mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isBadRequest());
                    default -> fail("API Path が異常");
                }
                assertThat(request.getObjectField()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getObjectField()).isEqualTo(JsonNullable.of(null));
                assertThat(request.getObjectField().isPresent()).isTrue();
                assertThat(request.getObjectField().get()).isNull();
            }
            case "objectArray.json" -> {
                switch (apiPath) {
                    case API_PATH -> mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(mvcResult -> {
                            val responseBody = mvcResult.getResponse().getContentAsString();
                            assertThat(responseBody).isNotBlank();
                            assertThat(responseBody).isEqualTo("{\"status\":\"OK\"}");
                        });
                    case REQUIRED_API_PATH -> mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isBadRequest());
                    default -> fail("API Path が異常");
                }
                assertThat(request.getObjectArrayField()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getObjectArrayField()).isEqualTo(JsonNullable.of(null));
                assertThat(request.getObjectArrayField().get()).isNull();
                assertThat(request.getObjectArrayField().isPresent()).isTrue();
            }
            case "objectArrayItem.json" -> {
                // 要素が null の場合はバリデーションエラーとならない
                mockMvc
                    .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(mvcResult -> {
                        val responseBody = mvcResult.getResponse().getContentAsString();
                        assertThat(responseBody).isNotBlank();
                        assertThat(responseBody).isEqualTo("{\"status\":\"OK\"}");
                    });

                assertThat(request.getObjectArrayField()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getObjectArrayField())
                    .isEqualTo(JsonNullable.of(Collections.singletonList(null)));
                assertThat(request.getObjectArrayField().isPresent()).isTrue();
                assertThat(request.getObjectArrayField().get()).hasSize(1);
                assertThat(request.getObjectArrayField().get().getFirst()).isNull();
            }
            case "objectArrayItemField.json" -> {
                switch (apiPath) {
                    case API_PATH -> mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(mvcResult -> {
                            val responseBody = mvcResult.getResponse().getContentAsString();
                            assertThat(responseBody).isNotBlank();
                            assertThat(responseBody).isEqualTo("{\"status\":\"OK\"}");
                        });
                    case REQUIRED_API_PATH -> mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isBadRequest());
                    default -> fail("API Path が異常");
                }

                assertThat(request.getObjectArrayField()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getObjectArrayField().isPresent()).isTrue();
                assertThat(request.getObjectArrayField().get()).hasSize(1);
                assertThat(request.getObjectArrayField().get().getFirst().getInnerId())
                    .isEqualTo(JsonNullable.of(null));
                assertThat(request.getObjectArrayField().get().getFirst().getInnerId().isPresent()).isTrue();
                assertThat(request.getObjectArrayField().get().getFirst().getInnerId().get()).isNull();
                assertThat(request.getObjectArrayField().get().getFirst().getInnerName())
                    .isEqualTo(JsonNullable.of(null));
                assertThat(request.getObjectArrayField().get().getFirst().getInnerName().isPresent()).isTrue();
                assertThat(request.getObjectArrayField().get().getFirst().getInnerName().get()).isNull();
            }
            case "objectField1.json" -> {
                if (apiPath.equals(API_PATH)) {
                    mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(mvcResult -> {
                            val responseBody = mvcResult.getResponse().getContentAsString();
                            assertThat(responseBody).isNotBlank();
                            assertThat(responseBody).isEqualTo("{\"status\":\"OK\"}");
                        });
                }

                assertThat(request.getObjectField()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getObjectField().isPresent()).isTrue();
                assertThat(request.getObjectField().get().getId()).isEqualTo(JsonNullable.of(null));
                assertThat(request.getObjectField().get().getId().get()).isNull();
                assertThat(request.getObjectField().get().getId().isPresent()).isTrue();
            }
            case "objectField2.json" -> {
                if (apiPath.equals(API_PATH)) {
                    mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(mvcResult -> {
                            val responseBody = mvcResult.getResponse().getContentAsString();
                            assertThat(responseBody).isNotBlank();
                            assertThat(responseBody).isEqualTo("{\"status\":\"OK\"}");
                        });
                }

                assertThat(request.getObjectField()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getObjectField().isPresent()).isTrue();
                assertThat(request.getObjectField().get().getName()).isEqualTo(JsonNullable.of(null));
                assertThat(request.getObjectField().get().getName().get()).isNull();
                assertThat(request.getObjectField().get().getName().isPresent()).isTrue();
            }
            case "objectFieldBoth.json" -> {
                if (apiPath.equals(API_PATH)) {
                    mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(mvcResult -> {
                            val responseBody = mvcResult.getResponse().getContentAsString();
                            assertThat(responseBody).isNotBlank();
                            assertThat(responseBody).isEqualTo("{\"status\":\"OK\"}");
                        });
                }

                assertThat(request.getObjectField()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getObjectField().isPresent()).isTrue();
                assertThat(request.getObjectField().get().getId()).isEqualTo(JsonNullable.of(null));
                assertThat(request.getObjectField().get().getId().get()).isNull();
                assertThat(request.getObjectField().get().getId().isPresent()).isTrue();
                assertThat(request.getObjectField().get().getName()).isEqualTo(JsonNullable.of(null));
                assertThat(request.getObjectField().get().getName().get()).isNull();
                assertThat(request.getObjectField().get().getName().isPresent()).isTrue();
            }
            case "string.json" -> {
                switch (apiPath) {
                    case API_PATH -> mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(mvcResult -> {
                            val responseBody = mvcResult.getResponse().getContentAsString();
                            assertThat(responseBody).isNotBlank();
                            assertThat(responseBody).isEqualTo("{\"status\":\"OK\"}");
                        });
                    case REQUIRED_API_PATH -> mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isBadRequest());
                    default -> fail("API Path が異常");
                }

                assertThat(request.getStringField()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getStringField()).isEqualTo(JsonNullable.of(null));
                assertThat(request.getStringField().isPresent()).isTrue();
                assertThat(request.getStringField().get()).isNull();
            }
            case "stringArray.json" -> {
                switch (apiPath) {
                    case API_PATH -> mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(mvcResult -> {
                            val responseBody = mvcResult.getResponse().getContentAsString();
                            assertThat(responseBody).isNotBlank();
                            assertThat(responseBody).isEqualTo("{\"status\":\"OK\"}");
                        });
                    case REQUIRED_API_PATH -> mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isBadRequest());
                    default -> fail("API Path が異常");
                }

                assertThat(request.getStringArrayField()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getStringArrayField()).isEqualTo(JsonNullable.of(null));
                assertThat(request.getStringArrayField().isPresent()).isTrue();
                assertThat(request.getStringArrayField().get()).isNull();
            }
            case "stringArrayItem.json" -> {
                // 要素が null の場合はバリデーションエラーとならない
                mockMvc
                    .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(mvcResult -> {
                        val responseBody = mvcResult.getResponse().getContentAsString();
                        assertThat(responseBody).isNotBlank();
                        assertThat(responseBody).isEqualTo("{\"status\":\"OK\"}");
                    });

                assertThat(request.getStringArrayField()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getStringArrayField().isPresent()).isTrue();
                assertThat(request.getStringArrayField().get())
                    .hasSize(1)
                    .isEqualTo(Collections.singletonList(null));
                assertThat(request.getStringArrayField().get().getFirst()).isNull();
            }
            case "stringDateFormat.json" -> {
                switch (apiPath) {
                    case API_PATH -> mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(mvcResult -> {
                            val responseBody = mvcResult.getResponse().getContentAsString();
                            assertThat(responseBody).isNotBlank();
                            assertThat(responseBody).isEqualTo("{\"status\":\"OK\"}");
                        });
                    case REQUIRED_API_PATH -> mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isBadRequest());
                    default -> fail("API Path が異常");
                }

                assertThat(request.getStringDateFormat()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getStringDateFormat()).isEqualTo(JsonNullable.of(null));
                assertThat(request.getStringDateFormat().isPresent()).isTrue();
                assertThat(request.getStringDateFormat().get()).isNull();
            }
            case "stringDateTimeFormat.json" -> {
                switch (apiPath) {
                    case API_PATH -> mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(mvcResult -> {
                            val responseBody = mvcResult.getResponse().getContentAsString();
                            assertThat(responseBody).isNotBlank();
                            assertThat(responseBody).isEqualTo("{\"status\":\"OK\"}");
                        });
                    case REQUIRED_API_PATH -> mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isBadRequest());
                    default -> fail("API Path が異常");
                }

                assertThat(request.getStringDateTimeFormat()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getStringDateTimeFormat()).isEqualTo(JsonNullable.of(null));
                assertThat(request.getStringDateTimeFormat().isPresent()).isTrue();
                assertThat(request.getStringDateTimeFormat().get()).isNull();
            }
            case "stringToEnum.json" -> {
                switch (apiPath) {
                    case API_PATH -> mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(mvcResult -> {
                            val responseBody = mvcResult.getResponse().getContentAsString();
                            assertThat(responseBody).isNotBlank();
                            assertThat(responseBody).isEqualTo("{\"status\":\"OK\"}");
                        });
                    case REQUIRED_API_PATH -> mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isBadRequest());
                    default -> fail("API Path が異常");
                }

                assertThat(request.getStringToEnum()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getStringToEnum()).isEqualTo(JsonNullable.of(null));
                assertThat(request.getStringToEnum().isPresent()).isTrue();
                assertThat(request.getStringToEnum().get()).isNull();
            }
            case "stringBinaryFormat.json" -> {
                switch (apiPath) {
                    case API_PATH -> mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(mvcResult -> {
                            val responseBody = mvcResult.getResponse().getContentAsString();
                            assertThat(responseBody).isNotBlank();
                            assertThat(responseBody).isEqualTo("{\"status\":\"OK\"}");
                        });
                    case REQUIRED_API_PATH -> mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isBadRequest());
                    default -> fail("API Path が異常");
                }

                assertThat(request.getStringBinaryFormat()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getStringBinaryFormat()).isEqualTo(JsonNullable.of(null));
                assertThat(request.getStringBinaryFormat().isPresent()).isTrue();
                assertThat(request.getStringBinaryFormat().get()).isNull();
            }
            case "stringByteFormat.json" -> {
                // byte[] を required かつ nullable: true にするとコンパイルエラーのため不可
                mockMvc
                    .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(mvcResult -> {
                        val responseBody = mvcResult.getResponse().getContentAsString();
                        assertThat(responseBody).isNotBlank();
                        assertThat(responseBody).isEqualTo("{\"status\":\"OK\"}");
                    });

                assertThat(request.getStringByteFormat()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getStringByteFormat()).isEqualTo(JsonNullable.of(null));
                assertThat(request.getStringByteFormat().isPresent()).isTrue();
                assertThat(request.getStringByteFormat().get()).isNull();
            }
            case "stringEmailFormat.json" -> {
                switch (apiPath) {
                    case API_PATH -> mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(mvcResult -> {
                            val responseBody = mvcResult.getResponse().getContentAsString();
                            assertThat(responseBody).isNotBlank();
                            assertThat(responseBody).isEqualTo("{\"status\":\"OK\"}");
                        });
                    case REQUIRED_API_PATH -> mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isBadRequest());
                    default -> fail("API Path が異常");
                }

                assertThat(request.getStringEmailFormat()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getStringEmailFormat()).isEqualTo(JsonNullable.of(null));
                assertThat(request.getStringEmailFormat().isPresent()).isTrue();
                assertThat(request.getStringEmailFormat().get()).isNull();
            }
            case "stringHostnameFormat.json" -> {
                switch (apiPath) {
                    case API_PATH -> mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(mvcResult -> {
                            val responseBody = mvcResult.getResponse().getContentAsString();
                            assertThat(responseBody).isNotBlank();
                            assertThat(responseBody).isEqualTo("{\"status\":\"OK\"}");
                        });
                    case REQUIRED_API_PATH -> mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isBadRequest());
                    default -> fail("API Path が異常");
                }

                assertThat(request.getStringHostnameFormat()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getStringHostnameFormat()).isEqualTo(JsonNullable.of(null));
                assertThat(request.getStringHostnameFormat().isPresent()).isTrue();
                assertThat(request.getStringHostnameFormat().get()).isNull();
            }
            case "stringIpv4Format.json" -> {
                switch (apiPath) {
                    case API_PATH -> mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(mvcResult -> {
                            val responseBody = mvcResult.getResponse().getContentAsString();
                            assertThat(responseBody).isNotBlank();
                            assertThat(responseBody).isEqualTo("{\"status\":\"OK\"}");
                        });
                    case REQUIRED_API_PATH -> mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isBadRequest());
                    default -> fail("API Path が異常");
                }

                assertThat(request.getStringIpv4Format()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getStringIpv4Format()).isEqualTo(JsonNullable.of(null));
                assertThat(request.getStringIpv4Format().isPresent()).isTrue();
                assertThat(request.getStringIpv4Format().get()).isNull();
            }
            case "stringIpv6Format.json" -> {
                switch (apiPath) {
                    case API_PATH -> mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(mvcResult -> {
                            val responseBody = mvcResult.getResponse().getContentAsString();
                            assertThat(responseBody).isNotBlank();
                            assertThat(responseBody).isEqualTo("{\"status\":\"OK\"}");
                        });
                    case REQUIRED_API_PATH -> mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isBadRequest());
                    default -> fail("API Path が異常");
                }

                assertThat(request.getStringIpv6Format()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getStringIpv6Format()).isEqualTo(JsonNullable.of(null));
                assertThat(request.getStringIpv6Format().isPresent()).isTrue();
                assertThat(request.getStringIpv6Format().get()).isNull();
            }
            case "stringPasswordFormat.json" -> {
                switch (apiPath) {
                    case API_PATH -> mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(mvcResult -> {
                            val responseBody = mvcResult.getResponse().getContentAsString();
                            assertThat(responseBody).isNotBlank();
                            assertThat(responseBody).isEqualTo("{\"status\":\"OK\"}");
                        });
                    case REQUIRED_API_PATH -> mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isBadRequest());
                    default -> fail("API Path が異常");
                }

                assertThat(request.getStringPasswordFormat()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getStringPasswordFormat()).isEqualTo(JsonNullable.of(null));
                assertThat(request.getStringPasswordFormat().isPresent()).isTrue();
                assertThat(request.getStringPasswordFormat().get()).isNull();
            }
            case "stringUriFormat.json" -> {
                switch (apiPath) {
                    case API_PATH -> mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(mvcResult -> {
                            val responseBody = mvcResult.getResponse().getContentAsString();
                            assertThat(responseBody).isNotBlank();
                            assertThat(responseBody).isEqualTo("{\"status\":\"OK\"}");
                        });
                    case REQUIRED_API_PATH -> mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isBadRequest());
                    default -> fail("API Path が異常");
                }

                assertThat(request.getStringUriFormat()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getStringUriFormat()).isEqualTo(JsonNullable.of(null));
                assertThat(request.getStringUriFormat().isPresent()).isTrue();
                assertThat(request.getStringUriFormat().get()).isNull();
            }
            case "stringUuidFormat.json" -> {
                switch (apiPath) {
                    case API_PATH -> mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(mvcResult -> {
                            val responseBody = mvcResult.getResponse().getContentAsString();
                            assertThat(responseBody).isNotBlank();
                            assertThat(responseBody).isEqualTo("{\"status\":\"OK\"}");
                        });
                    case REQUIRED_API_PATH -> mockMvc
                        .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
                        .andDo(print())
                        .andExpect(status().isBadRequest());
                    default -> fail("API Path が異常");
                }

                assertThat(request.getStringUuidFormat()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getStringUuidFormat()).isEqualTo(JsonNullable.of(null));
                assertThat(request.getStringUuidFormat().isPresent()).isTrue();
                assertThat(request.getStringUuidFormat().get()).isNull();
            }
            default -> fail("case 不足");
        }
    }

    @SuppressWarnings("java:S5961")
    @ParameterizedTest
    @MethodSource(value = "argumentsStream")
    @DisplayName("フィールドの欠損あり")
    void fieldIsMissingTest(String apiPath, String jsonName) throws Exception {
        String jsonStr;
        val jsonPath = String.join("/", "/json/fieldIsMissing", jsonName);
        try (val is = SampleGradleOasGeneratorApiTest.class.getResourceAsStream(jsonPath)) {
            jsonStr = IOUtils.toString(Objects.requireNonNull(is), Charset.defaultCharset());
        }

        mockMvc
            .perform(post(apiPath).contentType(MediaType.APPLICATION_JSON).content(jsonStr))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(mvcResult -> {
                val responseBody = mvcResult.getResponse().getContentAsString();
                assertThat(responseBody).isNotBlank();
                assertThat(responseBody).isEqualTo("{\"status\":\"OK\"}");
            });

        val request = objectMapper.readValue(jsonStr, PostV1SampleGradleOasGeneratorRequest.class);

        switch (jsonName) {
            /*
             * Jsonのフィールドが定義されていない場合
             * JsonNullable.isPresent: false
             * (undefined の状態)となること
             */
            case "boolean.json" -> {
                assertThat(request.getBooleanField()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getBooleanField()).isEqualTo(JsonNullable.undefined());
                assertThat(request.getBooleanField().isPresent()).isFalse();
                assertThatThrownBy(() -> request.getBooleanField().get())
                    .isInstanceOfSatisfying(
                        NoSuchElementException.class,
                        e -> assertThat(e.getMessage()).isEqualTo("Value is undefined"));
            }
            case "double.json" -> {
                assertThat(request.getDoubleField()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getDoubleField()).isEqualTo(JsonNullable.undefined());
                assertThat(request.getDoubleField().isPresent()).isFalse();
                assertThatThrownBy(() -> request.getDoubleField().get())
                    .isInstanceOfSatisfying(
                        NoSuchElementException.class,
                        e -> assertThat(e.getMessage()).isEqualTo("Value is undefined"));
            }
            case "float.json" -> {
                assertThat(request.getFloatField()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getFloatField()).isEqualTo(JsonNullable.undefined());
                assertThat(request.getFloatField().isPresent()).isFalse();
                assertThatThrownBy(() -> request.getFloatField().get())
                    .isInstanceOfSatisfying(
                        NoSuchElementException.class,
                        e -> assertThat(e.getMessage()).isEqualTo("Value is undefined"));
            }
            case "int32.json" -> {
                assertThat(request.getInt32Field()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getInt32Field()).isEqualTo(JsonNullable.undefined());
                assertThat(request.getInt32Field().isPresent()).isFalse();
                assertThatThrownBy(() -> request.getInt32Field().get())
                    .isInstanceOfSatisfying(
                        NoSuchElementException.class,
                        e -> assertThat(e.getMessage()).isEqualTo("Value is undefined"));
            }
            case "int64.json" -> {
                assertThat(request.getInt64Field()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getInt64Field()).isEqualTo(JsonNullable.undefined());
                assertThat(request.getInt64Field().isPresent()).isFalse();
                assertThatThrownBy(() -> request.getInt64Field().get())
                    .isInstanceOfSatisfying(
                        NoSuchElementException.class,
                        e -> assertThat(e.getMessage()).isEqualTo("Value is undefined"));
            }
            case "intArray.json" -> {
                assertThat(request.getIntArrayField()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getIntArrayField()).isEqualTo(JsonNullable.undefined());
                assertThat(request.getIntArrayField().isPresent()).isFalse();
                assertThatThrownBy(() -> request.getIntArrayField().get())
                    .isInstanceOfSatisfying(
                        NoSuchElementException.class,
                        e -> assertThat(e.getMessage()).isEqualTo("Value is undefined"));
            }
            case "intArrayItem.json" -> {
                assertThat(request.getIntArrayField()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getIntArrayField().isPresent()).isTrue();
                assertThat(request.getIntArrayField().get()).isEmpty();
            }
            case "integer.json" -> {
                assertThat(request.getIntegerNoFormat()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getIntegerNoFormat()).isEqualTo(JsonNullable.undefined());
                assertThat(request.getIntegerNoFormat().isPresent()).isFalse();
                assertThatThrownBy(() -> request.getIntegerNoFormat().get())
                    .isInstanceOfSatisfying(
                        NoSuchElementException.class,
                        e -> assertThat(e.getMessage()).isEqualTo("Value is undefined"));
            }
            case "number.json" -> {
                assertThat(request.getNumberNoFormat()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getNumberNoFormat()).isEqualTo(JsonNullable.undefined());
                assertThat(request.getNumberNoFormat().isPresent()).isFalse();
                assertThatThrownBy(() -> request.getNumberNoFormat().get())
                    .isInstanceOfSatisfying(
                        NoSuchElementException.class,
                        e -> assertThat(e.getMessage()).isEqualTo("Value is undefined"));
            }
            case "object.json" -> {
                assertThat(request.getObjectField()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getObjectField()).isEqualTo(JsonNullable.undefined());
                assertThat(request.getObjectField().isPresent()).isFalse();
                assertThatThrownBy(() -> request.getObjectField().get())
                    .isInstanceOfSatisfying(
                        NoSuchElementException.class,
                        e -> assertThat(e.getMessage()).isEqualTo("Value is undefined"));
            }
            case "objectArray.json" -> {
                assertThat(request.getObjectArrayField()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getObjectArrayField()).isEqualTo(JsonNullable.undefined());
                assertThat(request.getObjectArrayField().isPresent()).isFalse();
                assertThatThrownBy(() -> request.getObjectArrayField().get())
                    .isInstanceOfSatisfying(
                        NoSuchElementException.class,
                        e -> assertThat(e.getMessage()).isEqualTo("Value is undefined"));
            }
            case "objectArrayItem.json" -> {
                assertThat(request.getObjectArrayField()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getObjectArrayField().isPresent()).isTrue();
                assertThat(request.getObjectArrayField().get()).isEmpty();
            }
            case "objectArrayItemField.json" -> {
                assertThat(request.getObjectArrayField()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getObjectArrayField().isPresent()).isTrue();
                assertThat(request.getObjectArrayField().get()).hasSize(1);
                assertThat(request.getObjectArrayField().get().getFirst().getInnerId())
                    .isEqualTo(JsonNullable.of(1));
                assertThat(request.getObjectArrayField().get().getFirst().getInnerId().isPresent()).isTrue();
                assertThat(request.getObjectArrayField().get().getFirst().getInnerId().get()).isEqualTo(1);
                assertThat(request.getObjectArrayField().get().getFirst().getInnerName())
                    .isEqualTo(JsonNullable.undefined());
                assertThat(request.getObjectArrayField().get().getFirst().getInnerName().isPresent()).isFalse();
                assertThatThrownBy(() -> request.getObjectArrayField().get().getFirst().getInnerName().get())
                    .isInstanceOfSatisfying(
                        NoSuchElementException.class,
                        e -> assertThat(e.getMessage()).isEqualTo("Value is undefined"));
            }
            case "objectField1.json" -> {
                assertThat(request.getObjectField()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getObjectField().isPresent()).isTrue();
                assertThat(request.getObjectField().get().getId()).isEqualTo(JsonNullable.undefined());
                assertThat(request.getObjectField().get().getId().isPresent()).isFalse();
                assertThatThrownBy(() -> request.getObjectField().get().getId().get())
                    .isInstanceOfSatisfying(
                        NoSuchElementException.class,
                        e -> assertThat(e.getMessage()).isEqualTo("Value is undefined"));
                assertThat(request.getObjectField().get().getName().get()).isEqualTo("hoge");
            }
            case "objectField2.json" -> {
                assertThat(request.getObjectField()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getObjectField().isPresent()).isTrue();
                assertThat(request.getObjectField().get().getId().get()).isEqualTo(100);
                assertThat(request.getObjectField().get().getName()).isEqualTo(JsonNullable.undefined());
                assertThat(request.getObjectField().get().getName().isPresent()).isFalse();
                assertThatThrownBy(() -> request.getObjectField().get().getName().get())
                    .isInstanceOfSatisfying(
                        NoSuchElementException.class,
                        e -> assertThat(e.getMessage()).isEqualTo("Value is undefined"));
            }
            case "objectFieldBoth.json" -> {
                assertThat(request.getObjectField()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getObjectField().isPresent()).isTrue();
                assertThat(request.getObjectField().get().getId()).isEqualTo(JsonNullable.undefined());
                assertThat(request.getObjectField().get().getId().isPresent()).isFalse();
                assertThatThrownBy(() -> request.getObjectField().get().getId().get())
                    .isInstanceOfSatisfying(
                        NoSuchElementException.class,
                        e -> assertThat(e.getMessage()).isEqualTo("Value is undefined"));
                assertThat(request.getObjectField().get().getName()).isEqualTo(JsonNullable.undefined());
                assertThat(request.getObjectField().get().getName().isPresent()).isFalse();
                assertThatThrownBy(() -> request.getObjectField().get().getName().get())
                    .isInstanceOfSatisfying(
                        NoSuchElementException.class,
                        e -> assertThat(e.getMessage()).isEqualTo("Value is undefined"));
            }
            case "string.json" -> {
                assertThat(request.getStringField()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getStringField()).isEqualTo(JsonNullable.undefined());
                assertThat(request.getStringField().isPresent()).isFalse();
                assertThatThrownBy(() -> request.getStringField().get())
                    .isInstanceOfSatisfying(
                        NoSuchElementException.class,
                        e -> assertThat(e.getMessage()).isEqualTo("Value is undefined"));
            }
            case "stringArray.json" -> {
                assertThat(request.getStringArrayField()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getStringArrayField()).isEqualTo(JsonNullable.undefined());
                assertThat(request.getStringArrayField().isPresent()).isFalse();
                assertThatThrownBy(() -> request.getStringArrayField().get())
                    .isInstanceOfSatisfying(
                        NoSuchElementException.class,
                        e -> assertThat(e.getMessage()).isEqualTo("Value is undefined"));
            }
            case "stringArrayItem.json" -> {
                assertThat(request.getStringArrayField()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getStringArrayField().isPresent()).isTrue();
                assertThat(request.getStringArrayField().get()).isEmpty();
            }
            case "stringDateTimeFormat.json" -> {
                assertThat(request.getStringDateTimeFormat()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getStringDateTimeFormat()).isEqualTo(JsonNullable.undefined());
                assertThat(request.getStringDateTimeFormat().isPresent()).isFalse();
                assertThatThrownBy(() -> request.getStringDateTimeFormat().get())
                    .isInstanceOfSatisfying(
                        NoSuchElementException.class,
                        e -> assertThat(e.getMessage()).isEqualTo("Value is undefined"));
            }
            case "stringToEnum.json" -> {
                assertThat(request.getStringToEnum()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getStringToEnum()).isEqualTo(JsonNullable.undefined());
                assertThat(request.getStringToEnum().isPresent()).isFalse();
                assertThatThrownBy(() -> request.getStringToEnum().get())
                    .isInstanceOfSatisfying(
                        NoSuchElementException.class,
                        e -> assertThat(e.getMessage()).isEqualTo("Value is undefined"));
            }
            case "stringDateFormat.json" -> {
                assertThat(request.getStringDateFormat()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getStringDateFormat()).isEqualTo(JsonNullable.undefined());
                assertThat(request.getStringDateFormat().isPresent()).isFalse();
                assertThatThrownBy(() -> request.getStringDateFormat().get())
                    .isInstanceOfSatisfying(
                        NoSuchElementException.class,
                        e -> assertThat(e.getMessage()).isEqualTo("Value is undefined"));
            }
            case "stringBinaryFormat.json" -> {
                assertThat(request.getStringBinaryFormat()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getStringBinaryFormat()).isEqualTo(JsonNullable.undefined());
                assertThat(request.getStringBinaryFormat().isPresent()).isFalse();
                assertThatThrownBy(() -> request.getStringBinaryFormat().get())
                    .isInstanceOfSatisfying(
                        NoSuchElementException.class,
                        e -> assertThat(e.getMessage()).isEqualTo("Value is undefined"));
            }
            case "stringByteFormat.json" -> {
                assertThat(request.getStringByteFormat()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getStringByteFormat()).isEqualTo(JsonNullable.undefined());
                assertThat(request.getStringByteFormat().isPresent()).isFalse();
                assertThatThrownBy(() -> request.getStringByteFormat().get())
                    .isInstanceOfSatisfying(
                        NoSuchElementException.class,
                        e -> assertThat(e.getMessage()).isEqualTo("Value is undefined"));
            }
            case "stringEmailFormat.json" -> {
                assertThat(request.getStringEmailFormat()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getStringEmailFormat()).isEqualTo(JsonNullable.undefined());
                assertThat(request.getStringEmailFormat().isPresent()).isFalse();
                assertThatThrownBy(() -> request.getStringEmailFormat().get())
                    .isInstanceOfSatisfying(
                        NoSuchElementException.class,
                        e -> assertThat(e.getMessage()).isEqualTo("Value is undefined"));
            }
            case "stringHostnameFormat.json" -> {
                assertThat(request.getStringHostnameFormat()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getStringHostnameFormat()).isEqualTo(JsonNullable.undefined());
                assertThat(request.getStringHostnameFormat().isPresent()).isFalse();
                assertThatThrownBy(() -> request.getStringHostnameFormat().get())
                    .isInstanceOfSatisfying(
                        NoSuchElementException.class,
                        e -> assertThat(e.getMessage()).isEqualTo("Value is undefined"));
            }
            case "stringIpv4Format.json" -> {
                assertThat(request.getStringIpv4Format()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getStringIpv4Format()).isEqualTo(JsonNullable.undefined());
                assertThat(request.getStringIpv4Format().isPresent()).isFalse();
                assertThatThrownBy(() -> request.getStringIpv4Format().get())
                    .isInstanceOfSatisfying(
                        NoSuchElementException.class,
                        e -> assertThat(e.getMessage()).isEqualTo("Value is undefined"));
            }
            case "stringIpv6Format.json" -> {
                assertThat(request.getStringIpv6Format()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getStringIpv6Format()).isEqualTo(JsonNullable.undefined());
                assertThat(request.getStringIpv6Format().isPresent()).isFalse();
                assertThatThrownBy(() -> request.getStringIpv6Format().get())
                    .isInstanceOfSatisfying(
                        NoSuchElementException.class,
                        e -> assertThat(e.getMessage()).isEqualTo("Value is undefined"));
            }
            case "stringPasswordFormat.json" -> {
                assertThat(request.getStringPasswordFormat()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getStringPasswordFormat()).isEqualTo(JsonNullable.undefined());
                assertThat(request.getStringPasswordFormat().isPresent()).isFalse();
                assertThatThrownBy(() -> request.getStringPasswordFormat().get())
                    .isInstanceOfSatisfying(
                        NoSuchElementException.class,
                        e -> assertThat(e.getMessage()).isEqualTo("Value is undefined"));
            }
            case "stringUriFormat.json" -> {
                assertThat(request.getStringUriFormat()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getStringUriFormat()).isEqualTo(JsonNullable.undefined());
                assertThat(request.getStringUriFormat().isPresent()).isFalse();
                assertThatThrownBy(() -> request.getStringUriFormat().get())
                    .isInstanceOfSatisfying(
                        NoSuchElementException.class,
                        e -> assertThat(e.getMessage()).isEqualTo("Value is undefined"));
            }
            case "stringUuidFormat.json" -> {
                assertThat(request.getStringUuidFormat()).isExactlyInstanceOf(JsonNullable.class);
                assertThat(request.getStringUuidFormat()).isEqualTo(JsonNullable.undefined());
                assertThat(request.getStringUuidFormat().isPresent()).isFalse();
                assertThatThrownBy(() -> request.getStringUuidFormat().get())
                    .isInstanceOfSatisfying(
                        NoSuchElementException.class,
                        e -> assertThat(e.getMessage()).isEqualTo("Value is undefined"));
            }
            default -> fail("case 不足");
        }
    }
}
