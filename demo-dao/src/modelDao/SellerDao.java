package modelDao;

import java.util.List;

import modelEntities.Department;
import modelEntities.Seller;

public interface SellerDao {
    void insert(Seller obj);
    void update(Seller obj );
    void deleteById(Integer id);
    Seller findById(Integer id);
    List<Seller> findAll();
    List<Seller> findByDepartment(Department department);
}
