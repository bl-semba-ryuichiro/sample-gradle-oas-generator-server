/* © 2024 Beans Labo Co., Ltd. */
package jp.co.beanslabo.sample.gradle.oas.generator.controller;

import jp.co.beanslabo.sample.gradle.oas.generator.model.PostV1SampleGradleOasGenerator200Response;
import jp.co.beanslabo.sample.gradle.oas.generator.model.PostV1SampleGradleOasGeneratorRequest;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Delegate implementation for SampleGradleOasGeneratorApi.
 */
@Component
public class SampleGradleOasGeneratorApiDelegateImpl implements SampleGradleOasGeneratorApiDelegate {

    /**
     * </inheritDoc>
     */
    @Override
    public ResponseEntity<PostV1SampleGradleOasGenerator200Response> postV1SampleGradleOasGenerator(PostV1SampleGradleOasGeneratorRequest postV1SampleGradleOasGeneratorRequest) throws Exception {

        val response = new PostV1SampleGradleOasGenerator200Response();
        response.setStatus("OK");

        return ResponseEntity.ok(response);
    }
}
