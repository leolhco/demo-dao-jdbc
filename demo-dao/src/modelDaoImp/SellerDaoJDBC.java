package modelDaoImp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insert'");
    }

    @Override
    public void update(Seller obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void deleteById(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

    @Override
    public List<Seller> findAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
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
    private Seller instantiateSeller(ResultSet rst, Department dp) throws SQLException {
        return new Seller(rst.getInt("Id"), rst.getString("Name"), rst.getString("Email"), rst.getDate("BirthDate"), rst.getDouble("BaseSalary"), dp);
    }
    private Department instantiateDepartment(ResultSet rst) throws SQLException {
        return new Department(rst.getInt("DepartmentId"), rst.getString("DepName"));
    }
    
}
