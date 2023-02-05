import com.spring.MySpringApplicationContext;

/**
 * 模拟spring的SpringApplicationContext容器
 */
public class SpringTest {
    public static void main(String[] args) throws ClassNotFoundException {
        MySpringApplicationContext mySpringApplicationContext = new MySpringApplicationContext(MyAppConfig.class);
    }
}