/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.gov.es.pmo.identity_parser.acesso_cidadao_parser.model;

import br.gov.es.pmo.identity_parser.pmo_base.model.IIdentityParser;
import br.gov.es.pmo.identity_parser.pmo_base.utils.ApiClient;
import java.util.Optional;
import org.json.JSONObject;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

/**
 *
 * @author gean.carneiro
 */
@Component("idsvr")
public class AcessoCidadaoParser implements IIdentityParser<String>{
    
    private final String SUB_NOVO = "subNovo";
    private final String SUB = "sub";
    private final String APELIDO = "apelido";
    private final String EMAIL = "email";
    private final String EMAIL_COORPORATIVO = "corporativo";
    
    private final ApiClient apiClient = new ApiClient("https://sistemas.es.gov.br/prodest/acessocidadao.webapi");
        
    @Override
    public String getId(OAuth2User user, String accessToken, String clientToken) {
        return (String) Optional.ofNullable(user.getAttribute(SUB_NOVO)).orElse(user.getAttribute(SUB));
    }

    @Override
    public String getNome(OAuth2User user, String accessToken, String clientToken) {
        return (String) user.getAttribute(APELIDO);
    }

    @Override
    public String getEmail(OAuth2User user, String accessToken, String clientToken) {
                
        JSONObject resp = apiClient.doGetRequest("/api/cidadao/" + getId(user, accessToken, clientToken) + "/email", clientToken)
                            .block();
        
        
        return (String) Optional.ofNullable(resp).map((_resp) -> Optional.ofNullable(_resp.get(EMAIL)).orElse(_resp.get(EMAIL_COORPORATIVO))).orElse(null);
    }
    
    
}
