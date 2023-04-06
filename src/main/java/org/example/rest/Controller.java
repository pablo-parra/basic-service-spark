package org.example.rest;

import com.google.gson.Gson;
import org.apache.http.entity.ContentType;
import org.example.core.exception.UnableToGetEmployee;
import org.example.core.interactors.EmployeeService;
import org.example.core.usecase.FindEmployeeById;
import org.example.rest.dto.EmployeeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;
import static org.apache.http.HttpStatus.SC_OK;
import static spark.Spark.before;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.port;

public class Controller {

    private final EmployeeService employeeService;
    private final Gson gson;

    public Controller(Gson gson, EmployeeService employeeService) {
        this.employeeService = employeeService;
        this.gson = gson;

        setUpServer();
    }

    private void setUpServer() {
        port(PORT);
        endpoints();
        exceptions();

        before((req, res) -> {
            if (res.status() != SC_INTERNAL_SERVER_ERROR) {
                res.type(ContentType.APPLICATION_JSON.getMimeType());
            }
        });
    }

    private void endpoints() {
        get("/health", (req, res) -> STATUS_UP);
        get("/employee/:id", this::employeeById, gson::toJson);
    }

    private EmployeeDTO employeeById(Request req, Response res) {
        final var employee = new FindEmployeeById(employeeService).execute(req.params(ID));
        res.status(SC_OK);
        return EmployeeDTO.fromDomain(employee);
    }

    private void exceptions() {
        exception(UnableToGetEmployee.class, this::serverError);
    }

    private void serverError(Exception e, Request request, Response response) {
        LOG.error(e.getClass().getSimpleName(), e);
        response.status(SC_INTERNAL_SERVER_ERROR);
        response.body(gson.toJson(e.getMessage()));
    }

    private static final int PORT = 8080;
    private static final String ID = ":id";
    private static final String STATUS_UP = "{\"status\":\"UP\"}";
    private static final Logger LOG = LoggerFactory.getLogger(Controller.class);
}
