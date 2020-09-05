package facades;

import dto.EmployeeDTO;
import entities.Employee;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * Rename Class to a relevant name Add add relevant facade methods
 */
public class EmployeeFacade {

    private static EmployeeFacade instance;
    private static EntityManagerFactory emf;
    
    //Private Constructor to ensure Singleton
    private EmployeeFacade() {}
    
    
    /**
     * 
     * @param _emf
     * @return an instance of this facade class.
     */
    public static EmployeeFacade getEmployeeFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new EmployeeFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public EmployeeDTO getEmployeeById (int id) {
        EntityManager em = emf.createEntityManager();
        try {
            Employee employee = em.find(Employee.class, id);
            EmployeeDTO employDTO = new EmployeeDTO(employee);
            return employDTO;
        } finally {
            em.close();
        }
    }
    
    public List<EmployeeDTO> getEmployeeByName (String name) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<EmployeeDTO> query =
                em.createQuery("SELECT e.id, e.name, e.address FROM Employee e WHERE e.name = :name", EmployeeDTO.class);
                query.setParameter("name", name);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<EmployeeDTO> getAllEmployees () {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<EmployeeDTO> query =
                em.createQuery("SELECT e.id, e.name, e.address FROM Employee e", EmployeeDTO.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<EmployeeDTO> getEmployeesWithHighestSalary () {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<EmployeeDTO> query =
                    em.createQuery("SELECT e.id, e.name, e.address FROM Employee e WHERE e.salary = (SELECT MAX(e.salary) FROM Employee e)", EmployeeDTO.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public Employee createEmployee(String name, String address, BigDecimal salary) {
        EntityManager em = emf.createEntityManager();
        Employee employee = new Employee(name, address, salary);
        try {
            em.getTransaction().begin();
            em.persist(employee);
            em.getTransaction().commit();
            return employee;
        } finally {
            em.close();
        }
    }

}
