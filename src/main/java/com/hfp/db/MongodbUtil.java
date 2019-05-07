package com.hfp.db;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.hfp.test.Department;

@Component
public class MongodbUtil {
	@Autowired
    private  MongoTemplate  mongoTemplate;

	public void insert(Department department) {
        mongoTemplate.insert(department);
    }

    public void deleteById(Integer id) {
        Criteria criteria = Criteria.where("id").is(id);
        Query query = new Query(criteria);
        mongoTemplate.remove(query, Department.class);
    }

    public void update(Department department) {
        Criteria criteria = Criteria.where("id").is(department.getId());
        Query query = new Query(criteria);
        Update update = new Update();
        update.set("name", department.getName());
        mongoTemplate.updateMulti(query, update, Department.class);
    }

    public Department getById(int id) {
        Criteria criteria = Criteria.where("id").is(id);
        Query query = new Query(criteria);
        return mongoTemplate.findOne(query, Department.class);
    }
    
    public List<Department> getAll() {
        return mongoTemplate.findAll(Department.class);
    }
}
