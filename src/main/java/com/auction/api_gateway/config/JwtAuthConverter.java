package com.auction.api_gateway.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.*;

@Component
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter  jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    @Value("${jwt.auth.converter.principle-attribute}")
    private  String principleAttribute;
    @Value("${jwt.auth.converter.resource-id}")
    private  String resourceId;
    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
      Collection<GrantedAuthority> authorities = Stream.concat(
          jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
          extractResourceRole(jwt).stream()
        )
              .collect(Collectors.toSet());
        return new JwtAuthenticationToken(
                jwt,
                authorities,
                getPricipleClaimName(jwt));
    }

    private String getPricipleClaimName(Jwt jwt) {
        String claimName = JwtClaimNames.SUB;
        if (principleAttribute != null) {
            claimName = principleAttribute;

        }
        return jwt.getClaim(claimName);
    }

    private Collection<? extends GrantedAuthority> extractResourceRole(Jwt jwt) {
        Map<String, Object> resourceAccess;
        Map<String, Object> resource;
        Collection<String> resourceRoles;
        if (jwt.getClaim("resource_access") == null) {
           return Set.of();
        }
        resourceAccess = jwt.getClaim("resource_access");

        if (jwt.getClaim(resourceId) == null) {
            return Set.of();
        }
        resource = (Map<String, Object>) jwt.getClaim(resourceId);

        resourceRoles = (Collection<String>) resource.get("roles");

        return resourceRoles
                .stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
    }
}
