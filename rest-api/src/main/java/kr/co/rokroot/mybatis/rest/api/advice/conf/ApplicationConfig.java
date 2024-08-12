/*
 * Author: rok_root
 * Created time: 2021. 07. 25
 * Copyrights rok_root. All rights reserved.
 */

package kr.co.rokroot.mybatis.rest.api.advice.conf;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.rokroot.mybatis.rest.api.advice.db.MariaDataSource;
import kr.co.rokroot.mybatis.rest.api.advice.db.MSDataSource;
import kr.co.rokroot.mybatis.rest.api.advice.db.OracleDataSource;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@ComponentScan(basePackages = "kr.co.rokroot.mybatis.rest.api",
        includeFilters = @ComponentScan.Filter({ Service.class, Repository.class, Component.class }),
        useDefaultFilters = false)
@Import({ LoggingConfig.class, MariaDataSource.class, OracleDataSource.class, MSDataSource.class })
public class ApplicationConfig {

    @Bean
    public StandardPBEStringEncryptor jasyptEncryptor() {
        EnvironmentStringPBEConfig encryptConfig = new EnvironmentStringPBEConfig();
        encryptConfig.setAlgorithm("PBEWithMD5AndTripleDES");
        encryptConfig.setPassword("????dhogodiehla~!~!sdlfkvlGDFDGF");
        encryptConfig.setPoolSize(1);

        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setConfig(encryptConfig);
        encryptor.initialize();

        return encryptor;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

        return objectMapper;
    }

}
