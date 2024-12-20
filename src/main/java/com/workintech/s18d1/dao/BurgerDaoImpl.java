package com.workintech.s18d1.dao;

import com.workintech.s18d1.entity.BreadType;
import com.workintech.s18d1.entity.Burger;
import com.workintech.s18d1.exceptions.BurgerException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@Slf4j

public class BurgerDaoImpl implements BurgerDao {
    private EntityManager entityManager;

    @Autowired
    public BurgerDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    @Override
    public Burger save(Burger burger) {
        entityManager.persist(burger);
        return burger;

    }

    @Override
    public Burger findById(long id) {
     Burger burger =    entityManager.find(Burger.class, id);
        if(burger == null){
            throw new BurgerException("Burger not find"+ id, HttpStatus.NOT_FOUND);
        }
        return burger;



    }

    @Override
    public List<Burger> findAll() {
        TypedQuery<Burger> query = entityManager.createQuery("Select b From Burger b", Burger.class);
        return query.getResultList();

    }

    @Override
    public List<Burger> findByPrice(Integer price) {
        TypedQuery<Burger> query = entityManager.createQuery("Select b From Burger b Where b.price > :price ORDER BY b.price DESC", Burger.class);
        query.setParameter("price", price);
        return query.getResultList();
    }

    @Override
    public List<Burger> findByBreadType(BreadType breadType) {
        TypedQuery<Burger> query = entityManager.createQuery("Select b From Burger b Where b.breadType > :breadType ORDER BY b.name DESC", Burger.class);
        query.setParameter("breadType", breadType);
        return query.getResultList();
    }

    @Override
    public List<Burger> findByContent(String content) {
        TypedQuery<Burger> query = entityManager.createQuery("Select b From Burger b Where b.contents LIKE CONCAT('%', :content, '%') ORDER BY b.name", Burger.class);
        query.setParameter("content", content);
        return query.getResultList();
    }

    @Override
    @Transactional
    public Burger update(Burger burger) {
        return entityManager.merge(burger);


    }

    @Transactional
    @Override
    public Burger remove(Long id) {

        Burger found = findById(id);
        entityManager.remove(found);
        return found;
    }
}