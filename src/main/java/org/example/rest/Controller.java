package org.example.rest;

import com.google.gson.Gson;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.example.core.exception.UnableToGetEmployee;
import org.example.core.interactors.EmployeeService;
import org.example.core.usecase.FindEmployeeById;
import org.example.rest.dto.EmployeeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Spark;

public class Controller {

    private final EmployeeService employeeService;
    private final Gson gson;

    public Controller(Gson gson, EmployeeService employeeService) {
        this.employeeService = employeeService;
        this.gson = gson;

        setUpServer();
    }

    private void setUpServer() {
        Spark.port(PORT);
        endpoints();
        exceptions();

        Spark.before((req, res) -> {
            if (res.status() != HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                res.type(ContentType.APPLICATION_JSON.getMimeType());
            }
        });
    }

    private void endpoints() {
        Spark.get("/health", (req, res) -> STATUS_UP);
        Spark.get("/employee/:id", this::employeeById, gson::toJson);
    }

    private EmployeeDTO employeeById(Request req, Response res) {
        final var employee = new FindEmployeeById(employeeService).execute(req.params(ID));
        res.status(HttpStatus.SC_OK);
        return EmployeeDTO.fromDomain(employee);
    }

    private void exceptions() {
        Spark.exception(UnableToGetEmployee.class, this::serverError);
    }

    private void serverError(Exception e, Request request, Response response) {
        LOG.error(e.getClass().getSimpleName(), e);
        response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        response.body(gson.toJson(e.getMessage()));
    }

    private static final int PORT = 8080;
    private static final String ID = ":id";
    private static final String STATUS_UP = "{\"status\":\"UP\"}";
    private static final Logger LOG = LoggerFactory.getLogger(Controller.class);
}
