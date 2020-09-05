
package dbfacade;

import entity.Customer;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;


public class CustomerFacade {
    
    private static EntityManagerFactory emf;
    private static CustomerFacade instance;
    
    private CustomerFacade() {}
    
    public static CustomerFacade getCustomerFacade (EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new CustomerFacade();
        }
        return instance;
    }
    
    public Customer findById (int id) {
        EntityManager em = emf.createEntityManager();
        try {
            Customer customer = em.find(Customer.class, id);
            return customer;
        } finally {
            em.close();
        }
    }
    
    public List<Customer> findByLastName (String name) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Customer> query =
                em.createQuery("Select customer from Customer customer where customer.lastName = :lastname", Customer.class);
                query.setParameter("lastname", name);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public int getNumberOfCustomers() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Long> query = 
                    em.createQuery("Select count(customer) from Customer customer", Long.class);
            return query.getSingleResult().intValue();
        } finally {
            em.close();
        }
    }
    
    public List<Customer> allCustomers() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery query =
                    em.createQuery("Select customer from Customer customer", Customer.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public Customer addCustomer(String fName, String lName) {
        EntityManager em = emf.createEntityManager();
        Customer customer = new Customer(fName, lName);
        try {
            em.getTransaction().begin();
            em.persist(customer);
            em.getTransaction().commit();
            return customer;
        } finally {
            em.close();
        }
    }
    
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        CustomerFacade facade = CustomerFacade.getCustomerFacade(emf);
        Customer c1 = facade.addCustomer("Kaj", "Madsen");
        Customer c2 = facade.addCustomer("Knud", "Larsen");
        Customer c3 = facade.addCustomer("Rasmus", "Rasmussen");
        // Find by id
        System.out.println("Cus1: " + facade.findById(c1.getId()).getFirstName());
        System.out.println("Cus2: " + facade.findById(c2.getId()).getFirstName());
        System.out.println("Cus3: " + facade.findById(c3.getId()).getFirstName());
        // Find by last name
        
        List<Customer> lastNames = facade.findByLastName(c1.getLastName());
        for (Customer lastName : lastNames) {
            System.out.println(lastName.getId());
        }
        lastNames = facade.findByLastName(c2.getLastName());
        for (Customer lastName : lastNames) {
            System.out.println(lastName.getId());
        }
        lastNames = facade.findByLastName(c3.getLastName());
        for (Customer lastName : lastNames) {
            System.out.println(lastName.getId());
        }
        
        // Number of customers
        System.out.println("Number of customers " + facade.getNumberOfCustomers());
        // All customers
        List<Customer> customers = facade.allCustomers();
        for (Customer customer : customers) {
            System.out.println(customer);
        }
    }
    
}
