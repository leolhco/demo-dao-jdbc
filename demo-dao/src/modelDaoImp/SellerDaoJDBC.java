package modelDaoImp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.BD;
import db.DbException;
import modelDao.SellerDao;
import modelEntities.Department;
import modelEntities.Seller;

public class SellerDaoJDBC implements SellerDao{

    private Connection conn = null;
    
    public SellerDaoJDBC(Connection conn){
         this.conn = conn;
    }
    
    @Override
    public void insert(Seller obj) {
        PreparedStatement pst = null;
        String sql = "INSERT INTO seller "
                   + "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
                   + "VALUES (?, ?, ?, ?, ?)";
        try {
            pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pst.setString(1, obj.getName());
            pst.setString(2, obj.getEmail());
            pst.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
            pst.setDouble(4, obj.getBaseSalary());
            pst.setInt(5, obj.getDp().getId());

            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
                BD.closeResultSet(rs);
            } else {
                throw new DbException("Erro inesperado! Nenhuma linha inserida!");
            }

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            BD.closeStatement(pst);
        }
    }

    
    @Override
    public void update(Seller obj) {
        PreparedStatement pst = null;
        String sql = "UPDATE seller "
                   + "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
                   + "WHERE Id = ?";
        try {
            pst = conn.prepareStatement(sql);

            pst.setString(1, obj.getName());
            pst.setString(2, obj.getEmail());
            pst.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
            pst.setDouble(4, obj.getBaseSalary());
            pst.setInt(5, obj.getDp().getId());
            pst.setInt(6, obj.getId());

            pst.executeUpdate();

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            BD.closeStatement(pst);
        }
    }

    
    @Override
    public void deleteById(Integer id) {
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement("DELETE FROM seller WHERE Id = ?");

            pst.setInt(1, id);
            int rows = pst.executeUpdate();

            if (rows == 0) {
                throw new DbException("Id inexistente!");
            }

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            BD.closeStatement(pst);
        }
    }

   
    @Override
    public List<Seller> findAll() {
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement(
                "SELECT seller.*, department.Name as DepName "
              + "FROM seller INNER JOIN department "
              + "ON seller.DepartmentId = department.Id "
              + "ORDER BY Name"
            );

            rs = pst.executeQuery();

            List<Seller> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while (rs.next()) {
                Department dep = map.get(rs.getInt("DepartmentId"));
                if (dep == null) {
                    dep = instantiateDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), dep);
                }

                Seller sel = instantiateSeller(rs, dep);
                list.add(sel);
            }

            return list;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            BD.closeResultSet(rs);
            BD.closeStatement(pst);
        }
    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement pst = null;
        ResultSet rst = null;
        String sql = "SELECT seller.*, department.Name as DepName " 
                   + "FROM seller INNER JOIN department "
                   + "ON seller.DepartmentId = department.Id "
                   + "WHERE seller.Id = ?";
        try {
           
            pst = this.conn.prepareStatement(sql);
            
            pst.setInt(1, id);
            
            rst = pst.executeQuery();

            if(rst.next()) {
                Department dp = instantiateDepartment(rst);
                Seller seller = instantiateSeller(rst, dp);
                return seller;
            }

            return null;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
          finally {
            BD.closeResultSet(rst);
            BD.closeStatement(pst);
          }

    }
    
    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
        
            pst = conn.prepareStatement(
            "SELECT seller.*, department.Name as DepName "
          + "FROM seller INNER JOIN department "
          + "ON seller.DepartmentId = department.Id "
          + "WHERE DepartmentId = ? "
          + "ORDER BY Name");

        pst.setInt(1, department.getId());
        rs = pst.executeQuery();

        List<Seller> list = new ArrayList<>();
        Map<Integer, Department> map = new HashMap<>();

        while (rs.next()) {
            Department dep = map.get(rs.getInt("DepartmentId"));

            if (dep == null) {
                dep = instantiateDepartment(rs);
                map.put(rs.getInt("DepartmentId"), dep);
            }

            Seller sel = instantiateSeller(rs, dep);
            list.add(sel);
        }

        return list;

       } catch (SQLException e) {
        
        throw new DbException(e.getMessage());
       
       } finally {
        BD.closeResultSet(rs);
        BD.closeStatement(pst);
       }
    } 

    private Seller instantiateSeller(ResultSet rst, Department dp) throws SQLException {
        return new Seller(rst.getInt("Id"), rst.getString("Name"), rst.getString("Email"), rst.getDate("BirthDate"), rst.getDouble("BaseSalary"), dp);
    }
    
    private Department instantiateDepartment(ResultSet rst) throws SQLException {
        return new Department(rst.getInt("DepartmentId"), rst.getString("DepName"));
    }
    
}
