/*
 * Author: rok_root
 * Created time: 2021. 07. 25
 * Copyrights rok_root. All rights reserved.
 */

package kr.co.rokroot.spring.demo.api.swagger.mybatis.advice;

import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class BaseConstants {

    public static final String ROOT = "/";
    public static final String CSRF = ROOT + "csrf";
    public static final String RESET = ROOT + "reset";
    public static final String FAVICON = ROOT + "favicon.ico";
    public static final String SWAGGER_UI_URL = ROOT + "swagger-ui.html";
    public static final String SWAGGER_API_DOCS = ROOT + "v2/api-docs";
    public static final String SWAGGER_SCRIPTS_URL = ROOT + "webjars/**";
    public static final String SWAGGER_RESOURCES_URL = ROOT + "swagger-resources/**";


    private static BaseConstants baseConstants;
    public static synchronized BaseConstants getInstance() {
        if (baseConstants == null) {
            baseConstants = new BaseConstants();
            initialize(baseConstants);
        }
        return baseConstants;
    }


    public Map<String, Map<String, List>> baseInfoMap = new HashMap<>();
    public Map<String, List> authMap = new HashMap<>();

    public static void initialize(BaseConstants constants) {
        constants.baseInfoMap.clear();
        constants.baseInfoMap.put("AUTH", constants.authMap);
    }
}
