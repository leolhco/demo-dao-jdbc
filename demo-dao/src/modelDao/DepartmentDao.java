package modelDao;

import java.util.List;

import modelEntities.Department;

public interface DepartmentDao {
    void insert(Department obj);
    void update(Department obj);
    void deleteById(Integer Id);
    List<Department> findAll();
}
