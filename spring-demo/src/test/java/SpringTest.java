import com.service.UserService;
import com.spring.MySpringApplicationContext;

/**
 * 模拟spring的SpringApplicationContext容器
 */
public class SpringTest {
    public static void main(String[] args) {
        MySpringApplicationContext mySpringApplicationContext = new MySpringApplicationContext(MyAppConfig.class);
        UserService userService = (UserService) mySpringApplicationContext.getBean("userService");
        userService.test();
    }
}