import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    java
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)

    // Open API
    alias(libs.plugins.openapi.generator)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(23)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.web)

    // Open API
    implementation(libs.swagger.annotations)
    implementation(libs.jackson.datatype.jsr310)
    implementation(libs.jackson.databind.nullable)

    // Lombok
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)

    // Test
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.commons.io)
    testRuntimeOnly(libs.junit.platform.launcher)
}

group = "jp.co.beanslabo"
version = "1.0.0-SNAPSHOT"

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
    configureEach {
        resolutionStrategy.cacheChangingModulesFor(0, TimeUnit.SECONDS)
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("PASSED", "FAILED", "SKIPPED")
        exceptionFormat = TestExceptionFormat.FULL
        showCauses = true
        showExceptions = true
        showStackTraces = true
    }
}

/* ---------------------------------------- */
/* Open API                                 */
/* ---------------------------------------- */

openApiValidate {
    inputSpec = "$projectDir/open_api/sample-oas3.yaml"
}

openApiGenerate {
    validateSpec = true
    generatorName = "spring"

    inputSpec = "$projectDir/open_api/sample-oas3.yaml"
    outputDir = "${layout.buildDirectory.get()}/generated/sources/openapi"

    packageName = "jp.co.beanslabo"
    apiPackage = "jp.co.beanslabo.sample.gradle.oas.generator.controller"
    invokerPackage = "jp.co.beanslabo.sample.gradle.oas.generator.invoker"
    modelPackage = "jp.co.beanslabo.sample.gradle.oas.generator.model"

    globalProperties.set(
        mapOf(
            "apis" to "",
            "models" to "",
            "modelDocs" to "",
            "supportingFiles" to "RFC3339DateFormat.java,ApiUtil.java",
        )
    )

    configOptions.set(
        mapOf(
            "dateLibrary" to "java8",
        )
    )

    additionalProperties.set(
        mapOf(
            "cleanupOutput" to true,
            "delegatePattern" to true,
            "serializableModel" to true,
            "serializationLibrary" to "jackson",
            "unhandledException" to true,
            "useBeanValidation" to true,
            "useSpringBoot3" to true,
            "useTags" to true,
        )
    )
}

// 自動生成されるファイルをコンパイル対象に追加
sourceSets {
    main {
        java {
            srcDirs(
                "src/main/java",
                "${layout.buildDirectory.get()}/generated/sources/openapi/src/main/java"
            )
        }
        resources {
            srcDirs(
                "src/main/resources",
                "${layout.buildDirectory.get()}/generated/sources/openapi/src/main/resources"
            )
        }
    }
}

// コンパイル時にOpenAPIの生成ファイルも生成
tasks.named("compileJava") {
    dependsOn("openApiGenerate")
}

// clean時にOpenAPIの生成ファイルも削除
tasks.named("openApiGenerate") {
    dependsOn("clean")
}
