package com.example.ignite.spring.data;

import com.example.ignite.spring.data.config.SpringAppCfg;
import com.example.ignite.spring.data.model.Person;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.PageRequest;

import javax.cache.Cache;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

@SpringBootApplication
public class SpringDataExample {
    private static AnnotationConfigApplicationContext ctx;

    private static PersonRepository repo;

    public static void main(String[] args) {
        igniteSpringDataInit();

        populateRepository();

        findPersons();

        queryRepository();

        System.out.println("\n>>> Cleaning out the repository...");

        repo.deleteAll();

        System.out.println("\n>>> Repository size: " + repo.count());

        ctx.destroy();
    }

    private static void igniteSpringDataInit() {
        ctx = new AnnotationConfigApplicationContext();

        ctx.register(SpringAppCfg.class);

        ctx.refresh();

        repo = ctx.getBean(PersonRepository.class);
    }


    private static void populateRepository() {
        TreeMap<Long, Person> persons = new TreeMap<>();

        persons.put(1L, new Person(1L, 2000L, "John", "Smith", 15000, "Worked for Apple"));
        persons.put(2L, new Person(2L, 2000L, "Brad", "Pitt", 16000, "Worked for Oracle"));
        persons.put(3L, new Person(3L, 1000L, "Mark", "Tomson", 10000, "Worked for Sun"));
        persons.put(4L, new Person(4L, 2000L, "Erick", "Smith", 13000, "Worked for Apple"));
        persons.put(5L, new Person(5L, 1000L, "John", "Rozenberg", 25000, "Worked for RedHat"));
        persons.put(6L, new Person(6L, 2000L, "Denis", "Won", 35000, "Worked for CBS"));
        persons.put(7L, new Person(7L, 1000L, "Abdula", "Adis", 45000, "Worked for NBC"));
        persons.put(8L, new Person(8L, 2000L, "Roman", "Ive", 15000, "Worked for Sun"));

        repo.save(persons);

        System.out.println("\n>>> Added " + repo.count() + " Persons into the repository.");
    }


    private static void findPersons() {
        Person person = repo.findOne(2L);

        System.out.println("\n>>> Found Person [id=" + 2L + ", val=" + person + "]");

        ArrayList<Long> ids = new ArrayList<>();

        for (long i = 0; i < 5; i++)
            ids.add(i);

        Iterator<Person> persons = repo.findAll(ids).iterator();

        System.out.println("\n>>> Persons list for specific ids: ");

        while (persons.hasNext())
            System.out.println("   >>>   " + persons.next());
    }


    private static void queryRepository() {
        System.out.println("\n>>> Persons with name 'John':");

        List<Person> persons = repo.findByFirstName("John");

        for (Person person: persons)
            System.out.println("   >>>   " + person);


        Cache.Entry<Long, Person> topPerson = repo.findTopByLastNameLike("Smith");

        System.out.println("\n>>> Top Person with surname 'Smith': " + topPerson.getValue());


        List<Long> ids = repo.selectId(1000L, new PageRequest(0, 4));

        System.out.println("\n>>> Persons working for organization with ID > 1000: ");

        for (Long id: ids)
            System.out.println("   >>>   [id=" + id + "]");
    }
}