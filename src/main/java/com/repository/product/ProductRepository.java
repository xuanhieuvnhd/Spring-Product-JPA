package com.repository.product;

import com.model.Product;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class ProductRepository implements IProductRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Product> findAll() {
        TypedQuery<Product> query = em.createQuery("Select p from Product as p", Product.class);
        return query.getResultList();
    }

    @Override
    public Product findById(Long id) {
        TypedQuery<Product> query = em.createQuery("Select p from Product as p where p.id = :id", Product.class);
        query.setParameter("id", id);
        try {
            return query.getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void save(Product product) {
            if (product.getId() == null) {
                em.persist(product);
            }else {
                em.merge(product);
            }
    }

    @Override
    public void delete(Long id) {
        Product product = this.findById(id);
        em.remove(product);
    }

    @Override
    public List<Product> findByName(String name) {
        TypedQuery<Product> query = em.createQuery("Select p from Product as p where p.name like :name", Product.class);
        query.setParameter("name", name);
        return query.getResultList();
    }
}
