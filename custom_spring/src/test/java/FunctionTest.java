import com.xcl.factory.BeanFactory;
import com.xcl.service.AccountService;
import org.junit.Test;

import java.math.BigDecimal;

public class FunctionTest {

//    private DataSource dataSource = (DataSource) BeanFactory.getBean("dataSource");
//    @Test
//    public void testDataSource() throws SQLException {
//        Connection connection = dataSource.getConnection();
//        String sql = "select * from account";
//        PreparedStatement preparedStatement = connection.prepareStatement(sql);
//        ResultSet resultSet = preparedStatement.executeQuery();
//        while (resultSet.next()) {
//            Integer id = resultSet.getInt("id");
//            String username = resultSet.getString("username");
//            BigDecimal money = resultSet.getBigDecimal("money");
//            System.out.println(id+username+money);
//        }
//    }

    //accountService对象
    private AccountService accountService = (AccountService)BeanFactory.getBean("accountService");
    @Test
    public  void transfer(){
        accountService.transfer(123456789,123456799,new BigDecimal(1000));
    }
}
