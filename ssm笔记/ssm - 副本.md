# 一、MyBatis



##  1、MyBatis 简介

### 1.1  MyBatis历史

MyBatis最初是Apache的一个开源项目**iBatis**, 2010年6月这个项目由Apache Software Foundation迁移到了Google Code。随着开发团队转投Google Code旗下， iBatis3.x正式更名为MyBatis。代码于2013年11月迁移到Github。

iBatis一词来源于“internet”和“abatis”的组合，是一个基于Java的持久层框架。 iBatis提供的持久层框架包括SQL Maps和Data Access Objects（DAO）。



### 1.2 MyBatis特性

1） MyBatis 是支持定制化 SQL、存储过程以及高级映射的优秀的持久层框架

2） MyBatis 避免了几乎所有的 JDBC 代码和手动设置参数以及获取结果集

3） MyBatis可以使用简单的XML或注解用于配置和原始映射，将接口和Java的POJO（Plain Old Java Objects，普通的		Java对象）映射成数据库中的记录

4） MyBatis 是一个 半自动的ORM（Object Relation Mapping）框架



### 1.3  和其它持久化层技术对比

#### 1）JDBC

* SQL 夹杂在Java代码中耦合度高，导致硬编码内伤
* 维护不易且实际开发需求中 SQL 有变化，频繁修改的情况多见代码冗长，开发效率低

#### 2） Hibernate 和 JPA

* 操作简便，开发效率高
* 程序中的长难复杂 SQL 需要绕过框架
* 内部自动生产的 SQL，不容易做特殊优
* 基于全映射的全自动框架，大量字段的 POJO 进行部分映射时比较困难。
* 反射操作太多，导致数据库性能下降

#### 3）MyBatis

* 轻量级，性能出色

* SQL 和 Java 编码分开，功能边界清晰。Java代码专注业务、SQL语句专注数据

* 开发效率稍逊于HIbernate，但是完全能够接受

	

## 2、搭建MyBatis



### 2.1  开发环境

IDE：idea 2019.2

构建工具：maven 3.5.4

MySQL版本：MySQL 8

MyBatis版本：MyBatis 3.5.7

MySQL不同版本的注意事项

* 驱动类 driver-class-name

	1) MySQL 5版本使用 jdbc5 驱动，驱动类使用：com.mysql.jdbc.Driver 

	2) MySQL 8版本使用 jdbc8 驱动，驱动类使用：com.mysql.cj.jdbc.Driver

*  连接地址 url 

	1) MySQL 5版本的url：jdbc:mysql://localhost:3306/ssm

	2) MySQL 8 版 本 的 url： jdbc:mysql://localhost:3306/ssm?serverTimezone=UTC 

	3) 否则运行测试用例报告如下错误：

	​	java.sql.SQLException: The server time zone value 'ÖÐ¹ú±ê×¼Ê±¼ä' is unrecognized or represents more

	

### 2.2 创建maven工程

##### ① 打包方式：jar

##### ② 引入依赖 

```XML
<dependencies>
    
    <!-- Mybatis核心 -->
    <dependency>
        <groupId>org.mybatis</groupId>
        <artifactId>mybatis</artifactId>
        <version>3.5.7</version>
    </dependency>

    <!-- junit测试 -->
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.12</version>
        <scope>test</scope>
    </dependency>

    <!-- MySQL驱动 -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.16</version>
    </dependency>
    
</dependencies>
```



### 2.3  创建MyBatis的核心配置文件

* 核心配置文件主要用于 **配置连接数据库的环境 以及 MyBatis的全局配置信息** 
* 核心配置文件存放的 位置是 **src/main/resources** 目录下
* 习惯上命名为 ==mybatis-config.xml==，这个文件名仅仅只是建议，并非强制要求。将来整合Spring  之后，这个配置文件可以省略，所以大家操作时可以直接复制、粘贴。

```XML
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN" "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>

    <!--设置连接数据库的环境-->
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/ssm?serverTimezone=UTC"/>
                <property name="username" value="root"/>
                <property name="password" value="lhc"/>
            </dataSource>
        </environment>
    </environments>
    
    <!--引入mybatis映射文件-->
    <mappers>
        <mapper resource="mappers/UserMapper.xml"/>
    </mappers>
    
</configuration>
```



###  2.4 创建mapper接口

MyBatis中的mapper接口相当于以前的dao。但是区别在于，mapper仅仅是接口，我们不需要    提供实现类。

```JAVA
public interface UserMapper {

    /**
    * 添加用户信息
    */
    int insertUser();

}
```



### 2.5 创建MyBatis的映射文件

相关概念：**ORM**（**O**bject **R**elationship **M**apping）对象关系映射。

* 对象：Java的实体类对象

* 关系：关系型数据库

* 映射：二者之间的对应关系

	

| **Java概念** | **数据库概念** |
| ------------ | -------------- |
| 类           | 表             |
| 属性         | 字段/列        |
| 对象         | 记录/行        |



**1）映射文件的命名规则**

* 表所对应的实体类的类名+Mapper.xml

	例如：表 **t_user**，映射的实体类为**User**，所对应的映射文件为==UserMapper.xml== 因此一个映射文件对应一个实体类，对应一张表的操作

* MyBatis映射文件用于编写SQL，访问以及操作表中的数据

* MyBatis映射文件存放的位置是**src/main/resources/mappers**目录下

**2） MyBatis中可以面向接口操作数据，要保证两个一致：**

* mapper接口的全类名和映射文件的命名空间（namespace）保持一致
* mapper接口中方法的方法名和映射文件中编写SQL的标签的id属性保持一致

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.atguigu.mybatis.mapper.UserMapper">

    <!--int insertUser();-->
    <insert id="insertUser">
    	insert into t_user values(null,'admin','123456',23,'男','12345@qq.com')
    </insert>

</mapper>
```



### **2.6 、通过 junit 测试功能**

==MyBatis.java==

* **SqlSession**：代表Java程序和**数据库**之间的**会话**。（HttpSession是Java程序和浏览器之间的      会话）

* **SqlSessionFactory**：是“生产” **SqlSession** 的“工厂”。


```java
public class MyBatis {

    @Test
    public void testInsert() throws IOException {

        //1. 获取核心配置文件的输入流
        InputStream is = Resources.getResourceAsStream("mybatis.config.xml");
        //2. 获取SqlSessionFactoryBuilder对象
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        //3. 获取SqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(is);
        //4. 获取SqlSession对象，是MyBatis提供的操作数据库的对象
        SqlSession sqlSession = sqlSessionFactory.openSession(true); // true代表自动提交事务

        //5. 有两种方式 执行UserMapper.xml 中的sql语句

        // 方式一：
        //通过sql以及的唯一标识找到sql并执行，唯一标识是namespace.sqlId
        //int result = sqlSession.insert("com.atguigu.mybatis.mapper.UserMapper.insertUser");

        //方式二：（常用）
        //1). 获取UserMapper的代理实现类对象
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        //2). 调用mapper接口中的方法，实现数据库的添加功能
        int result = mapper.insertUser();

        //打印返回结果
        System.out.println("结果" + result);

        //提交事务 （如果在获取SqlSession对象时 没有设置默认提交 就需要手动提交）
        //sqlSession.commit();

        //关闭sqlSession
        sqlSession.close();


    }
}

```

**工厂模式**：如果创建某一个对象，使用的过程基本固定，那么我们就可以把创建这个对象的 相关代码封装到一个工厂类中，以后都使用这个工厂类来“生产”我们需要的对象。



### **2.7** **、加入log4j日志功能**

**①加入依赖**  (**pom.xml**)

```xml
<!-- log4j日志 -->
<dependency>
    <groupId>log4j</groupId>
    <artifactId>log4j</artifactId>
    <version>1.2.17</version>
</dependency>
```



**②加入log4j的配置文件**

log4j的配置文件名为**log4j.xml**，存放的位置是**src/main/resources**目录下

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE log4j:configuration SYSTEM "log4j.dtd">

<log4j:configuration xmlns:log4j="http://jakarta.apache.org/log4j/">

    <appender name="STDOUT" class="org.apache.log4j.ConsoleAppender">
        <param name="Encoding" value="UTF-8"/>
        <layout class="org.apache.log4j.PatternLayout">
            <param name="ConversionPattern" value="%-5p %d{MM-dd HH:mm:ss,SSS} %m  (%F:%L) \n" />
        </layout>
    </appender>
    <logger name="java.sql">
        <level value="debug"/>
    </logger>
    <logger name="org.apache.ibatis">
        <level value="info"/>
    </logger>
    <root>
        <level value="debug"/>
        <appender-ref ref="STDOUT"/>
    </root>
</log4j:configuration>
```

**日志的级别**

​	FATAL(致命)>ERROR(错误)>WARN(警告)>INFO(信息)>DEBUG(调试)

​	从左到右打印的内容越来越详细



## 3、MyBatis的增删改查

​	我们可以将获取SqlSession对象的 步骤封装到 **SqlSessionUtil** 中  ,就可以直接从这个工具类中获取SqlSession

### **SqlSessionUtil**

```java
public class SqlSessionUtil {

    public static SqlSession getSqlSession() {

        SqlSession sqlSession = null;

        try {

            //1. 获取核心配置文件的输入流
            InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
            //2. 获取SqlSessionFactoryBuilder对象
            SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
            //3. 获取SqlSessionFactory对象
            SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(is);
            //4. 获取SqlSession对象，是MyBatis提供的操作数据库的对象
            sqlSession = sqlSessionFactory.openSession(true); // true代表自动提交事务

        } catch (IOException e) {
            e.printStackTrace();
        }
        return sqlSession;
    }
}
```

### **UserMapper接口**

```java
//新增用户信息
int insertUser();

//删除用户信息
void deleteUser();

//修改用户信息
void updateUser();

//查询当个用户信息
User getUserById();

//查询多个用户信息
List<User> getUserList();
```

### MyBatis映射文件 

```xml
<!--int insertUser();-->
<insert id="insertUser">
	insert into t_user values(null,'admin','123456',23,'男')
</insert>

<!--int deleteUser();-->
<delete id="deleteUser">
	delete from t_user where id = 7
</delete>

<!--void updateUser();-->
<update id="updateUser">
    update t_user set username = 'root', password= '123' WHERE id = 3
</update>

<!--User getUserById();-->
<select id="getUserById" resultType="com.atguigu.mybatis.bean.User"> 
    select * from t_user where id = 2
</select>

<!--List<User> getUserList();-->
<select id="getUserList" resultType="com.atguigu.mybatis.bean.User"> 
    select * from t_user
</select>

<!--
    注意：
    1、查询的标签select必须设置属性resultType或resultMap，用于设置实体类和数据库表的映射 关系
       resultType：自动映射，用于属性名和表中字段名一致的情况
       resultMap：自定义映射，用于一对多或多对一或字段名和属性名不一致的情况
-->
```



### MyBatis测试类

```java
@Test
public void testDelete() {
    //1. 获取SqlSession
    SqlSession sqlSession = SqlSessionUtil.getSqlSession();
    //2. 获取UserMapper的代理实现类对象
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    //3. 调用mapper接口中的方法，实现数据库的修改功能
    mapper.insertUser();
    //关闭SqlSession
    sqlSession.close();
}


@Test
public void testDelete() {
    SqlSession sqlSession = SqlSessionUtil.getSqlSession();
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    mapper.deleteUser();
    sqlSession.close();
}

@Test
public void testUpdate() {
    SqlSession sqlSession = SqlSessionUtil.getSqlSession();
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    mapper.updateUser();
    sqlSession.close();
}

@Test
public void testGetUser() {
    SqlSession sqlSession = SqlSessionUtil.getSqlSession();
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    User user = mapper.getUserById();
    sqlSession.close();
    System.out.println(user);
}

@Test
public void testGetUserList() {
    SqlSession sqlSession = SqlSessionUtil.getSqlSession();
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    List<User> userList = mapper.getUserList();
    sqlSession.close();
    for (User user : userList) {
        System.out.println(user);
    }
}
```



## 4、核心配置文件详解

==核心配置文件 mybatis.config.xml==

### 4.1 核心配置文件之 environments

```xml

<!-- 
    environments：配置多个连接数据库的环境
        属性 default：设置默认使用的环境的id
-->
<environments default="development">
    <!-- 	
        environment：配置某个具体的环境
            属性 id：表示连接数据库的环境的唯一标识，不能重复
    -->
    <environment id="development">
        <!--
			transactionManager：设置事务管理方式
				属性 type="JDBC|MANAGED"
        			JDBC：表示当前环境中，执行SQL时，使用的是JDBC中原生的事务管理方式
        			MANAGED：被管理，例如Spring
		-->
        <transactionManager type="JDBC"/>
        <!--
            dataSource：配置数据源
				属性 type：设置数据源的类型 type="POOLED|UNPOOLED|JNDI"
                    POOLED：表示使用数据库连接池缓存数据库连接
                    UNPOOLED：表示不使用数据库连接池
                    JNDI：表示使用上下文中的数据源
        -->		
        <dataSource type="POOLED">
            <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
            <property name="url" value="jdbc:mysql://localhost:3306/ssm?serverTimezone=UTC"/>
            <property name="username" value="root"/>
        </dataSource>
    </environment>
    
    
    <environment id="test">
        <transactionManager type="JDBC"/>
        <dataSource type="POOLED">
            <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
            <property name="url" value="jdbc:mysql://localhost:3306/ssmserverTimezone=UTC"/>
            <property name="username" value="root"/>
            <property name="password" value="123456"/>
        </dataSource>
    </environment>
    
</environments>
```



### 4.2 核心配置文件之 properties



1) 先创建配置文件 jdbc.properties

```properties
#建议在设置key的时候 添加前缀，防止出现重名
jdbc.driver=com.mysql.cj.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/ssm?serverTimezone=UTC
jdbc.username=root
jdbc.password=lhc
```

2) 在核心配置文件中 引入并使用 

```xml
<!-- 引入properties文件，此后就可以在当前文件中使用 ${key}的方式访问 value-->
<properties resource="jdbc.properties" /> 

<!-- 改为读取配置文件的方式 -->
<dataSource type="POOLED">
    <property name="driver" value="${jdbc.driver}"/>
    <property name="url" value="${jdbc.url}"/>
    <property name="username" value="${jdbc.username}"/>
    <property name="password" value="${jdbc.password}"/>
</dataSource>
```



### 4.3 核心配置文件之 typeAliases

```xml
<!--设置实体类别名-->
    <typeAliases>
    <!--
    typeAlias：设置某个类型的别名
		属性：
            type：设置需要设置别名的类型
            alias：设置某个类型的别名，若不设置该属性，那么该类型拥有默认的别名，即类名 且不区分大小写
    -->
        
 
    <!--1. 单个注册(实体类多时，都需依次注册)-->
    <!-- <typeAlias type="com.atguigu.mybatis.pojo.User"></typeAlias> -->  
    <!--2. 以包为单位，将包下所有的类型设置默认的类型别名，即类名 且不区分大小写-->
    <package name="com.atguigu.mybatis.pojo"/>
        
</typeAliases>
```



### 4.4 核心配置文件之 mappers

```xml
<!--4. 引入mybatis映射文件-->
<mappers>
    <!-- 1) 引入单个映射文件 -->
    <!--<mapper resource="mappers/UserMapper.xml"/>-->
    <!--
    	2）以包的方式引入配置文件，但是必须满足两个条件：
        	① mapper接口 和 映射文件的 所在的包必须一致
         	② mapper接口的名字 和 映射文件的名字 必须保持一致
    -->
    <package name="com.atguigu.mybatis.mapper"/>
</mappers>
```



* 关于 **java** 和  **resources** 两个目录

	

	* java 和 resources 两个目录只是用来将 **java文件**和**配置文件**分开书写的目录

	* 程序加载后 两个目录的文件会加载到同一**class类路径**下

	

![1659164100113](C:\Users\huach\AppData\Roaming\Typora\typora-user-images\1659164100113.png)



### 4.5   创建核心配置文件的模板

**mybatis.config.xml**  模板

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN" "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>

    <!--
        MyBatis 核 心 配 置 文 件 中 ， 标 签 的 顺 序 ：
        properties?,settings?,typeAliases?,typeHandlers?, 
	    objectFactory?,objectWrapperFactory?,reflectorFactory?,
		plugins?,environments?,databaseIdProvider?,mappers?
    -->

    <!--1. 引入properties文件-->
    <properties resource="jdbc.properties"/>

    <!--2. 设置类型别名-->
    <typeAliases>
        <package name="实体类的包名"/>  
    </typeAliases>

    <!--3. 配置连接数据库的环境-->
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="${jdbc.driver}"/>
                <property name="url" value="${jdbc.url}"/>
                <property name="username" value="${jdbc.username}"/>
                <property name="password" value="${jdbc.password}"/>
            </dataSource>
        </environment>
    </environments>

    <!--4. 引入mybatis映射文件-->
    <mappers>
        <package name="映射文件的包名"/>  
    </mappers>

</configuration>
```



### 4.6 创建映射文件的模板

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="接口的完全限定名称">

</mapper>
```



## 5、MyBatis获取参数值的两种方式



**MyBatis** 获取参数值的两种方式：**${}**和**#{}**

* **${} 使用字符串拼接** 的方式拼接sql，若为字符串类型或日期类型的字段进行赋值时，需要手动加单引号；
* **#{}使用占位符赋值**的方式拼接sql， 此时为字符串类型或日期类型的字段进行赋值时， 可以自动添加单引号；



### **5.1** **单个字面量类型的参数** 

   若mapper接口中的方法参数为**单个**的字面量类型

* 可以使用 **${}** 和 **#{}** 以**任意的名称获取参数的值** 
* 注意${} 需要手动添加单引号

```JAVA
/**
* mapper接口中的方法参数为 单个的字面量类型的参数
* @param username
* @return
*/
User getUserByUsername(String username);
```

```XML
<!--1. User getUserByUsername(String username);-->
<select id="getUserByUsername" resultType="User">
	select * from t_user where username = #{username}
</select>
```

```JAVA
//mapper接口的参数为 单个字面量类型的参数
@Test
public void getUserByUsername() {
    //1. 获取SqlSession
    SqlSession sqlSession = SqlSessionUtil.getSqlSession();
    //2. 获取UserMapper的代理实现类对象
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    //3. 调用mapper接口中的方法
    User user = mapper.getUserByUsername("link");
    System.out.println(user);
    //关闭SqlSession
    sqlSession.close();
}
```





### **5.2 **多个字面量类型的参数  

​	若mapper接口中的方法参数为**多个**的字面量类型，可以有一下几种处理方式



##### 1）自动封装成map集合 



​			以 **arg0,arg1...为键**，以**参数为值** ， 以 **param1,param2...为键**，以**参数为值**

​		    因此只需要通过**${}和#{}**访问**map集合的键**就可以获取相**对应的值**，

​		    注意${}需要手动加单引号

```java
/**
* mapper接口中的方法参数为 多个的字面量类型的参数
* @param username
* @param password
* @return
* 此时MyBatis会自动将这些参数放在一个map集合中
*      1）以 arg0,arg1...为键，以参数 为值
*      2）以 param1,param2...为键，以参数 为值
* 只需要通过${}和#{}访问map集合的键就可以获取相对应的值
*/
User checkLogin(String username, String password);
```

```xml
<!-- User checkLogin(String username, String password);-->
<select id="checkLogin" resultType="User">
    select * from t_user where username = #{param1} and password = #{param2}
</select>
```

```java
// mapper接口的参数为 多个字面量类型的参数
@Test
public void checkLogin() {
    SqlSession sqlSession = SqlSessionUtil.getSqlSession();
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    User user = mapper.checkLogin("link","ok");
    System.out.println(user);
    sqlSession.close();
}
```



##### 2）手动封装成 map集合 

​	可以手动创建map集合，将这些数据放在map中 只需要通过${}和#{}访问map集合的键就可以获取相对应的值

​	注意${}需要手动加单引号  

```JAVA
/**
* mapper接口中的方法参数为 map集合类型
* @param map
* @return
*/
User checkLoginByMap(Map<String, Object> map);
```

```XML
<!--3. User checkLoginByMap(Map<String, Object> map);-->
<select id="checkLoginByMap" resultType="User">
	select * from t_user where username = #{username} and password = #{password}
</select>
```

```JAVA
// mapper接口的参数为 map类型的参数
@Test
public void checkLoginByMap() {
    SqlSession sqlSession = SqlSessionUtil.getSqlSession();
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    //创建一个map
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("username","link"); // ( 表字段为key，字段值为value )
    map.put("password","ok");
    //调用mapper接口中的方法
    User user = mapper.checkLoginByMap(map);
    System.out.println(user);
    sqlSession.close();
}
```



##### 3）封装成一个 实体类

- 可以使用${}和#{}，通过访问实体类对象中的属性名获取属性值
- 注意${}需要手动加单引号

```JAVA
/**
* mapper接口中的方法参数为 实体类类型
* @param user
*/
void insertUser(User user);
```

```XML
<!-- void insertUser(User user);-->
<insert id="insertUser">
	insert into t_user values (null,#{username},#{password},#{age},#{gender},#{email})
</insert>
```

```JAVA
// mapper接口的参数为 实体类类型的参数
@Test
public void insertUser() {
    SqlSession sqlSession = SqlSessionUtil.getSqlSession();
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    //创建一个User
    User user = new User(null,"root","123456",23,"女", "1234@qq.com");
    //调用mapper接口中的方法
    mapper.insertUser(user);
    sqlSession.close();
}
```



##### 4）使用@Param标识参数

​	通过**@Param注解**标识mapper接口中的方法参数

​	将这些参数放在map集合中，以@Param注解的value属性值为键，以参数为值；

​	以param1,param2...为键，以参数为值；

​	只需要通过${}和#{}访问map集合的键就可以获取相对应的值

​	注意${}需要手动加单引号

```JAVA
/**
* mapper接口中的方法参数 使用@Param标识
* @param username
* @param password
* @return
*/
User checkLoginByParam(@Param("username") String username,@Param("password") String password);
```

```XML
<!-- User checkLoginByParam(@Param("username") String username,@Param("password") String password); -->
<select id="checkLoginByParam" resultType="User">
select * from t_user where username = #{username} and password = #{password}
</select>
```

```JAVA
// mapper接口的参数 使用@Param标识
@Test
public void checkLoginByParam() {
    SqlSession sqlSession = SqlSessionUtil.getSqlSession();
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    //调用mapper接口中的方法
    User user = mapper.checkLoginByParam("link", "ok");
    System.out.println(user);
    sqlSession.close();
}
```



## 6、MyBatis的各种查询功能 

1. 如果查询出的数据只有**一条**，可以通过
	1. 实体类对象接收
	2. List集合接收
	3. Map集合接收，结果`{password=123456, sex=男, id=1, age=23, username=admin}`
2. 如果查询出的数据有**多条**，一定不能用实体类对象接收，会抛异常TooManyResultsException，可以通过
	1. 实体类类型的LIst集合接收
	2. Map类型的LIst集合接收
	3. 在mapper接口的方法上添加@MapKey注解

### 6.1 查询单个数据

```java
/**  
 * 查询用户的总记录数  
 * @return  
 * 在MyBatis中，对于Java中常用的类型都设置了类型别名  
 * 例如：java.lang.Integer-->int|integer  
 * 例如：int-->_int|_integer  
 * 例如：Map-->map,List-->list  
 */  
int getCount();
```

```xml
<!--int getCount();-->
<select id="getCount" resultType="_integer">
	select count(id) from t_user
</select>
```



### 6.2 查询一个实体对象

```java
/**
 * 根据用户id查询用户信息
 * @param id
 * @return
 */
User getUserById(@Param("id") int id);
```

```xml
<!--User getUserById(@Param("id") int id);-->
<select id="getUserById" resultType="User">
	select * from t_user where id = #{id}
</select>
```



### 6.3 查询一个List集合

```java
/**
 * 查询所有用户信息
 * @return
 */
List<User> getUserList();
```

```xml
<!--List<User> getUserList();-->
<select id="getUserList" resultType="User">
	select * from t_user
</select>
```



### 6.4. map集合：

当我们查询出来的数据没有相对应的实体类，我们就需要用到map集合

### 1） 查询一条数据为map集合

```java
/**  
 * 根据用户id查询用户信息为map集合  
 * @param id  
 * @return  
 */  
Map<String, Object> getUserToMap(@Param("id") int id);
```

```xml
<!--Map<String, Object> getUserToMap(@Param("id") int id);-->
<select id="getUserToMap" resultType="map">
	select * from t_user where id = #{id}
</select>
```

```java
@Test
public void getUserToMap() {

    SqlSession sqlSession = SqlSessionUtil.getSqlSession();
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    //3. 调用mapper接口中的方法
    Map<String, Object> userToMap = mapper.getUserToMap(1);
    System.out.println(userToMap);
    // 输出结果: 
    // {password=123456, sex=男, id=1, age=23, username=admin}
    sqlSession.close();
}
```



### 2） 查询多条数据为map集合

#### 方法一

```java
/**  
 * 查询所有用户信息为map集合  
 * @return  
 * 将表中的数据以map集合的方式查询，一条数据对应一个map；若有多条数据，就会产生多个map集合，此时可以将这些map放在一个list集合中获取  
 */  
List<Map<String, Object>> getAllUserToMap();
```

```xml
<!--Map<String, Object> getAllUserToMap();-->  
<select id="getAllUserToMap" resultType="map">  
	select * from t_user  
</select>
```

```JAVA
@Test
public void getUserToMap() {

    SqlSession sqlSession = SqlSessionUtil.getSqlSession();
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    //3. 调用mapper接口中的方法

    List<Map<String, Object>> allUserToMap = mapper.getAllUserToMap();
    System.out.println(allUserToMap);
    // 输出结果: 
    //[{password=123456, sex=男, id=1, age=23, username=admin},
    //{password=123456, sex=男, id=2, age=23, username=张三},
    //{password=123456, sex=男, id=3, age=23, username=张三}
    sqlSession.close();
}
```



#### 方法二

```java
/**
 * 查询所有用户信息为map集合
 * @return
 * 将表中的数据以map集合的方式查询，一条数据对应一个map；若有多条数据，就会产生多个map集合，并且最终要以一个map的方式返回数据，此时需要通过@MapKey注解设置map集合的键，值是每条数据所对应的map集合
 */
@MapKey("id") //设置id属性 为这个map的key value为 一个单记录map
Map<String, Object> getAllUserToMap();
```

```xml
<!--Map<String, Object> getAllUserToMap();-->
<select id="getAllUserToMap" resultType="map">
	select * from t_user
</select>
```

```JAVA
@Test
public void getUserToMap() {

    SqlSession sqlSession = SqlSessionUtil.getSqlSession();
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    //3. 调用mapper接口中的方法
    Map<String, Object> userToMap = mapper.getUserToMap(1);
    System.out.println(userToMap);
    // 输出结果: 
    //1={password=123456, sex=男, id=1, age=23, username=admin},
    //2={password=123456, sex=男, id=2, age=23, username=张三},
    //3={password=123456, sex=男, id=3, age=23, username=张三}
    sqlSession.close();
}
```



## 7、特殊SQL的执行



### 7.1 模糊查询

```java
/**
 * 根据用户名进行模糊查询
 * @param username 
 * @return java.util.List<com.atguigu.mybatis.pojo.User>
 * @date 2022/2/26 21:56
 */
List<User> getUserByLike(@Param("username") String username);
```

```xml
<!--List<User> getUserByLike(@Param("username") String username);-->
<select id="getUserByLike" resultType="User">
	<!--select * from t_user where username like '%${mohu}%'-->  
	<!--select * from t_user where username like concat('%',#{mohu},'%')-->  
	select * from t_user where username like "%"#{mohu}"%"
</select>
```

- 其中`select * from t_user where username like "%"#{mohu}"%"`是最常用的

### 7.2 批量删除

- **只能使用\${}**

```java
/**
 * 根据id批量删除
 * @param ids 
 * @return int
 * @date 2022/2/26 22:06
 */
int deleteMore(@Param("ids") String ids);
```

```xml
<delete id="deleteMore">
	delete from t_user where id in (${ids})
</delete>
```

```java
//测试类
@Test
public void deleteMore() {
	SqlSession sqlSession = SqlSessionUtils.getSqlSession();
	SQLMapper mapper = sqlSession.getMapper(SQLMapper.class);
	int result = mapper.deleteMore("1,2,3,8");
	System.out.println(result);
}
```

**为什么不能使用 #{}**

使用#{} 解析后的sql语句为 **delete from t_user where id in ('1,2,3')**

这样是将`1,2,3`看做是一个整体，只有id = 1,2,3 的数据会被删除，不存在id 为 1,2,3 的数据

正确的语句应该是`delete from t_user where id in (1,2,3)`，或者`delete from t_user where id in ('1','2','3')`



### 7.3 动态设置表名

- **只能使用${}**，因为表名不能加单引号

```java
/**
 * 查询指定表中的数据
 * @param tableName 
 * @return java.util.List<com.atguigu.mybatis.pojo.User>
 * @date 2022/2/27 14:41
 */
List<User> getUserByTable(@Param("tableName") String tableName);
```

```xml
<!--List<User> getUserByTable(@Param("tableName") String tableName);-->
<select id="getUserByTable" resultType="User">
	select * from ${tableName}
</select>
```



### 7.4 添加功能获取自增的主键



**使用场景**

​	两表 班级表 和 学生表

* t_clazz(clazz_id,clazz_name)  

- t_student(student_id,student_name,clazz_id)  

	1）添加班级信息  

	2）获取新添加的班级的id  

	3）为班级分配学生，即将某**学生的班级id修改为新添加的班级的id**
	
	

**在JDBC中如何获取**

我们在学习**JDBC**的时候 也会有**获取自增列的主键**

```java
public void testJDBC(){
    try{
        
        class.forName("");
        Connection connection = DriverManager.getConnection("","","");
        //添加语句
        String sql = "insert into t_user" values(...);
        //1. 获取preparedStatement预编译对象
        //第二个参数为：设置 可以获取自动递增的主键（默认为不可以）
        PreparedStatement ps = connection.PreparedStatement(sql, Statement.RETURN_GEBERATED_KEYS);
        //2. 执行sql语句
        ps.excuteUpdate();
        //3. 获取自动递增的主键 结果集
        ResultSet resultSet = ps.getGeneratedKey();
        //4. 结果集中的指针指向 当前添加记录的主键
        resultSet.next();
        //5. 得到 当前添加记录的 自动递增主键
        int id = resultSet.getInt(1);
        
    } catch(...){
        ...
    }
    
}
```



**在mybatis中如何获取：**

在mapper.xml中设置两个属性
- **useGeneratedKeys：设置使用自增的主键**  
- **keyProperty：**因为增删改有统一的返回值是受影响的行数，因此只能将获取的自增的主键放在传输的参数**user对象的某个属性中**

```java
/**
 * 添加用户信息
 * @param user 
 * @date 2022/2/27 15:04
 */
void insertUser(User user);
```

```xml
<!--void insertUser(User user);-->
<insert id="insertUser" useGeneratedKeys="true" keyProperty="id">
	insert into t_user values (null,#{username},#{password},#{age},#{sex},#{email})
</insert>
```

```java
//测试类
@Test
public void insertUser() {
	SqlSession sqlSession = SqlSessionUtils.getSqlSession();
	SQLMapper mapper = sqlSession.getMapper(SQLMapper.class);
	User user = new User(null, "ton", "123", 23, "男", "123@321.com");
	mapper.insertUser(user);
	System.out.println(user);
	//输出：user{id=10, username='ton', password='123', age=23, sex='男', email='123@321.com'}，自增主键存放到了user的id属性中
}
```



## 8、自定义映射resultMap



### 8.0 resultType 与 resultMap

```
查询的标签select必须设置属性resultType或resultMap，用于设置实体类和数据库表的映射关系
1）resultType：自动映射，用于 属性名和表中字段名一致 的情况
2）resultMap：自定义映射，用于 一对多、多对一、字段名和属性名不一致 的情况
```

resultType 用于：

​		配置mybatis查询的返回结果类型

​		1）如果返回值类型是 基本数据类型 或 String类型 ，就配置为对应的类型名即可

​		2）如果查询结果为对象  要求 **实体类字段** 和 **数据库字段** 完全一致 ，

  		 （不一致需要通过 **设置别名的方式** 或者 **设置全局配置信息 mapUnderscoreToCamelCase****）

resultMap 用于：

​	也是为了配置mybatis查询的返回结果类型，但主要目的是：

​	1）处理字段名和属性名的映射关系

​	2）处理多对一映射关系

​	3）处理一对多映射关系						



### 8.1  处理字段和属性的映射关系



**情况一：**若字段名和实体类中的属性名不一致，但是字段名符合数据库的规则（**使用_**），实体类中的属性名符合Java的规则（**使用驼峰**）

两种方式处理 **字段名(数据库) 和 属性(实体类) 的映射关系**  

1) 可以通过**为字段起别名**的方式，保证和实体类中的属性名保持一致  

```xml
<!--List<Emp> getAllEmp();-->
<select id="getAllEmp" resultType="Emp">
	select empId emp_id ,emp_name empName ,age ,sex ,email from t_emp
</select>
```

2)  可以在**MyBatis的核心配置文件**中的`setting`标签中，设置一个**全局配置信息 mapUnderscoreToCamelCase**，这样在查询表中数据时，自动将 **_类型** 的字段名转换为 **驼峰**

```xml
<settings>
    <setting name="mapUnderscoreToCamelCase" value="true"/>
</settings>
```

```xml
<!--List<Emp> getAllEmp();-->
<select id="getAllEmp" resultType="Emp">
	select eid ,emp_name ,age ,sex ,email from t_emp
</select>
```



**情况二：**若字段名和实体类中的属性名不一致，没有但是

通过**resultMap设置自定义映射**，**即使字段名和属性名一致的属性也要映射**，也就是全部属性都要列出来

```xml
<!-- resultMap标签：设置自定义映射  
		id属性：表示自定义映射的唯一标识，不能重复 （和select标签的 resultMap属性值 保持一致且唯一 即可）
		type属性：查询的数据要映射的实体类的类型  
-->
<resultMap id="empResultMap" type="Emp">
    <!-- id标签：设置主键的映射关系  
		result标签：设置普通字段的映射关系  
			property属性：设置映射关系中实体类中的属性名  
			column属性：设置映射关系中表中的字段名
	-->
	<id property="empId" column="emp_id"></id>
	<result property="empName" column="emp_name"></result>
	<result property="age" column="age"></result>
	<result property="sex" column="sex"></result>
</resultMap>

<!--List<Emp> getAllEmp();-->
<select id="getAllEmp" resultMap="empResultMap">
	select * from t_emp
</select>
```



### 8.2 处理多对一映射关系

> 查询员工信息以及员工所对应的部门信息

```java
// 查询 指定员工信息 及其对应的 部门信息
public class Emp {  
	private Integer empId;  
	private String empName;  
	private Integer age;  
	private String gender;  
	private Dept dept;		//引用部门表实体类 
    ...
}
```



#### 1）级联方式处理映射关系

```xml
<resultMap id="empAndDeptResultMapOne" type="Emp">
	<id property="empId" column="emp_id"></id>
	<result property="empName" column="emp_name"></result>
	<result property="age" column="age"></result>
	<result property="sex" column="sex"></result>
    
	<result property="dept.did" column="dept_id"></result>
	<result property="dept.deptName" column="dept_name"></result>
</resultMap>

<!--Emp getEmpAndDept(@Param("empId")Integer empId);-->
<select id="getEmpAndDept" resultMap="empAndDeptResultMapOne">
	select * from t_emp left join t_dept on t_emp.emp_id = t_dept.dept_id where t_emp.emp_id = #{empId}
</select>
```



#### 2）使用association处理映射关系

- association标签：处理多对一的映射关系
- property属性：需要处理多对的映射关系的属性名
- javaType属性：该属性的类型

```xml
<resultMap id="empAndDeptResultMapTwo" type="Emp">
	<id property="empId" column="emp_id"></id>
	<result property="empName" column="emp_name"></result>
	<result property="age" column="age"></result>
	<result property="gender" column="gender"></result>
    <!-- association标签：处理多对一的映射关系 (处理 实体类类型 的属性)
			property属性：需要处理多对的映射关系的属性名 ( 该实体类类型属性 的属性名)
			javaType属性：该属性的类型 ( 实体类类型属性 的类型 )
	-->
	<association property="dept" javaType="Dept">
		<id property="did" column="dept_id"></id>
		<result property="deptName" column="dept_name"></result>
	</association>
</resultMap>

<!--Emp getEmpAndDept(@Param("empId")Integer empId);-->
<select id="getEmpAndDept" resultMap="empAndDeptResultMapTwo">
	select * from t_emp left join t_dept on t_emp.emp_id = t_dept.dept_id where t_emp.emp_id = #{empId}
</select>
```



#### 3）分步查询

先对**员工表查询**，查询出 指定**员工信息** 及 对应的**部门id** 

再通过**部门id** 查询出指定**部门信息**，给 **实体类中的dept属性** 赋值



##### 1. 查询员工信息 （分步查询第一步）

* property属性：需要处理多对的映射关系的 的属性名 ( 该实体类类型属性 的属性名)

- select属性：设置分布查询的sql的唯一标识（namespace.SQLId或mapper接口的全类名.方法名）

	​				就是绑定 通过部门id查询部门信息的那个 mapper接口.方法名
	​							com.atguigu.mybatis.mapper.DeptMapper（mapper接口）
	​							.getEmpAndDeptByStepTwo（.方法名）

	​				 **说白了就是 绑定分布查询的第二步** ，**获取第二步的查询结果**，**赋值给 property属性值**(需要处理多对的映				射关系的 的属性名) 

- column属性：设置分步查询的条件， 即 **分布查询第二步 的查询条件**

```java
============================== EmpMapper ============================
/**
 * 通过分步查询，员工及所对应的部门信息
 * 分步查询第一步：查询员工信息
 */
Emp getEmpAndDeptByStepOne(@Param("empId") Integer empId);
```

```xml
========================= EmpMapper.xml==============================
<resultMap id="empAndDeptByStepResultMap" type="Emp">
    <id property="empId" column="emp_id"></id>
    <result property="empName" column="emp_name"></result>
    <result property="age" column="age"></result>
    <result property="gender" column="gender"></result>

    <!-- association标签：处理多对一的映射关系 (处理 实体类类型 的属性)
                property属性：需要处理多对的映射关系的 的属性名 ( 该实体类类型属性 的属性名)
                select属性：设置分布查询的sql的唯一标识（namespace.SQLId或mapper接口的全类名.方法名）
                            就是绑定 通过部门id查询部门信息的那个 mapper接口.方法名
                                com.lhc.mybatis.mapper.DeptMapper（mapper接口）
                                .getEmpAndDeptByStepTwo（.方法名）
                column属性：设置分步查询的条件，即 分布查询第二步 的查询条件
        -->
    <association property="dept"
                 select="com.lhc.mybatis.mapper.DeptMapper.getEmpAndDeptByStepTwo"
                 column="dept_id"></association>
</resultMap>
<!--Emp getEmpAndDeptByStepOne(@Param("empId") Integer empId);-->
<select id="getEmpAndDeptByStepOne" resultMap="empAndDeptByStepResultMap">
    select * from t_emp where emp_id = #{empId}
</select>
```

##### 2. 查询部门信息（分步查询第二步）

```java
============================== DeptMapper ============================
/**
 * 通过分步查询，员工及所对应的部门信息
 * 分步查询第二步：通过deptId查询员工对应的部门信息

 */
Dept getEmpAndDeptByStepTwo(@Param("deptId") Integer deptId); //第一步中 select属性 就是绑定这个
```

```xml
========================= EmpMapper.xml==============================
<!--此处的resultMap仅是处理字段和属性的映射关系-->
<resultMap id="EmpAndDeptByStepTwoResultMap" type="Dept">
	<id property="deptId" column="dept_id"></id>
	<result property="deptName" column="dept_name"></result>
</resultMap>

<!--Dept getEmpAndDeptByStepTwo(@Param("deptId") Integer deptId);-->
<select id="getEmpAndDeptByStepTwo" resultMap="EmpAndDeptByStepTwoResultMap">
	select * from t_dept where dept_id = #{deptId}
</select>
```

**测试**

```java
@Test
public void checkLogin() {
    SqlSession sqlSession = SqlSessionUtil.getSqlSession();
    EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);
    //调用分布查询 第一步即可
    Emp emp = mapper.getEmpAndDeptByStepOne(1);
    System.out.println(emp);
    sqlSession.close();
}
```



### 8.3 处理一对多映射关系		

```java
public class Dept {
    private Integer deptId;
    private String deptName;
    private List<Emp> emps;		//部门中的员工集合
}
```



#### 1）使用collection处理映射关系

- collection：用来处理一对多的映射关系
- ofType：表示该属性对饮的集合中存储的数据的类型

```xml
<resultMap id="DeptAndEmpResultMap" type="Dept">
	<id property="deptId" column="dept_id"></id>
	<result property="deptName" column="dept_name"></result>
    <!-- collection：用来处理一对多的映射关系
			property属性：需要处理多对的映射关系的属性名 (该实体类集合类型属性 的属性名)	
			ofType：表示该属性对应的集合中存储的数据的类型 （该集合中存储数据 的类型）
	-->
	<collection property="emps" ofType="Emp">
		<id property="deptId" column="emp_id"></id>
		<result property="empName" column="emp_name"></result>
		<result property="age" column="age"></result>
		<result property="sex" column="sex"></result>
	</collection>
</resultMap>
<!--Dept getDeptAndEmp(@Param("deptId") Integer deptId);-->
<select id="getDeptAndEmp" resultMap="DeptAndEmpResultMap">
	select * from t_dept left join t_emp on t_dept.dept_id = t_emp.dept_id where t_dept.dept_id = #{deptId}
</select>
```

#### 2）分步查询

**1. 查询部门信息**

```java
/**
 * 通过分步查询，查询部门及对应的所有员工信息
 * 分步查询第一步：查询部门信息
 */
Dept getDeptAndEmpByStepOne(@Param("deptId") Integer deptId);
```

```xml
<resultMap id="DeptAndEmpByStepOneResultMap" type="Dept">
	<id property="deptId" column="dept_id"></id>
	<result property="deptName" column="dept_name"></result>
	<collection property="emps"
				select="com.atguigu.mybatis.mapper.EmpMapper.getDeptAndEmpByStepTwo"
				column="dept_id"></collection>
</resultMap>
<!--Dept getDeptAndEmpByStepOne(@Param("deptId") Integer deptId);-->
<select id="getDeptAndEmpByStepOne" resultMap="DeptAndEmpByStepOneResultMap">
	select * from t_dept where dept_id = #{deptId}
</select>
```

**2. 根据部门id查询部门中的所有员工**

```java
/**
 * 通过分步查询，查询部门及对应的所有员工信息
 * 分步查询第二步：根据部门id查询部门中的所有员工
 */
List<Emp> getDeptAndEmpByStepTwo(@Param("deptId") Integer deptId);
```

```xml
<!--List<Emp> getDeptAndEmpByStepTwo(@Param("deptId") Integer deptId);-->
<select id="getDeptAndEmpByStepTwo" resultType="Emp">
	select * from t_emp where dept_id = #{deptId}
</select>
```



### 8.4 延迟加载

[MyBatis 延迟加载，也称为**懒加载**，是指在进行**关联查询**时，按照设置**延迟规则推迟对关联对象的select查询**。延迟加载可以有效的**减少数据库压力**。

**1）延迟加载的优缺点：**

```txt
优点：
	先从单表查询，需要时再从关联表去关联查询，⼤⼤提⾼数据库性能，因为查询单表要⽐关联查询多张表速度要快。

缺点： 
	因为只有当需要⽤到数据时，才会进⾏数据库查询，这样在⼤批量数据查询时，因为查询⼯作也要消耗时间，所以可能	造成⽤户等待时间变⻓，造成⽤户体验下降。

在多表中：
	⼀对多，多对多 ：通常情况下采⽤延迟加载
	⼀对⼀，多对⼀ ：通常情况下采⽤⽴即加载
	
注意：
	延迟加载是基于嵌套查询来实现的 
```



**2）开启延迟加载：**

​	开启延迟加载就要在**核心配置文件**中设置 **开启延迟加载**的**全局配置信息**（settings标签中）：

- lazyLoadingEnabled：延迟加载的全局开关。当开启时，所有关联对象都会延迟加载  

  ​	<setting name="lazyLoadingEnabled" value="true"/>	默认为false  所以必须设置

- aggressiveLazyLoading：当开启时，**任何方法的调用**都会**加载该对象的所有属性**。 否则 每个属性会按需加载  

	​	 <setting name="aggressiveLazyLoading" value="false"/>	默认为false  所以也可以不设置

```xml
<settings>
	<!--延迟加载-->
	<setting name="lazyLoadingEnabled" value="true"/>     <!--默认为false，true为开启延迟加载-->
	<!--按需加载-->
    <setting name="aggressiveLazyLoading" value="false"/>   <!--默认为false，false为开启按需加载-->
</settings>
```



**3）使用延迟加载：**	

​	可通过 **association标签** 或 **collection标签** 中的 **fetchType属性** 设置 当前的分步查询**是否使用延迟加载****

- fetchType="lazy"						(延迟加载)
- fetchType="eager"                      (立即加载)

思考一下为什么还要设置一个使用延迟加载，直接开启了 默认使用不久完事了吗?

* 因为 **开启延迟加载**是在**全局配置信息**中设置的，是**针对于mybatis中所有的分布查询**来设置的，当我们想要其中一个分布查询 不使用延迟加载功能时，我们就可以通过 是否使用延迟加载 来控制

```xml
<resultMap id="empAndDeptByStepResultMap" type="Emp">
	<id property="eid" column="eid"></id>
	<result property="empName" column="emp_name"></result>
	<result property="age" column="age"></result>
	<result property="sex" column="sex"></result>
	<result property="email" column="email"></result>
	<association property="dept"
				 select="com.atguigu.mybatis.mapper.DeptMapper.getEmpAndDeptByStepTwo"
				 column="did"
				 fetchType="lazy"></association>
</resultMap>
```



**4）测试 开启延迟加载 和 关闭延迟加载** 

```java
 ============= 多对一 分布查询（第一步） EmpMapper ================
/**
* 通过分步查询，员工及所对应的部门信息
* 分步查询第一步：查询员工信息
*/
Emp getEmpAndDeptByStepOne(@Param("empId") Integer empId);
```

```xml
========================= EmpMapper.xml==============================
<resultMap id="empAndDeptByStepResultMap" type="Emp">
    <id property="empId" column="emp_id"></id>
    <result property="empName" column="emp_name"></result>
    <result property="age" column="age"></result>
    <result property="gender" column="gender"></result>

    <!-- association标签：处理多对一的映射关系 (处理 实体类类型 的属性)
                property属性：需要处理多对的映射关系的 的属性名 ( 该实体类类型属性 的属性名)
                select属性：设置分布查询的sql的唯一标识（namespace.SQLId或mapper接口的全类名.方法名）
                            就是绑定 通过部门id查询部门信息的那个 mapper接口.方法名
                                com.lhc.mybatis.mapper.DeptMapper（mapper接口）
                                .getEmpAndDeptByStepTwo（.方法名）
                column属性：设置分步查询的条件，即 分布查询第二步 的查询条件
        -->
    <association property="dept"
                 select="com.lhc.mybatis.mapper.DeptMapper.getEmpAndDeptByStepTwo"
                 column="dept_id"></association>
</resultMap>
<!--Emp getEmpAndDeptByStepOne(@Param("empId") Integer empId);-->
<select id="getEmpAndDeptByStepOne" resultMap="empAndDeptByStepResultMap">
    select * from t_emp where emp_id = #{empId}
</select>
```

```java
============= 多对一 分布查询（第二步）DeptMapper ================
/**
* 通过分步查询，员工及所对应的部门信息
* 分步查询第二步：通过deptId查询员工对应的部门信息
*/
Dept getEmpAndDeptByStepTwo(@Param("deptId") Integer deptId); //第一步中 select属性 就是绑定这个方法
```

```java
========================= DeptMapper.xml ==============================
<!--此处的resultMap仅是处理字段和属性的映射关系-->
<resultMap id="EmpAndDeptByStepTwoResultMap" type="Dept">
    <id property="deptId" column="dept_id"></id>
    <result property="deptName" column="dept_name"></result>
</resultMap>
	
<!--Dept getEmpAndDeptByStepTwo(@Param("deptId") Integer deptId);-->
    <select id="getEmpAndDeptByStepTwo" resultMap="EmpAndDeptByStepTwoResultMap">
    select * from t_dept where dept_id = #{deptId}
</select>
```

```java
@Test
public void checkLogin() {

    SqlSession sqlSession = SqlSessionUtil.getSqlSession();
    EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);
    //开启了延迟加载之后我们调用 查询员工及所对应的部门信息的方法（分布查询）
    Emp emp = mapper.getEmpAndDeptByStepOne(1);
    //这里我只获取emp.getEmpName() 他就会按需加载，不会去执行第二步（获取部门信息）
    System.out.println("只需获取员工的名字：" + emp.getEmpName());

    sqlSession.close();
}
```

**结果**

```java
// 没有开启延迟加载
DEBUG 08-01 19:49:27,882 ==>  Preparing: select * from t_emp where emp_id = ?  (BaseJdbcLogger.java:137) 
DEBUG 08-01 19:49:27,912 ==> Parameters: 1(Integer)  (BaseJdbcLogger.java:137) 
DEBUG 08-01 19:49:27,935 ====>  Preparing: select * from t_dept where dept_id = ?  (BaseJdbcLogger.java:137) 
DEBUG 08-01 19:49:27,935 ====> Parameters: 1(Integer)  (BaseJdbcLogger.java:137) 
DEBUG 08-01 19:49:27,937 <====      Total: 1  (BaseJdbcLogger.java:137) 
DEBUG 08-01 19:49:27,937 <==      Total: 1  (BaseJdbcLogger.java:137) 
只需获取员工的名字：张三
//开启了延迟加载
DEBUG 08-01 19:51:16,819 ==>  Preparing: select * from t_emp where emp_id = ?  (BaseJdbcLogger.java:137) 
DEBUG 08-01 19:51:16,847 ==> Parameters: 1(Integer)  (BaseJdbcLogger.java:137) 
DEBUG 08-01 19:51:16,905 <==      Total: 1  (BaseJdbcLogger.java:137) 
只需获取员工的名字：张三
```





## 9、动态SQL

- Mybatis框架的动态SQL技术是一种根据特定条件动态拼装SQL语句的功能，它存在的意义是为了解决拼接SQL语句字符串时的痛点问题

### if

* if标签可通过test属性（即传递过来的数据）的表达式进行判断，若表达式的结果为true，则标签中的内容会执行；反之标签中的内容不会执行

* 在where后面添加一个恒成立条件 1=1 , 这个恒成立条件并不会影响查询的结果 但是可以避免sql错误

```xml
<!--List<Emp> getEmpByCondition(Emp emp);-->
<select id="getEmpByCondition" resultType="Emp">
	select * from t_emp 
    where 1=1
	<if test="empName != null and empName !=''">
		and emp_name = #{empName}
	</if>
	<if test="age != null and age !=''">
		and age = #{age}
	</if>
	<if test="sex != null and sex !=''">
		and sex = #{sex}
	</if>
	<if test="email != null and email !=''">
		and email = #{email}
	</if>
</select>
```



### where

​	where和if一般结合使用：

- 若where标签中的**if条件都不满足**，则where标签没有任何功能，即**不会添加where关键字**  
- 若where标签中的**if条件满足**，则where标签会**自动添加where关键字**，
- 若where标签中的**非首if条件满足**，将**条件最前方多余的and/or去掉**  

```xml
<!--List<Emp> getEmpByCondition(Emp emp);-->
<select id="getEmpByCondition" resultType="Emp">
	select * from t_emp
	<where>
		<if test="empName != null and empName !=''">
			emp_name = #{empName}
		</if>
		<if test="age != null and age !=''">
			and age = #{age}
		</if>
		<if test="sex != null and sex !=''">
			and sex = #{sex}
		</if>
		<if test="email != null and email !=''">
			and email = #{email}
		</if>
	</where>
</select>
```



### trim

- trim用于去掉或添加标签中的内容  
- 常用属性
	- prefix：在trim标签中的内容的**前面添加某些内容**  
	- prefixOverrides：在trim标签中的内容的**前面去掉某些内容**  
	- suffix：在trim标签中的内容的**后面添加某些内容** 
	- suffixOverrides：在trim标签中的内容的**后面去掉某些内容**
- 若trim中的标签都不满足条件，则trim标签没有任何效果，也就是只剩下`select * from t_emp`

```xml
<!--List<Emp> getEmpByCondition(Emp emp);-->
<select id="getEmpByCondition" resultType="Emp">
	select * from t_emp
	<trim prefix="where" suffixOverrides="and|or">
		<if test="empName != null and empName !=''">
			emp_name = #{empName} and
		</if>
		<if test="age != null and age !=''">
			age = #{age} and
		</if>
		<if test="sex != null and sex !=''">
			sex = #{sex} or
		</if>
		<if test="email != null and email !=''">
			email = #{email}
		</if>
	</trim>
</select>
```

```java
//测试类
@Test
public void getEmpByCondition() {
	SqlSession sqlSession = SqlSessionUtils.getSqlSession();
	DynamicSQLMapper mapper = sqlSession.getMapper(DynamicSQLMapper.class);
	List<Emp> emps= mapper.getEmpByCondition(new Emp(null, "张三", null, null, null, null));
	System.out.println(emps);
}
```

![](E:\ssm笔记\Resources/trim%E6%B5%8B%E8%AF%95%E7%BB%93%E6%9E%9C.png)



插入语句中使用trim

```xml
<insert id="insertSelective" useGeneratedKeys="true" keyProperty="id" >

    insert into com_coment
    <trim prefix="(" suffix=")" suffixOverrides="," >
      domain,
      status,
      <if test="gmtCreated != null" >
        gmt_created,
      </if>
      <if test="gmtModified != null" >
        gmt_modified,
      </if>
    </trim>
    <trim prefix="values (" suffix=")" suffixOverrides="," >
      #{domain,jdbcType=INTEGER},
      #{status,jdbcType=INTEGER},
      <if test="gmtCreated != null" >
        #{gmtCreated,jdbcType=TIMESTAMP},
      </if>
      <if test="gmtModified != null" >
        #{gmtModified,jdbcType=TIMESTAMP},
      </if>
    </trim>
  </insert>
```





### choose、when、otherwise

- choose、when、otherwise 相当于 if...else if...else

	​	when 			-->		第一个为**if**、后面的为**else if**

	​	otherwise	  -->		**else**  

- when至少要有一个，otherwise至多只有一个

- 如果其中一个when 满足了，就不会继续往下执行了，即**只会执行一个 when 或者 otherwise**

```xml
<!-- java形式 -->
if(...){
	...
}else if(...){
	...
}else{
	...
}

<!-- Mapper形式 -->
<choose>
    
    <when test="...">
    	...
    </when>
    
    <when test="...">
    	...
    </when>
    
    <otherwise>
        ...
    </otherwise>
    
</choose>
```

案例演示：

```xml
<select id="getEmpByChoose" resultType="Emp">
	select * from t_emp
	<where>
		<choose>
			<when test="empName != null and empName != ''">
				emp_name = #{empName}
			</when>
			<when test="age != null and age != ''">
				age = #{age}
			</when>
			<when test="sex != null and sex != ''">
				sex = #{sex}
			</when>
			<when test="email != null and email != ''">
				email = #{email}
			</when>
			<otherwise>
				did = 1
			</otherwise>
		</choose>
	</where>
</select>
```

```java
@Test
public void getEmpByChoose() {
	SqlSession sqlSession = SqlSessionUtils.getSqlSession();
	DynamicSQLMapper mapper = sqlSession.getMapper(DynamicSQLMapper.class);
	List<Emp> emps = mapper.getEmpByChoose(new Emp(null, "张三", 23, "男", "123@qq.com", null));
	System.out.println(emps);
}

```

![](E:\ssm笔记\Resources/choose%E6%B5%8B%E8%AF%95%E7%BB%93%E6%9E%9C.png)



### foreach

- 属性：  

	- collection：设置要循环的数组或集合  
		
	- item：表示集合或数组中的每一个数据  

	- separator：设置循环体之间的分隔符，分隔符前后默认有一个空格，如` , `

	- open：设置foreach标签中的内容的开始符  

	- close：设置foreach标签中的内容的结束符

		

**批量删除**

```xml
<!--int deleteMoreByArray(Integer[] eids);-->
<delete id="deleteMoreByArray">
	delete from t_emp where eid in
	<foreach collection="eids" item="eid" separator="," open="(" close=")">
		#{eid}
	</foreach>
</delete>
```



```java
@Test
public void deleteMoreByArray() {
	SqlSession sqlSession = SqlSessionUtils.getSqlSession();
	DynamicSQLMapper mapper = sqlSession.getMapper(DynamicSQLMapper.class);
	int result = mapper.deleteMoreByArray(new Integer[]{6, 7, 8, 9});
	System.out.println(result);
}
```

![](Resources/foreach测试结果1.png)



**批量添加**

```xml
<!--int insertMoreByList(@Param("emps") List<Emp> emps);-->
<insert id="insertMoreByList">
	insert into t_emp values
	<foreach collection="emps" item="emp" separator=",">
		(null,#{emp.empName},#{emp.age},#{emp.sex},#{emp.email},null)
	</foreach>
</insert>
```



```java
@Test
public void insertMoreByList() {
	SqlSession sqlSession = SqlSessionUtils.getSqlSession();
	DynamicSQLMapper mapper = sqlSession.getMapper(DynamicSQLMapper.class);
	Emp emp1 = new Emp(null,"a",1,"男","123@321.com",null);
	Emp emp2 = new Emp(null,"b",1,"男","123@321.com",null);
	Emp emp3 = new Emp(null,"c",1,"男","123@321.com",null);
	List<Emp> emps = Arrays.asList(emp1, emp2, emp3);
	int result = mapper.insertMoreByList(emps);
	System.out.println(result);
}
```

![](Resources/foreach测试结果2.png)



### SQL片段

- sql片段，可以记录一段公共sql片段，在使用的地方通过include标签进行引入
- 声明sql片段：`<sql>`标签

```xml
<sql id="empColumns">eid,emp_name,age,sex,email</sql>

```

- 引用sql片段：`<include>`标签

```xml
<!--List<Emp> getEmpByCondition(Emp emp);-->
<select id="getEmpByCondition" resultType="Emp">
	select <include refid="empColumns"></include> from t_emp
</select>

```



## 10、MyBatis的缓存



### MyBatis的一级缓存

- 一级缓存是**SqlSession**级别的，通过同一个SqlSession查询的数据会被缓存，下次查询相同的数据，就会从缓存中直接获取，不会从数据库重新访问  
- 使一级缓存失效的四种情况：  

```
1. 不同的SqlSession对应不同的一级缓存  
2. 同一个SqlSession但是查询条件不同
3. 同一个SqlSession两次查询期间执行了任何一次增删改操作
4. 同一个SqlSession两次查询期间手动清空了缓存
```



### MyBatis的二级缓存

二级缓存是SqlSessionFactory级别，通过同一个SqlSessionFactory创建的SqlSession查询的结果会被缓存；此后若再次执行相同的查询语句，结果就会从缓存中获取  

- 二级缓存开启的条件

```
1. 在核心配置文件中，设置全局配置属性cacheEnabled="true"，默认为true，不需要设置
2. 在映射文件中设置标签<cache />
3. 二级缓存必须在SqlSession关闭或提交之后有效 （将一级缓存的数据 提交到 二级缓存中）
4. 查询的数据所转换的实体类类型必须实现序列化的接口
```

- 使二级缓存失效的情况：

	两次查询之间执行了任意的增删改，会使一级和二级缓存同时失效



### 二级缓存的相关配置

在**mapper配置文件**中添加的**cache标签**可以设置一些属性

1）eviction属性：缓存回收策略   （默认LRU）

* LRU（Least Recently Used） – 最近最少使用的：移除最长时间不被使用的对象。  
* FIFO（First in First out） – 先进先出：按对象进入缓存的顺序来移除它们。  
* SOFT – 软引用：移除基于垃圾回收器状态和软引用规则的对象。  
* WEAK – 弱引用：更积极地移除基于垃圾收集器状态和弱引用规则的对象。

2）flushInterval属性：刷新间隔，单位毫秒

​		默认情况是不设置，也就是没有刷新间隔，缓存仅仅调用语句（增删改）时刷新

3）size属性：引用数目，正整数

​		代表缓存最多可以存储多少个对象，太大容易导致内存溢出 （使用默认值即可）

4）readOnly属性：只读，true/false

- true：只读缓存

	​	会给所有调用者返回缓存对象的相同实例。因此这些对象不能被修改。这提供了很重要的**性能优势**。  

- false：读写缓存

	​	会返回缓存对象的拷贝（通过序列化）。这会**慢一些，但是安全**，因此默认是false



### MyBatis缓存查询的顺序

- 先查询二级缓存，因为二级缓存中可能会有其他程序已经查出来的数据，可以拿来直接使用  
- 如果二级缓存没有命中，再查询一级缓存  
- 如果一级缓存也没有命中，则查询数据库  
- SqlSession关闭之后，一级缓存中的数据会写入二级缓存



### 整合第三方缓存EHCache（了解）

第三方缓存属于二级缓存，mybatis二级缓存可以使用第三方缓存，

#### 添加依赖

```xml
<!-- Mybatis EHCache整合包 -->
<dependency>
	<groupId>org.mybatis.caches</groupId>
	<artifactId>mybatis-ehcache</artifactId>
	<version>1.2.1</version>
</dependency>
<!-- slf4j日志门面的一个具体实现 -->
<dependency>
	<groupId>ch.qos.logback</groupId>
	<artifactId>logback-classic</artifactId>
	<version>1.2.3</version>
</dependency>
```

#### 各个jar包的功能

| jar包名称       | 作用                            |
| --------------- | ------------------------------- |
| mybatis-ehcache | Mybatis和EHCache的整合包        |
| ehcache         | EHCache核心包                   |
| slf4j-api       | SLF4J日志门面包                 |
| logback-classic | 支持SLF4J门面接口的一个具体实现 |

#### 创建EHCache的配置文件ehcache.xml

- 名字必须叫 **ehcache.xml**

```xml
<?xml version="1.0" encoding="utf-8" ?>
<ehcache xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:noNamespaceSchemaLocation="../config/ehcache.xsd">
    <!-- 磁盘保存路径 -->
    <diskStore path="D:\atguigu\ehcache"/>
    <defaultCache
            maxElementsInMemory="1000"
            maxElementsOnDisk="10000000"
            eternal="false"
            overflowToDisk="true"
            timeToIdleSeconds="120"
            timeToLiveSeconds="120"
            diskExpiryThreadIntervalSeconds="120"
            memoryStoreEvictionPolicy="LRU">
    </defaultCache>
</ehcache>
```

#### 设置二级缓存的类型

- 在xxxMapper.xml文件中设置二级缓存类型

```xml
<cache type="org.mybatis.caches.ehcache.EhcacheCache"/>
```

#### 加入logback日志

- 存在SLF4J时，作为简易日志的log4j将失效，此时我们需要借助SLF4J的具体实现logback来打印日志。创建logback的配置文件`logback.xml`，名字固定，不可改变

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration debug="true">
    <!-- 指定日志输出的位置 -->
    <appender name="STDOUT"
              class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <!-- 日志输出的格式 -->
            <!-- 按照顺序分别是：时间、日志级别、线程名称、打印日志的类、日志主体内容、换行 -->
            <pattern>[%d{HH:mm:ss.SSS}] [%-5level] [%thread] [%logger] [%msg]%n</pattern>
        </encoder>
    </appender>
    <!-- 设置全局日志级别。日志级别按顺序分别是：DEBUG、INFO、WARN、ERROR -->
    <!-- 指定任何一个日志级别都只打印当前级别和后面级别的日志。 -->
    <root level="DEBUG">
        <!-- 指定打印日志的appender，这里通过“STDOUT”引用了前面配置的appender -->
        <appender-ref ref="STDOUT" />
    </root>
    <!-- 根据特殊需求指定局部日志级别 -->
    <logger name="映射文件的包名" level="DEBUG"/>
</configuration>
```

#### EHCache配置文件说明

| 属性名                          | 是否必须 | 作用                                                         |
| ------------------------------- | -------- | ------------------------------------------------------------ |
| maxElementsInMemory             | 是       | 在内存中缓存的element的最大数目                              |
| maxElementsOnDisk               | 是       | 在磁盘上缓存的element的最大数目，若是0表示无穷大             |
| eternal                         | 是       | 设定缓存的elements是否永远不过期。 如果为true，则缓存的数据始终有效， 如果为false那么还要根据timeToIdleSeconds、timeToLiveSeconds判断 |
| overflowToDisk                  | 是       | 设定当内存缓存溢出的时候是否将过期的element缓存到磁盘上      |
| timeToIdleSeconds               | 否       | 当缓存在EhCache中的数据前后两次访问的时间超过timeToIdleSeconds的属性取值时， 这些数据便会删除，默认值是0,也就是可闲置时间无穷大 |
| timeToLiveSeconds               | 否       | 缓存element的有效生命期，默认是0.,也就是element存活时间无穷大 |
| diskSpoolBufferSizeMB           | 否       | DiskStore(磁盘缓存)的缓存区大小。默认是30MB。每个Cache都应该有自己的一个缓冲区 |
| diskPersistent                  | 否       | 在VM重启的时候是否启用磁盘保存EhCache中的数据，默认是false   |
| diskExpiryThreadIntervalSeconds | 否       | 磁盘缓存的清理线程运行间隔，默认是120秒。每个120s， 相应的线程会进行一次EhCache中数据的清理工作 |
| memoryStoreEvictionPolicy       | 否       | 当内存缓存达到最大，有新的element加入的时候， 移除缓存中element的策略。 默认是LRU（最近最少使用），可选的有LFU（最不常使用）和FIFO（先进先出 |



## 11、MyBatis的逆向工程

- 正向工程：先创建Java实体类，由框架负责根据实体类生成数据库表。Hibernate是支持正向工程的
- 逆向工程：先创建数据库表，由框架负责根据数据库表，反向生成如下资源：  

	​	1）Java实体类  

	​	2）Mapper接口  

	​	3）Mapper映射文件

### 创建逆向工程的步骤



#### 添加依赖和插件

```xml
<dependencies>
	<!-- MyBatis核心依赖包 -->
	<dependency>
		<groupId>org.mybatis</groupId>
		<artifactId>mybatis</artifactId>
		<version>3.5.9</version>
	</dependency>
	<!-- junit测试 -->
	<dependency>
		<groupId>junit</groupId>
		<artifactId>junit</artifactId>
		<version>4.13.2</version>
		<scope>test</scope>
	</dependency>
	<!-- MySQL驱动 -->
	<dependency>
		<groupId>mysql</groupId>
		<artifactId>mysql-connector-java</artifactId>
		<version>8.0.27</version>
	</dependency>
	<!-- log4j日志 -->
	<dependency>
		<groupId>log4j</groupId>
		<artifactId>log4j</artifactId>
		<version>1.2.17</version>
	</dependency>
</dependencies>
<!-- 控制Maven在构建过程中相关配置 -->
<build>
	<!-- 构建过程中用到的插件 -->
	<plugins>
		<!-- 具体插件，逆向工程的操作是以构建过程中插件形式出现的 -->
		<plugin>
			<groupId>org.mybatis.generator</groupId>
			<artifactId>mybatis-generator-maven-plugin</artifactId>
			<version>1.3.0</version>
			<!-- 插件的依赖 -->
			<dependencies>
				<!-- 逆向工程的核心依赖 -->
				<dependency>
					<groupId>org.mybatis.generator</groupId>
					<artifactId>mybatis-generator-core</artifactId>
					<version>1.3.2</version>
				</dependency>
				<!-- 数据库连接池 -->
				<dependency>
					<groupId>com.mchange</groupId>
					<artifactId>c3p0</artifactId>
					<version>0.9.2</version>
				</dependency>
				<!-- MySQL驱动 -->
				<dependency>
					<groupId>mysql</groupId>
					<artifactId>mysql-connector-java</artifactId>
					<version>8.0.27</version>
				</dependency>
			</dependencies>
		</plugin>
	</plugins>
</build>
```

#### 创建MyBatis的核心配置文件

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <properties resource="jdbc.properties"/>
    <typeAliases>
        <package name=""/>
    </typeAliases>
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="${jdbc.driver}"/>
                <property name="url" value="${jdbc.url}"/>
                <property name="username" value="${jdbc.username}"/>
                <property name="password" value="${jdbc.password}"/>
            </dataSource>
        </environment>
    </environments>
    <mappers>
        <package name=""/>
    </mappers>
</configuration>
```

#### 创建逆向工程的配置文件

- 文件名必须是：`generatorConfig.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
<generatorConfiguration>
    <!--
    targetRuntime: 执行生成的逆向工程的版本
    MyBatis3Simple: 生成基本的CRUD（清新简洁版）
    MyBatis3: 生成带条件的CRUD（奢华尊享版）
    -->
    <context id="DB2Tables" targetRuntime="MyBatis3">
        <!-- 数据库的连接信息 -->
        <jdbcConnection driverClass="com.mysql.cj.jdbc.Driver"
                        connectionURL="jdbc:mysql://localhost:3306/ssm?serverTimezone=UTC"
                        userId="root"
                        password="lhc">
        </jdbcConnection>
        <!-- javaBean的生成策略 （实体类）
                targetPackage 向那个包生成这个实体类
                targetProject 生成到那个项目中（即上面那个包 的位置）
        -->
        <javaModelGenerator targetPackage="com.lhc.mybatis.pojo" targetProject=".\src\main\java">
            <property name="enableSubPackages" value="true" /> <!--是否可以使用子包（比如 com.lhc 会解析成 两个父子文件夹）-->
            <property name="trimStrings" value="true" /> <!-- 将数据库字段 生成实体类属性 -->
        </javaModelGenerator>
        <!-- SQL映射文件的生成策略 -->
        <sqlMapGenerator targetPackage="com.lhc.mybatis.mapper"
                         targetProject=".\src\main\resources">
            <property name="enableSubPackages" value="true" />
        </sqlMapGenerator>
        <!-- Mapper接口的生成策略 -->
        <javaClientGenerator type="XMLMAPPER"
                          targetPackage="com.lhc.mybatis.mapper" targetProject=".\src\main\java">
            <property name="enableSubPackages" value="true" />
        </javaClientGenerator>
        <!-- 逆向分析的表 -->
        <!-- tableName设置表名 设置为*号，可以对应所有表，此时不写domainObjectName -->
        <!-- domainObjectName属性指定生成出来的实体类的类名 -->
        <table tableName="t_emp" domainObjectName="Emp"/>
        <table tableName="t_dept" domainObjectName="Dept"/>
    </context>
</generatorConfiguration>
```

#### 执行MBG插件的generate目标

- 
- 
- ![](E:/MyBatis%E7%AC%94%E8%AE%B0/Resources/%E6%89%A7%E8%A1%8CMBG%E6%8F%92%E4%BB%B6%E7%9A%84generate%E7%9B%AE%E6%A0%87.png)
- 如果出现报错：`Exception getting JDBC Driver`，可能是pom.xml中，数据库驱动配置错误
- dependency中的驱动![](E:/MyBatis%E7%AC%94%E8%AE%B0/Resources/dependency%E4%B8%AD%E7%9A%84%E9%A9%B1%E5%8A%A8.png)
	- mybatis-generator-maven-plugin插件中的驱动![](E:/MyBatis%E7%AC%94%E8%AE%B0/Resources/%E6%8F%92%E4%BB%B6%E4%B8%AD%E7%9A%84%E9%A9%B1%E5%8A%A8.png)
	- 两者的驱动版本应该相同
- 执行结果![](E:/MyBatis%E7%AC%94%E8%AE%B0/Resources/%E9%80%86%E5%90%91%E6%89%A7%E8%A1%8C%E7%BB%93%E6%9E%9C.png)



### 使用逆向工程进行增删查改

```java
@Test
public void testMBG(){
    SqlSession sqlSession = SqlSessionUtil.getSqlSession();
    EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);

    //1. 根据主键id，查询单条数据
    Emp emp = mapper.selectByPrimaryKey(1);
    System.out.println(emp);

    //2. 查询所有数据
    List<Emp> list = mapper.selectByExample(null);
    System.out.println(list);

    //3. 根据条件来查询
    //3.1 创建条件对象
    EmpExample empExample = new EmpExample();
    //3.2 添加条件（QBC风格）
    // == > ( emp_name = "张三" and age >= 20 ) or( gender = "男" )
    empExample.createCriteria().andEmpNameEqualTo("张三").andAgeGreaterThanOrEqualTo(20);
    empExample.or().andGenderEqualTo("男");
    //3.3 执行
    List<Emp> list = mapper.selectByExample(empExample);
    System.out.println(list);


    //4. 普通修改 (当修改时 属性设置为null，或者不设置 就会把字段修改为 null)
    //update t_emp set emp_name = ?, age = ?, gender = ?, dept_id = ? where emp_id = ?
    Emp emp = new Emp(1,"小黑",null,"女");
    mapper.updateByPrimaryKey(emp);

    //5. 选择修改 (当修改时 属性设置为null，或者不设置 就不会字段修改字段)
    //update t_emp SET emp_name = ?, gender = ? where emp_id = ?
    Emp emp = new Emp(1,"小黑",null,"女");
    mapper.updateByPrimaryKeySelective(emp);

    //6. 普通添加 (当添加时 属性设置为null，或者不设置 就会把字段的值添加为 null)
    //insert into t_emp (emp_id, emp_name, age, gender, dept_id) values (?, ?, ?, ?, ?)
    Emp emp = new Emp(null,"小白",null,"女");
    mapper.insert(emp);

    //7. 选择性添加 (当修改时 属性设置为null，或者不设置 就不会字段添加字段值，即让他遵循本数据库的默				认值约束)
    //insert into t_emp ( emp_name, gender ) values ( ?, ? )
    Emp emp = new Emp(null,"小白",null,"女");
    mapper.insertSelective(emp);

}
```





## 12、分页插件



​	limit index, pageSize 

​	pageSize：每页显示的条数

​	pageNum：当前页的页码

​	index：当前页的起始索引，**index=(pageNum-1)*pageSize**

​	count：总记录数

​	totalPage：总页数

​	totalPage = count / pageSize; 

​	if(count % pageSize != 0){ totalPage += 1;

​	}

​	pageSize=4，pageNum=1，index=0 limit 0,4

​	pageSize=4，pageNum=3，index=8 limit 8,4

​	pageSize=4，pageNum=6，index=20 limit 8,4

​	首页 上一页 2 3 4 5 6 下一页 末页



### 分页插件使用步骤

#### 添加依赖

​	在pom.xml文件中添加依赖

```xml
<!-- https://mvnrepository.com/artifact/com.github.pagehelper/pagehelper -->
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper</artifactId>
    <version>5.1.2</version>
</dependency>
```

#### 配置分页插件

- 在MyBatis的核心配置文件（mybatis-config.xml）中配置插件

```xml
<plugins>
	<!--设置分页插件-->
	<plugin interceptor="com.github.pagehelper.PageInterceptor"></plugin>
</plugins>
```

### 分页插件的使用

#### 开启分页功能

- 在查询功能之前使用`PageHelper.startPage(int pageNum, int pageSize)`开启分页功能
- pageNum：当前页的页码  
	- pageSize：每页显示的条数

```java
@Test
public void testPage(){
    SqlSession sqlSession = SqlSessionUtil.getSqlSession();
    EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);

    //在查询功能之前 开启分页
    PageHelper.startPage(1,4); //第一页，每页四条记录
    //查询所有数据
    List<Emp> list = mapper.selectByExample(null);
    //打印所有数据
     System.out.println("-------------------------");
    list.forEach(System.out::println);
}
```

```java
DEBUG 08-02 09:17:12,715 Cache Hit Ratio [SQL_CACHE]: 0.0  (LoggingCache.java:60) 
DEBUG 08-02 09:17:12,749 ==>  Preparing: SELECT count(0) FROM t_emp  (BaseJdbcLogger.java:137) 
DEBUG 08-02 09:17:12,782 ==> Parameters:   (BaseJdbcLogger.java:137) 
DEBUG 08-02 09:17:12,805 <==      Total: 1  (BaseJdbcLogger.java:137) 
DEBUG 08-02 09:17:12,806 ==>  Preparing: select emp_id, emp_name, age, gender, dept_id from t_emp LIMIT ?  (BaseJdbcLogger.java:137) 
DEBUG 08-02 09:17:12,807 ==> Parameters: 4(Integer)  (BaseJdbcLogger.java:137) 
DEBUG 08-02 09:17:12,809 <==      Total: 4  (BaseJdbcLogger.java:137) 
-------------------------
Emp{empId=1, empName='小黑', age=null, gender='女'}
Emp{empId=2, empName='李四', age=21, gender='男'}
Emp{empId=3, empName='王五', age=15, gender='男'}
Emp{empId=4, empName='赵六', age=19, gender='男'}
```



#### 分页相关数据

##### 方法一：直接输出

```java
@Test
public void testPageHelper01() {
    SqlSession sqlSession = SqlSessionUtil.getSqlSession();
    EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);
    //1. 在查询功能之前 开启分页
    Page<Object> page = PageHelper.startPage(1, 4);
    //2. 查询所有数据
    List<Emp> emps = mapper.selectByExample(null);
    //3. 在查询功能之后，打印分页数据
    System.out.println("-------------------------");
    System.out.println(page);
    //4. 打印获取的记录
    System.out.println("-------------------------");
    emps.forEach(System.out::println);
}
```

```java
DEBUG 08-02 09:24:05,588 Cache Hit Ratio [SQL_CACHE]: 0.0  (LoggingCache.java:60) 
DEBUG 08-02 09:24:05,629 ==>  Preparing: SELECT count(0) FROM t_emp  (BaseJdbcLogger.java:137) 
DEBUG 08-02 09:24:05,659 ==> Parameters:   (BaseJdbcLogger.java:137) 
DEBUG 08-02 09:24:05,681 <==      Total: 1  (BaseJdbcLogger.java:137) 
DEBUG 08-02 09:24:05,682 ==>  Preparing: select emp_id, emp_name, age, gender, dept_id from t_emp LIMIT ?  (BaseJdbcLogger.java:137) 
DEBUG 08-02 09:24:05,683 ==> Parameters: 4(Integer)  (BaseJdbcLogger.java:137) 
DEBUG 08-02 09:24:05,685 <==      Total: 4  (BaseJdbcLogger.java:137) 
-------------------------
Page{count=true, pageNum=1, pageSize=4, startRow=0, endRow=4, total=30, pages=8, reasonable=false, pageSizeZero=false}
-------------------------
Emp{empId=1, empName='小黑', age=null, gender='女'}
Emp{empId=2, empName='李四', age=21, gender='男'}
Emp{empId=3, empName='王五', age=15, gender='男'}
Emp{empId=4, empName='赵六', age=19, gender='男'}

```



##### 方法二使用PageInfo

- 在查询获取list集合之后，使用`PageInfo<T> pageInfo = new PageInfo<>(List<T> list, intnavigatePages)`获取分页相关数据
- list：分页之后的数据  
	- navigatePages：导航分页的页码数

```java
@Test
public void testPageHelper02() {

    SqlSession sqlSession = SqlSessionUtil.getSqlSession();
    EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);

    //1. 在查询功能之前 开启分页
    PageHelper.startPage(3,4); //第3页，每页四条记录
    //2. 查询所有数据
    List<Emp> emps = mapper.selectByExample(null);
    //3. 查询功能之后，获取分页相关数据
    System.out.println("-------------------------");
    PageInfo<Emp> page = new PageInfo<>(emps,5); //（记录数集合, 导航分页的页码数）
    System.out.println(page);

}
```

```java
DEBUG 08-02 09:28:08,231 Cache Hit Ratio [SQL_CACHE]: 0.0  (LoggingCache.java:60) 
DEBUG 08-02 09:28:08,265 ==>  Preparing: SELECT count(0) FROM t_emp  (BaseJdbcLogger.java:137) 
DEBUG 08-02 09:28:08,298 ==> Parameters:   (BaseJdbcLogger.java:137) 
DEBUG 08-02 09:28:08,317 <==      Total: 1  (BaseJdbcLogger.java:137) 
DEBUG 08-02 09:28:08,321 ==>  Preparing: select emp_id, emp_name, age, gender, dept_id from t_emp LIMIT ?, ?  (BaseJdbcLogger.java:137) 
DEBUG 08-02 09:28:08,322 ==> Parameters: 8(Integer), 4(Integer)  (BaseJdbcLogger.java:137) 
DEBUG 08-02 09:28:08,323 <==      Total: 4  (BaseJdbcLogger.java:137) 
-------------------------
PageInfo{pageNum=3, pageSize=4, size=4, startRow=9, endRow=12, total=30, pages=8, list=Page{count=true, pageNum=3, pageSize=4, startRow=8, endRow=12, total=30, pages=8, reasonable=false, pageSizeZero=false}, prePage=2, nextPage=4, isFirstPage=false, isLastPage=false, hasPreviousPage=true, hasNextPage=true, navigatePages=5, navigateFirstPage=1, navigateLastPage=5, navigatepageNums=[1, 2, 3, 4, 5]}
```

- 其中list中的数据**等同于方法一**中直接输出的page数据

	list=Page{count=true, pageNum=3, pageSize=4, startRow=8, endRow=12, total=30, pages=8, reasonable=false, pageSizeZero=false}

##### 常用数据：

- pageNum：当前页的页码  
- pageSize：每页显示的条数  
- size：当前页显示的真实条数  
- total：总记录数  
- pages：总页数  
- prePage：上一页的页码  
- nextPage：下一页的页码
- isFirstPage/isLastPage：是否为第一页/最后一页  
- hasPreviousPage/hasNextPage：是否存在上一页/下一页  
- navigatePages：导航分页的页码数  
- navigatepageNums：导航分页的页码，\[1,2,3,4,5]

# 二、Spring



## 1、Spring简介



### 1.1、Spring概述

官网地址：<https://spring.io/>

Spring 是最受欢迎的企业级 Java 应用程序开发框架，数以百万的来自世界各地的开发人员使用

Spring 框架来创建性能好、易于测试、可重用的代码。

Spring 框架是一个开源的 Java 平台，它最初是由 Rod Johnson 编写的，并且于 2003 年 6 月首次在 Apache 2.0 许可下发布。

Spring 是轻量级的框架，其基础版本只有 2 MB 左右的大小。

Spring 框架的核心特性是可以用于开发任何 Java 应用程序，但是在 Java EE 平台上构建 web 应用程序是需要扩展的。 Spring 框架的目标是使 JavaEE 开发变得更容易使用，通过启用基于 POJO 编程模型来促进良好的编程实践。

```
1、Spring是轻量级的开源的JavaEE框架
2、Spring可以解决企业应用开发的复杂性
3、Spring有两个核心部分：IOC和Aop
	（1）IOC：控制反转，把创建对象过程交给Spring管理
	（2）Aop：面向切面，不修改源代码进行功能增强
4、Spring特点
	（1）方便耦合，简便开发
	（2）Aop编程支持
	（3）方便程序测试
	（4）方便和其他框架的集合
	（5）方便进行事物操作
	（6）降低API开发难度	
```



### 1.2 、Spring家族

项目列表：<https://spring.io/projects>



### 1.3  Spring Framework

Spring 基础框架，可以视为 Spring 基础设施，基本上任何其他 Spring 项目都是以 Spring Framework 为基础的。



#### 1） Spring Framework 特性

* 非侵入式：使用 Spring Framework 开发应用程序时，Spring 对应用程序本身的结构影响非常小。对领域模型可以做到零污染；对功能性组件也只需要使用几个简单的注解进行标记，完全不会  破坏原有结构，反而能将组件结构进一步简化。这就使得基于 Spring Framework 开发应用程序时结构清晰、简洁优雅。

* 控制反转：IOC——Inversion of  Control，翻转资源获取方向。把自己创建资源、向环境索取资源变成环境将资源准备好，我们享受资源注入。

* 面向切面编程：AOP——Aspect Oriented Programming，在不修改源代码的基础上增强代码功能。

* 容器：Spring IOC 是一个容器，因为它包含并且管理组件对象的生命周期。组件享受到了容器化的管理，替程序员屏蔽了组件创建过程中的大量细节，极大的降低了使用门槛，大幅度提高了开发效率。

* 组件化：Spring 实现了使用简单的组件配置组合成一个复杂的应用。在 Spring 中可以使用 XML 和 Java 注解组合这些对象。这使得我们可以基于一个个功能明确、边界清晰的组件有条不紊的搭建超大型复杂应用系统。

* 声明式：很多以前需要编写代码才能实现的功能，现在只需要声明需求即可由框架代为实现。  

* 一站式：在 IOC 和 AOP 的基础上可以整合各种企业应用的开源框架和优秀的第三方类库。而且Spring 旗下的项目已经覆盖了广泛领域，很多方面的功能性需求可以在 Spring Framework 的基础上全部使用 Spring 来实现。

	

#### 2）Spring Framework 五大功能模块 

| 功能模块                | **功能介绍**                                                |
| ----------------------- | ----------------------------------------------------------- |
| Core Container          | 核心容器，在 Spring 环境下使用任何功能都必须基于 IOC 容器。 |
| AOP&Aspects             | 面向切面编程                                                |
| Testing                 | 提供了对 junit 或 TestNG 测试框架的整合。                   |
| Data Access/Integration | 提供了对数据访问/集成的功能。                               |
| Spring MVC              | 提供了面向Web应用程序的集成功能。                           |

 

## 2、IOC



### 2.1  IOC容器



#### 1）IOC思想

IOC：Inversion of Control，翻译过来是**反转控制**。

​			把对象创建和对象之间的调用过程，交给Spring进行管理 ，降低对象之间的耦合度

**① 获取资源的传统方式**

自己做饭：买菜、洗菜、择菜、改刀、炒菜，全过程参与，费时费力，必须清楚了解资源创建整个过程  中的全部细节且熟练掌握。

在应用程序中的组件需要获取资源时，传统的方式是组件**主动**的从容器中获取所需要的资源，在这样的  模式下开发人员往往需要知道在具体容器中特定资源的获取方式，增加了学习成本，同时降低了开发效率。

**② 反转控制方式获取资源**

点外卖：下单、等、吃，省时省力，不必关心资源创建过程的所有细节。

反转控制的思想完全颠覆了应用程序组件获取资源的传统方式：反转了资源的获取方向——改由容器主动的将资源推送给需要的组件，开发人员不需要知道容器是如何创建资源对象的，只需要提供接收资源的方式即可，极大的降低了学习本，提高了开发的效率。这种行为也称为查找的**被动**形式。

**③ DI**

DI：Dependency Injection，翻译过来是**依赖注入**。

DI 是 IOC 的另一种表述方式：即组件以一些预先定义好的方式（例如：setter 方法）接受来自于容器的资源注入。相对于IOC而言，这种表述更直接。

所以结论是：**IOC 就是一种反转控制的思想， 而 DI 是对 IOC 的一种具体实现。**



#### 2）IOC 底层原理

​		xml 解析、工厂模式、反射

```java
// 第一步  xml配置文件，配置创建的对象
<bean id="user" class="com.lhc.spring.bean.User"></bean>

//第二步：调用工厂类，来获取需要的类
class UserFactory{
	
	public static User getUser() {
		//1. xml解析得到（com.lhc.spring.bean.User 字符串）
		String classValue = class属性值   
		//2. 通过反射创建对象 (通过反射创建对象，都是默认使用无参构造)
		Class clazz = Class.forName(classValue);
		return (User)clazz.newInstance();
	}

}

//由此我们也可以说明：<bean> 中的class属性值必须是拥有无参构造器的组件类，
//不能是接口类型

```



![1659440917826](E:\ssm笔记\spring\IOC底层.png)



#### 3）IOC 容器在 Spring 中的实现

IOC 思想基于 IOC 容器完成，IOC 容器底层就是**对象工厂**。IOC 容器中管理的组件也叫做 bean。在创建bean 之前，首先需要创建 IOC 容器。**Spring 提供了 IOC 容器的两种实现方式（两个接口）**：

**① BeanFactory** 

* 这是 IOC 容器的基本实现，是 Spring 内部使用的接口。**面向 Spring 本身**，不提供给开发人员使用。

* 加载配置文件时候不会创建对象，在获取对象（使用）才去创建对象   **--->懒汉式**

**② ApplicationContext**

* **BeanFactory 的子接口**，提供了**更多高级特性**。**面向 Spring 的使用者**，几乎所有场合都使用
* 加载配置文件时候 **默认** 就会把在配置文件对象进行创建    **---> 饿汉式**

	

#### 4）ApplicationContext 接口实现类

![](E:\ssm笔记\spring\OC 容器在 Spring 中的实现.png)



| **类型名**                          | **简介**                                                     |
| ----------------------------------- | ------------------------------------------------------------ |
| **ClassPathXmlApplicationContext**  | 通过读取**类路径**下的 **XML** 格式的配置文件创建 **IOC 容器**对象 |
| **FileSystemXmlApplicationContext** | 通过**文件系统路径**读取 **XML** 格式的配置文件创建 **IOC 容器**对象 |
| **ConfigurableApplicationContext**  | **ApplicationContext 的子接口**，包含一些扩展方法refresh() 和 close() ，让 ApplicationContext 具有启动、关闭和刷新上下文的能力。 |
| **WebApplicationContext**           | 专门为 Web 应用准备，基于 Web 环境创建 IOC 容器对象，并将对象引入存入 ServletContext 域中。 |



### 2.2  基于XML管理bean



**什么是Bean 管理**

bean管理指的是两个操作

* Spring创建对象

* Spring属性注入

	

**Bean管理操作的两种方式**

* 基于xml配置文件方式创建对象

* 基于xml方式注入属性

	

------------------------------------------------------------------------------------------------------------------------------【spring01_helloword】

####  实验一：入门案例



**① 创建Maven Module**

**②引入依赖** （pom.xml)

```xml
<dependencies>
	<!-- 基于Maven依赖传递性，导入spring-context依赖即可导入当前所需所有jar包 -->
	<dependency>
		<groupId>org.springframework</groupId>
		<artifactId>spring-context</artifactId>
		<version>5.3.1</version>
	</dependency>
	<!-- junit测试 -->
	<dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.12</version>
        <scope>test</scope>
	</dependency>
</dependencies>
```

**③创建类** （HelloWorld）

```java
public class HelloWorld {

    public void say(){
        System.out.println("hello spring");
    }
}
```

**⑤在Spring的配置文件中配置bean** （applicationContext.xml）

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <!--
        配置HelloWorld所对应的bean，即将HelloWorld的对象交给Spring的IOC容器管理 通过bean标签配置IOC容器所管理的bean
            属性：
            id：设置bean的唯一标识
            class：设置bean所对应类型的全类名
    -->
    <bean id="helloworld" class="com.lhc.spring.pojo.HelloWorld"></bean>
</beans>
```

**⑥创建测试类测试** 

```java
@Test
public void test() {
    //获取IOC容器
    ApplicationContext ioc = new ClassPathXmlApplicationContext("applicationContext.xml");
    //获取IOC容器中的bean
    HelloWorld helloworld = (HelloWorld) ioc.getBean("helloworld");
	helloworld.say();
}
//结果：hello spring
```

**⑦思路**

![1659449839890](E:\ssm笔记\spring\思路.png)

**⑧注意**

Spring 底层默认通过反射技术调用组件类的**无参构造器**来创建组件对象



----------------------------------------------------------------------------------------------------------------------------------【spring02_ioc_xml】

#### 实验二：获取bean 



**spring-ioc.xml**

```xml
<bean id="studentOne" class="com.lhc.spring.pojo.Student"></bean>
```

###### 方式一：根据bean的id获取

由于 id 属性指定了 bean 的唯一标识，所以根据 bean 标签的 id 属性可以精确获取到一个组件对象。

```java
@Test
public void testIOC(){
	//获取IOC容器
	ApplicationContext ioc = new ClassPathXmlApplicationContext("spring-ioc.xml");
	//获取bean
	Student bean = (Student) ioc.getBean("studentOne");
}
```

###### 方式二：根据bean的类型获取 （常用）

```java
@Test
public void testIOC(){
    ApplicationContext ioc = new ClassPathXmlApplicationContext("spring-ioc.xml");
    Student bean = ioc.getBean(Student.class);
}
```

**注意**：当根据bean的类型获取bean时，要求IOC容器中**有且只能有一个**类型匹配的bean

```xml
<!-- 当IOC容器中一共配置了两个： -->
<bean id="studentOne" class="com.lhc.spring.pojo.Student"></bean>
<bean id="studentTwo" class="com.lhc.spring.pojo.Student"></bean>
<!-- 会出现异常:NoUniqueBeanDefinitionException -->
```

###### 方式三：根据bean的 id和类型 获取

```java
@Test
public void testIOC(){
	ApplicationContext ioc = new ClassPathXmlApplicationContext("spring-ioc.xml");
	Student bean = ioc.getBean("studentOne",Student.class);
}
// 这种方式就不怕 IOC容器中有相同类型匹配的bean
```

###### 扩展

如果组件类实现了接口，根据接口类型可以获取 bean 吗？ 

* 可以，**前提是bean类型唯一**

如果一个接口有多个实现类，这些实现类都配置了 bean，根据接口类型可以获取 bean 吗？ 

* 不行，因为bean不唯一

```java
// 根据bean的 上级接口、父类 获取bean
@Test
public void testIOC(){
    ApplicationContext ioc = new ClassPathXmlApplicationContext("spring-ioc.xml");
    Person bean = ioc.getBean(Person.class); //Student是Person接口的实现类
}
```

###### 结论

根据类型来获取bean时，在满足bean唯一性的前提下，其实只是看：『对象 **instanceof** 指定的类型』的返回结果，只要返回的是true就可以认定为和类型匹配，能够获取到。



#### 实验三：依赖注入 - setter注入



**创建学生类Student**

```java
public class Student {
    private Integer sid;
    private String sname;
    private Integer age;
    private String gender;
    
    //无参、有参 构造器
    //set ger 方法
    //toString 方法
}
```

**配置bean时为属性赋值**

```xml
<bean id="studentTwo" class="com.lhc.spring.pojo.Student">
    <!--
		property标签：通过组件类的setXxx()方法给组件对象设置属性
             name属性：指定属性名（这个属性名是getXxx()、setXxx()方法定义的，和成员变量无关）
             value属性：指定属性值
     -->
    <property name="sid" value="1001"></property>
    <property name="sname" value="张三"></property>
    <property name="age" value="18"></property>
    <property name="gender" value="男"></property>
</bean>
```

**测试**

```java
@Test
public void testDI01(){
    //获取IOC容器
    ApplicationContext ioc = new ClassPathXmlApplicationContext("spring-ioc.xml");
    //获取bean
    Person bean = ioc.getBean("studentTwo", Student.class);
    System.out.println(bean);
}
// Student{sid=1001, sname='张三', age=18, gender='男'}
```



#### 实验四：依赖注入 - 构造器注入



**在Student类中添加有参构造**

```java
public Student(Integer sid, String sname, Integer age, String gender) {
    this.sid = sid;
    this.sname = sname;
    this.age = age;
    this.gender = gender;
}	
```

**配置bean**

```xml
<bean id="studentThree" class="com.lhc.spring.pojo.Student">
    <!-- 使用有参构造器注入： 
		属性赋值的书写顺序 要和 组件类有参构造器内参数的顺序一致，
 		顺序不一致 可能导致 赋值错位，或者 报错（类型不匹配）
	-->
    <constructor-arg value="1002"></constructor-arg>
    <constructor-arg value="李四"></constructor-arg>
    <constructor-arg value="33"></constructor-arg>
    <constructor-arg value="女"></constructor-arg>
</bean>

```

**注意**

```java
/*
注意：
	constructor-arg标签还有两个属性可以进一步描述构造器参数：
		index属性：指定参数所在位置的索引（从0开始）
		name属性：指定参数名
应用场景：
	当组件类中，有俩个构造器，且里面的 参数不一致 但参数类型和顺序一致时，就需要使用上面两个属性，去准确的标注给那个参数赋值（防止他选错构造器赋值）
*/

//例题：

//1）如一个组件类中有以下两个构造器：（共同点：就是参数类型，顺序一致 ；不同点：参数不同）
public Student(Integer sid, String sname) {
	this.sid = sid;
	this.sname = sname;
}

public Student(Integer age, String gender) {
	this.age = age;
	this.gender = gender;
}

//2）我们使用 bean 构造器方式注入
<bean id="studentThree" class="com.lhc.spring.pojo.Student">
    <!-- 这里 属性赋值的书写顺序 要和 实体类构造器内属性的顺序一致 -->
    <constructor-arg value="1002"></constructor-arg>
    <constructor-arg value="李四"></constructor-arg>
</bean>

// 现在你知道 1002 和 李四 是调用那个构造器去赋值吗？ 不知道！！！
// 他有可能将 1002 和 李四 赋值给了 age 和 gender

//3）此时我们就需要使用 constructor-arg标签下的 index属性 和name属性
<bean id="studentThree" class="com.lhc.spring.pojo.Student">
    <constructor-arg value="1002" name="sid"></constructor-arg>
    <constructor-arg value="李四" name="sname"></constructor-arg>
</bean>

```

**测试**

```java
@Test
public void testDI02(){
    //获取IOC容器
    ApplicationContext ioc = new ClassPathXmlApplicationContext("spring-ioc.xml");
    //获取bean
    Person bean = ioc.getBean("studentThree",Student.class);
    System.out.println(bean);
}
//Student{sid=1002, sname='李四', age=33, gender='女'}
```



#### 实验五：特殊值处理



###### 字面量赋值

* 什么是字面量？

​		int a = 10;

* 声明一个变量a，初始化为10，此时a就不代表字母a了，而是作为一个变量的名字。当我们引用a 的时候，我们实际上拿到的值是10。

* 而如果a是带引号的：'a'，那么它现在不是一个变量，它就是代表a这个字母本身，这就是字面量。所以字面量没有引申含义，就是我们看到的这个数据本身。

```xml
<!-- 使用value属性给bean的属性赋值时，Spring会把value属性的值看做字面量 -->
<property name="name" value="张三"/>
```

###### null值

```xml

<property name="age">
	<null />
</property>

<!--
	这种写法，为name所赋的值是 字符串null
	<property name="name" value="null"></property>
-->
```

###### xml实体

```xml
<!-- 小于号在XML文档中用来定义标签的开始，不能随便使用 -->
<!-- 解决方案一：使用XML实体来代替 -->
<property name="expression" value="a &lt; b"/>
```

###### CDATA节

```xml
<property name="expression">
	<!-- 解决方案二：使用CDATA节 -->
	<!-- CDATA中的C代表Character，是文本、字符的含义，CDATA就表示纯文本数据 -->
	<!-- XML解析器看到CDATA节就知道这里是纯文本，就不会当作XML标签或属性来解析 -->
	<!-- 所以CDATA节中写什么符号都随意 -->
	<value><![CDATA[a < b]]></value>
</property>
```



#### 实验六：为类类型属性赋值

   

创建班级类Clazz

```java
public class Clazz {
	private Integer clazzId;	
	private String clazzName;
    
    //无参、有参 构造器
    //set ger 方法
    //toString 方法
}	
```

修改Student类

```java
//在Student类中添加 clazz属性
private Clazz clazz;

//set、get 方法 重写toString
```



###### 方式一：引用外部已声明的bean

配置Clazz类型的bean：

```xml
<bean id="clazzOne" class="com.lhc.spring.pojo.Clazz">
    <property name="clazzId" value="1111"></property>
    <property name="clazzName" value="无敌暴龙班"></property>
</bean>
```

为Student中的clazz属性赋值：

```xml
<bean id="studentFour" class="com.lhc.spring.pojo.Student">
    <property name="sid" value="1004"></property>
    <property name="sname" value="赵六"></property>
    <property name="age" value="26"></property>
    <property name="gender" value="女"></property>
    <!-- ref属性：引用IOC容器中某个bean的id，将所对应的bean为属性赋值 -->
    <property name="clazz" ref="clazzOne"></property>
</bean>
```

错误演示：

````xml
<bean id="studentFour" class="com.lhc.spring.pojo.Student">
    <property name="sid" value="1004"></property>
    <property name="sname" value="赵六"></property>
    <property name="age" value="26"></property>
    <property name="gender" value="女"></property>
	<property name="clazz" value="clazzOne"></property>
</bean>

<!--
如果错把ref属性写成了value属性，会抛出异常： 
	Caused by: java.lang.IllegalStateException:Cannot convert value of type 'java.lang.String' to required type'com.atguigu.spring.bean.Clazz' for property 'clazz': no matching editors or 		  conversion strategy found

意思是不能把String类型转换成我们要的Clazz类型，说明我们使用value属性时，Spring只把这个属性看做一个普通的字符串，不会认为这是一个bean的id，更不会根据它去找到bean来赋值
-->
````



###### 方式二：内部bean

```xml
<bean id="studentFour" class="com.lhc.spring.pojo.Student">
    <property name="sid" value="1004"></property>
    <property name="sname" value="赵六"></property>
    <property name="age" value="26"></property>
    <property name="gender" value="女"></property>
	<property name="clazz">
            <!-- 在一个bean中再声明一个bean就是内部bean -->
            <!-- 内部bean只能用于给属性赋值，不能在外部通过IOC容器获取，因此可以省略id属性 -->
            <bean id="clazzInner" class="com.lhc.spring.pojo.Clazz">
                <property name="clazzId" value="2222"></property>
                <property name="clazzName" value="士兵突击班"></property>
            </bean>
        </property>
</bean>
```



###### 方式三：级联属性赋值

```xml
<bean id="studentFour" class="com.lhc.spring.pojo.Student">
    <property name="sid" value="1004"></property>
    <property name="sname" value="赵六"></property>
    <property name="age" value="26"></property>
    <property name="gender" value="女"></property>
    <!-- 一定先引用某个bean为属性赋值，才可以使用级联方式 更新属性 -->
    <property name="clazz" ref="clazzOne"></property>
    <property name="clazz.clazzId" value="3333"></property>
    <property name="clazz.clazzName" value="最强王者班"></property>
</bean>
```



###### 注意：

​		类类型，也包括接口类型，比如我们在学 javaweb 三层架构的时候

​		Service里面拥有dao接口类型的属性，Controller里面拥有service接口类型的属性

```java
// UserController中拥有 userService接口类型属性
public class UserController {
    private UserService userService = null;
    ...
}
//UserServiceImpl中拥有 userDAO接口类型属性
public class UserServiceImpl implements UserService {
    private UserDAO userDAO = null;
}
```

```xml
<!--dao-->
<bean id="userDAO" class="com.atguigu.book.dao.impl.UserDAOImpl"/>

<!--service-->
<bean id="userService" class="com.atguigu.book.service.impl.UserServiceImpl">
	<property name="userDAO" ref="userDAO" />
</bean>

<!--controller-->
<bean id="user" class="com.atguigu.book.controller.UserController">
	<property name="userService" ref="userService" />
</bean>
```





#### 实验七：为数组类型属性赋值

修改Student类

```java
//添加 hobbies 属性

private String[] hobbies;

//set、get方法 toString方法
```

配置bean

```xml
<bean id="studentFour" class="com.atguigu.spring.bean.Student">
    <property name="id" value="1004"></property>
    <property name="name" value="赵六"></property>
    <property name="age" value="26"></property>
    <property name="sex" value="女"></property>
    <property name="clazz" ref="clazzOne"></property>
    <!-- 为数组类型属性赋值 -->
    <property name="hobbies">
        <array>
            <!--value：为字面量类型的属性赋值，ref：为类类型的属性赋值 -->
            <value>抽烟</value>
            <value>喝酒</value>
            <value>烫头</value>
        </array>
	</property>
</bean>
```



#### 实验八：为集合类型属性赋值



##### 1）为List集合类型属性赋值

① 在Clazz类中添加属性

```java

private List<Student> students;

//set、get 方法 重写toString
```

② 配置bean：

###### 方式一：**在list标签内赋值**

```xml
<bean id="clazzTwo" class="com.lhc.spring.pojo.Clazz">
    <property name="clazzId" value="4444"></property>
    <property name="clazzName" value="Javaee0222"></property>
    <!--为list集合类型属性赋值（如果为set集合类型，直接将标签改为set即可）-->
    <property name="students">
        <list>
            <!--value：为字面量类型的属性赋值，ref：为类类型的属性赋值 -->
            <ref bean="studentOne"></ref>
            <ref bean="studentTwo"></ref>
            <ref bean="studentThree"></ref>
        </list>
    </property>
</bean>
```

###### 方式二：引用list集合类型的bean

```xml
<bean id="clazzTwo" class="com.lhc.spring.pojo.Clazz">
    <property name="clazzId" value="4444"></property>
    <property name="clazzName" value="Javaee0222"></property>
    <!--引用list集合类型的bean-->
    <property name="students" ref="studentList"></property>   
</bean>

<!-- 配置一个list集合类型的bean，需要使用util的约束 -->
<util:list id="studentList">
	<!-- value：为字面量类型的属性赋值，ref：为类类型的属性赋值 -->
    <ref bean="studentOne"></ref>
    <ref bean="studentTwo"></ref>
    <ref bean="studentThree"></ref>
</util:list>
```



##### 2）为Map集合类型属性赋值

① 创建教师类Teacher：

```java
public class Teacher {
    
	private Integer teacherId;
	private String teacherName;

    //无参、有参 构造器
    //set ger 方法
    //toString 方法
}
```

② 在Student类中添加以下代码：

```java
// 添加 teacherMap属性
private Map<String, Teacher> teacherMap;

//set、get 方法 重写toString
```

③ 配置文件添加 两个teacher组件bean

```xml
<bean id="teacherOne" class="com.lhc.spring.pojo.Teacher">
    <property name="teacherId" value="10010"></property>
    <property name="teacherName" value="大宝"></property>
</bean>

<bean id="teacherTwo" class="com.lhc.spring.pojo.Teacher">
    <property name="teacherId" value="10086"></property>
    <property name="teacherName" value="二宝"></property>
</bean>

```

④ 配置bean

###### 方式一：在map标签内赋值

```xml
<bean id="studentFive" class="com.lhc.spring.pojo.Student">
    <property name="sid" value="1005"></property>
    <property name="sname" value="赵六"></property>
    <property name="age" value="26"></property>
    <property name="gender" value="女"></property>
    <property name="clazz" ref="clazzOne"></property>
    <property name="hobbies">
        <array>
            <value>抽烟</value>
            <value>喝酒</value>
            <value>烫头</value>
        </array>
    </property>
    
    <!--为Map属性赋值 -->
    <property name="teacherMap">
        <map>
            <entry>
                <key>
                    <value>10010</value>
                </key>
                <ref bean="teacherOne"></ref>
            </entry>
            <entry>
                <key>
                    <value>10086</value>
                </key>
                <ref bean="teacherTwo"></ref>
            </entry>
        </map>
    </property>
    
</bean>
```

###### 方式二：引用map集合类型的bean

```xml
<bean id="studentFive" class="com.lhc.spring.pojo.Student">
    <property name="sid" value="1005"></property>
    <property name="sname" value="赵六"></property>
    <property name="age" value="26"></property>
    <property name="gender" value="女"></property>
    <property name="clazz" ref="clazzOne"></property>
    <property name="hobbies">
        <array>
            <value>抽烟</value>
            <value>喝酒</value>
            <value>烫头</value>
        </array>
    </property>
    <!--引用map集合类型的bean-->
    <property name="teacherMap" ref="teacherMap"></property>
</bean>

<!--配置一个map集合类型的bean 需要使用util的约束-->
<util:map id="teacherMap">
	<entry>
		<key>
			<value>10010</value>
		</key>
		<ref bean="teacherOne"></ref>
    </entry>
    <entry>
		<key>
			<value>10086</value>
		</key>
		<ref bean="teacherTwo"></ref>
	</entry>  
</util:map>
```



#### 实验九：p命名空间

引入p命名空间后，可以通过以下方式为bean的各个属性赋值

```xml
 <!--使用p命名空间，给属性赋值-->
<bean id="studentSex" class="com.lhc.spring.pojo.Student"
      p:sid="1005" p:sname="小明" p:clazz-ref="clazzOne" p:teacherMap-ref="teacherMap">
</bean>
```



#### 实验十：引入外部属性文件

1）加入依赖 (**pom.xml**)

```xml
<!-- MySQL驱动 -->
<dependency>
	<groupId>mysql</groupId>
	<artifactId>mysql-connector-java</artifactId>
	<version>8.0.16</version>
</dependency>
<!-- 数据源 -->
<dependency>
	<groupId>com.alibaba</groupId>
	<artifactId>druid</artifactId>
	<version>1.0.31</version>
</dependency>
```

2）创建外部属性文件 **jdbc.properties**

```properties
jdbc.driver=com.mysql.cj.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/ssm?serverTimezone=UTC 
jdbc.user=root 
jdbc.password=lhc
```

3）引入属性文件  (**spring-datasource.xml**)

```xml
<!--引入jdbc.properties,之后可以通过${key}的方式访问value-->
<context:property-placeholder location="jdbc.properties"></context:property-placeholder>
```

4）配置bean (**spring-datasource.xml**)

```xml
<!-- class 为Datasource的实现类DruidDataSource-->
<bean id="datasource" class="com.alibaba.druid.pool.DruidDataSource">
    <property name="driverClassName" value="${jdbc.driver}"></property>
    <property name="url" value="${jdbc.url}"></property>
    <property name="username" value="${jdbc.username}"></property>
    <property name="password" value="${jdbc.password}"></property>
</bean>
```

5）测试 

```java
@Test
public void testDataSource() throws SQLException {
    ApplicationContext ioc = new ClassPathXmlApplicationContext("spring- datasource.xml");
    DataSource dataSource = ioc.getBean(DataSource.class); 
    Connection connection = dataSource.getConnection();  
    System.out.println(connection);
}
```



#### 实验十一：bean的作用域

**1）概念**

在Spring中可以通过配置bean标签的scope属性来指定bean的作用域范围，各取值含义参加下表：

| **取值**          | **含义**                                | **创建对象的时机** |
| ----------------- | --------------------------------------- | ------------------ |
| singleton（默认） | 在IOC容器中，这个bean的对象始终为单实例 | IOC容器初始化时    |
| prototype         | 这个bean在IOC容器中有多个实例           | 获取bean时         |

如果是在WebApplicationContext环境下还会有另外两个作用域（但不常用）：

| **取值** | **含义**             |
| -------- | -------------------- |
| request  | 在一个请求范围内有效 |
| session  | 在一个会话范围内有效 |



**2）创建类User**

```java
public class User {

    private Integer id;
    private String username;
    private String password;
    private Integer age;
    
    //有参、无参构造器，set、get方法，toString方法
    
}
```

**3）配置bean**

```xml
<!--  scope属性：
		singleton（默认值），bean在IOC容器中只有一个实例，IOC容器初始化时创建对象
		prototype，bean在IOC容器中可以有多个实例，getBean()时创建对象 
-->
<bean class="com.atguigu.bean.User" scope="prototype"></bean>
```

**4）测试**

```java
//测试 两次从ioc中获取user对象，是否为同一个对象
@Test
public void testBeanScope(){
	ApplicationContext ioc = new ClassPathXmlApplicationContext("spring-scope.xml");
	User user1 = ioc.getBean(User.class); 
    User user2 = ioc.getBean(User.class); 						
    System.out.println(user1==user2);
}
```



#### 实验十二：bean的生命周期



**1）具体的生命周期过程**

① **bean对象创建（调用无参构造器）** 

② **给bean对象设置属性 依赖注入（调用set方法）**

③ bean对象初始化之前操作（由bean的后置处理器负责） 

④ **bean对象初始化（需在配置bean时指定初始化方法）**

⑤ bean对象初始化之后操作（由bean的后置处理器负责）

⑥ bean对象就绪可以使用

**⑦ bean对象销毁（需在配置bean时指定销毁方法）** 

**⑧ IOC容器关闭**



注意：**bean的作用域不同，bean的生命周期也不同**

* 当我们的bean设置为**单例**：bean的生命周期（创建bean对象、依赖注入、初始化） 在获取ioc容器时完成

	 bean对象销毁 在ioc容器关闭时完成 

* 当我们的bean设置为**多例**：bean的生命周期（创建bean对象、依赖注入、初始化）在获取bean对象时完成

	ioc容器关闭  不会让bean对象销毁



**2）修改类User**

```java
//无参构造器
public User() {
    System.out.println("生命周期1：实例化");
}

//set方法
public void setId(Integer id) {
    System.out.println("生命周期2：依赖注入");
    this.id = id;
}

//初始化
public void initMethod(){
    System.out.println("生命周期3：初始化");
}

//销毁
public void destroyMethod(){
    System.out.println("生命周期4：销毁");
}
```

**3）配置bean**

```xml
<!-- 使用init-method属性指定初始化方法 -->
<!-- 使用destroy-method属性指定销毁方法 -->
<bean class="com.lhc.spring.pojo.User"  init-method="initMethod" destroy-method="destroyMethod">
    
        <property name="id" value="1001"></property>
        <property name="username" value="admin"></property>
        <property name="password" value="123456"></property>
        <property name="age" value="23"></property>
    
</bean>
```

**4）测试**

```java
@Test
public void test(){
    //ApplicationContext没有提供 关闭和刷新容器的方法，
    //ApplicationContext ioc = new ClassPathXmlApplicationContext("spring-lifecycle.xml");
    //他的子接口ConfigurableApplicationContext 以及实现类有：
    ClassPathXmlApplicationContext ioc = new ClassPathXmlApplicationContext("spring-lifecycle.xml");
    User bean = ioc.getBean(User.class);
    ioc.close();
}

/*
输出：（bean设置为多例时没有 销毁）
生命周期1：实例化
生命周期2：依赖注入
生命周期3：初始化
生命周期4：销毁
*/
```



**5）bean的后置处理器**

* bean的后置处理器会在生命周期的**初始化前后**添加额外的操作
* 需要实现 **BeanPostProcessor**接口， 且配置到**IOC**容器中
* 需要注意的是，bean后置处理器不是单独针对某一个bean生效，而是**针对IOC容器中所有bean都会执行**

① 创建bean的后置处理器：

```java
public class MyBeanPostProcessor implements BeanPostProcessor {


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        
        System.out.println("MyBeanPostProcessor-->后置处理器的postProcessBeforeInitialization");
        System.out.println("☆☆☆" + beanName + " = " + bean);
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
        
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        
        System.out.println("MyBeanPostProcessor->后置处理器的postProcessAfterInitialization");
        System.out.println("★★★" + beanName + " = " + bean);
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
        
    }
}
```

② 在IOC容器中配置后置处理器：

```XML
<!-- bean的后置处理器要放入IOC容器才能生效 -->
<bean id="MyBeanPostProcessor" class="com.lhc.spring.process.MyBeanPostProcessor"/>
```

③ 测试：

```java
@Test
public void test(){
    ClassPathXmlApplicationContext ioc = new ClassPathXmlApplicationContext("spring-lifecycle.xml");
    User bean = ioc.getBean(User.class);
    ioc.close();
}

/*

输出：

生命周期1：实例化
生命周期2：依赖注入
MyBeanPostProcessor-->后置处理器的postProcessBeforeInitialization
☆☆☆com.lhc.spring.pojo.User#0 = User{id=1001, username='admin', password='123456', age=23}
生命周期3：初始化
MyBeanPostProcessor->后置处理器的postProcessAfterInitialization
★★★com.lhc.spring.pojo.User#0 = User{id=1001, username='admin', password='123456', age=23}
User{id=1001, username='admin', password='123456', age=23}
生命周期4：销毁

*/
```



#### 实验十三：FactoryBean

**简介**

FactoryBean是Spring提供的一种整合第三方框架的常用机制。和普通的bean不同，配置一个 FactoryBean类型的bean，在**获取bean**的时候得到的并不是class属性中配置的这个类的对象，而是 **getObject()方法的返回值**。通过这种机制，Spring可以帮我们把复杂组件创建的详细过程和繁琐细节都屏蔽起来，只把最简洁的使用界面展示给我们。

将来我们整合Mybatis时，Spring就是通过FactoryBean机制来帮我们创建SqlSessionFactory对象的。

```JAVA
public interface FactoryBean<T> {
    
    String OBJECT_TYPE_ATTRIBUTE = "factoryBeanObjectType";

    //提供一个对象交给IOC管理
    @Nullable
    T getObject() throws Exception;

    //设置所提供对象的类型
    @Nullable
    Class<?> getObjectType();

    //设置对象否为单例
    default boolean isSingleton() {
        return true; 	//默认为单例
    }
    
}
```



1）创建类UserFactoryBean

```java
public class UserFactoryBean implements FactoryBean<User> { 
    
    @Override
	public User getObject() throws Exception { 
        return new User();
	}

	@Override
	public Class<?> getObjectType() { 
        return User.class;
	}
    
}
```

2）配置bean

```xml
<!-- 将userfactorybean中的 getObject方法返回值（ new User() ）交给IOC管理 -->
<bean id="user" class="com.lhc.spring.factory.UserFactoryBean"></bean>  
```

3）测试

```java
@Test
public void testUserFactoryBean(){
	//获取IOC容器
	ApplicationContext ioc = new ClassPathXmlApplicationContext("spring-factory.xml");
    //获取bean
	User user = (User) ioc.getBean("user"); 
    System.out.println(user);
}
```



#### 实验十四：基于xml的自动装配

**自动装配：**

​	根据指定的策略，在IOC容器中匹配某一个bean，自动为指定的bean中所依赖的类类型或接口类型属性赋值

**场景模拟** （ 模拟一下 **三层架构**）

① 创建类UserController

```java
public class UserController {

    // UserService 属性
	private UserService userService;

    //set方法
	public void setUserService(UserService userService) { 
        this.userService = userService;
	}

    //业务方法
	public void saveUser(){ 
        userService.saveUser();
	}

}
```

② 创建接口UserService

```JAVA
public interface UserService {
	void saveUser();
}
```

③ 创建类UserServiceImpl实现接口UserService

```JAVA
public class UserServiceImpl implements UserService {

    // UserDao 属性
	private UserDao userDao;

    // set方法
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

    // 业务方法
	@Override
	public void saveUser() { 
        userDao.saveUser();
	}
    
}
```

④ 创建接口UserDao

```java
public interface UserDao {
	void saveUser();
}
```

⑤ 创建类UserDaoImpl实现接口UserDao

```java
public class UserDaoImpl implements UserDao {

    //业务方法
	@Override
	public void saveUser() {
		System.out.println("保存成功");
	}

}
```

⑥ 手动装配 (手动依赖注入)：

```xml
<bean id="userDao" class="com.lhc.spring.dao.impl.UserDaoImpl"></bean>

<bean id="userService" class="com.lhc.spring.service.impl.UserServiceImpl">
	<property name="userDao" ref="userDao"></property>
</bean>

<bean id="userController" class="com.lhc.spring.controller.UserController">
	<property name="userService" ref="userService"></property>
</bean>
```

⑦ 测试

```java
@Test
public void test() {
	ApplicationContext ioc = new ClassPathXmlApplicationContext("spring-autowire.xml");
	UserController userController = ioc.getBean(UserController.class);
	userController.saveUser();  //保存成功！
}
```



**自动装配**

① 使用bean标签的autowire属性设置自动装配效果自动装配方式：byType

​	byType：根据类型匹配IOC容器中的某个兼容类型的bean，为属性自动赋值

* 若在IOC中，没有任何一个兼容类型的bean能够为属性赋值，则该属性不装配，即值为默认值 null

* 若在IOC中，有多个兼容类型的bean能够为属性赋值，则抛出异常 NoUniqueBeanDefinitionException

```xml
<bean id="userController" class="com.atguigu.autowire.xml.controller.UserController" autowire="byType"></bean>

<bean id="userService" class="com.atguigu.autowire.xml.service.impl.UserServiceImpl" autowire="byType"></bean>

<bean id="userDao" class="com.atguigu.autowire.xml.dao.impl.UserDaoImpl"></bean>
```

② 使用bean标签的autowire属性设置自动装配效果自动装配方式：byName

​	byName：将自动装配的属性的属性名，作为bean的id在IOC容器中匹配相对应的bean进行赋值

```xml
<bean id="userController" class="com.atguigu.autowire.xml.controller.UserController" autowire="byName"></bean>

<bean id="userService" class="com.atguigu.autowire.xml.service.impl.UserServiceImpl" autowire="byName"></bean>

<bean id="userDao" class="com.atguigu.autowire.xml.dao.impl.UserDaoImpl"></bean>
```

**测试**

```java
@Test
public void test() {
	ApplicationContext ioc = new ClassPathXmlApplicationContext("spring-autowire.xml");
	UserController userController = ioc.getBean(UserController.class);
	userController.saveUser();  //保存成功！
}
```



### 2.3  基于注解管理bean

#### 实验一：标记与扫描

**①注解**

和 XML 配置文件一样，注解本身并不能执行，注解本身**仅仅只是做一个标记**，具体的功能是框架检测到注解标记的位置，然后针对这个位置按照注解标记的功能来执行具体操作。

本质上：所有一切的操作都是Java代码来完成的，XML和注解只是告诉框架中的Java代码如何执行。

**②扫描**

Spring 为了知道程序员在哪些地方标记了什么注解，就需要通过扫描的方式，来进行检测。然后根据注解进行后续操作。

**③新建Maven Module**

```xml
<dependencies>

    <!-- 基于Maven依赖传递性，导入spring-context依赖即可导入当前所需所有jar包 -->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>5.3.1</version>
    </dependency>

    <!-- junit测试 -->
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.12</version>
        <scope>test</scope>
    </dependency>

</dependencies>
```



**④创建Spring配置文件**

​	**标识组件的常用注解**

​	@Component：将类标识为普通组件  

​	@Controller：将类标识为控制层组件  

​	@Service：将类标识为业务层组件 

​	@Repository：将类标识为持久层组件



@Controller、@Service、@Repository 这三个注解只是在@Component注解 的基础上起了三个新的名字。对于Spring使用IOC容器管理这些组件来说没有区别。只是给开发人员看的，让我们能够便于分辨组件。

注意：虽然它们本质上一样，但是为了代码的可读性，为了程序结构严谨我们肯定不能随便胡乱标记。



**⑥创建组件**

创建控制层组件

```java
@Controller
public class UserController {

}
```

创建接口UserService

```java
public interface UserService {

}
```

创建业务层组件UserServiceImpl

```java
@Service
public class UserServiceImpl implements UserService {

}
```

创建接口UserDao

```java
public interface UserDao {

}
```

创建持久层组件UserDaoImpl

```java
@Repository
public class UserDaoImpl implements UserDao {

}
```



**⑦扫描组件**

情况一：最基本的扫描方式

```xml
<!-- 扫描 com.lhc.spring包下的所有组件 -->
<context:component-scan base-package="com.lhc.spring"></context:component-scan>
```

情况二：指定要**排除**的组件

```xml

<context:component-scan base-package="com.lhc.spring">
<!-- 
	context:exclude-filter标签：指定排除规则 
		type：设置排除的依据
			annotation：根据注解排除，expression中设置要 排除的 注解的全类名
			assignable：根据类型排除，expression中设置要 排除的 类型的全类名
-->
	<context:exclude-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
	<!--<context:exclude-filter type="assignable" expression="com.lhc.spring.controller.UserController"/>-->
</context:component-scan>
```

情况三：**仅扫描**指定组件

```xml
<!-- 
		use-default-filters属性：
			true  扫描指定包下所有类
			false 都不扫描 （设为false后 ，在里面指定要扫描那个组件）
-->
<context:component-scan base-package="com.lhc.spring" use-default-filters="false">
<!-- 
	context:include-filter标签：指定在原有扫描规则的基础上追加的规则 
		( 此时必须设置use-default-filters="false"，让他不扫描 )

		type：设置包含的依据
			annotation，根据注解追加，expression中设置要追加的 注解的全类名
			assignable，根据类型追加，expression中设置要追加的 类型的全类名
-->
<context:include-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
<!--<context:include-filter type="assignable" 
									expression="com.lhc.spring.controller.UserController"/>-->
</context:component-scan>
```

**⑧测试**

```java
@Test
public void testAutowireByAnnotation(){ 
    ApplicationContext ioc = new ClassPathXmlApplicationContext("applicationContext.xml"); 
    UserController userController = ioc.getBean(UserController.class);
    System.out.println(userController);
    UserService userService = ioc.getBean(UserService.class); 
    System.out.println(userService);
    UserDao userDao = ioc.getBean(UserDao.class); 
    System.out.println(userDao);
}
```

**⑨组件所对应的bean的id**

在我们使用XML方式管理bean的时候，每个bean都有一个唯一标识，便于在其他地方引用。现在使用注解后，每个组件仍然应该有一个唯一标识。

**默认情况：**     

​	类名首字母小写就是bean的id 

​	例如：	UserDaoImpl类对应的bean的id就是userDaoImpl

**自定义bean的id：**    

​	可通过标识组件的注解的value属性设置自定义的bean的id

​	例如：@Service("userService")    

​	 默认为 userServiceImpl  --》public class UserServiceImpl implements UserService { }

​	**当配置文件中有相同类型的bean，就需要使用 @Service("userService") 指定**  



#### 实验二：基于注解的自动装配

**① 场景模拟**

参考基于xml的自动装配

​	在UserController中声明UserService对象

​	在UserServiceImpl中声明UserDao对象

**② @Autowired注解**

在成员变量上直接标记@Autowired注解即可完成自动装配，不需要提供setXxx()方法。以后我们在项目中的正式用法就是这样

```java
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    public void saveUser(){ 
        userService.saveUser();
    }

}
```



```java
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public void saveUser() { 
        userDao.saveUser();
    }
    
}
```



```java
@Repository
public class UserDaoImpl implements UserDao { 
    
    @Override
    public void saveUser() {
    	System.out.println("保存成功");
    }
    
}
```



**③ @Autowired注解其他细节**

@Autowired注解 可以标记在**构造器和set方法**上

```java

@Controller
public class UserController {

    private UserService userService;

    //将注解添加在有参构造器上
    @Autowired
    public UserController(UserService userService){ 
        this.userService = userService;
    }

    public void saveUser(){ 
        userService.saveUser();
    }

}
```

```java
@Controller
public class UserController {

	private UserService userService;

    //将注解添加在set方法上
    @Autowired
    public void setUserService(UserService userService){ 
        this.userService = userService;
    }

    public void saveUser(){
    	userService.saveUser();
    }
}
```





**④ @Autowired 工作流程**

![1659703930542](E:\ssm笔记\spring\1659703930542.png)



**原理：**

1）默认通过**byType**的方式，在IOC容器中通过类型匹配某个bena为属性赋值

2）若有多个类型匹配的bean，此时会**自动转换为byName**的方式实现自动装配的效果，

​	（默认将要赋值的属性的属性名作为bean的id  匹配某个bean为属性赋值 ）

3）若 IOC容器中有**多少个类型匹配 且 零个id匹配的bean**    报错：NoUniqueBeanDefinitionException

​	 可在要赋值的属性上添加  **@Qualifier注解** 通过给注解的value属性值，来指定某个bean的id，用·这个bean为属性赋值



**补充：**

**@Autowired注解** 还有一个**required属性**，默认为true 

​		true：要求必须完成自动装配，在自动装配无法找到相应的bean时报 NoSuchBeanDefinitionException

​		false：不必须  ，即 能装配就装配，不能就使用属性的默认值 （默认值为null，在使用时报 NullPointerException ）

​		

**步骤：**

​	首先根据所需要的组件类型到 IOC容器中查找

* 能够找到唯一的 bean（**类型或者id唯一都行**）  直接执行装配 
* 如果完全找不到匹配这个类型的bean：装配失败
* 和所需类型匹配的bean不止一个
	* 没有 **@Qualifier注解**：根据 @Autowired标记位置成员变量的变量名作为**bean的id**进行  匹配
		* 能够找到：执行装配
		* 找不到：装配失败
	* 使用 **@Qualifier注解**：根据@Qualifier注解中指定的名称作为**bean的id**进行匹配
		* 能够找到：执行装配
		* 找不到：装配失败

```java
@Controller
public class UserController {

    @Autowired(required = false)  
    @Qualifier("userServiceImpl") 
    private UserService userService;

    public void saveUser(){ 
        userService.saveUser();
    }

}
```





## 3、AOP



### 3.1 场景模拟



#### 1）举例



**① 声明计算器接口Calculator**，包含加减乘除的抽象方法

```java
public interface Calculator {

	int add(int i, int j);

	int sub(int i, int j);

	int mul(int i, int j);

	int div(int i, int j);

}
```

**② 创建实现类**

```java
public class CalculatorPureImpl implements Calculator {

    @Override
    public int add(int i, int j) {

        int result = i + j;
        System.out.println("方法内部 result = " + result);
        return result;
    }

    @Override
    public int sub(int i, int j) {

        int result = i - j;
        System.out.println("方法内部 result = " + result);
        return result;
    }

    @Override
    public int mul(int i, int j) {

        int result = i * j;
        System.out.println("方法内部 result = " + result);
        return result;
    }

    @Override
    public int div(int i, int j) {

        int result = i / j;
        System.out.println("方法内部 result = " + result);
        return result;
    }
    
}
```

**③ 创建带日志功能的实现类**

```java
public class CalculatorLogImpl implements Calculator {

@Override
    public int add(int i, int j) {

        System.out.println("[日志] add 方法开始了，参数是：" + i + "," + j);
        int result = i + j;
        System.out.println("方法内部 result = " + result);
        System.out.println("[日志] add 方法结束了，结果是：" + result);
        return result;
    }

    @Override
    public int sub(int i, int j) {

        System.out.println("[日志] sub 方法开始了，参数是：" + i + "," + j);
        int result = i - j;
        System.out.println("方法内部 result = " + result);
        System.out.println("[日志] sub 方法结束了，结果是：" + result);
        return result;
    }

    @Override
    public int mul(int i, int j) {

        System.out.println("[日志] mul 方法开始了，参数是：" + i + "," + j);
        int result = i * j;
        System.out.println("方法内部 result = " + result);
        System.out.println("[日志] mul 方法结束了，结果是：" + result);
        return result;
    }

    @Override
    public int div(int i, int j) {

        System.out.println("[日志] div 方法开始了，参数是：" + i + "," + j);
        int result = i / j;
        System.out.println("方法内部 result = " + result);
        System.out.println("[日志] div 方法结束了，结果是：" + result);
        return result;
        
    }
}
```



#### 2）提出问题

**① 现有代码缺陷**

针对带日志功能的实现类，我们发现有如下缺陷：

* 对核心业务功能有干扰，导致程序员在开发核心业务功能时分散了精力
* 附加功能分散在各个业务功能方法中，不利于统一维护

**② 解决思路**

解决这两个问题，核心就是：**解耦**

我们需要把附加功能从业务功能代码中抽取出来。

**③ 困难**

解决问题的困难：要抽取的代码在方法内部，靠以前把子类中的重复代码抽取到父类的方式没法解决。  所以需要引入新的技术。



### 3.2  代理模式

#### 1) 概念

**① 介绍**

二十三种设计模式中的一种，属于**结构型模式**。它的作用就是通过提供一个代理类，让我们在调用目标方法的时候，不再是直接对目标方法进行调用，而是通过代理类**间接调用**。让不属于目标方法核心逻辑的代码从目标方法中剥离出来——**解耦**。调用目标方法时先调用代理对象的方法，减少对目标方法的调用和打扰，同时让附加功能能够集中在一起也有利于统一维护。

![1659709479772](E:\ssm笔记\spring\1659709479772.png)



使用代理后

![1659709578797](E:\ssm笔记\spring\1659709578797.png)

**② 生活中的代理**

* 广告商找大明星拍广告需要经过经纪人
* 合作伙伴找大老板谈合作要约见面时间需要经过秘书
* 房产中介是买卖双方的代理

**③相关术语**

代理：将非核心逻辑剥离出来以后，封装这些非核心逻辑的 类、对象、方法。

目标：核心逻辑代码的类、对象、方法



#### 2）静态代理

创建静态代理类：

```java
public class CalculatorStaticProxy implements Calculator {

    // 将被代理的目标对象声明为成员变量
    private Calculator target;

    public CalculatorStaticProxy(Calculator target) { 
        this.target = target;
    }

    @Override
    public int add(int i, int j) {

        // 附加功能由代理类中的代理方法来实现
        System.out.println("[日志] add 方法开始了，参数是：" + i + "," + j);

        // 通过目标对象来实现核心业务逻辑
        int addResult = target.add(i, j);

        System.out.println("[日志] add 方法结束了，结果是：" + addResult);
        return addResult;
        
    }
    
}
```

静态代理确实实现了解耦，但是由于代码都写死了，完全不具备任何的灵活性。就拿日志功能来  说，将来其他地方也需要附加日志，那还得再声明更多个静态代理类，那就产生了大量重复的代  码，日志功能还是分散的，没有统一管理。

提出进一步的需求：将日志功能集中到一个代理类中，将来有任何日志需求，都通过这一个代理  类来实现。这就需要使用动态代理技术了



#### 3）动态代理

![1659709729685](E:\ssm笔记\spring\1659709729685.png)

生产代理对象的工厂类：

```java
public class ProxyFactory {

    private Object target;

    public ProxyFactory(Object target) {
        this.target = target;
    }

    public Object getProxy() {

        //1. 获取代理类的类加载器
        ClassLoader classLoader = this.getClass().getClassLoader();
        //2. 获取目标对象实现的所有的接口的Class对象数组
        Class<?>[] interfaces = target.getClass().getInterfaces();
        //3. 重写目标对象
        InvocationHandler h = new InvocationHandler() {
            /*
             * Object proxy  : 代理对象
             * Method method : 要执行的方法
             * Object[] args : 要执行的方法的参数列表
             * */
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                Object result = null;

                try {
                    //1）执行目标方法前的日志
                    System.out.println("[动态代理][日志] " + method.getName() + "，参数：" + 																		Arrays.toString(args));
                    //2）调用目标类核心方法
                    result = method.invoke(target, args);

                    //3）调用目标类方法后的日志
                   System.out.println("[动态代理][日志] " + method.getName() + "，结果：" + result);

                } catch (Exception e) {
                    e.printStackTrace();
                    //4）出现异常时的日志
                    System.out.println("[动态代理][日志] "+method.getName()+"，异常："+e);
                } finally {
                    //5）方法执行完毕时的日志
                    System.out.println("[动态代理][日志] "+method.getName()+"，方法执行完毕");
                }
                return result;
            }
        };
        

        /*
         * ClassLoader loader  ：指定加载动态生成代理类的 类加载器
         * Class[] interfaces  ：获取目标对象实现的所有的接口的Class对象数组 （为了确保代理类和目标类实现相同的功能																			   相同的功能）
         * InvocationHandler h ：设置代理类中的抽象方法如何重写 （重写目标对象，调用目标类核心方法 并加入需要的日志信息																			  需要的日志信息）
         * */
        return Proxy.newProxyInstance(classLoader, interfaces, h);

    }
}

```

**测试**

```java
@Test
public void test(){

    /*CalculatorStaticProxy proxy = new CalculatorStaticProxy(new CalculatorImpl());
        proxy.add(1,4);*/

    ProxyFactory proxyFactory = new ProxyFactory(new CalculatorImpl());
    //获取动态代理类
    //（我们不知道动态代理类的类型，但是我们知道 动态代理类和目标类 实现了相同的接口，即可以向上转型为 																			相同的接口类型）
    Calculator proxy = (Calculator) proxyFactory.getProxy();
    proxy.div(1,1);

}
```

**动态代理有两种：**

① jdk动态代理：要求必须有接口，最终生成的代理类和目标类实现相同的接口，在com.sun.proxy包下，类名为$proxy1

② cglib动态代理：最终生成的代理类会继承目标类，并且和目标类在相同的包下



### 3.3 AOP概念及相关术语

#### 1）概述

AOP（Aspect Oriented Programming）是一种设计思想，是软件设计领域中的面向切面编程，它是面向对象编程的一种补充和完善，它以通过预编译方式和运行期动态代理方式实现在不修改源代码的情况  下给程序动态统一添加额外功能的一种技术。

#### 2）相关术语

##### ①横切关注点

从每个方法中抽取出来的同一类**非核心业务**。在同一个项目中，我们可以使用多个横切关注点对相关方 法进行多个不同方面的增强。

这个概念不是语法层面天然存在的，而是根据附加功能的逻辑上的需要：有十个附加功能，就有十个横切关注点。

![1659709833897](E:\ssm笔记\spring\1659709833897.png)



##### ② 通知
每一个横切关注点上要做的事情都需要写一个方法来实现，这样的方法就叫通知方法。

* 前置通知：在被代理的目标方法前执行
* 返回通知：在被代理的目标方法成功结束后执行（寿终正寝）
* 异常通知：在被代理的目标方法异常结束后执行（死于非命） 
* 后置通知：在被代理的目标方法最终结束后执行（盖棺定论）
* 环绕通知：使用try...catch...finally结构围绕整个被代理的目标方法，包括上面四种通知对应的所有位置

![1659709886370](E:\ssm笔记\spring\1659709886370.png)

##### ③ 切面

封装通知方法的类。

![1659709915312](E:\ssm笔记\spring\1659709915312.png)

##### ④ 目标

被代理的目标对象。

##### ⑤ 代理

向目标对象应用通知之后创建的代理对象。

##### ⑥ 连接点

这也是一个纯逻辑概念，不是语法定义的。

把方法排成一排，每一个横切位置看成x轴方向，把方法从上到下执行的顺序看成y轴，x轴和y轴的交叉点就是连接点。

![1659709956008](E:\ssm笔记\spring\1659709956008.png)

##### ⑦ 切入点

定位连接点的方式。

每个类的方法中都包含多个连接点，所以连接点是类中客观存在的事物（从逻辑上来说）。

 如果把连接点看作数据库中的记录，那么切入点就是查询记录的 SQL 语句。

Spring 的 AOP 技术可以**通过切入点定位到特定的连接点**。

切入点通过 org.springframework.aop.Pointcut 接口进行描述，它使用类和方法作为连接点的查询条件。



##### ⑧ 总结

将**目标**中的每一个 **横切关注点**（非核心业务代码），把他抽取出来 变成一个个**通知**封装到**切面**中，在通过**切入点**表达式将这些通知作用到目标对象中的**连接点**上，形成一个**代理**



#### 3）作用

**简化代码**：把方法中固定位置的重复的代码抽取出来，让被抽取的方法更专注于自己的核心功能，  提高内聚性。

**代码增强**：把特定的功能封装到切面类中，看哪里有需要，就往上套，被套用了切面逻辑的方法就  被切面给增强了。



### 3.4 基于注解的AOP

#### 1）技术说明

![1659710009805](E:\ssm笔记\spring\1659710009805.png)

* **动态代理**（InvocationHandler）：JDK原生的实现方式，需要被代理的目标类必须实现接口。因为这个技术要求代理对象和目标对象实现同样的接口（兄弟两个拜把子模式）。
* **cglib**：通过继承被代理的目标类（认干爹模式）实现代理，所以不需要目标类实现接口。
* **AspectJ**：本质上是静态代理，将代理逻辑“织入”被代理的目标类编译得到的字节码文件，所以最终效果是动态的。weaver就是织入器。Spring只是借用了AspectJ中的注解。

#### 2）准备工作

##### ① 添加依赖

在IOC所需依赖基础上再加入下面依赖即可：

```xml
<!-- spring-aspects会帮我们传递过来aspectjweaver -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-aspects</artifactId>
    <version>5.3.1</version>
</dependency>
```

##### ② 准备被代理的目标资源

接口：

```JAVA
public interface Calculator {

    int add(int i, int j);

    int sub(int i, int j);

    int mul(int i, int j);

    int div(int i, int j);

}
```

实现类：

```JAVA

@Component
public class CalculatorPureImpl implements Calculator {

    @Override
    public int add(int i, int j) {
        int result = i + j;
        System.out.println("方法内部 result = " + result);
        return result;
    }

    @Override
    public int sub(int i, int j) {
        int result = i - j;
        System.out.println("方法内部 result = " + result);
        return result;
    }

    @Override
    public int mul(int i, int j) {
        int result = i * j;
        System.out.println("方法内部 result = " + result);
        return result;
    }

    @Override
    public int div(int i, int j) {
        int result = i / j;
        System.out.println("方法内部 result = " + result);
        return result;
    }
    
}
```

#### 3）创建切面类并配置

```JAVA
@Component 	//放入ioc容器中
@Aspect  	//将当前组件标识为切面类
public class LogAspect {

    //@Pointcut 设置一个公共的切入点表达式 方法
    @Pointcut("execution(* com.lhc.spring.aop.annotation.CalculatorImpl.*(..))")
    public void pointCut() {}

    /**
     * 1. 前置通知：@Before(value="切入点表达式")
     *
     * @param joinPoint: 连接点信息 （通过它可以获取该连接点对应的方法信息）
     */
    //@Before("execution(* com.lhc.spring.aop.annotation.CalculatorImpl.*(..))")
    @Before("pointCut()") //使用公共的切入点表达式
    public void beforeAdviceMethod(JoinPoint joinPoint) {

        //获取连接点所对应方法的方法名
        String methodName = joinPoint.getSignature().getName();
        //获取连接点所对应方法的参数
        String args = Arrays.toString(joinPoint.getArgs());

        System.out.println("Logger-->前置通知，方法名：" + methodName + "，参数：" + args);
    }


    /**
     * 2. 后置通知：@After(value="切入点表达式")
     *
     * @param joinPoint: 连接点信息
     */
    @After("pointCut()")
    public void afterMethod(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println("Logger-->后置通知，方法名：" + methodName + "，执行完毕");
    }

    /**
     * 3. 返回通知：@AfterReturning(value = "切入点表达式", returning = "result")
     * 						returning属性: 就是用来接收目标对象方法执行之后的返回值
     *              		  属性值 "result": 就是将该返回值指定给 通知方法的同名参数
     *
     * @param joinPoint: 连接点信息
     * @param result:    表示目标对象方法执行之后的返回值 (参数名 和 returning属性值 保持一致)
     */
    @AfterReturning(value = "pointCut()", returning = "result")
    public void afterReturningMethod(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println("Logger-->返回通知，方法名：" + methodName + "，结 果：" + result);
    }

    /**
     * 4. 异常通知：@AfterThrowing(value = "切入点表达式", throwing = "ex")
     * 				throwing属性：就是用来接收目标对象方法执行过程中出现的异常，
     *               属性值 "ex"： 就是将该异常指定给 通知方法的同名参数
     *
     * @param joinPoint: 连接点信息
     * @param ex:        表示目标对象方法执行出现的异常 （参数名 和 throwing属性值 保持一致）
     */
    @AfterThrowing(value = "pointCut()", throwing = "ex")
    public void afterThrowingMethod(JoinPoint joinPoint, Throwable ex) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println("Logger-->异常通知，方法名：" + methodName + "，异常：" + ex);
    }
    
    /**
     * 5. 环绕通知：@Around(value = "切入点表达式")
     * 
     * @param proceedingJoinPoint：可执行连接点 （可以通过它来执行目标对象方法）
     * @return 目标对象方法执行的返回值
     */
    @Around("pointCut()")
    public Object aroundMethod(ProceedingJoinPoint proceedingJoinPoint) {

        Object result = null;
        try {
            System.out.println("环绕通知-->前置通知");
            //表示目标对象方法的执行
            result = proceedingJoinPoint.proceed();
            System.out.println("环绕通知-->返回通知");

        } catch (Throwable e) {
            e.printStackTrace();
            System.out.println("环绕通知-->异常通知");

        } finally {
            System.out.println("环绕通知-->后置通知");

        }
        return result;
    }
    
}

```

在Spring的配置文件中配置：

```XML

<!--
基于注解的AOP的实现：
	1、将目标对象和切面交给IOC容器管理（注解+扫描）
	2、开启AspectJ的自动代理，为目标对象自动生成代理
	3、将切面类通过注解@Aspect标识
-->
<contest:component-scan base-package="com.lhc.spring.aop.annotation"></contest:component-scan>

<!-- 开启基于注解的AOP -->
<aop:aspectj-autoproxy/>
```

测试

```java
@Test
public void test(){
    ApplicationContext ioc = new ClassPathXmlApplicationContext("spring-aop-annotation.xml");
    
    //我们使用了AOP之后，只能通过代理对象来访问目标对象（CalculatorImpl），而不能直接访问目标对象了
    //在IOC中已经获取不到目标对象了，要通过代理对象的接口来获取代理对象
    //Calculator calculator = ioc.getBean(CalculatorImpl.class); --> NoSuchBeanDefinitionException
    Calculator calculator = ioc.getBean(Calculator.class);
    calculator.div(1,1);
    
}

```



#### 4）各种通知

* 前置通知：使用**@Before**注解标识，在被代理的目标方法前执行

* 返回通知：使用**@AfterReturning**注解标识，在被代理的目标方法成功结束后执行（寿终正寝） 

* 异常通知：使用**@AfterThrowing**注解标识，在被代理的目标方法异常结束后执行（死于非命） 

* 后置通知：使用**@After**注解标识，在被代理的目标方法最终结束后执行（盖棺定论）

* 环绕通知：使用**@Around**注解标识，使用try...catch...finally结构围绕整个被代理的目标方法，包括上面四种通知对应的所有位置

	

**各种通知的执行顺序**：

* Spring版本5.3.x以前： 

	​	前置通知

	​	目标操作

	​	后置通知

	​	返回通知或异常通知

* Spring版本5.3.x以后：

	​	前置通知

	​	目标操作

	​	返回通知或异常通知

	​	后置通知

	

#### 5）切入点表达式语法

**① 作用**

![1659710270886](E:\ssm笔记\spring\1659710270886.png)

**② 语法细节**

![1659710314518](E:\ssm笔记\spring\1659710314518.png)



#### 6）重用切入点表达式

**设置一个公共的切入点表达式 方法**

```java
@Component 
@Aspect  
public class LoggerAspect {
    
    //@Pointcut("切入点表达式") 设置一个公共的切入点表达式 方法
    @Pointcut("execution(* com.lhc.spring.aop.annotation.CalculatorImpl.*(..))")
    public void pointCut() {}
    
}
```

**① 在同一个切面中使用**

```java
@Component 
@Aspect  
public class LoggerAspect {
    
    //和 公共的切入点表达式 在同一个切面类时，直接使用改方法
    @Before("pointCut()")
    public void beforeMethod(JoinPoint joinPoint){
        String methodName = joinPoint.getSignature().getName(); 
        String args = Arrays.toString(joinPoint.getArgs());
        System.out.println("Logger-->前置通知，方法名："+methodName+"，参数："+args);
    }
}
```

**② 在不同切面中使用**

```java
@Component
@Aspect
public class ValidateAspect {    
    
	//和 公共的切入点表达式 不在同一个切面类时  ，需要使用该方法的全类名
    @Before("com.lhc.spring.aop.annotation.LoggerAspect.pointCut()")
    public void beforeMethod(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String args = Arrays.toString(joinPoint.getArgs());
        System.out.println("ValidateAspect-->前置通知，方法名：" + methodName + "，参数：" + args);
    }
    
}
```



#### 7）获取通知的相关信息

**①获取连接点信息**

获取连接点信息可以在通知方法的参数位置设置JoinPoint类型的形参

**② 获取目标方法的返回值**

@AfterReturning中的属性returning，用来将通知方法的某个形参，接收目标方法的返回值

**③ 获取目标方法的异常**

@AfterThrowing中的属性throwing，用来将通知方法的某个形参，接收目标方法的异常



#### 8）切面的优先级
相同目标方法上同时存在多个切面时，切面的优先级控制切面的**内外嵌套**顺序。 

​	优先级高的切面：外面
​	优先级低的切面：里面

使用@Order注解可以控制切面的优先级：

​	@Order(较小的数)：优先级高
​	@Order(较大的数)：优先级低

![1659710566168](E:\ssm笔记\spring\1659710566168.png)

```java
@Component
@Aspect
@Order(1) //设置切面的优先级
public class ValidateAspect {

    //和公共的切入点表达式不在同一个切面类时,需要使用该方法的全类名
    @Before("com.lhc.spring.aop.annotation.LoggerAspect.pointCut()")
    public void beforeMethod(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String args = Arrays.toString(joinPoint.getArgs());
        System.out.println("ValidateAspect-->前置通知，方法名：" + methodName + "，参数：" + args);
    }

}
```



### 3.5 基于XML的AOP（了解）

#### 1）准备工作

参考基于注解的AOP环境

#### 2）实现

```xml
<!--扫描组件-->
<context:component-scan base-package="com.lhc.spring.aop.xml"></context:component-scan>

<aop:config>

    <!-- 0. 设置一个公共的切入点表达式-->
    <aop:pointcut id="pointCut" expression="execution(* com.lhc.spring.aop.xml.*.*(..))"/>

    <!-- 1. aop:aspect：将IOC容器中的某个bean设置为切面
                ref：引用某个bean的id （默认id为 类名的小驼峰）
    -->
    <aop:aspect ref="loggerAspect">

        <!-- 1) aop:before：将切面中的通知方法设置为 前置通知
                    method：切面中的方法名
                    pointcut：设置切入点表达式
                    pointcut-ref：引用某个公共的切入点表达式
        -->
        <aop:before method="beforeAdviceMethod" pointcut-ref="pointCut"></aop:before>

        <!-- 2) aop:after：将切面中的通知方法设置为 后置通知 -->
        <aop:after method="beforeAdviceMethod" pointcut-ref="pointCut"></aop:after>

        <!-- 3) aop:after-returning：将切面中的通知方法设置为 返回通知
                    returning属性：就是用来接收目标对象方法执行之后的返回值，
                    属性值 "result"： 就是将该返回值指定给 通知方法的同名参数
        -->
        <aop:after-returning method="afterReturningMethod" returning="result" pointcut-ref=																"pointCut"></aop:after-returning>

        <!-- 4) aop:after-throwing：将切面中的通知方法设置为 异常通知
                    throwing属性：就是用来接收目标对象方法执行过程中出现的异常，
                    属性值 "ex"： 就是将该异常指定给 通知方法的同名参数
        -->
        <aop:after-throwing method="afterThrowingMethod" throwing="ex" pointcut-ref="pointCut">																		</aop:after-throwing>

        <!-- 5) aop:around：将切面中的通知方法设置为 环绕通知 -->
        <aop:around method="aroundMethod" pointcut-ref="pointCut"></aop:around>
        
    </aop:aspect>

    <!-- 2. aop:aspect：将IOC容器中的某个bean设置为切面
                order：设置切面的优先级
    -->
    <aop:aspect ref="validateAspect" order="1">
        <aop:before method="beforeMethod" pointcut-ref="pointCut"></aop:before>
    </aop:aspect>

</aop:config>

```



## 4、声明式事务



### 4.1 JdbcTemplate



#### 1）简介
​	Spring 框架对 JDBC 进行封装，使用 JdbcTemplate 方便实现对数据库操作

#### 2）准备工作



##### ① 加入依赖

```xml
<dependencies>

    <!-- 基于Maven依赖传递性，导入spring-context依赖即可导入当前所需所有jar包 -->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>5.3.1</version>
    </dependency>

    <!-- Spring 持久化层支持jar包 -->
    <!-- Spring 在执行持久化层操作、与持久化层技术进行整合过程中，需要使用orm、jdbc、tx三个
        jar包 -->
    <!-- 导入 orm 包就可以通过 Maven 的依赖传递性把其他两个也导入 -->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-orm</artifactId>
        <version>5.3.1</version>
    </dependency>

    <!-- Spring 测试相关 -->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-test</artifactId>
        <version>5.3.1</version>
    </dependency>

    <!-- junit测试 -->
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.12</version>
        <scope>test</scope>
    </dependency>

    <!-- MySQL驱动 -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.16</version>
    </dependency>
    <!-- 数据源 -->
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>druid</artifactId>
        <version>1.0.31</version>
    </dependency>

</dependencies>

```

##### ② 创建jdbc.properties

```properties
jdbc.user=root 
jdbc.password=atguigu
jdbc.url=jdbc:mysql://localhost:3306/ssm 
jdbc.driver=com.mysql.cj.jdbc.Driver
```

##### ③ 配置Spring的配置文件

```xml
 <!-- 导入外部属性文件 -->
<context:property-placeholder location="classpath:jdbc.properties"/>

<!-- 配置数据源 -->
<bean id="druidDataSource" class="com.alibaba.druid.pool.DruidDataSource">
    <property name="url" value="${jdbc.url}"/>
    <property name="driverClassName" value="${jdbc.driver}"/>
    <property name="username" value="${jdbc.username}"/>
    <property name="password" value="${jdbc.password}"/>
</bean>

<!-- 配置 JdbcTemplate -->
<bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
    <!-- 装配数据源 -->
    <property name="dataSource" ref="druidDataSource"/>
</bean>
```



#### 3）测试

① 在测试类装配 JdbcTemplate

```java
//指定当前测试类在Spring的测试环境中执行，此时就可以通过注入的方式获取IOC容器中的bean
@RunWith(SpringJUnit4ClassRunner.class)
//设置Spring测试环境的配置文件
@ContextConfiguration("classpath:spring-jdbc.xml")
public class JdbcTemplateTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
}
```

② 测试增删改功能

```java
@Test //测试增删改功能
public void testUpdate() {
	String sql = "insert into t_user values(null,?,?,?,?,?)";
	int result = jdbcTemplate.update(sql, "张三", "ok", 11, "男", "123@qq.com");
	System.out.println(result);
}
```

③ 查询一条数据为实体类对象

```java
@Test //查询一条数据为一个实体类对象
public void testSelectEmpById() {
    String sql = "select * from t_user where id = ?";
    User user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), 1);
    System.out.println(user);
}
```

④ 查询多条数据为一个list集合

```java
@Test
//查询多条数据为一个list集合
public void testSelectList() {
    String sql = "select * from t_user";
    List<User> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));

    //list.forEach(System.out::println);
    list.forEach(user -> System.out.println(user));
}
```


⑤查询单行单列的值

```java
@Test
//查询单行单列的值
public void selectCount() {
    String sql = "select count(id) from t_user";
    Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
    System.out.println(count);
}
```



### 4.2 声明式事务概念



####  1）编程式事务

事务功能的相关操作全部通过自己编写代码来实现：

```java
Connection conn = ...;

try {

    // 开启事务：关闭事务的自动提交
    conn.setAutoCommit(false);

    // 核心操作
    ...

    // 提交事务
    conn.commit();

}catch(Exception e){

    // 回滚事务
    conn.rollBack();

}finally{
    
    // 释放数据库连接
    conn.close();
    
}
```

**编程式的实现方式存在缺陷**：

​	细节没有被屏蔽：具体操作过程中，所有细节都需要程序员自己来完成，比较繁琐。

​	代码复用性不高：如果没有有效抽取出来，每次实现功能都需要自己编写代码，代码就没有得到复用。



#### 2）声明式事务



既然事务控制的代码有规律可循，代码的结构基本是确定的，所以框架就可以将固定模式的代码抽取出来, 进行相关的装

封装起来后，我们只需要在配置文件中进行简单的配置即可完成操作

* 好处1：提高开发效率
* 好处2：消除了冗余的代码
* 好处3：框架会综合考虑相关领域中在实际开发环境下有可能遇到的各种问题，进行了健壮性、性能等各个方面的化

我们可以总结下面两个概念：

* **编程式**：自己写代码实现功能
* **声明式**：通过配置让框架实现功能



### 4.3 基于注解的声明式事务



#### 1）准备工作

##### ① 加入依赖

```xml
<dependencies>

    <!-- 基于Maven依赖传递性，导入spring-context依赖即可导入当前所需所有jar包 -->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>5.3.1</version>
    </dependency>

    <!-- Spring 持久化层支持jar包 -->
    <!-- Spring 在执行持久化层操作、与持久化层技术进行整合过程中，需要使用orm、jdbc、tx三个jar包 -->
    <!-- 导入 orm 包就可以通过 Maven 的依赖传递性把其他两个也导入 -->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-orm</artifactId>
        <version>5.3.1</version>
    </dependency>

    <!-- Spring 测试相关 -->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-test</artifactId>
        <version>5.3.1</version>
    </dependency>

    <!-- junit测试 -->
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.12</version>
        <scope>test</scope>
    </dependency>

    <!-- MySQL驱动 -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.16</version>
    </dependency>
    <!-- 数据源 -->
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>druid</artifactId>
        <version>1.0.31</version>
    </dependency>

</dependencies>
```

##### ② 创建jdbc.properties

```properties
jdbc.driver=com.mysql.cj.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/ssm?serverTimezone=UTC
jdbc.username=root
jdbc.password=lhc
```

##### ③ 配置Spring的配置文件

```xml
<!--扫描组件-->
<context:component-scan base-package="com.lhc.spring"></context:component-scan>

<!-- 导入外部属性文件 -->
<context:property-placeholder location="classpath:jdbc.properties"/>

<!-- 配置数据源 -->
<bean id="druidDataSource" class="com.alibaba.druid.pool.DruidDataSource">
    <property name="url" value="${jdbc.url}"/>
    <property name="driverClassName" value="${jdbc.driver}"/>
    <property name="username" value="${jdbc.username}"/>
    <property name="password" value="${jdbc.password}"/>
</bean>

<!-- 配置 JdbcTemplate -->
<bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
    <!-- 装配数据源 -->
    <property name="dataSource" ref="druidDataSource"/>
</bean>
```

##### ④ 创建表

```sql
CREATE TABLE `t_book` (
`book_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
`book_name` varchar(20) DEFAULT NULL COMMENT '图书名称',
`price` int(11) DEFAULT NULL COMMENT '价格',
`stock` int(10) unsigned DEFAULT NULL COMMENT '库存（无符号）', PRIMARY KEY (`book_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

insert	into `t_book`(`book_id`,`book_name`,`price`,`stock`) values (1,'斗破苍穹',80,100),(2,'斗罗大陆',50,100);

CREATE TABLE `t_customer` (
`user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
`username` varchar(20) DEFAULT NULL COMMENT '用户名',
`balance` int(10) unsigned DEFAULT NULL COMMENT '余额（无符号）', PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

insert	into `t_customer`(`user_id`,`username`,`balance`) values (1,'admin',50);
```

##### ⑤ 创建组件

创建BookController：

```java
@Controller
public class BookController {

    @Autowired
    private BookService bookService;

    public void buyBook(Integer bookId, Integer userId){ 
        bookService.buyBook(bookId, userId);
    }
    
}
```

创建接口BookService：

```java
public interface BookService {
    void buyBook(Integer bookId, Integer userId);
}
```

创建实现类BookServiceImpl：

```java
@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookDao bookDao;

    @Override
    public void buyBook(Integer bookId, Integer userId) {
        //查询图书的价格
        Integer price = bookDao.getPriceByBookId(bookId);
        //更新图书的库存
        bookDao.updateStock(bookId);
        //更新用户的余额
        bookDao.updateBalance(userId, price);
    }
}
```

创建接口BookDao：

```java
public interface BookDao {

    Integer getPriceByBookId(Integer bookId);

    void updateStock(Integer bookId);

    void updateBalance(Integer userId, Integer price);

}
```

创建实现类BookDaoImpl：

```java
@Repository
public class BookDaoImpl implements BookDao {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Integer getPriceByBookId(Integer bookId) {
        String sql = "select price from t_book where book_id = ?"; 
        return jdbcTemplate.queryForObject(sql, Integer.class, bookId);
    }

    @Override
    public void updateStock(Integer bookId) {
        String sql = "update t_book set stock = stock - 1 where book_id = ?";
        jdbcTemplate.update(sql, bookId);
    }

    @Override
    public void updateBalance(Integer userId, Integer price) {
        String sql = "update t_customer set balance = balance - ? where user_id = ?";
        jdbcTemplate.update(sql, price, userId);
    }

}
```



#### 2）测试无事务情况

##### ① 创建测试类

```java
@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration("classpath:tx-annotation.xml") 
public class TxByAnnotationTest {

    @Autowired
    private BookController bookController;

    @Test
    public void testBuyBook(){ 
        bookController.buyBook(1, 1);
    }

}
```

##### ② 模拟场景
用户购买图书，先查询图书的价格，再更新图书的库存和用户的余额

假设用户id为1的用户，购买id为1的图书

用户余额为50，而图书价格为80

购买图书之后，用户的余额为-30，数据库中余额字段设置了无符号，因此无法将-30插入到余额字段

此时执行sql语句会抛出SQLException

##### ③ 观察结果
因为没有添加事务，图书的库存更新了，但是用户的余额没有更新
显然这样的结果是错误的，购买图书是一个完整的功能，更新库存和更新余额要么都成功要么都失败



#### 3）加入事务

##### ① 添加事务配置

在Spring的配置文件中添加配置：

```xml
<!-- 1. 配置事务管理器 （切面）-->
<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    <!--给数据源属性 赋值-->
    <property name="dataSource" ref="druidDataSource"></property>
</bean>

<!-- 2. 开启事务的注解驱动 
    
	将使用@Transactional注解标识的方法或类中所有方法都使用事务进行管理
    即：将切面（事务管理器）中的通知方法（事务方法）作用于目标对象连接点（被@Transactional标记的类或方法）

            transaction-manager属性：指定事务管理器
                1）事务管理器id默认为transactionManager
                2）transaction-manager属性值默认为transactionManager
                即：在事务管理器未修改id的情况下，都可以不写，会默认指定
 -->
<tx:annotation-driven transaction-manager="transactionManager"/>
```

![1659878151946](C:\Users\huach\AppData\Roaming\Typora\typora-user-images\1659878151946.png)



##### ② 添加事务注解

因为service层表示业务逻辑层，一个方法表示一个完成的功能，因此处理事务一般在service层处理      

* @Transactional标识在方法上，只会影响该方法

* @Transactional标识的类上，会影响类中所有的方法

```java
@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookDao bookDao;

    @Override
    @Transactional 
    public void buyBook(Integer bookId, Integer userId) {
        //查询图书的价格
        Integer price = bookDao.getPriceByBookId(bookId);
        //更新图书的库存
        bookDao.updateStock(bookId);
        //更新用户的余额
        bookDao.updateBalance(userId, price);
    }
}
```

##### ③ 观察结果

​	由于使用了Spring的声明式事务，更新库存和更新余额都没有执行



#### 4）事务属性：只读

##### ① 介绍
对一个查询操作来说，如果我们把它设置成只读，就能够明确告诉数据库，这个操作不涉及写操作。这  样数据库就能够针对查询操作来进行优化。

##### ② 使用方式

```java
@Override
@Transactional(
    readOnly = true
)
public void buyBook(Integer bookId, Integer userId) {
    //查询图书的价格
    Integer price = bookDao.getPriceByBookId(bookId);
    //更新图书的库存
    bookDao.updateStock(bookId);
    //更新用户的余额
    bookDao.updateBalance(userId, price);
}
```

##### ③ 注意
对增删改操作设置只读会抛出下面异常：
Caused by: java.sql.SQLException: **Connection is read-only. Queries leading to data modification are not allowed**



#### 5）事务属性：超时

##### ① 介绍

事务在执行过程中，有可能因为遇到某些问题，导致程序卡住，从而长时间占用数据库资源。而长时间  占用资源，大概率是因为程序运行出现了问题（可能是Java程序或MySQL数据库或网络连接等等）。

此时这个很可能出问题的程序应该被回滚，撤销它已做的操作，事务结束，把资源让出来，让其他正常  程序可以执行。

概括来说就是一句话：**超时回滚，释放资源**

##### ② 使用方式

```java
@Override
@Transactional(
    //设置超时时间为3s
    timeout = 3 
)
public void buyBook(Integer bookId, Integer userId) {

    try {
        TimeUnit.SECONDS.sleep(5); //设置休眠时间 5(SECONDS) -> 5(秒)
    } catch (InterruptedException e) {
        e.printStackTrace();
    }

    //查询图书的价格
    Integer price = bookDao.getPriceByBookId(bookId);
    //更新图书的库存
    bookDao.updateStock(bookId);
    //更新用户的余额
    bookDao.updateBalance(userId, price);
}
```

##### ③ 观察结果
执行过程中抛出异常：

org.springframework.transaction.TransactionTimedOutException: Transaction timed out: deadline was Fri Jun 04 16:25:39 CST 2022



#### 6）事务属性：回滚策略

##### ① 介绍

声明式事务默认只针对运行时异常回滚，编译时异常不回滚。

可以通过**@Transactional中相关属性设置回滚策略**

**设置回滚的异常：**

* rollbackFor属性：需要设置一个Class类型的对象
* rollbackForClassName属性：需要设置一个异常的全类名

**设置不回滚的异常：**

* noRollbackFor属性：需要设置一个Class类型的对象 

* rollbackFor属性：需要设置一个异常的全类名

	

##### ② 使用方式

```java
@Override
@Transactional(
	
    //当出现 ArithmeticException(数学运算异常) 时不造成回滚
    //noRollbackFor = {ArithmeticException.class} 
    noRollbackForClassName = "java.lang.ArithmeticException"
    
)
public void buyBook(Integer bookId, Integer userId) {

    try {
        TimeUnit.SECONDS.sleep(5); //设置休眠时间 5s
    } catch (InterruptedException e) {
        e.printStackTrace();
    }

    //查询图书的价格
    Integer price = bookDao.getPriceByBookId(bookId);
    //更新图书的库存
    bookDao.updateStock(bookId);
    //更新用户的余额
    bookDao.updateBalance(userId, price);

    //在这里故意制造员工运行时异常
    System.out.println(1/0);
}
```



##### ③ 观察结果
虽然购买图书功能中出现了数学运算异常（ArithmeticException），但是我们设置的回滚策略是，当出现ArithmeticException不发生回滚，因此购买图书的操作正常执行



#### 7）事务属性：事务隔离级别

##### ① 介绍
数据库系统必须具有隔离并发运行各个事务的能力，使它们不会相互影响，避免各种并发问题。一个事务与其他事务隔离的程度称为隔离级别。SQL标准中规定了多种事务隔离级别，不同隔离级别对应不同的干扰程度，隔离级别越高，数据一致性就越好，但并发性越弱。

**隔离级别一共有四种：**

* 读未提交：READ UNCOMMITTED 

	​	允许Transaction01读取Transaction02 未提交的修改。

* 读已提交：READ COMMITTED

	​	要求Transaction01只能读取Transaction02已提交的修改。

* 可重复读：REPEATABLE READ 

	​	确保Transaction01可以多次从一个字段中读取到相同的值

	​	即Transaction01执行期间禁止其它事务对这个字段进行更新。

* 串行化：SERIALIZABLE 

	确保Transaction01可以多次从一个表中读取到相同的行，

	在Transaction01执行期间，禁止其它事务对这个表进行添加、更新、删除操作。

	可以避免任何并发问题，但性能十分低下。



各个隔离级别解决并发问题的能力见下表：

| **隔离级别**     | **脏读** | **不可重复读** |      **幻读**       | 加锁读 |
| ---------------- | :------: | :------------: | :-----------------: | :----: |
| READ UNCOMMITTED |    √     |       √        |          √          |   ×    |
| READ COMMITTED   |    ×     |       √        |          √          |   ×    |
| REPEATABLE READ  |    ×     |       ×        | √（mysql 不会出现） |   ×    |
| SERIALIZABLE     |    ×     |       ×        |          ×          |   √    |



各种数据库产品对事务隔离级别的支持程度：

| **隔离级别**     | **Oracle** | **MySQL** |
| ---------------- | ---------- | --------- |
| READ UNCOMMITTED | ×          | √         |
| READ COMMITTED   | √(默认)    | √         |
| REPEATABLE READ  | ×          | √(默认)   |
| SERIALIZABLE     | √          | √         |



##### ② 使用方式

```java
@Transactional(isolation = Isolation.DEFAULT)		    //使用数据库默认的隔离级别
@Transactional(isolation = Isolation.READ_UNCOMMITTED)	 //读未提交
@Transactional(isolation = Isolation.READ_COMMITTED)	 //读已提交
@Transactional(isolation = Isolation.REPEATABLE_READ)	 //可重复读
@Transactional(isolation = Isolation.SERIALIZABLE)		 //串行化
```



#### 8）事务属性：事务传播行为

##### ① 介绍
当事务方法被另一个事务方法调用时，必须指定事务应该如何传播。

例如：方法可能继续在现有事务中运行，也可能开启一个新事务，并在自己的事务中运行。



**下面例题：**

需求：顾客拥有100块钱，但是他下单了两本书（一本80，一本50）

过程：

* 我们在 CheckoutServiceImpl 和BookServiceImpl中 都设置了事务

* 我们从Controller开始执行，调用CheckoutServiceImpl会开启事务，CheckoutServiceImpl 调用 BookServiceImpl

* **默认的事务传播行为是：当前线程上有已经开启的事务可用，那么就在这个事务中运行**

* 故会让BookServiceImpl不开启新事务 而是在CheckoutServiceImpl的事务中运行

* 这样顾客在BookServiceImpl买第一本书的是可以执行成功的，但是买第二本书的时候，余额不足 报错，报错就回滚

* 由于现在他的两次在BookServiceImpl层买书，但都是在CheckoutServiceImpl层的事务中执行的操作

* 结果：两次买书操作都回滚，一本书也没买到

	

* 假如我们不想让他全部回滚，就需要修改**事务的传播行为**：**不管当前线程上是否有已经开启的事务，都要开启新事务**

* 同样的场景，执行到BookServiceImpl的时候 就会开启新的事务，第一次买书成功提交事务，第二次余额不足 就回滚

* 结果：第一本书成功购买，第二本书购买失败 回滚



##### ② 测试
(1) 创建接口CheckoutService

```java
public interface CheckoutService {
	void checkout(Integer[] bookIds, Integer userId);
}
```

(2) 创建实现类CheckoutServiceImpl：

```java
@Service
public class CheckoutServiceImpl implements CheckoutService {

    @Autowired
    private BookService bookService;

    @Override 
    @Transactional 
    //一次购买多本图书
    public void checkout(Integer[] bookIds, Integer userId) {  
        for (Integer bookId : bookIds) {
        	bookService.buyBook(bookId, userId);
        }
    }
}
```

(3) 在BookController中添加方法：

```java
@Autowired
private CheckoutService checkoutService;

public void checkout(Integer[] bookIds, Integer userId){ 
    checkoutService.checkout(bookIds, userId);
}
```

(4) 在数据库中将用户的余额修改为100元

##### ③ 观察结果

​	两本书都购买失败



##### ④ 修改事务传播行为

```java
@Override
@Transactional(

	propagation = Propagation.REQUIRES_NEW //修改事务传播行为

)
public void buyBook(Integer bookId, Integer userId) {

	//查询图书的价格
	Integer price = bookDao.getPriceByBookId(bookId);
  	//更新图书的库存
  	bookDao.updateStock(bookId);
	//更新用户的余额
    bookDao.updateBalance(userId, price);
}
```

##### ⑤ 再次测试结果

​	第一本书都购买成功，第二本书购买失败



##### ⑥ 常见的事务传播行为

* @Transactional(propagation = Propagation.REQUIRED)      （默认）

	表示如果当前线程上有已经开启的事务可用，那么就在这个事务中运行。

* @Transactional(propagation = Propagation.REQUIRES_NEW)

	表示不管当前线程上是否有已经开启的事务，都要开启新事务。

	



### 4.4 基于XML的声明式事务

#### 1）场景模拟

​	参考基于注解的声明式事务

#### 2）修改Spring配置文件

将Spring配置文件中去掉 tx:annotation-driven 标签，并添加配置：

```xml
<aop:config>
    <!-- 配置事务通知和切入点表达式 -->
    <aop:advisor advice-ref="txAdvice" 
                 pointcut="execution(* com.lhc.spring.service.impl.*.*(..))"></aop:advisor>
</aop:config>

<!-- tx:advice标签：配置事务通知 -->
<!-- id属性：给事务通知标签设置唯一标识，便于引用 -->
<!-- transaction-manager属性：关联事务管理器 -->
<tx:advice id="txAdvice" transaction-manager="transactionManager">
    
    <tx:attributes>
        <!-- tx:method标签：配置具体的事务方法 -->
        <!-- name属性：指定方法名，可以使用星号代表多个字符 -->
        <tx:method name="get*" read-only="true"/>
        <tx:method name="query*" read-only="true"/>
        <tx:method name="find*" read-only="true"/>

        <!-- read-only属性：设置只读属性 -->
        <!-- rollback-for属性：设置回滚的异常 -->
        <!-- no-rollback-for属性：设置不回滚的异常 -->
        <!-- isolation属性：设置事务的隔离级别 -->
        <!-- timeout属性：设置事务的超时属性 -->
        <!-- propagation属性：设置事务的传播行为 -->
        <tx:method name="save*" read-only="false" 
                   rollback-for="java.lang.Exception" propagation="REQUIRES_NEW"/>
        <tx:method name="update*" read-only="false" 
                   rollback-for="java.lang.Exception" propagation="REQUIRES_NEW"/>
        <tx:method name="delete*" read-only="false" 
                   rollback- for="java.lang.Exception" propagation="REQUIRES_NEW"/>
    </tx:attributes>
    
</tx:advice>
```

注意：基于xml实现的声明式事务，必须引入aspectJ的依赖

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-aspects</artifactId>
    <version>5.3.1</version>
</dependency>
```





# 三、SpringMVC



## 1、SpringMVC简介



### 1.1 什么是MVC



**1）MVC 是一种软件架构的思想，将软件按照模型、视图、控制器来划分**

**M**：Model 模型层，指工程中的JavaBean，作用是处理数据

​	JavaBean分为两类：

​	一类称为实体类Bean：专门存储业务数据的，如 Student、User 等
​	一类称为业务处理 Bean：指 Service 或 Dao 对象，专门用于处理业务逻辑和数据访问。

**V**：View 视图层，指工程中的 html 或 jsp 等页面，作用是与用户进行交互，展示数据

**C**：Controller 控制层，指工程中的servlet，作用是接收请求和响应浏览器



**2）MVC的工作流程**

* 用户通过视图层发送请求到服务器，在服务器中请求被Controller接收
* Controller 调用相应的**Model层**处理请求，处理完毕将结果返回到Controller
* Controller再根据请求处理的结果找到相应的**View视图**，渲染数据后最终响应给浏览器



### 1.2 什么是SpringMVC

* SpringMVC是Spring的一个后续产品，是Spring的一个子项目

* SpringMVC 是 Spring 为表述层开发提供的一整套完备的解决方案

* 在表述层框架历经 Strust、WebWork、Strust2 等诸多产品的历代更迭之后

	目前业界普遍选择了 SpringMVC 作为 JavaEE 项目**表述层**开发的**首选方案**。

**注**：三层架构分为表述层（或表示层）、业务逻辑层、数据访问层

​		表述层 表示**前台页面**和**后台servlet**



### 1.3 SpringMVC的特点

* Spring 家族原生产品，与 IOC 容器等基础设施无缝对接

* 基于原生的Servlet，通过了功能强大的 **前端控制器DispatcherServlet，对请求和响应进行统一处理**

* 表述层各细分领域需要解决的问题全方位覆盖，提供全面解决方案

* 代码清新简洁，大幅度提升开发效率

* 内部组件化程度高，可插拔式组件即插即用，想要什么功能配置相应组件即可

* 性能卓著，尤其适合现代大型、超大型互联网项目要求

	

## 2、入门案例



### 2.1 开发环境

IDE：idea 2019.2

构建工具：maven3.5.4 服务器：tomcat8.5

Spring版本：5.3.1



### 2.2 创建maven工程



#### ① 添加web模块

#### ② 打包方式：war

```xml
<packaging>war</packaging>
```

#### ③ 引入依赖

```xml
<dependencies>
    <!-- SpringMVC -->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-webmvc</artifactId>
        <version>5.3.1</version>
    </dependency>

    <!-- 日志 -->
    <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-classic</artifactId>
        <version>1.2.3</version>
    </dependency>

    <!-- ServletAPI -->
    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>javax.servlet-api</artifactId>
        <version>3.1.0</version>
        <scope>provided</scope>
    </dependency>

    <!-- Spring5和Thymeleaf整合包 -->
    <dependency>
        <groupId>org.thymeleaf</groupId>
        <artifactId>thymeleaf-spring5</artifactId>
        <version>3.0.12.RELEASE</version>
    </dependency>
</dependencies>
```

注：由于 Maven 的传递性，我们不必将所有需要的包全部配置依赖，而是配置最顶端的依赖，其他靠传递性导入。

![1659947421861](E:\ssm笔记\spring\1659947421861.png)



### 2.3 配置web.xml



**注册SpringMVC的前端控制器DispatcherServlet**



#### ① 默认配置方式

此配置作用下，SpringMVC的配置文件默认位于WEB-INF下，默认名称为：<servlet-name>- servlet.xml

例如，以下配置所对应SpringMVC的配置文件位于WEB-INF下，文件名为：springMVC- servlet.xml

```xml
<!-- 配置SpringMVC的前端控制器，对浏览器发送的请求统一进行处理 -->
<servlet>
    <servlet-name>springMVC</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>springMVC</servlet-name>
    
    <!--

            设置springMVC的核心控制器所能处理的请求的请求路径

            url-pattern中 / 和 /* 的区别
            /  ：匹配浏览器向服务器发送的所有请求（不包括.jsp）
            /* ：匹配浏览器向服务器发送的所有请求（包括.jsp）

     -->
    <url-pattern>/</url-pattern>
</servlet-mapping>
```



#### ② 扩展配置方式

* 通过**init-param标签设置SpringMVC配置文件的位置和名称**

* 通过**load-on-startup标签设置SpringMVC前端控制器DispatcherServlet的初始化时间**

```xml
<servlet>
	<servlet-name>springMVC</servlet-name>
	<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    
    <!-- 1. 通过init-param标签设置SpringMVC配置文件的位置和名称 -->
    <init-param>
        <!-- contextConfigLocation为固定值 -->
        <param-name>contextConfigLocation</param-name>
        <!-- 使用classpath:表示从类路径查找配置文件，例如maven工程中的src/main/resources -->
        <param-value>classpath:springMVC.xml</param-value>
    </init-param>
    
    <!-- 2. 通过load-on-startup标签设置SpringMVC前端控制器DispatcherServlet的初始化时间 

    		作为框架的核心组件，在启动过程中有大量的初始化操作要做而这些操作放在第一次请求时才执行会严重影				响访问速度，因此需要通过此标签将启动控制DispatcherServlet的初始化时间提前到服务器启动时
    -->
	<load-on-startup>1</load-on-startup>
</servlet>

<servlet-mapping>
	<servlet-name>springMVC</servlet-name>
	<url-pattern>/</url-pattern>
</servlet-mapping>
```



### 2.4 创建请求控制器

由于前端控制器对浏览器发送的请求进行了统一的处理（获取请求参数，向域对象存储数据，页面跳转 转发和重定向 ..）

但是具体的请求有不同的处理过程，因此需要创建处理具体请求的类，即**请求控制器**

请求控制器中每一个处理请求的方法成为控制器方法

因为SpringMVC的请求控制器由一个POJO（普通的Java类）担任，因此需要通过@Controller注解将其标识为一个控制层组件，交给Spring的 IOC容器管理，此时SpringMVC才能够识别控制器的存在

```JAVA
@Controller
public class HelloController {

}
```



### 2.5 创建SpringMVC的配置文件

```xml
<!-- 自动扫描包 -->
<context:component-scan base-package="com.lhc.controller"/>

<!-- 配置Thymeleaf视图解析器 -->
<bean id="viewResolver" class="org.thymeleaf.spring5.view.ThymeleafViewResolver">
    <property name="order" value="1"/>
    <property name="characterEncoding" value="UTF-8"/>
    <property name="templateEngine">
        <bean class="org.thymeleaf.spring5.SpringTemplateEngine">
            <property name="templateResolver">
               <bean class="org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver">

                    <!-- 视图前缀 -->
                    <property name="prefix" value="/WEB-INF/templates/"/>

                    <!-- 视图后缀 -->
                    <property name="suffix" value=".html"/>
                    <property name="templateMode" value="HTML5"/>
                    <property name="characterEncoding" value="UTF-8"/>
                </bean>
            </property>
        </bean>
    </property>
</bean>

<!--
	处理静态资源，例如html、js、css、jpg
		若只设置该标签，则只能访问静态资源，其他请求则无法访问
		此时必须设置<mvc:annotation-driven/>解决问题
-->
<mvc:default-servlet-handler/>

<!-- 开启mvc注解驱动 -->
<mvc:annotation-driven>
    
    <mvc:message-converters>
        <!-- 处理响应中文内容乱码 -->
        <bean class="org.springframework.http.converter.StringHttpMessageConverter">
            <property name="defaultCharset" value="UTF-8" />
            <property name="supportedMediaTypes">
                <list>
                    <value>text/html</value>
                    <value>application/json</value>
                </list>
            </property>
        </bean>
    </mvc:message-converters>
    
</mvc:annotation-driven>
```



### 2.6 测试HelloWorld



#### ① 实现对首页的访问

在请求控制器中创建处理请求的方法

```java
// @RequestMapping注解：处理请求和控制器方法之间的映射关系
// 		value属性可以通过请求地址匹配请求
// 		value = "/" 表示当前工程的上下文路径 即：localhost:8080/springmvc01/
@RequestMapping("/")
public String portal() {
    // 返回 逻辑视图
    return "index";
    // 返回后会被配置文件中配置的视图解析器来解析,加上前置后置，通过thymeleaf的渲染 完成视图的跳转
}
```



#### ② 通过超链接跳转到指定页面

在主页index.html中设置超链接

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
	<h1>index.html</h1>
	<a th:href="@{/hello}">测试springmvc</a>   <!-- http://localhost:8080/springmvc01/hello -->
	<a href="/hello">测试绝对路径</a>           <!-- http://localhost:8080/hello -->
</body>
</html>
```

在请求控制器中创建处理请求的方法

```java
// localhost:8080/springmvc01/hello
@RequestMapping("/hello")
public String hello(){
    // 返回 逻辑视图
    return "success"; 
    
}
```

在 templates目录下创建success.html , 这样点击上面那个超链接，就会跳转到此页面中

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
	<h1>success.html</h1>
</body>
</html>
```



### 2.7 总结

* 浏览器发送请求，若请求地址符合前端控制器的url-pattern，该请求就会被前端控制器DispatcherServlet处理。
* 前端控制器会读取SpringMVC的核心配置文件，通过扫描组件找到控制器（HelloController）
* 将请求地址和控制器中 **@RequestMapping**注解的value属性值进行匹配，若匹配成功，该注解所标识的控制器方法就是处理请求的方法。
* 处理请求的方法需要返回一个字符串类型的视图名称(逻辑视图)，该视图名称会被视图解析器解析，加上前缀和后缀组成视图的路径，通过Thymeleaf对视图进行渲染，最终转发到视图所对应页面



## 3、@RequestMapping注解



#### 3.1 @RequestMapping注解的功能

从注解名称上我们可以看到，@RequestMapping注解的作用就是将请求和处理请求的控制器方法关联起来，建立映射关系。

**SpringMVC** 接收到指定的请求，就会来找到在映射关系中对应的控制器方法来处理这个请求。



#### 3.2 @RequestMapping注解的位置

@RequestMapping标识一个类：设置映射请求的请求路径的**初始信息**

@RequestMapping标识一个方法：设置映射请求请求路径的**具体信息**

```java
@Controller 
@RequestMapping("/test")
public class RequestMappingController {

    //此时请求映射所映射的请求的请求路径为：/test/testRequestMapping
    @RequestMapping("/hello")
    public String testRequestMapping(){ 
        return "success";
    }

}
```



#### 3.3 @RequestMapping注解的属性



##### 1）value属性

@RequestMapping注解的 value属性通过请求的请求地址匹配请求映射

@RequestMapping注解的 value属性是一个**字符串类型的数组**，表示该请求映射能够匹配多个请求地址所对应的请求

@RequestMapping注解的 value属性必须设置，至少通过请求地址匹配请求映射

```html
<a th:href="@{/hello}"> 测试@RequestMapping的value属性 </a><br>
<a th:href="@{/abc}"> 测试@RequestMapping的value属性 </a><br>
```

```java
@RequestMapping(
	value = {"/hello", "/abc"}
)
public String testRequestMapping(){ 
	return "success";
}
```



##### 2）method属性

* @RequestMapping注解的method属性通过请求的请求方式（get或post）匹配请求映射

* @RequestMapping注解的method属性是一个**RequestMethod类型的数组**，表示该请求映射能够匹配 多种请求方式的请求

* 若当前请求的请求地址满足请求映射的value属性，但是请求方式不满足method属性，则浏览器报错

	**HTTP Status 405：Request method ' POST ' not supported**   	 
	
	**HTTP Status 405：Request method ' GET ' not supported**

```html
<a th:href="@{/abc}"> 测试@RequestMapping的method属性 </a> <br>
<form th:action="@{/abc}" method="post">
	<input type="submit">
</form>
```

```java
@RequestMapping(
	value = {"/abc"}, 
    method = {RequestMethod.GET, RequestMethod.POST}
)
public String testRequestMapping(){ 
    return "success";
}
```

**注：**

1、对于处理指定请求方式的控制器方法，SpringMVC中提供了@RequestMapping的派生注解  

* 处理 get请求的映射	     -->   @GetMapping
* 处理 post请求的映射       -->   @PostMapping
* 处理 put请求的映射         -->   @PutMapping
* 处理 delete请求的映射    -->   @DeleteMapping

2、常用的请求方式有 get，post，put，delete

* 但是目前浏览器只支持 get 和 post，若在 form表单提交时，为 method设置了其他请求方式的字符串（put或delete），则按照默认的请求方式 get处理

* 若要发送 put 和 delete 请求，则需要通过spring提供的**过滤器HiddenHttpMethodFilter**，在RESTful部分会讲到

	

3、**输入地址让浏览器直接访问，都是get请求**

* 尽管这个地址是表单提交时post请求跳转的地址，如果我们直接复制跳转后的地址，在浏览器访问，就变成get请求

	

##### 3）params属性（了解）

@RequestMapping注解的params属性通过**请求的请求参数 匹配请求映射**

@RequestMapping注解的params属性是一个**字符串类型的数组**，可以通过四种表达式设置请求参数和请求映射的匹配关系

* "param"：要求请求映射所匹配的请求**必须携带**param请求参数

* "!param"：要求请求映射所匹配的请求必须**不能携带**param请求参数

* "param=value"：要求请求映射所匹配的请求**必须携带param请求参数且param=value**

* "param!=value"：要求请求映射所匹配的请求，**要么不携带param请求参数，要么携带param请求参数不能为value**


若当前请求满足@RequestMapping注解的value和method属性，但是**不满足params属性**，此时页面回报错

**HTTP Status 400 - Parameter conditions "username, !password, age=20, gender!=女" not met for actual request parameters: username={admin}, age={20}, gender={女}**

```html
<a th:href="@{/hello?username=admin&age=20}">测试@RequestMapper注解的params属性</a> <br>
```

```java
@RequestMapping(
    value = {"/hello","/abc"},
    method = {RequestMethod.GET,RequestMethod.POST},
    params = {"username","!password","age=20","gender!=女"}
)
public String testRequestMapping(){
    return "success";
}
```



##### 3）headers属性（了解）

@RequestMapping注解的headers属性通过**请求的请求头信息 匹配请求映射**

@RequestMapping注解的headers属性是一个字符串类型的数组，可通过四种表达式设置请求头信息和请求映射的匹配关系

* "header"：要求请求映射所匹配的请求必须携带 header 请求头信息                   

* "!header"： 要求请求映射所匹配的请求必须不能携带 header 请求头信息 

* "header=value"：要求请求映射所匹配的请求必须携带 header 请求头信息且 header=value 

* "header!=value"：要求请求映射所匹配的请求 要么不携带 header请求头信息。要么携带请求头信息不能为value

	

	若当前请求满足@RequestMapping注解的value和method属性，但是不满足headers属性，此时页面显示

	404错误，即资源未找到  **HTTP Status 404 -**

```java
@RequestMapping(
    value = {"/hello","/abc"},
    headers = {"referer"}
)
public String testRequestMapping(){
    return "success";
}
```



#### 3.4 value属性值

* SpringMVC支持ant风格的路径

* SpringMVC支持路径的占位符



##### 1）ant风格的路径

?：	  表示任意的单个字符 
*：		表示任意的0个或多个字符
**：	   表示任意层数的任意目录

注意：

* 在使用 ** 时，只能使用 /**/  的方式
* 不能匹配 ? ，因为 ?后面的字符都会被解析成请求参数

```HTML
<a th:href="@{/a/az/dc/b/test/ant}"> 测试@RequestMapper注解支持ant风格的路径 </a>
```

```java
@RequestMapping("/**/test/ant")
public String testAnt(){
    return "success";
}
```



##### 2）路径中的占位符（重点）



原始方式：/deleteUser?id=1 

rest方式：/user/delete/1

SpringMVC路径中的占位符常用于RESTful风格中，当请求路径中将**某些数据通过路径的方式传输到服务器中**，就可以在相应的@RequestMapping注解的value属性中通过占位符{xxx}表示传输的数据，在通过@PathVariable注解，将占位符所表示的数据赋值给控制器方法的形参

```html
<a th:href="@{/test/rest/1/admin}"> 测试@RequestMapper注解的value属性中占位符 </a> 
```

```java
@RequestMapping("/test/rest/{id}/{username}")
public String testRest(@PathVariable("id") Integer id, @PathVariable("username") String username) {
    System.out.println("id：" + id + "，username；" + username);
    return "success";
}
//最终输出的内容为--> id：1，username：admin
```



## 4、SpringMVC获取请求参数



#### 4.1 通过ServletAPI获取

将HttpServletRequest作为控制器方法的形参，此时HttpServletRequest类型的参数表示封装了当前请求的请求报文的对象

```html
<form th:action="@{/testParam}" method="post">
	用户名：<input type="text" name="username">
	密码：<input type="text" name="password">
	<input type="submit"value="登录">
</form>
```

```java
@RequestMapping("/testParam")
public String testParam1(HttpServletRequest request){ 
    String username = request.getParameter("username"); 
    String password = request.getParameter("password");
    System.out.println("username:"+username+",password:"+password); 
    return "success";
}
```



#### 4.2 通过控制器方法的形参获取

在**控制器方法的形参位置**，**设置和请求参数同名的形参**，当浏览器发送请求，匹配到请求映射时，在 DispatcherServlet中就会将请求参数赋值给相应的形参

```html
<form th:action="@{/testParam}" method="post">
	用户名：<input type="text" name="username">
	密码：<input type="text" name="password">
	<input type="submit"value="登录">
</form>
```

```java
@RequestMapping("/testParam")
public String testParam2(String username, String password){ 
    
    System.out.println("username:"+username+",password:"+password); 
    return "success";
    
}
```

控制器方法的形参位置**，**设置和请求参数同名的形参不一致。就需要使用 **@RequestParam**指定请求参数

```java
@RequestMapping("/testParam")
public String testParam2(@RequestParam("username") String name, String password){ 
    
    System.out.println("username:" + name + ",password:" + password); 
    return "success";
    
}
```



注：

若请求所传输的请求参数中有**多个同名的请求参数，**此时可以在控制器方法的形参中设置**字符串数组**或者**字符串类型的形参**接收此请求参数

* 若使用字符串数组类型的形参，此参数的数组中包含了每一个数据

* 若使用字符串类型的形参，此参数的值为每个数据中间使用逗号拼接的结果



##### 1）@RequestParam

@RequestParam 是将**请求参数**和**控制器方法的形参**创建映射关系

@RequestParam 注 解一共有三个属 性 ：     

* **value：**指定为形参赋值的请求参数的参数名

* **required**：设置是否必须传输此请求参数（默认为true）

	 若true：则当前请求必须传输 value所指定的请求参数，否则 400：Required String parameter 'xxx' is not present；

	若false：则当前请求不是必须传输value所指定的请求参数，若没有传输，则注解所标识的形参的值为 null

* **defaultValue：**当value所指定的请求参数**没有传输**或**传输的值为""**时，则使用默认值为形参赋值

	注意：**defaultValue的优先级比required高，当设置了defaultValue时，required设不设置就不重要了**（没有传值就使用默认值，有就用有的，即不可能出现未传输请求参数的情况）

```java
@RequestMapping("/testParam")
public String testParam2(
    @RequestParam(value = "username", required = false, defaultValue = "hello") String uname,
    String password
    ) {
        System.out.println("username:" + uname + ",password:" + password);
        return "success";
}	
```



##### 2）@RequestHeader

@RequestHeader是将请求头信息和控制器方法的形参创建映射关系

@RequestHeader注解一共有三个属性：value、required、defaultValue，用法同@RequestParam

```java
@RequestMapping("/testParam")
public String testParam2(@RequestHeader("referer") String referer) {
        System.out.println("referer" + referer);
        return "success";
}
```



##### 3）@CookieValue

@CookieValue是将cookie数据和控制器方法的形参创建映射关系

@CookieValue注解一共有三个属性：value、required、defaultValue，用法同@RequestParam

```java
@RequestMapping("/testParam")
public String testParam2(@CookieValue("JSESSIONID") String jsessionid) {
        System.out.println("jsessionid:" + jsessionid);
        return "success";
}
```



##### 4） 通过POJO获取请求参数
可以在控制器方法的形参位置设置一个实体类类型的形参，此时若浏览器传输的请求参数的**参数名**和实体类中的**属性名**一致，那么请求参数就会为此属性赋值

```java
public class User {
    private Integer id;
    private String username;
    private String password;
    
    //有参 无参 构造器
    //get set 方法
    //toString方法
}  
```

```html
<form th:action="@{/testpojo}" method="post">
	用户名：<input type="text" name="username"><br>
	密码：<input type="password" name="password"><br>
	性别：<input type="radio" name="sex" value="男">男<input type="radio" name="sex" value="女">女		<br>
	年龄：<input type="text" name="age"><br>
	邮箱：<input type="text" name="email"><br>
	<input type="submit">
</form>
```

```java
@RequestMapping("/testpojo") 
public String testPOJO(User user){
    System.out.println(user); 
    return "success";
}
//最终结果-->User{id=null, username='张三', password='123', age=23, sex='男', email='123@qq.com'}
```



#### 4.3 解决获取请求参数的乱码问题

解决获取请求参数的乱码问题，可以使用SpringMVC提供的编码过滤器CharacterEncodingFilter，但是必须在web.xml中进行注册

```xml
<!--配置springMVC的编码过滤器-->
<filter>
    
    <filter-name>CharacterEncodingFilter</filter-name>
    <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
    
    <init-param>
        <param-name>encoding</param-name>
        <param-value>UTF-8</param-value>
    </init-param>
    <init-param>
        <param-name>forceEncoding</param-name>
        <param-value>true</param-value>
    </init-param>
    
</filter>

<filter-mapping>
    <filter-name>CharacterEncodingFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

注：
**SpringMVC中处理编码的过滤器一定要配置到其他过滤器之前，否则无效**



## 5、域对象共享数据 （model）



### 5.1 向request请求域共享数据

#### 1）使用ServletAPI

```java
@RequestMapping("/testServletAPI")
public String testServletAPI(HttpServletRequest request){ 
    request.setAttribute("testScope", "hello,servletAPI"); 
    return "success";
}
```



#### 2） 使用ModelAndView

```java
@RequestMapping("/test/mav") 
public ModelAndView testModelAndView(){
    /**
    * ModelAndView有Model和View的功能
    * Model：主要用于向请求域共享数据
    * View：主要用于设置视图，实现页面跳转
    */
    ModelAndView mav = new ModelAndView();
    
    //1）向请求域共享数据
    mav.addObject("testScope", "hello,ModelAndView");
    //2）设置视图，实现页面跳转
    mav.setViewName("success"); 
    return mav; 
}
```







#### 3）Model 、map、ModelMap



这三种方法，底层都会将数据封装到**ModelAndView**中，来向请求域中共享数据



##### ① 使用Model

```java
@RequestMapping("/test/model")
public String testModel(Model model){ 
    model.addAttribute("testScope", "hello,Model"); 
    return "success";
}
```



##### ② 使用map

```java
@RequestMapping("/test/map")
public String testMap(Map<String, Object> map){ 
    map.put("testScope", "hello,Map");
	return "success";
}
```



##### ③ 使用ModelMap

```java
@RequestMapping("/test/modelMap")
public String testModelMap(ModelMap modelMap){ 
    modelMap.addAttribute("testScope", "hello,ModelMap"); 
    return "success";
}
```



##### ④ Model、ModelMap、Map的关系

Model、ModelMap、Map类型的参数其实本质上都是 **BindingAwareModelMap** 类型的

```java
public class BindingAwareModelMap extends ExtendedModelMap {}

public class ExtendedModelMap extends ModelMap implements Model {} 

public class ModelMap extends LinkedHashMap<String, Object> {}

public interface Model{}
```





### 5.2 向session会话域共享数据

```java
@RequestMapping("/test/session")
public String testSession(HttpSession session){ 
    session.setAttribute("testSessionScope", "hello,session"); 
    return "success";
}
```



### 5.3 向application应用域共享数据

```java
@RequestMapping("/test/application")
public String testApplication(HttpSession session){
    //先从session中获取application
    ServletContext application = session.getServletContext();
    application.setAttribute("testApplicationScope", "hello,application");
    return "success";
}
```



## 6、SpringMVC的视图 （view）

SpringMVC中的视图是View接口，视图的作用渲染数据，将模型Model中的数据展示给用户

SpringMVC视图的种类很多，默认有**转发视图**和**重定向视图**  

* 当工程引入jstl的依赖，转发视图会自动转换为**JstlView**
* 若使用的视图技术为Thymeleaf，在SpringMVC的配置文件中配置了Thymeleaf的视图解析器，由此视图解析器解析之后所得到的是**ThymeleafView**



### 6.1 ThymeleafView

当控制器方法中所设置的**视图名称没有任何前缀**时，此时的视图名称会被SpringMVC配置文件中所配置的视图解析器解析，视图名称拼接视图前缀和视图后缀所得到的最终路径，会通过转发的方式实现跳转

```java
@RequestMapping("/test/view/thymeleaf")
public String testThymeleafView(){
    return "success";
}
```

![1660100517672](E:\ssm笔记\spring\1660100517672.png)



内层步骤：

```java
//浏览器发送请求，

//【DispatcherServlet内】 调用 handle（控制器方法），获得一个ModelAndView对象
mv = ha.handle(processedRequest, response, mappedHandler.getHandler());

// 【控制器方法中】 返回一个视图名称
return: "success";

//【DispatcherServlet内】 执行处理转发结果
this.processDispatchResult(processedRequest, response, mappedHandler, mv, (Exception)dispatchException);  ==进入该给方法==》

//【DispatcherServlet内】视图渲染 （将mv中的数据共享到请求域中，根据逻辑视图名称 创建相对应的视图）
this.render(mv, request, response); ==进入到该方法==》

//【DispatcherServlet内】获取视图名称（success）
String viewName = mv.getViewName(); 
// 解析视图名称 获取视图(Thymeleaf)
View view = this.resolveViewName(viewName, mv.getModelInternal(), locale, request);
```









### 6.2 InternalResourceView（转发视图）

​	SpringMVC中默认的转发视图是**InternalResourceView** 

SpringMVC中创建转发视图的情况：当控制器方法中所设置的**视图名称以"forward:"为前缀**时，创InternalResourceView视图，此时的**视图名称不会被SpringMVC配置文件中所配置的ThymeleafView视图解析器解析**，而是会将前缀"forward:"去掉，剩余部分作为最终路径通过转发的方式实现跳转

① 视图名称以"forward:"为前缀，创建InternalResourceView视图 ，前缀去掉，服务器内部转发

```java
@RequestMapping("/test/view/forward")
public String testInternalResourceView(){
    //转发到：/test/mav
    return "forward:/test/mav";
}
```

![1660101238688](E:\ssm笔记\spring\1660101238688.png)

② 服务器内部转发到此处，就会再创建一个 **ThymeleafView**视图 （此时，就是单纯的在服务器端页面跳转，不会经过Thymeleaf解析 视图渲染）

```java
@RequestMapping("/test/mav")
public ModelAndView testModelAndView(){
    ModelAndView mav = new ModelAndView();
    //1）向请求域共享数据
    mav.addObject("testScope", "hello,ModelAndView");
    //2）设置视图，实现页面跳转
    mav.setViewName("success");
    return mav;
}
```

![1660102426621](E:\ssm笔记\spring\1660102426621.png)



### 6.3 RedirectView （重定向视图）

SpringMVC中默认的重定向视图是**RedirectView**

当控制器方法中所设置的**视图名称以"redirect:"为前缀**时，创建RedirectView视图，此时的视图名称不  会被SpringMVC配置文件中所配置的视图解析器解析，而是会将前缀"redirect:"去掉，剩余部分作为最终路径通过重定向的方式实现跳转

​	

① 视图名称以"redirect:"为前缀**时，创建RedirectView视图，前缀去掉，重定向  /test/mav

（ 服务器重定向  /test/mav， 将 / 解析为 http://localhost:8080  并且会自动添加上下文路径 sprigmvc05 ）

```JAVA
@RequestMapping("/test/view/redirect")
public String testRedirectView(){
    //重定向到：/test/mav
    return "redirect:/test/mav";
}
```

![1660103744804](E:\ssm笔记\spring\1660103744804.png)

② 重定向，让浏览器再次发送请求，到这里

```java
@RequestMapping("/test/mav")
public ModelAndView testModelAndView(){
    ModelAndView mav = new ModelAndView();
    //1）向请求域共享数据
    mav.addObject("testScope", "hello,ModelAndView");
    //2）设置视图，实现页面跳转
    mav.setViewName("success");
    return mav;
}
```

![1660104093322](E:\ssm笔记\spring\1660104093322.png)



注：

**重定向视图在解析时，会先将redirect:前缀去掉，然后会判断剩余部分是否以/开头，若是则会自动拼接上下文路径**



### 6.4 视图控制器view-controller

当控制器方法中，**仅仅用来实现页面跳转**，即只需要设置视图名称时，可以将处理器方法使用view- controller标签进行表示

```XML
<!--
	视图控制器：为当前请求直接设置视图名称实现页面跳转
         path：设置处理的请求地址
         view-name：设置请求地址所对应的视图名称
-->
<mvc:view-controller path="/" view-name="index"></mvc:view-controller>
```

注：
当SpringMVC中**设置任何一个view-controller时**，**其他控制器中的请求映射将全部失效**，此时需  要在SpringMVC的核心配置文件中**设置开启mvc注解驱动**的标签：<mvc:annotation-driven />

```XML
<!-- 设置开启mvc注解驱动 -->
<mvc:annotation-driven />
```













## 7、RESTful



### 7.1RESTful简介

​	REST：Representational State Transfer，表现层资源状态转移。

#### ①资源

资源是一种看待服务器的方式，即，将服务器看作是由很多离散的资源组成。每个资源是服务器上一个  可命名的抽象概念。因为资源是一个抽象的概念，所以它不仅仅能代表服务器文件系统中的一个文件、  数据库中的一张表等等具体的东西，可以将资源设计的要多抽象有多抽象，只要想象力允许而且客户端  应用开发者能够理解。与面向对象设计类似，资源是以名词为核心来组织的，首先关注的是名词。一个资源可以由一个或多个URI来标识。URI既是资源的名称，也是资源在Web上的地址。对某个资源感兴趣的客户端应用，可以通过资源的URI与其进行交互。

#### ② 资源的表述

资源的表述是一段对于资源在某个特定时刻的状态的描述。可以在客户端-服务器端之间转移（交换）。资源的表述可以有多种格式，例如 HTML/XML/JSON/纯文本/图片/视频/音频 等等。资源的表述格式可以通过协商机制来确定。请求-响应方向的表述通常使用不同的格式。

#### ③ 状态转移

状态转移说的是：在客户端和服务器端之间转移（transfer）代表资源状态的表述。通过转移和操作资源的表述，来间接实现操作资源的目的。



### 7.2 RESTful的实现



具体说，就是 HTTP 协议里面，四个表示操作方式的动词：GET、POST、PUT、DELETE。

它们分别对应四种基本操作：**GET 用来获取资源，POST 用来新建资源，PUT 用来更新资源，DELETE 用来删除资源**

**REST** 风格提倡 URL地址使用统一的风格设计，从前到后各个单词使用斜杠分开，不使用问号键值对方式携带请求参数，而是将要发送给服务器的数据作为 URL 地址的一部分，以保证整体风格的一致性。

| 操作     | 传统方式         | REST风格                |
| -------- | ---------------- | ----------------------- |
| 查询操作 | getUserById?id=1 | user/1-->get请求方式    |
| 保存操作 | saveUser         | user-->post请求方式     |
| 删除操作 | deleteUser?id=1  | user/1-->delete请求方式 |
| 更新操作 | updateUser       | user-->put请求方式      |



### 7.3 HiddenHttpMethodFilter

由于浏览器只支持发送get和post方式的请求，那么该如何发送put和delete请求呢？

SpringMVC 提供了 HiddenHttpMethodFilter 帮助我们将 POST 请求转换为 DELETE 或 PUT 请求HiddenHttpMethodFilter                                       处 理 put 和 delete 请 求 的 条 件 ： 

* 当前请求的请求方式必须为post
* 当前请求必须传输请求参数 _method

满足以上条件，HiddenHttpMethodFilter 过滤器就会将当前请求的请求方式转换为请求参数
_method 的值，因此请求参数  _method的值才是最终的请求方式

在web.xml中注册**HiddenHttpMethodFilter**

```xml
<filter>
	<filter-name>HiddenHttpMethodFilter</filter-name>
	<filter-class>org.springframework.web.filter.HiddenHttpMethodFilter</filter-class>
</filter>
<filter-mapping>
	<filter-name>HiddenHttpMethodFilter</filter-name>
	<url-pattern>/*</url-pattern>
</filter-mapping>
```

目前为止，SpringMVC中提供了两个过滤器：CharacterEncodingFilter和HiddenHttpMethodFilter

在web.xml中注册时，必须先注册CharacterEncodingFilter，再注册HiddenHttpMethodFilter 原因：

在 CharacterEncodingFilter 中通过 request.setCharacterEncoding(encoding) 方法设置字符集的
request.setCharacterEncoding(encoding) 方法要求前面不能有任何获取请求参数的操作而 HiddenHttpMethodFilter 恰恰有一个获取请求方式的操作： **String paramValue = request.getParameter(this.methodParam);**

```java
/**
 * @Author: 刘华昌
 * @Date: 2022/8/10 20:14
 * @ClassName:
 * @Description:
 * 查询所有的用户信息 --> /user --> get
 * 根据id查询用户信息 --> /user/1 --> get
 * 添加用户信息 --> /user --> post
 * 修改用户信息 --> /user --> put
 * 删除用户信息 --> /user/1 --> delete
 */
@Controller
public class TestRestController {

    @GetMapping("/user")
    public String getAllUser() {
        System.out.println("查询所有的用户信息 --> /user --> get");
        return "success";
    }

    @GetMapping("/user/{id}")
    public String getUserById(@PathVariable("id") Integer id) {
        System.out.println("根据id查询用户信息 --> /user/" + id + " --> get");
        return "success";
    }

    @PostMapping("/user")
    public String insertUser() {
        System.out.println("添加用户信息 --> /user --> post");
        return "success";
    }

    @PutMapping("/user")
    public String updateUser() {
        System.out.println("修改用户信息 --> /user --> put");
        return "success";
    }

    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable("id") Integer id) {
        System.out.println("删除用户信息 --> /user/" + id + " --> delete");
        return "success";
    }

}

```

```html
<a th:href="@{/user}"> 查询所有的用户信息 </a> <br>

<a th:href="@{/user/1}"> 根据id查询用户信息 </a> <br>

<form th:action="@{/user}" method="post">
    <input type="submit" value="添加用户信息">
</form>

<form th:action="@{/user}" method="post">
    <input type="hidden" name="_method" th:value="put">
    <input type="submit" value="修改用户信息">
</form>

<form th:action="@{/user/1}" method="post">
    <input type="hidden" name="_method" th:value="delete">
    <input type="submit" value="删除用户信息">
</form>
```





HiddenHttpMethodFilter将过滤器就会将当前请求的请求方式转换为请求参数_method 的值

源码分析

```html
<form th:action="@{/user}" method="post">
    <input type="hidden" name="_method" th:value="put">
    <input type="submit" value="修改用户信息">
</form>
```

HiddenHttpMethodFilter：

```java

//

private String methodParam = "_method";

/**
 * 过滤器方法 
 *
 * @param request:请求
 * @param response:响应
 * @param filterChain:过滤器链 用来放行的
 */
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    
        HttpServletRequest requestToUse = request;
    	//如果当前的请求方式为post，并且没有异常 （满足条件进入方法）
        if ("POST".equals(request.getMethod()) && request.getAttribute("javax.servlet.error.exception") == null) {
            
            // 获取name为_method 请求参数值（put）
            String paramValue = request.getParameter(this.methodParam);
            
            if (StringUtils.hasLength(paramValue)) { 
                // put -> PUT
                String method = paramValue.toUpperCase(Locale.ENGLISH);
                //检查ALLOWED_METHODS是否有 PUT这个值
                if (ALLOWED_METHODS.contains(method)) {
                    // 创建一个新的请求对象 请求方式为PUT
                    requestToUse = new HiddenHttpMethodFilter.HttpMethodRequestWrapper(request, method);
                }
            }
        }
		//放行 
        filterChain.doFilter((ServletRequest)requestToUse, response);
    }


```





## 8、RESTful案例

### 8.1 准备工作

和传统 CRUD 一样，实现对员工信息的增删改查。

#### 1）搭建环境

​	① pom.xml

​	② web.xml

​	③ springmvc.xml 

#### 2）准备实体类

```java
package com.atguigu.mvc.bean;

public class Employee {

	private Integer id; 
    
    private String lastName;

	private String email;
    
	private Integer gender; 	//1 male, 0 female 

    	
	// get set 有参无参构造器 

```



#### 3）准备dao模拟数据

```java
package com.lhc.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.lhc.pojo.Employee;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeeDao {

    private static Map<Integer, Employee> employees = null;

    static {
        employees = new HashMap<Integer, Employee>();

        employees.put(1001, new Employee(1001, "E-AA", "aa@163.com", 1));
        employees.put(1002, new Employee(1002, "E-BB", "bb@163.com", 1));
        employees.put(1003, new Employee(1003, "E-CC", "cc@163.com", 0));
        employees.put(1004, new Employee(1004, "E-DD", "dd@163.com", 0));
        employees.put(1005, new Employee(1005, "E-EE", "ee@163.com", 1));
    }

    private static Integer initId = 1006;

    public void save(Employee employee) {
        
        if (employee.getId() == null) {
            employee.setId(initId++);
        }
        employees.put(employee.getId(), employee);
        
    }

    public Collection<Employee> getAll() {
        return employees.values();
    }

    public Employee get(Integer id) {
        return employees.get(id);
    }

    public void delete(Integer id){
        employees.remove(id);
    }
}
```



#### 4）准备Controller

```java
/**
 * @Author: 刘华昌
 * @Date: 2022/8/10 21:32
 * @ClassName:
 * @Description: 查询所有的有员工信息 --> /employee --> get
 * 跳转到添加页面 --> /to/add --> get
 * 添加员工信息 --> /employee --> post
 * 跳转到修改页面 --> /employee/1 -->get
 * 修改员工信息 --> /employee --> put
 * 删除员工信息 --> /employee/1 --> delete
 */
@Controller
public class EmployeeController {

    @Autowired
    private EmployeeDao employeeDao;
    
}
```



#### 5）功能清单

| **功能**            | **URL** **地址** | **请求方式** |
| ------------------- | ---------------- | ------------ |
| 访问首页√           | /                | GET          |
| 查询全部数据√       | /employee        | GET          |
| 删除√               | /employee/1      | DELETE       |
| 跳转到添加数据页面√ | /to/add          | GET          |
| 执行保存√           | /employee        | POST         |
| 跳转到更新数据页面√ | /employee/1      | GET          |
| 执行更新√           | /employee        | PUT          |



### 8.2  功能实现



#### 1）访问首页

① 配置 view-controller

```XML
<mvc:view-controller path="/" view-name="index"/>
```

② 创建 首页

```HTML
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8" >
    <title>Title</title>
</head>
<body>
<h1>首页</h1>
<a th:href="@{/employee}">访问员工信息</a>
</body>
</html>
```



#### 2）查询所有员工信息

① 控制器方法

```java
@GetMapping("/employee")
public String getAllEmployee(Model model) {
    //获取所有员工信息
    Collection<Employee> allEmp = employeeDao.getAll();
    //将所有的员工信息在请求域中共享数据
    model.addAttribute("allEmployee", allEmp);
    //跳转到列表页面
    return "employee_list";
}
```

② 创建 employee_list.html

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Employee Info</title>
    <script type="text/javascript" th:src="@{/static/js/vue.js}"></script>
</head>
<body>

    <table border="1" cellpadding="0" cellspacing="0" style="text-align: center;" id="dataTable">
        <tr>
            <th colspan="5">Employee Info</th>
        </tr>
        <tr>
            <th>id</th>
            <th>lastName</th>
            <th>email</th>
            <th>gender</th>
            <th>options(<a th:href="@{/toAdd}">add</a>)</th>
        </tr>
        <tr th:each="employee : ${employeeList}">
            <td th:text="${employee.id}"></td>
            <td th:text="${employee.lastName}"></td>
            <td th:text="${employee.email}"></td>
            <td th:text="${employee.gender}"></td>
            <td>
            </td>
        </tr>
    </table>
</body>
</html>
```



#### 3） 删除员工信息

① 首页添加 处理delete请求方式的表单

```html
<!-- 作用：通过超链接控制表单的提交，将post请求转换为delete请求 -->
<form id="delete_form" method="post">
    <!-- HiddenHttpMethodFilter要求：必须传输_method请求参数，并且值为最终的请求方式 -->
    <input type="hidden" name="_method" value="delete"/>
</form>
```



② 首页添加 删除超链接 并绑定点击事件

- 引入vue.js

```html
<script type="text/javascript" th:src="@{/static/js/vue.js}"></script>
```

- 删除超链接

```html
<a class="deleteA" @click="deleteEmployee" th:href="@{'/employee/'+${employee.id}}">delete</a>
```

- 通过vue处理点击事件

```html
<script type="text/javascript">
    var vue = new Vue({
        el:"#dataTable",
        methods:{
            //event表示当前事件
            deleteEmployee:function (event) {
                //通过id获取表单标签
                var delete_form = document.getElementById("delete_form");
                //将触发事件的超链接的href属性为表单的action属性赋值
                delete_form.action = event.target.href;
                //提交表单
                delete_form.submit();
                //阻止超链接的默认跳转行为
                event.preventDefault();
            }
        }
    });
</script>
```



③ 控制器方法

```java
@DeleteMapping (value = "/employee/{id}")
public String deleteEmployee(@PathVariable("id") Integer id){
    //删除
    employeeDao.delete(id);
    //重定向到列表功能 /employee  (服务器重新发送请求，默认为get请求，即被getAllEmployee处理)
    return "redirect:/employee";
}
```



#### 4）跳转到添加数据页面

① 配置view-controller

```xml
<!-- 这个功能 我们仅需要页面跳转功能，所以可以直接写到 springmvc.xml文件中 -->
<mvc:view-controller path="/to/add" view-name="employee_add"></mvc:view-controller>
```

②创建 employee_add.html

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>add employee</title>
</head>
<body>

<form th:action="@{/employee}" method="post">
    lastName：<input type="text" name="lastName"><br>
    email：<input type="text" name="email"><br>
    gender：<input type="radio" name="gender" value="1">male
    <input type="radio" name="gender" value="0">female <br>
    <input type="submit" value="add"> <br>
</form>

</body>
</html>
```



#### 5）执行保存
① 控制器方法

```java
@PostMapping("/employee")
public String addEmployee(Employee employee){
    //保存员工信息
    employeeDao.save(employee);
    //重定向到列表功能 /employee  (服务器重新发送请求，默认为get请求，即被getAllEmployee处理)
    return "redirect:/employee";
}
```



#### 6）跳转到更新数据页面
① 首页添加 修改的超链接

```html
<a th:href="@{'/employee/'+${employee.id}}">update</a>
```

② 控制器方法

```java
@GetMapping("/employee/{id}")
public String updateEmployee(@PathVariable("id") Integer id, Model model){
    //根据id查询员工信息
    Employee employee = employeeDao.get(id);
    //将员工信息共享到请求域中
    model.addAttribute("employee",employee);
    //跳转到employee_update
    return "employee_update";
}
```



③创建employee_update.html

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
<meta charset="UTF-8">
<title>Update Employee</title>
</head>
<body>

<form th:action="@{/employee}" method="post">
    <input type="hidden" name="_method" value="put">
    <input type="hidden" name="id" th:value="${employee.id}">
    lastName:<input type="text" name="lastName" th:value="${employee.lastName}"> <br>
    email:<input type="text" name="email" th:value="${employee.email}"><br>
    <!--
		th:field="${employee.gender}"可用于单选框或复选框的回显
		若单选框的value和employee.gender的值一致，则添加checked="checked"属性
	-->
    gender:<input type="radio" name="gender" value="1" th:field="${employee.gender}">male
    <input type="radio" name="gender" value="0" th:field="${employee.gender}">female<br>
    <input type="submit" value="update"><br>
</form>

</body>
</html>
```



#### 7）执行更新

①控制器方法

```java
@PutMapping(value = "/employee")
public String updateEmployee(Employee employee) {
    //执行修改
    employeeDao.save(employee);
    //重定向到列表功能 /employee  (服务器重新发送请求，默认为get请求，即被getAllEmployee处理)
    return "redirect:/employee";
}
```



### 8.3 处理静态资源

① 我们在**此工程**中的**web.xml**文件中配置了一个 SpringMVC的前端控制器**DispatcherServlet**

**它不能处理静态资源，只作用于当前工程**

```xml
<servlet>
    <servlet-name>springmvc</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:springmvc.xml</param-value>
    </init-param>
    <load-on-startup>1</load-on-startup>
</servlet>

<servlet-mapping>
    <servlet-name>springmvc</servlet-name>
    <url-pattern>/</url-pattern>
</servlet-mapping>
```

② 在我们的**tomcat**中的web.xml中也有一个 **DefaultServlet**

用于**处理静态资源  作用于 使用它的所有工程**

```xml
<servlet>
    <servlet-name>default</servlet-name>
    <servlet-class>org.apache.catalina.servlets.DefaultServlet</servlet-class>
    <init-param>
        <param-name>debug</param-name>
        <param-value>0</param-value>
    </init-param>
    <init-param>
        <param-name>listings</param-name>
        <param-value>false</param-value>
    </init-param>
    <load-on-startup>1</load-on-startup>
</servlet>

<servlet-mapping>
    <servlet-name>default</servlet-name>
    <url-pattern>/</url-pattern>
</servlet-mapping>
```



**逻辑上，此工程中的web.xml 会继承 tomcat中的web.xml，** 即先遵循 此工程中web.xml的配置，此工程中web.xml没有配置的就遵循 tomcat中的web.xml，**当两个web.xml有相同配置的不同处理时(冲突)，以此工程为准**

**此工程中web.xml的DispatcherServlet** 和 **tomcat中web.xml的DefaultServlet**  ，**都是被设置为处理  / （所有请求**）

**当配置冲突时以此过程中的设置为准（子类优先）**，**只会让此工程的web.xml的DispatcherServlet 去处理所有的请求**，这样就导致了 此工程现在不能处理静态资源

③ 解决：springmvc.xml中设置标签，让**产生冲突的配置**，先让DispatcherServlet处理，处理不了的再让DefaultServlet

```xml
<!-- 注意：下面两个缺一不可 -->

<!--1、开放对静态资源的访问-->
<mvc:default-servlet-handler />

<!--2、设置开启mvc注解驱动 -->
<mvc:annotation-driven /> 
```







## 9、SpringMVC处理ajax请求



### 9.1 回顾 ajax

```html


testAjax () {
    //axios表示将要发送一个异步请求
    axios({
        method:"",              //发送方式
        url:"",                 //发送地址
        params:{},              //发送参数 (普通参数用params) （也可以直接拼接到url中）
        data:{}                 //发送参数 (JSON格式参数用data )
    })
        .then(function (value) {    //当有成功回应时 执行里面的方法  （回调）

            console.log(value);     // value.data可以获取到服务器响应内容
        })
        .catch(function (reason) {  //当没有成功给我回应时 执行这里的方法 ()

            console.log(reason);    // reason.response.data可以获取到服务器响应的的内容
                                    // reason.message / reason.stack 可以查看错误信息
        })
}

<!--

    关于 data、 params
    1、params
        1）以name=value&name=value的方式发送请求参数
        2）不管使用的请求方式是GET还是POST，请求参数都会被拼接到URL地址后
        3）这种方式的请求参数可以通过 request.getParameter()获取

    2、data
        1）以json格式发送请求参数
        2）请求参数会被保护到请求报文的请求体传输到服务器
        3）只适用于 POST PUT PATCH 这些请求方式
        3）这种方式的请求参数不可以通过 request.getParameter()获取，需要使用流来获取

-->
```



在使用了axios发送ajax请求之后，浏览器发送到服务器的请求参数有两种格式：

1、普通格式：name=value&name=value...

* 之前，请求参数可以通过**request.getParameter()**获取
* 在SpringMVC中，可以**直接通过控制器方法的形参获取此类请求参数**

2、json格式：{key:value,key:value,...}

* 此时无法通过request.getParameter()获取
* 之前，使用操作json的相关jar包gson或jackson处理此类请求参数，可以将其转换为指定的**实体类对象**或**map集合**。
* 在SpringMVC中，直接使用**@RequestBody注解**标识控制器方法的形参即可将此类请求参数转换为指定的 **json字符串，实体类对象、map集合 ...**



### 9.2 SpringMVC中获取普通格式参数



① 在浏览器向服务器端发送一个 ajax请求

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>首页</title>
</head>
<body>

<!-- 被Vue挂载的div -->
<div id="app">
    <!-- 通过点击事件，调用testAjax() 向服务器端发送异步请求 -->
    <input type="button" value="测试SpringMVC处理ajax请求" @click="testAjax()">
</div>

</body>
</html>
```

通过vue和axios处理点击事件：

```html
<script type="text/javascript" th:src="@{/js/vue.js}"></script>
<script type="text/javascript" th:src="@{/js/axios.min.js}"></script>
<script type="text/javascript">
    var vue = new Vue({
        //设置挂载容器
        el: "#app",
        methods: {
            
            testAjax() {
                //发送异步请求
                axios.post(    //直接使用 .post表示发送post请求
                 
                   //将 param的请求参数直接拼接到 url地址发送给服务器 (springmvc08为上下文路径）
                   url:"/springmvc08/test/ajax?id=1001", 
            
                ).then(response => { //接收浏览器正常运行 响应回来的数据
                    console.log(response.data);
                });
            }
            
        }
    })
</script>
```

② 在服务器处理浏览器 ajax请求

```java
@RequestMapping("/test/ajax") 
public void testAjax(Integer id, HttpServletResponse response) throws IOException {
    System.out.println("id:" + id); // 1001
    //向浏览器响应数据
    response.getWriter().write("hello,ajax"); 
}
```





### 9.2 SpringMVC中获取json格式参数



Json格式的请求参数是被存放在 请求报文的请求体当中 传输给服务器的，@RequestBody 可以**获取请求体**，需要在控制器方法设置一个形参，使用@RequestBody进行标识，当前请求的**请求体**就会为当前注解所标识的**形参赋值**



**使用@RequestBody注解 获取JSON格式数据的步骤**：

① 导入jackson的依赖

```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.12.1</version>
</dependency>
```



② 在SpringMVC的核心配置文件中开启mvc的注解驱动

```xml
<mvc:annotation-driven />
```



③ 浏览器向服务器端发送 ajax请求 （使用data发送json格式的请求参数）

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>首页</title>
</head>
<body>

<!-- 被Vue挂载的div -->
<div id="app">
    <!-- 通过点击事件，调用testAjax() 向服务器端发送异步请求 -->
    <input type="button" value="测试@RequestBody处理JSON格式的ajax请求" @click="testRequestBody()">
</div>
    
</body>
</html>
```

通过vue和axios处理点击事件：

```html
<script type="text/javascript" th:src="@{/js/vue.js}"></script>
<script type="text/javascript" th:src="@{/js/axios.min.js}"></script>
<script type="text/javascript">
    var vue = new Vue({
        //设置挂载容器
        el: "#app",
        methods: {
            
           testRequestBody(){
                axios.post(
                    //data的请求参数为json格式，会封装到请求报文的请求体中，发送给服务器
                    {username:"admin",password:"123456"}
                ).then(response=>{
                    console.log(response.data);
                })
            }
            
        }
    })
</script>
```



**④ 浏览器使用 @RequestBody注解 处理json格式的请求参数**

```java
@RequestMapping("/test/RequestBody/json") 
public void testRequestBodyJson(@RequestBody String requestBody, HttpServletResponse response) throws IOException {

    System.out.println(requestBody);
    
    //向浏览器响应数据
    response.getWriter().write("hello,ajax"); 
}

// {"username":"admin","password":"123456"}  ( 接收到的是json格式的字符串 )

```



- 我们还用一个实体类来接收json格式的形参

```JAVA
public class User {

    private Integer id;
    private String username;
    private String password;
    
    //...
}
```

```JAVA
//通过@RequestBody注解 标注一个实体类来接收 Json格式的请求参数
@RequestMapping("/test/ajax")
public void testRequestBodyJson(@RequestBody User user, HttpServletResponse response) throws IOException {
    
    System.out.println(user);    
    //向浏览器响应数据
    response.getWriter().write("hello,RequestBody");

}

//User{id=null, username='admin', password='123456'}  （ 接收到的是一个user对象 ）

```



- 我们还可以用一个map集合来 接收json格式请求参数

```JAVA

//通过@RequestBody注解 标注一个Map集合来接收 Json格式的请求参数
@RequestMapping("/test/RequestBody/json")
public void testRequestBodyJson02(@RequestBody Map<String,Object> map, HttpServletResponse response) throws IOException {
	
    System.out.println("map:" + map);
    //向浏览器响应数据
    response.getWriter().write("hello,RequestBody");

}

//{username=admin, password=123456}	  （ 接收到的是一个map集合 k=v ）
```



思考：我们有 @RequestBody注解 来方便接收浏览器发送的请求，但是服务器端向浏览器响应数据，我们还需要 **将需要响应的数据转换为json格式， 再手动获取流，通过流向浏览器响应数据**



### 9.3 SpringMVC 向浏览器响应数据



* @ResponseBody用于标识一个控制器方法，可以将该方法的返回值 自动转为json格式 并作为响应报文的响应体响应到浏览器 
* 在使用 @RequestMapping后，返回值通常解析为**跳转路径**，但是加上 @ResponseBody 后返回结果不会被解析为跳转路径，而是直接写入 HTTP response body 中。 比如异步获取 json 数据，加上 @ResponseBody 后，会直接返回 json 数据
	

#### 1）使用@ResponseBody响应浏览器ajax请求



① 导入jackson的依赖

```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.12.1</version>
</dependency>
```



② 在SpringMVC的核心配置文件中开启mvc的注解驱动

此时在 HandlerAdaptor中会自动装配一个消息转换器：**MappingJackson2HttpMessageConverter**，可以将响应到浏览器的Java对象转换为Json格式的字符串

```xml
<mvc:annotation-driven />
```



③ 向服务器发送json格式的ajax请求

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>首页</title>
</head>
<body>

<div id="app">
    
	 <input type="button" value="测试使用@ResponseBody响应JSON格式的ajax请求" @click="testResponseBody()"> <br>

</div>
</body>
</html>
```

通过vue和axios处理点击事件:

```html
<script type="text/javascript" th:src="@{/js/vue.js}"></script>
<script type="text/javascript" th:src="@{/js/axios.min.js}"></script>
<script type="text/javascript">
    var vue = new Vue({
        //设置挂载容器
        el: "#app",
        methods: {
            testResponseBody(){
                axios.post(
                    "/springmvc08/test/ResponseBody/json",
                    {username:"admin",password:"123456"}
                ).then(response=>{
                    console.log(response.data); //将浏览器正常响应的数据打印到控制台
                })
            }
        }
    })
</script>
```



④ 在处理器方法上使用@ResponseBody注解进行标识，他会**将控制器方法的返回值 自动转换为Json格式的字符串 做为响应报文的响应体响应到浏览器**

我们响应一个实体类：

```java
@RequestMapping("/test/ResponseBody/json")
@ResponseBody
public User testResponseUser(){
    return new User(1001,"admin","123456",23,"男");
}

/*
	浏览器 控制台输出：
	{id: 1001, username: 'admin', password: '123456'}
*/
```

我们响应一个Map集合

```java
@RequestMapping("/test/ResponseBody/json")
@ResponseBody
public Map<String,Object> testResponseBodyJson(){
    User user1 = new User(1001, "admin1", "123456", 23, "男");
    User user2 = new User(1002, "admin2", "123456", 23, "男");
    User user3 = new User(1003, "admin3", "123456", 23, "男");
    Map<String,Object> map = new HashMap<>();
    map.put("1001",user1);
    map.put("1002",user2);
    map.put("1003",user3);
    return map;
}
/*
	浏览器 控制台输出：
	1001: {id: 1001, username: "admin1", password: "123456", age: 23, gender: "男"}
	1002: {id: 1002, username: "admin2", password: "123456", age: 23, gender: "男"}
	1003: {id: 1003, username: "admin3", password: "123456", age: 23, gender: "男"}
*/
```

我们响应一个List集合

```java
 @RequestMapping("/test/ResponseBody/json")
@ResponseBody
public List<User> testResponseBodyJson() {
    User user1 = new User(1001, "admin1", "123456", 23, "男");
    User user2 = new User(1002, "admin2", "123456", 23, "男");
    User user3 = new User(1003, "admin3", "123456", 23, "男");
    List<User> list = Arrays.asList(user1, user2, user3);
    return list;
}
/*
	浏览器 控制台输出：
	0: {id: 1001, username: "admin1", password: "123456", age: 23, gender: "男"}
	1: {id: 1002, username: "admin2", password: "123456", age: 23, gender: "男"}
	2: {id: 1003, username: "admin3", password: "123456", age: 23, gender: "男"}
*/
```



#### 2）使用@ResponseBody响应浏览器普通请求



① 表单提交

```HTML
<form th:action="@{/testRequestBody}" method="post">
    用户名：<input type="text" name="username"><br>
    密码：<input type="password" name="password"><br>
    <input type="submit">
</form>
```

```JAVA
@RequestMapping("/testRequestBody")
@ResponseBody
public String testRequestBody(@RequestBody String requestBody){
    System.out.println(requestBody);
    return "success";
}

//服务器控制台：username=admin&password=123456
//浏览器跳转到 http://localhost:8080/springmvc08/testRequestBody 页面 并在页面显示 success
```

② 超链接

```xml
<a th:href="@{/test/ResponseBody}">测试@ResponseBody注解响应浏览器普通请求</a> <br>
```

```java
//3. 使用 @ResponseBody注解响应浏览器普通请求
@RequestMapping("/test/ResponseBody")
@ResponseBody
public User testResponseBody() {
    return new User(1001, "admin1", "123456");
}
//浏览器跳转到 http://localhost:8080/springmvc08/test/ResponseBody 页面
// 并在页面显示 {id: 1001, username: 'admin', password: '123456'}
```



### 9.4 @RestController注解

@RestController注解是springMVC提供的一个复合注解，标识在控制器的类上，就相当于为类添加了@Controller注解，并且为其中的每个方法添加了@ResponseBody注解



### 总结

HttpMessageConverter，报文信息转换器，将**请求报文转换为Java对象**，或将**Java对象转换为响应报文**

​		HttpMessageConverter提供了两个注解和两个类型：

​		@RequestBody，@ResponseBody

​		RequestEntity，ResponseEntity



1）@RequestBody 

@RequestBody可以**获取请求体**，需要在控制器方法设置一个形参，使用@RequestBody进行标识，当前请求的请求体就会为当前注解所标识的形参赋值 

2）@ResponseBody

@ResponseBody注解标识控制器方法，他会将控制器方法的返回值 自动转换为Json格式的字符串 做为响应报文的响应体响应到浏览器



3）RequestEntity 

 RequestEntity封装请求报文的一种类型，需要在控制器方法的形参中设置该类型的形参，当前请求的请求报文就会赋值给该形参，可以通过 getHeaders()获取请求头信息，通过 getBody()获取请求体信息

```java
@RequestMapping("/testRequestEntity")
public String testRequestEntity(RequestEntity<String> requestEntity){
    System.out.println("requestHeader:"+requestEntity.getHeaders());
    System.out.println("requestBody:"+requestEntity.getBody());
    return "success";
}

/*
输出结果：
requestHeader:[host:"localhost:8080", connection:"keep-alive", content-length:"27", cache-control:"max-age=0", sec-ch-ua:"" Not A;Brand";v="99", "Chromium";v="90", "Google Chrome";v="90"", sec-ch-ua-mobile:"?0", upgrade-insecure-requests:"1", origin:"http://localhost:8080", user-agent:"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36"]
requestBody:username=admin&password=123
*/
```

4） ResponseEntity

ResponseEntity用于控制器方法的返回值类型，该控制器方法的返回值就是响应到浏览器的响应报文







## 10、文件上传和下载



### 10.1 文件下载

ResponseEntity用于控制器方法的返回值类型，该控制器方法的返回值就是响应到浏览器的响应报文

使用ResponseEntity实现下载文件的功能

```java
@Controller
public class FileUpAndDownController {

    @RequestMapping("/test/down")
    public ResponseEntity<byte[]> testResponseEntity(HttpSession session) throws IOException {

        //获取ServletContext对象
        ServletContext servletContext = session.getServletContext();

        //获取服务器中文件的真实路径
        //1）使用servletContext.getRealPath("文件在webapp下的路径")
        //String realPath = servletContext.getRealPath("img/1.png");
        //realPath => E:\lhcSpring\springmvc09_file\target\springmvc09_file-1.0-SNAPSHOT\img\1.png
        //2）如果我们不知道路径之间的分隔符用什么符号 ，就使用File.separator获取分隔符，再拼接
        String realPath = servletContext.getRealPath("img");
        realPath = realPath + File.separator + "1.png";
        //realPath => E:\lhcSpring\springmvc09_file\target\springmvc09_file-1.0-SNAPSHOT\img\1.png

        //创建输入流
        InputStream is = new FileInputStream(realPath);
        //创建字节数组， is.available()表示获取当前字节输入流所对应的文件所有的字节数 即1.pmg文件的字节数
        byte[] bytes = new byte[is.available()];
        //将流读到字节数组中
        is.read(bytes);

        //创建HttpHeaders对象设置响应头信息
        MultiValueMap<String, String> headers = new HttpHeaders();
        //设置要下载方式以及下载文件的名字 (前面都是固定写法，我们只能修改one.png 即文件下载下来的文件名)
        headers.add("Content-Disposition", "attachment;filename=one.png");
        //设置响应状态码 （枚举OK代表 200）
        HttpStatus statusCode = HttpStatus.OK;
        
        //创建ResponseEntity对象
        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(bytes, headers, statusCode);
        //关闭输入流
        is.close();
        return responseEntity;

    }
}
```



### 10.2 文件上传



SpringMVC中将上传的文件封装到**MultipartFile对象**中，通过此对象可以获取文件相关信息



**文件上传要求**：form表单的请求方式必须为post，并且添加属性 enctype="multipart/form-data"

**上传步骤：**

① 添加依赖

```xml
<!-- https://mvnrepository.com/artifact/commons-fileupload/commons-fileupload -->
<dependency>
    <groupId>commons-fileupload</groupId>
    <artifactId>commons-fileupload</artifactId>
    <version>1.3.1</version>
</dependency>
```

② 在SpringMVC的配置文件中添加配置：

```xml
<!--必须通过文件解析器的解析才能将文件转换为MultipartFile对象
	SpringMVC在获取这个bean的时候，是通过id来获取的，所有必须将id设置为multipartResolver
-->
<bean id="multipartResolver" 	
      			class="org.springframework.web.multipart.commons.CommonsMultipartResolver"></bean>
```

③ 控制器方法：

```java
@Controller
public class FileUpAndDownController {	
    
	@RequestMapping("/test/up")
    public String testUp(MultipartFile photo, HttpSession session) throws IOException {

        //获取上传文件的文件名
        String filename = photo.getOriginalFilename();
        //处理文件重名问题(这样会导致相同文件名的内容被覆盖)
        //1）获取上传文件的后缀名 （保证文件重命名后 后缀名不改变）
        String hzName = filename.substring(filename.lastIndexOf("."));
        //2）使用UUID 将文件重命名
        filename = UUID.randomUUID().toString() + hzName;
        
        //获取ServletContext对象
        ServletContext servletContext = session.getServletContext();
        //获取当前工程下photo目录 的真实路径
        String photoPath = servletContext.getRealPath("photo");
        System.out.println(photoPath);
        
        //创建photoPath所对应的file对象
        File file = new File(photoPath);
        //判断file所对应目录是否存在,不存在就创建出来
        if (!file.exists()){
            file.mkdir();
        }
        //文件上传后 在服务器的全路径
        String finalPath = photoPath + File.separator + filename;

        //上传文件
        photo.transferTo(new File(finalPath));
        return "success";
    }
    
}
```



注意：

为什么上传相同名字的文件 **文件的内容会被覆盖**（文件的内容被覆盖 不是指文件被覆盖）

* 因为在创建输出流时，有一个boolean类型的属性可以设置**是否追加**（即是否覆盖），**默认为不追加**
* 所以在写操作的时候，第二次添加同名文件，就会把该文件写到第一次添加的文件中 将内容进行覆盖

解决：

我们可以使用 **时间戳** 或者 **UUID**  每次上传文件的时候 将文件名按照相应的规则随机重命名



## 11、拦截器

### 11.1 拦截器的配置

SpringMVC中的拦截器用于拦截控制器方法的执行

SpringMVC中的拦截器需要实现HandlerInterceptor

SpringMVC的拦截器必须在SpringMVC的配置文件中进行配置：

```xml
<mvc:interceptors>
    
    <!-- 方式一 -->
    <bean class="com.lhc.interceptor.FirstInterceptor"></bean>
    
    <!-- 方式二 -->
    <ref bean="firstInterceptor"></ref>
    
    <!-- 以上两种配置方式都是对DispatcherServlet所处理的所有的请求进行拦截 -->

    <!-- 方式三 -->
    <mvc:interceptor>
        <!--配置需要拦截的请求的请求路径，/**表示拦截所有请求 -->
        <mvc:mapping path="/**" />
        <!--设置需要拦截的请求-->
        <mvc:exclude-mapping path="/testRequestEntity"/>
        <!--设置需要排除的请求，即不需要拦截的请求-->
        <ref bean="firstInterceptor"></ref>
    </mvc:interceptor>
  
    
</mvc:interceptors>
```

### 11.2 拦截器的三个抽象方法

SpringMVC中的拦截器有三个抽象方法：

* preHandle：**控制器方法执行之前**执行preHandle()，其boolean类型的返回值表示是否拦截或放行，返回true为放行，即调用控制器方法；返回false表示拦截，即不调用控制器方法

	```java
	//执行preHandle
	if (!mappedHandler.applyPreHandle(processedRequest, response)) {
	    return;
	}
	
	//执行控制器方法
	mv = ha.handle(processedRequest, response, mappedHandler.getHandler());
	if (asyncManager.isConcurrentHandlingStarted()) {
	    return;
	}
	```

* postHandle：控制器方法执行之后执行postHandle()

	```java
	//执行控制器方法
	v = ha.handle(processedRequest, response, mappedHandler.getHandler());
	if (asyncManager.isConcurrentHandlingStarted()) {
	    return;
	}
	this.applyDefaultViewName(processedRequest, mv);
	//执行postHandle
	mappedHandler.applyPostHandle(processedRequest, response, mv);
	```

* afterComplation：处理完视图和模型数据，渲染视图完毕之后执行afterComplation()

	```java
	if (mv != null && !mv.wasCleared()) {
	    //执行render 视图渲染
	    this.render(mv, request, response);
	    if (errorView) {
	        WebUtils.clearErrorRequestAttributes(request);
	    }
	} else if (this.logger.isTraceEnabled()) {
	    this.logger.trace("No view rendering, null ModelAndView returned.");
	}
	
	if (!WebAsyncUtils.getAsyncManager(request).isConcurrentHandlingStarted()) {
	    if (mappedHandler != null) {
	        //执行afterComplation
	        mappedHandler.triggerAfterCompletion(request, response, (Exception)null);
	    }
	
	}
	```

	

### 11.3 多个拦截器的执行顺序

① 若每个拦截器的preHandle()都返回true

​	此时多个拦截器的执行顺序和拦截器在SpringMVC的配置文件的配置顺序有关：

​	preHandle()会按照配置的**顺序执行**，而postHandle()和afterComplation()会按照配置的**反序执行**

② 若某个拦截器的preHandle()返回了false

​	preHandle()返回false和它之前的拦截器的**preHandle()**都会执行，**postHandle()**都不执行，返回false的拦截器之前的拦	截器的**afterComplation()**会执行



## 12、异常处理器



### 12.1 基于配置的异常处理

SpringMVC提供了一个处理控制器方法执行过程中所出现的异常的接口：**HandlerExceptionResolver**

HandlerExceptionResolver接口的实现类有**：DefaultHandlerExceptionResolver**和**SimpleMappingExceptionResolver**

SpringMVC提供了自定义的异常处理器SimpleMappingExceptionResolver，使用方式：

```xml
<bean class="org.springframework.web.servlet.handler.SimpleMappingExceptionResolver">

    <!--1. exceptionMappings异常映射，用于页面跳转 -->
    <property name="exceptionMappings">
        <!--properties-->
        <props>
            <!-- key：处理器方法执行过程中出现的异常 
                     value：若出现指定异常时，设置一个新的视图名称，跳转到指定页面-->
            <prop key="java.lang.ArithmeticException">error</prop>
        </props>
    </property>

    <!--2. exceptionAttribute 用于共享数据，将出现的异常信息在请求域中进行共享 
                value="ex" 设置 ex为请求域中异常信息的属性名
        -->
    <property name="exceptionAttribute" value="ex"></property>

</bean>
```

### 12.2 基于注解的异常处理

```java
//@ControllerAdvice将当前类标识为异常处理的组件
@ControllerAdvice
public class Exception01 {

    //@ExceptionHandler用于设置所标识方法处理的异常
    @ExceptionHandler(ArithmeticException.class)
    //ex表示当前请求处理中出现的异常对象
    public String handleArithmeticException(Exception ex, Model model){
        model.addAttribute("ex", ex);
        return "error";
    }

}
```



## 13、注解配置SpringMVC

使用配置类和注解代替web.xml和SpringMVC配置文件的功能

### 13.1 创建初始化类，代替web.xml

在Servlet3.0环境中，容器会在类路径中查找实现javax.servlet.ServletContainerInitializer接口的类，如果找到的话就用它来配置Servlet容器。
Spring提供了这个接口的实现，名为SpringServletContainerInitializer，这个类反过来又会查找实现WebApplicationInitializer的类并将配置的任务交给它们来完成。Spring3.2引入了一个便利的WebApplicationInitializer基础实现，名为AbstractAnnotationConfigDispatcherServletInitializer，当我们的类扩展了AbstractAnnotationConfigDispatcherServletInitializer并将其部署到Servlet3.0容器的时候，容器会自动发现它，并用它来配置Servlet上下文。

```java
public class WebInit extends AbstractAnnotationConfigDispatcherServletInitializer {

    /** 
     * 指定spring的配置类（设置一个配置类来代替Spring配置文件）
     * @return
     */
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{SpringConfig.class};
    }

    /**
     * 指定SpringMVC的配置类
     * @return
     */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebConfig.class};
    }

    /**
     * 指定DispatcherServlet的映射规则，即url-pattern
     * @return
     */
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    /**
     * 添加过滤器
     * @return
     */
    @Override
    protected Filter[] getServletFilters() {
        //创建编码过滤器
        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding("UTF-8"); 
        encodingFilter.setForceRequestEncoding(true); //响应时 也开启编码过滤
        //创建请求过滤器
        HiddenHttpMethodFilter hiddenHttpMethodFilter = new HiddenHttpMethodFilter();
        
        return new Filter[]{encodingFilter, hiddenHttpMethodFilter};
    }
}

```

### 13.2 创建SpringConfig配置类，代替spring的配置文件

```java
@Configuration
public class SpringConfig {
	//ssm整合之后，spring的配置信息写在此类中
}
```

### 13.3 创建WebConfig配置类，代替SpringMVC的配置文件

```java
/**
 * 代替SpringMVC的配置文件
 * 1.扫描组件
 * 2.视图解析器
 * 3.配置默认的servlet
 * 4.开启mvc的注解驱动
 * 5.视图控制器
 * 6.文件上传解析器
 * 7.拦截器
 * 8.异常解析器
 */
@Configuration //将类标识为配置类
@ComponentScan("com.lhc.controller") //1、扫描组件
@EnableWebMvc //4.开启mvc的注解驱动
public class WebConfig implements WebMvcConfigurer {

    //3. 配置默认的servlet,处理静态资源
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    //5.视图控制器
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //添加视图控制器，设置处理的请求地址 和 设置请求地址所对应的视图名称
        registry.addViewController("/").setViewName("index");
    }

    //6.文件上传解析器
    @Bean // 注解@Bean 可以将标识方法的返回值作为bean进行管理，方法名作为bean的id
    public CommonsMultipartResolver multipartResolver(){
        return new CommonsMultipartResolver();
    }

    //7.拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //创建一个拦截器
        FirstInterceptor firstInterceptor = new FirstInterceptor();
        //添加拦截器，指定需要拦截的请求 和不需要拦截的请求
        registry.addInterceptor(firstInterceptor).addPathPatterns("/**").excludePathPatterns();
    }

    //8.异常解析器
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        //创建一个自定义异常处理器
        SimpleMappingExceptionResolver exceptionResolver = new SimpleMappingExceptionResolver();
        //创建一个Properties
        Properties prop = new Properties();
        //key:异常类型 value：出现该异常需要跳转的逻辑视图
        prop.setProperty("java.lang.ArithmeticException","error");
        //设置ex为请求域中异常信息的属性名
        exceptionResolver.setExceptionAttribute("ex");

        //将把我们创建的异常处理器添加到 该方法的参数中（参数为HandlerExceptionResolver类型的list集合）
        resolvers.add(exceptionResolver);
    }


    //2.视图解析器
    //1) 配置 生成模板解析器
    @Bean
    public ITemplateResolver templateResolver() {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        // ServletContextTemplateResolver需要一个ServletContext作为构造参数，可通过WebApplicationContext 的方法获得
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(webApplicationContext.getServletContext());
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        return templateResolver;
    }

    //2) 生成模板引擎 并为模板引擎 注入模板解析器
    @Bean
    public SpringTemplateEngine templateEngine(ITemplateResolver templateResolver) {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }

    //3)生成视图解析器 并未解析器 注入模板引擎
    @Bean
    public ViewResolver viewResolver(SpringTemplateEngine templateEngine) {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setCharacterEncoding("UTF-8");
        viewResolver.setTemplateEngine(templateEngine);
        return viewResolver;
    }

}
```





## 14、SpringMVC执行流程



### 14.1SpringMVC常用组件



- DispatcherServlet：**前端控制器**，不需要工程师开发，由框架提供

	作用：统一处理请求和响应，整个流程控制的中心，由它调用其它组件处理用户的请求

	

- HandlerMapping：**处理器映射器**，不需要工程师开发，由框架提供

	作用：根据请求的 url、method等 信息 **查找Handler**，即控制器方法

	

- Handler：**处理器**（控制器方法），需要工程师开发

	作用：在 DispatcherServlet的控制下 Handler对具体的用户请求进行处理

	

- HandlerAdapter：**处理器适配器**，不需要工程师开发，由框架提供

	作用：通过 HandlerAdapter对 **执行Handler**

	

- ViewResolver：**视图解析器**，不需要工程师开发，由框架提供

	作用：进行视图解析，得到相应的视图，例如：ThymeleafView、InternalResourceView、RedirectView

	

- View：**视图**

	作用：将模型数据通过页面展示给用户

	

### 14.2 DispatcherServlet初始化过程

DispatcherServlet 本质上是一个 Servlet，所以天然的遵循 Servlet 的生命周期。所以宏观上是 Servlet 生命周期来进行调度。

![images](E:/%E6%9D%A8%E5%8D%9A%E8%B6%85ssm/%E8%B5%84%E6%96%99/springMVC(1)/%E7%AC%94%E8%AE%B0/%E7%AC%94%E8%AE%B0/img/img005.png)

##### 1）初始化WebApplicationContext

所在类：org.springframework.web.servlet.FrameworkServlet

```java
protected WebApplicationContext initWebApplicationContext() {
    WebApplicationContext rootContext =
        WebApplicationContextUtils.getWebApplicationContext(getServletContext());
    WebApplicationContext wac = null;

    if (this.webApplicationContext != null) {
        // A context instance was injected at construction time -> use it
        wac = this.webApplicationContext;
        if (wac instanceof ConfigurableWebApplicationContext) {
            ConfigurableWebApplicationContext cwac = (ConfigurableWebApplicationContext) wac;
            if (!cwac.isActive()) {
                // The context has not yet been refreshed -> provide services such as
                // setting the parent context, setting the application context id, etc
                if (cwac.getParent() == null) {
                    // The context instance was injected without an explicit parent -> set
                    // the root application context (if any; may be null) as the parent
                    cwac.setParent(rootContext);
                }
                configureAndRefreshWebApplicationContext(cwac);
            }
        }
    }
    if (wac == null) {
        // No context instance was injected at construction time -> see if one
        // has been registered in the servlet context. If one exists, it is assumed
        // that the parent context (if any) has already been set and that the
        // user has performed any initialization such as setting the context id
        wac = findWebApplicationContext();
    }
    if (wac == null) {
        // No context instance is defined for this servlet -> create a local one
        // 创建WebApplicationContext
        wac = createWebApplicationContext(rootContext);
    }

    if (!this.refreshEventReceived) {
        // Either the context is not a ConfigurableApplicationContext with refresh
        // support or the context injected at construction time had already been
        // refreshed -> trigger initial onRefresh manually here.
        synchronized (this.onRefreshMonitor) {
            // 刷新WebApplicationContext
            onRefresh(wac);
        }
    }

    if (this.publishContext) {
        // Publish the context as a servlet context attribute.
        // 将IOC容器在应用域共享
        String attrName = getServletContextAttributeName();
        getServletContext().setAttribute(attrName, wac);
    }

    return wac;
}
```

##### 2）创建WebApplicationContext

所在类：org.springframework.web.servlet.FrameworkServlet

```java
protected WebApplicationContext createWebApplicationContext(@Nullable ApplicationContext parent) {
    Class<?> contextClass = getContextClass();
    if (!ConfigurableWebApplicationContext.class.isAssignableFrom(contextClass)) {
        throw new ApplicationContextException(
            "Fatal initialization error in servlet with name '" + getServletName() +
            "': custom WebApplicationContext class [" + contextClass.getName() +
            "] is not of type ConfigurableWebApplicationContext");
    }
    // 通过反射创建 IOC 容器对象
    ConfigurableWebApplicationContext wac =
        (ConfigurableWebApplicationContext) Bean Utils.instantiateClass(contextClass);

    wac.setEnvironment(getEnvironment());
    // 设置父容器
    wac.setParent(parent);
    String configLocation = getContextConfigLocation();
    if (configLocation != null) {
        wac.setConfigLocation(configLocation);
    }
    configureAndRefreshWebApplicationContext(wac);

    return wac;
}
```

##### 3）DispatcherServlet初始化策略

FrameworkServlet创建WebApplicationContext后，刷新容器，调用onRefresh(wac)，此方法在DispatcherServlet中进行了重写，调用了initStrategies(context)方法，初始化策略，即初始化DispatcherServlet的各个组件

所在类：org.springframework.web.servlet.DispatcherServlet

```java
protected void initStrategies(ApplicationContext context) {
   initMultipartResolver(context);
   initLocaleResolver(context);
   initThemeResolver(context);
   initHandlerMappings(context);
   initHandlerAdapters(context);
   initHandlerExceptionResolvers(context);
   initRequestToViewNameTranslator(context);
   initViewResolvers(context);
   initFlashMapManager(context);
}
```



### 14.3 DispatcherServlet调用组件处理请求



##### 1）processRequest()

FrameworkServlet 重写 HttpServlet中的 service()和 doXxx()，这些方法中调用了 processRequest(request, response)

所在类：org.springframework.web.servlet.FrameworkServlet

```java
protected final void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

    long startTime = System.currentTimeMillis();
    Throwable failureCause = null;

    LocaleContext previousLocaleContext = LocaleContextHolder.getLocaleContext();
    LocaleContext localeContext = buildLocaleContext(request);

    RequestAttributes previousAttributes = RequestContextHolder.getRequestAttributes();
    ServletRequestAttributes requestAttributes = buildRequestAttributes(request, response, previousAttributes);

    WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);
    asyncManager.registerCallableInterceptor(FrameworkServlet.class.getName(), new RequestBindingInterceptor());

    initContextHolders(request, localeContext, requestAttributes);

    try {
		// 执行服务，doService()是一个抽象方法，在DispatcherServlet中进行了重写
        doService(request, response);
    }
    catch (ServletException | IOException ex) {
        failureCause = ex;
        throw ex;
    }
    catch (Throwable ex) {
        failureCause = ex;
        throw new NestedServletException("Request processing failed", ex);
    }

    finally {
        resetContextHolders(request, previousLocaleContext, previousAttributes);
        if (requestAttributes != null) {
            requestAttributes.requestCompleted();
        }
        logResult(request, response, failureCause, asyncManager);
        publishRequestHandledEvent(request, response, startTime, failureCause);
    }
}
```

##### 2）doService()

所在类：org.springframework.web.servlet.DispatcherServlet

```java
@Override
protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
    logRequest(request);

    // Keep a snapshot of the request attributes in case of an include,
    // to be able to restore the original attributes after the include.
    Map<String, Object> attributesSnapshot = null;
    if (WebUtils.isIncludeRequest(request)) {
        attributesSnapshot = new HashMap<>();
        Enumeration<?> attrNames = request.getAttributeNames();
        while (attrNames.hasMoreElements()) {
            String attrName = (String) attrNames.nextElement();
            if (this.cleanupAfterInclude || attrName.startsWith(DEFAULT_STRATEGIES_PREFIX)) {
                attributesSnapshot.put(attrName, request.getAttribute(attrName));
            }
        }
    }

    // Make framework objects available to handlers and view objects.
    request.setAttribute(WEB_APPLICATION_CONTEXT_ATTRIBUTE, getWebApplicationContext());
    request.setAttribute(LOCALE_RESOLVER_ATTRIBUTE, this.localeResolver);
    request.setAttribute(THEME_RESOLVER_ATTRIBUTE, this.themeResolver);
    request.setAttribute(THEME_SOURCE_ATTRIBUTE, getThemeSource());

    if (this.flashMapManager != null) {
        FlashMap inputFlashMap = this.flashMapManager.retrieveAndUpdate(request, response);
        if (inputFlashMap != null) {
            request.setAttribute(INPUT_FLASH_MAP_ATTRIBUTE, Collections.unmodifiableMap(inputFlashMap));
        }
        request.setAttribute(OUTPUT_FLASH_MAP_ATTRIBUTE, new FlashMap());
        request.setAttribute(FLASH_MAP_MANAGER_ATTRIBUTE, this.flashMapManager);
    }

    RequestPath requestPath = null;
    if (this.parseRequestPath && !ServletRequestPathUtils.hasParsedRequestPath(request)) {
        requestPath = ServletRequestPathUtils.parseAndCache(request);
    }

    try {
        // 处理请求和响应
        doDispatch(request, response);
    }
    finally {
        if (!WebAsyncUtils.getAsyncManager(request).isConcurrentHandlingStarted()) {
            // Restore the original attribute snapshot, in case of an include.
            if (attributesSnapshot != null) {
                restoreAttributesAfterInclude(request, attributesSnapshot);
            }
        }
        if (requestPath != null) {
            ServletRequestPathUtils.clearParsedRequestPath(request);
        }
    }
}
```

##### 3）doDispatch()

所在类：org.springframework.web.servlet.DispatcherServlet

```java
protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
    HttpServletRequest processedRequest = request;
    HandlerExecutionChain mappedHandler = null;
    boolean multipartRequestParsed = false;

    WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);

    try {
        ModelAndView mv = null;
        Exception dispatchException = null;

        try {
            processedRequest = checkMultipart(request);
            multipartRequestParsed = (processedRequest != request);

            // Determine handler for the current request.
            /*
            	mappedHandler：调用链
                包含handler、interceptorList、interceptorIndex
            	handler：浏览器发送的请求所匹配的控制器方法
            	interceptorList：处理控制器方法的所有拦截器集合
            	interceptorIndex：拦截器索引，控制拦截器afterCompletion()的执行
            */
            mappedHandler = getHandler(processedRequest);
            if (mappedHandler == null) {
                noHandlerFound(processedRequest, response);
                return;
            }

            // Determine handler adapter for the current request.
           	// 通过控制器方法创建相应的处理器适配器（下面会用他来 调用所对应的控制器方法）
            HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());

            // Process last-modified header, if supported by the handler.
            String method = request.getMethod();
            boolean isGet = "GET".equals(method);
            if (isGet || "HEAD".equals(method)) {
                long lastModified = ha.getLastModified(request, mappedHandler.getHandler());
                if (new ServletWebRequest(request, response).checkNotModified(lastModified) && isGet) {
                    return;
                }
            }
			
            // 调用拦截器的preHandle()
            if (!mappedHandler.applyPreHandle(processedRequest, response)) {
                return;
            }

            // Actually invoke the handler.
            // 由处理器适配器调用具体的控制器方法，最终获得ModelAndView对象
            mv = ha.handle(processedRequest, response, mappedHandler.getHandler());

            if (asyncManager.isConcurrentHandlingStarted()) {
                return;
            }

            applyDefaultViewName(processedRequest, mv);
            // 调用拦截器的postHandle()
            mappedHandler.applyPostHandle(processedRequest, response, mv);
        }
        catch (Exception ex) {
            dispatchException = ex;
        }
        catch (Throwable err) {
            // As of 4.3, we're processing Errors thrown from handler methods as well,
            // making them available for @ExceptionHandler methods and other scenarios.
            dispatchException = new NestedServletException("Handler dispatch failed", err);
        }
        // 后续处理：处理模型数据和渲染视图
        processDispatchResult(processedRequest, response, mappedHandler, mv, dispatchException);
    }
    catch (Exception ex) {
        triggerAfterCompletion(processedRequest, response, mappedHandler, ex);
    }
    catch (Throwable err) {
        triggerAfterCompletion(processedRequest, response, mappedHandler,
                               new NestedServletException("Handler processing failed", err));
    }
    finally {
        if (asyncManager.isConcurrentHandlingStarted()) {
            // Instead of postHandle and afterCompletion
            if (mappedHandler != null) {
                mappedHandler.applyAfterConcurrentHandlingStarted(processedRequest, response);
            }
        }
        else {
            // Clean up any resources used by a multipart request.
            if (multipartRequestParsed) {
                cleanupMultipart(processedRequest);
            }
        }
    }
}
```

##### 4）processDispatchResult()

```java
private void processDispatchResult(HttpServletRequest request, HttpServletResponse response,
                                   @Nullable HandlerExecutionChain mappedHandler, @Nullable ModelAndView mv,
                                   @Nullable Exception exception) throws Exception {

    boolean errorView = false;

    if (exception != null) {
        if (exception instanceof ModelAndViewDefiningException) {
            logger.debug("ModelAndViewDefiningException encountered", exception);
            mv = ((ModelAndViewDefiningException) exception).getModelAndView();
        }
        else {
            Object handler = (mappedHandler != null ? mappedHandler.getHandler() : null);
            mv = processHandlerException(request, response, handler, exception);
            errorView = (mv != null);
        }
    }

    // Did the handler return a view to render?
    if (mv != null && !mv.wasCleared()) {
        // 处理模型数据和渲染视图
        render(mv, request, response);
        if (errorView) {
            WebUtils.clearErrorRequestAttributes(request);
        }
    }
    else {
        if (logger.isTraceEnabled()) {
            logger.trace("No view rendering, null ModelAndView returned.");
        }
    }

    if (WebAsyncUtils.getAsyncManager(request).isConcurrentHandlingStarted()) {
        // Concurrent handling started during a forward
        return;
    }

    if (mappedHandler != null) {
        // Exception (if any) is already handled..
        // 调用拦截器的afterCompletion()
        mappedHandler.triggerAfterCompletion(request, response, null);
    }
}
```

5）render

```java
 protected void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Locale locale = this.localeResolver != null ? this.localeResolver.resolveLocale(request) : request.getLocale();
        response.setLocale(locale);
     	//获取视图名称（控制器方法的返回值 如：success ）
        String viewName = mv.getViewName();
        View view;
        if (viewName != null) {
            //创建视图（如：ThyeleafView）
            view = this.resolveViewName(viewName, mv.getModelInternal(), locale, request);
            
            if (view == null) {
                ...
            }
        } else {
            view = mv.getView();
            if (view == null) {
             	...
            }
        }
     
     ...
         
 }
```





### 14.4 SpringMVC的执行流程

1) 用户向服务器发送请求，请求被SpringMVC 前端控制器 DispatcherServlet捕获。

2) DispatcherServlet对请求URL进行解析，得到请求资源标识符（URI），判断请求URI对应的映射：

a) 不存在

i. 再判断是否配置了mvc:default-servlet-handler

ii. 如果没配置，则控制台报映射查找不到，客户端展示404错误

![image-20210709214911404](E:/%E6%9D%A8%E5%8D%9A%E8%B6%85ssm/%E8%B5%84%E6%96%99/springMVC(1)/%E7%AC%94%E8%AE%B0/%E7%AC%94%E8%AE%B0/img/img006.png)

![image-20210709214947432](E:/%E6%9D%A8%E5%8D%9A%E8%B6%85ssm/%E8%B5%84%E6%96%99/springMVC(1)/%E7%AC%94%E8%AE%B0/%E7%AC%94%E8%AE%B0/img/img007.png)

iii. 如果有配置，则访问目标资源（一般为静态资源，如：JS,CSS,HTML），找不到客户端也会展示404错误

![image-20210709215255693](E:/%E6%9D%A8%E5%8D%9A%E8%B6%85ssm/%E8%B5%84%E6%96%99/springMVC(1)/%E7%AC%94%E8%AE%B0/%E7%AC%94%E8%AE%B0/img/img008.png)

![image-20210709215336097](E:/%E6%9D%A8%E5%8D%9A%E8%B6%85ssm/%E8%B5%84%E6%96%99/springMVC(1)/%E7%AC%94%E8%AE%B0/%E7%AC%94%E8%AE%B0/img/img009.png)

b) 存在则执行下面的流程

3) 根据该URI，调用HandlerMapping获得该Handler配置的所有相关的对象（包括Handler对象以及Handler对象对应的拦截器），最后以HandlerExecutionChain执行链对象的形式返回。

4) DispatcherServlet 根据获得的Handler，选择一个合适的HandlerAdapter。

5) 如果成功获得HandlerAdapter，此时将开始执行拦截器的preHandler(…)方法【正向】

6) 提取Request中的模型数据，填充Handler入参，开始执行Handler（Controller)方法，处理请求。在填充Handler的入参过程中，根据你的配置，Spring将帮你做一些额外的工作：

a) HttpMessageConveter： 将请求消息（如Json、xml等数据）转换成一个对象，将对象转换为指定的响应信息

b) 数据转换：对请求消息进行数据转换。如String转换成Integer、Double等

c) 数据格式化：对请求消息进行数据格式化。 如将字符串转换成格式化数字或格式化日期等

d) 数据验证： 验证数据的有效性（长度、格式等），验证结果存储到BindingResult或Error中

7) Handler执行完成后，向DispatcherServlet 返回一个ModelAndView对象。

8) 此时将开始执行拦截器的postHandle(...)方法【逆向】。

9) 根据返回的ModelAndView（此时会判断是否存在异常：如果存在异常，则执行HandlerExceptionResolver进行异常处理）选择一个适合的ViewResolver进行视图解析，根据Model和View，来渲染视图。

10) 渲染视图完毕执行拦截器的afterCompletion(…)方法【逆向】。

11) 将渲染结果返回给客户端

# 四、SSM整合