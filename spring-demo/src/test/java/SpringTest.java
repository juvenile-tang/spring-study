import com.spring.MySpringApplicationContext;

/**
 * 模拟spring的SpringApplicationContext容器
 */
public class SpringTest {
    public static void main(String[] args) {
        MySpringApplicationContext mySpringApplicationContext = new MySpringApplicationContext(MyAppConfig.class);
        System.out.println(mySpringApplicationContext.getBean("userService"));
        System.out.println(mySpringApplicationContext.getBean("userService"));
        System.out.println(mySpringApplicationContext.getBean("userService"));
    }
}