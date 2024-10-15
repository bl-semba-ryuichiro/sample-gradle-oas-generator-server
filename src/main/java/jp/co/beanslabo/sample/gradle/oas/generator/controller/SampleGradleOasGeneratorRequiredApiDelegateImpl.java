/* © 2024 Beans Labo Co., Ltd. */
package jp.co.beanslabo.sample.gradle.oas.generator.controller;

import jp.co.beanslabo.sample.gradle.oas.generator.model.PostV1SampleGradleOasGenerator200Response;
import jp.co.beanslabo.sample.gradle.oas.generator.model.PostV1SampleGradleOasGeneratorRequiredRequest;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Delegate implementation for SampleGradleOasGeneratorRequiredApi.
 */
@Component
public class SampleGradleOasGeneratorRequiredApiDelegateImpl implements SampleGradleOasGeneratorRequiredApiDelegate {

    /**
     * </inheritDoc>
     */
    @Override
    public ResponseEntity<PostV1SampleGradleOasGenerator200Response> postV1SampleGradleOasGeneratorRequired(PostV1SampleGradleOasGeneratorRequiredRequest postV1SampleGradleOasGeneratorRequiredRequest) throws Exception {

        val response = new PostV1SampleGradleOasGenerator200Response();
        response.setStatus("OK");

        return ResponseEntity.ok(response);
    }
}
