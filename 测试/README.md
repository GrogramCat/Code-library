# Junit

>JUnit ——是一个开发源代码的Java测试框架，用于编写和运行可重复的测试。他是用于单元测试框架体系xUnit的一个实例（用于java语言）。主要用于白盒测试，回归测试。

***

Junit 所有的测试方法都是@Text 修饰,以public void 开头

```java
@Test
public void textAdd() {
    assertEquals(4, 2+2);
}
```

***

@BeforeClass & @AfterClass 都只会执行一次

@BeforeClass在类加载时执

@AfterClass在类加载结束时执行

以上都为静态的方法

```java
@BeforeClass
public static void setUpBeforeClass() throws Exception {
    System.out.print("before class");
}

@AfterClass
public static void tearDownAfterClass() throws Exception {
    System.out.print("after class");
}
```

***

@Before & @After 在每个测试方法执行时都会执行一次


@Before 在测试方法执行前，@After 在测试方法执行后

```java
@Before
public void setUp() throws Exception {
    System.out.print("before");
}

@After
public void tearDown() throws Exception {
    System.out.print("after");
}
```

***

@Ignore 所修饰的测试方法会被测试运行器忽略,如下测试方法：

```java
@Ignore
@Test
public void text1() throws Exception {
    System.out.print("test1");
}
```

***

@Test(timeout = 毫秒),用来指定时间上限,如果测试方法执行的时间超过了指定的时间，则测试失败

```java
@Test(timeout = 1000)
public void testTime() {
    try {
        Thread.sleep(2000);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}
```

***

@Test(expected = 异常类)，用excepted来指定应该抛出的异常,如果在测试中没有抛出异常或者抛出的异常与指定异常不同，则测试失败

```java
@Test(expected = InedxOutBoundException.class)
public void textArrayList() throws Exception {
    new ArrayList<String>().get(1);
}
```

***

测试套件就是组织所要测试的类一起运行，如果单个类单独的运行是比较麻烦的，可以使用测试套件一起运行这些测试类。需要注意的是：
1. 测试套件的类是不包含其他任何方法
2. 同时要更改测试运行器为Suite.class
3. 将要测试的类作为数组传入到SuiteClasses({})中

```java
// 更改测试运行器以及将要测试的类放入SuiteClasses中
  @RunWith(Suite.class)
  @SuiteClasses({ AppTest.class, CountNumberTest.class })
  public class AllTests {
      // 没有测试方法
  }
```

*** 

在测试类中，如果我们要指定方法的执行顺序，可以使用注解FixMethodOrder

```java
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CountNumberTest {
    // 测试方法
}
// MethodSorter是一个枚举，它有以下枚举项，默认为DEFAULT
// NAME_ASCENDING: 按字母的升序执行
// JVM: 按照JVM中方法加载的顺序
// DEFAULT: 默认顺序，由方法名hashcode值来决定，如果hash值大小一致，则按名字的字典顺序确定。
```

## 例子
```java
/**
 * 方法类
 */
public class CountNumber {
    public boolean IfNumberIsPhoneNumber(String number) {
        if (0 == number.length())
            return false;
        if (number.length() == 11) {
            return true;
        } else {
            return false;
        }
    }
}

/**
 * 测试类
 */
public class CountNumberTest {

    private CountNumber countNumber;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        System.out.println("@BeforeClass");
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        System.out.println("@AfterClass");
    }

    @Before
    public void setUp() throws Exception {
        countNumber = new CountNumber();
        System.out.println("@before");
    }

    @After
    public void tearDown() throws Exception {
        countNumber = null;
        System.out.println("@after");
    }

    @Test
    public void testIfNumberIsPhoneNumber() throws Exception {
        System.out.println("Now,testing...");
        assertEquals(true, countNumber.IfNumberIsPhoneNumber("13590079433"));
        assertEquals(false,countNumber.IfNumberIsPhoneNumber("23213"));
        assertTrue(countNumber.IfNumberIsPhoneNumber("13652764553"));
        assertFalse(countNumber.IfNumberIsPhoneNumber("23131"));
    }
}
```
