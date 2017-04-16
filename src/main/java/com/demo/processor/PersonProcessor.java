package com.demo.processor;

import com.demo.model.Person;
import org.springframework.batch.item.ItemProcessor;

/**
 * Created by maharshi.bhatt on 16/4/17.
 */
public class PersonProcessor implements ItemProcessor<Person,Person> {

    @Override
    public Person process(Person person) throws Exception {
        return person;
    }
}
