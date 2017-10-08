package EmployeeManager;

import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by lsh on 08/10/2017.
 */
public class Mapper<T> implements RowMapper<T> {

    private Class<T> cls;
    private Map<String,Object> defValue;

    Mapper(Class<T> _cls) {
        cls = _cls;
    }

    Mapper(Class<T> _cls, Map<String,Object> _defValue){
        cls = _cls;
        Map<String,Object> defValue=_defValue;
    }

    public T mapRow(ResultSet result, int rowNum) throws SQLException {
        try {
            T obj = cls.newInstance();
            Field field=cls.getDeclaredField("Attrs");
            String[] Attrs=(String[])(field.get(obj));
            List<Object> argv=new ArrayList<Object>();
            for(String attr:Attrs){
                try {
                    argv.add(result.getObject(attr));
                }catch (SQLException e) {
                    if(defValue.containsKey(attr)) argv.add(defValue.get(attr));
                    else argv.add("");
                }
            }
            Constructor cons=cls.getDeclaredConstructor(ArrayList.class);
            return (T)(cons.newInstance(argv));
        } catch (Exception e) {
            System.out.println("error: "+e.getMessage());
            return null;
        }
    }
}
