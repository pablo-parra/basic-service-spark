package org.example.client;

import com.google.gson.Gson;
import org.apache.http.HttpHeaders;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.example.client.dto.EmployeeDTO;
import org.example.core.domain.Employee;
import org.example.core.exception.UnableToGetEmployee;
import org.example.core.interactors.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExternalService implements EmployeeService {

    public final Gson gson;

    public ExternalService(Gson gson) {
        this.gson = gson;
    }

    @Override
    public Employee getEmployee(String id) {
        try{
            final var response = Request.Get(urlEmployeeBy(id))
                    .addHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType())
                    .execute().returnContent().asString();
            return gson.fromJson(response, EmployeeDTO.class).toDomain();
        }catch(Exception ex){
            LOG.error("There was a problem calling EXTERNAL SERVICE: {}", ex.getMessage());
            throw new UnableToGetEmployee(ex.getCause());
        }
    }

    private String urlEmployeeBy(String id) {
        return HOST.concat(EMPLOYEE).concat("/").concat(id);
    }

    private static final String HOST = "http://localhost:8089/wiremock";
    private static final String EMPLOYEE = "/employee";
    private static final Logger LOG = LoggerFactory.getLogger(ExternalService.class);
}
