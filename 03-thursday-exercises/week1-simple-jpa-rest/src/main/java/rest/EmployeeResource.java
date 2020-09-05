package rest;

import com.google.gson.Gson;
import dto.EmployeeDTO;
import entities.Employee;
import facades.EmployeeFacade;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("employee")
public class EmployeeResource {
    
    //NOTE: Change Persistence unit name according to your setup
    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu"); 
    EmployeeFacade facade =  EmployeeFacade.getEmployeeFacade(emf);

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String demo() {
        return "{\"msg\":\"succes\"}";
    }
    
    @Path("all")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getEmployees() {
        try {
            List<EmployeeDTO> employees = facade.getAllEmployees();
           
            return new Gson().toJson(employees);
        } catch(NoResultException e) {
            return new Gson().toJson(null);
        }
    }
    
    @Path("{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getEmployeeByID (@PathParam("id") int id) {
        try {
            EmployeeDTO employDTO = facade.getEmployeeById(id);
            return new Gson().toJson(employDTO);
        } catch(NoResultException | NullPointerException e) {
            return new Gson().toJson(null);
        }
    }
    
    @Path("highestpaid")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getHighestPaid() {
        try {
            List<EmployeeDTO> employees = facade.getEmployeesWithHighestSalary();
           
            return new Gson().toJson(employees);
        } catch(NoResultException e) {
            return new Gson().toJson(null);
        }
    }
    
    @Path("name/{name}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getEmployeeByName (@PathParam("name") String name) {
        try {
            List<EmployeeDTO> employees = facade.getEmployeeByName(name);
            return new Gson().toJson(employees);
        } catch(NoResultException | NullPointerException e) {
            return new Gson().toJson(null);
        }
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(Employee entity) {
        throw new UnsupportedOperationException();
    }
    
    @PUT
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void update(Employee entity, @PathParam("id") int id) {
        throw new UnsupportedOperationException();
    }
}
