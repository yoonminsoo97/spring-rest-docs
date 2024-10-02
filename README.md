# Spring Rest Docs Example

#### [Spring Rest Docs 예제 문서 바로가기](https://raw.githack.com/yoonminsoo97/spring-rest-docs/main/src/main/resources/static/docs/index.html)

### 프로젝트 환경

- java 17.0.12
- spring boot 3.3.4
  - spring web mvc 6.1.13
  - spring rest docs 3.0.1
  - spring data jpa 3.3.4 (hibernate 6.5.3)
- h2 database 2.2.224
- jakarta validation api 3.0.2
- lombok 1.18.34

## 설정

### build.gradle

```groovy
tasks.named('test') {
    outputs.dir snippetsDir
    finalizedBy asciidoctor
}

tasks.named('asciidoctor') {
    dependsOn test
    configurations 'asciidoctorExt'
    inputs.dir snippetsDir
    finalizedBy copyDocument
    doFirst {
        delete file('src/main/resources/static/docs')
    }
}

task copyDocument(type: Copy) {
    dependsOn asciidoctor
    from file('build/docs/asciidoc')
    into file('src/main/resources/static/docs')
}

bootJar {
    dependsOn asciidoctor
    doFirst {
        delete file('static/docs')
    }
    from('${asciidoctor.outputDir}') {
        into 'static/docs'
    }
}
```