package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.builders.ApiInfoBuilder;

@SpringBootApplication
@EnableScheduling
@EnableSwagger2
public class WgAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(WgAppApplication.class, args);
	}

	@Bean
	public Docket petApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select() // ApiSelector : Swaggerで書き起こすAPIを選択する。
				.paths(PathSelectors.ant("/pets/**")) // 指定したパスに一致するものだけをSwaggerに起こしてくれる
				.build() // ApiSelectorを作成
				.useDefaultResponseMessages(false) // 定義していないステータスコードを自動で付与してくれる。今回は自動付与をOFFに
				.host("springbootswagger.example.com")
				.apiInfo(apiInfo()); // APIのインフォメーションを設定
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("Pet Service")
				.description("This is a pet service api")
				.version("1.0.0")
				.build();
	}
}
