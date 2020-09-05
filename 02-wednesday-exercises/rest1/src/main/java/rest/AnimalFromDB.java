package rest;

import com.google.gson.Gson;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import entity.Animal;
import java.util.Random;
import javax.persistence.NoResultException;
import javax.ws.rs.PathParam;

@Path("animals_db")
public class AnimalFromDB {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of AnimalFromDB
     */
    public AnimalFromDB() {
    }

    @Path("/animals")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAnimals() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Animal> query = em.createQuery("SELECT a FROM Animal a", Animal.class);
            List<Animal> animals = query.getResultList();
            return new Gson().toJson(animals);
        } finally {
            em.close();
        }
    }

    @Path("animalbyid/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAnimal(@PathParam("id") int id) {
        //Hvis den kaldes med .../animalbyid/2  vil id nu være lig 2.
        //Den værdi kan I så benytte til at slå op i databasen med em.find(
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Animal> query = em.createQuery("SELECT a FROM Animal a WHERE a.id = :id", Animal.class);
            query.setParameter("id", id);
            Animal animal = query.getSingleResult();
            return new Gson().toJson(animal);
        } catch (NoResultException e) {
            return new Gson().toJson(null);
        } finally {
            em.close();
        }

    }

    @Path("animalbytype/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAnimalByType(@PathParam("id") String type) {
        //Hvis den kaldes med .../animalbyid/2  vil id nu være lig 2.
        //Den værdi kan I så benytte til at slå op i databasen med em.find(
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Animal> query = em.createQuery("SELECT a FROM Animal a WHERE a.type = :type", Animal.class);
            query.setParameter("type", type);
            Animal animal = query.getSingleResult();
            return new Gson().toJson(animal);
        } catch (NoResultException e) {
            return new Gson().toJson(null);
        } finally {
            em.close();
        }
    }
    
    @Path("random_animal")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getRandomAnimal() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Long> countQuery = em.createQuery("SELECT COUNT(animal) FROM Animal animal", Long.class);
            long count = countQuery.getSingleResult();
            
            Random random = new Random();
            int randNumber = random.nextInt((int)count);
            
            TypedQuery<Animal> selectQuery = em.createQuery("SELECT a FROM Animal a", Animal.class);
            selectQuery.setFirstResult(randNumber);
            selectQuery.setMaxResults(1);
            Animal animal = selectQuery.getSingleResult();
            return new Gson().toJson(animal);
        } catch (NoResultException e) {
            return new Gson().toJson(null);
        } finally {
            em.close();
        }
    }
}
