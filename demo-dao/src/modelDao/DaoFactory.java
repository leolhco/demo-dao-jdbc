package modelDao;

import db.BD;
import modelDaoImp.SellerDaoJDBC;


public class DaoFactory {
    public static SellerDao creatSellerDao(){
        return new SellerDaoJDBC(BD.getConnection());
    }
}
