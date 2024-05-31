package md.ceiti.frontend.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import md.ceiti.frontend.exception.FrontendException;
import md.ceiti.frontend.service.CrudService;
import md.ceiti.frontend.util.ApiUtils;
import md.ceiti.frontend.util.ErrorHandler;
import md.ceiti.frontend.util.JwtUtils;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Setter
public class GenericCrudService<T> implements CrudService<T> {

    private final RestTemplate restTemplate;
    private Class<T> type;
    private String endpoint;
    private Map<String, String> insertQueryParams;
    private Map<String, String> updateQueryParams;
    private Map<String, String> deleteQueryParams;

    @Override
    public List<T> findAll() {
        try {
            T[] institutionResponses = restTemplate.exchange(
                    endpoint,
                    HttpMethod.GET,
                    ApiUtils.setHeader(JwtUtils.getJwtTokenFromCookie()),
                    getArrayClass()
            ).getBody();

            if (institutionResponses != null) {
                return Arrays.stream(institutionResponses).toList();
            } else {
                throw new FrontendException("Api non responsive");
            }
        } catch (HttpClientErrorException e) {
            ErrorHandler.handle(e);
            return null;
        }
    }

    @Override
    public T insert(T request) {
        try {
            return restTemplate.exchange(
                    endpoint + getQueryParams(request, insertQueryParams),
                    HttpMethod.POST,
                    ApiUtils.setHeader(request, JwtUtils.getJwtTokenFromCookie()),
                    type
            ).getBody();
        } catch (HttpClientErrorException e) {
            ErrorHandler.handle(e);
            return null;
        }
    }

    @Override
    public T update(T request) {
        try {
            return restTemplate.exchange(
                    endpoint + getQueryParams(request, updateQueryParams),
                    HttpMethod.PUT,
                    ApiUtils.setHeader(request, JwtUtils.getJwtTokenFromCookie()),
                    type
            ).getBody();
        } catch (HttpClientErrorException e) {
            ErrorHandler.handle(e);
            return null;
        }
    }

    @Override
    public void delete(T request) {
        try {
            restTemplate.exchange(
                    endpoint + getQueryParams(request, deleteQueryParams),
                    HttpMethod.DELETE,
                    ApiUtils.setHeader(request, JwtUtils.getJwtTokenFromCookie()),
                    Void.class
            );
        } catch (HttpClientErrorException e) {
            ErrorHandler.handle(e);
        }
    }

    @SuppressWarnings("unchecked")
    private Class<T[]> getArrayClass() {
        return (Class<T[]>) Array.newInstance(type, 0).getClass();
    }

    private String getQueryParams(T request, Map<String, String> map) {
        StringBuilder result = new StringBuilder();
        Set<String> keys = map.keySet();
        if (!keys.isEmpty()) {
            for (String key: keys) {
                result.append("&").append(getQueryParam(request, map, key));
            }
            result.replace(0, 0, "?");
        }

        return result.toString();
    }

    private String getQueryParam(T request, Map<String, String> map, String key) {
        String path = map.get(key);
        if (path == null) {
            throw new FrontendException(String.format(
                    "Cannot get path for key [%s]",
                    key
            ));
        }
        Optional<String> value = getNestedFieldValue(request, path);
        return value.map(s -> key + "=" + s).orElse("");
    }

    private Optional<String> getNestedFieldValue(T request, String path) {
        String[] fields = path.split("\\.");
        Object object = request;
        for (String field: fields) {
            object = getFieldValue(object, field);
            if (object == null) {
                return Optional.empty();
            }
        }

        if (object == null) {
            throw new FrontendException(String.format(
                    "Cannot get field form field path [%s] from object of class [%s]",
                    path,
                    type.getSimpleName()));
        }

        return Optional.of(object.toString());
    }

    private Object getFieldValue(Object object, String fieldName) {
        Class<?> localType = object.getClass();
        try {
            Field field = localType.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new FrontendException(String.format(
                    "Cannot get field [%s] from object of class [%s]",
                    fieldName,
                    localType.getSimpleName()));
        }
    }
}
