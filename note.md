常用知识点总结

* #### @RequestBody、@Requestparam、@ModelAtttribute注解区别联系

  * 三个注解都可以在后端用来接收前端传入的参数
  * @RequestParam用于将指定的请求参数赋值给方法中的形参 ，也可以不使用@RequestParam，直接接收，此时要求controller方法中的参数名称要跟form中name名称一致 
  * @RequestBody用于post请求，不能用于get请求 ，可以接收json格式的数据，并将其转换成对应的数据类型 
  * @ModelAttribute注解类型将参数绑定到Model对象 
  * 当前台界面使用GET或POST方式提交数据时，数据编码格式由请求头的ContentType指定。分为以下几种情况：
    * application/x-www-form-urlencoded，这种情况的数据@RequestParam、@ModelAttribute可以处理，@RequestBody也可以处理。
    * multipart/form-data，@RequestBody不能处理这种格式的数据。（form表单里面有文件上传时，必须要指定enctype属性值为multipart/form-data，意思是以二进制流的形式传输文件。）
    * application/json、application/xml等格式的数据，必须使用@RequestBody来处理。 

* #### @Mapper、@Repository注解区别联系

  * @Mapper和@Repository是常用的两个注解，两者都是用在dao上，两者功能差不多 
  * @Repository需要在Spring中配置扫描地址（@MapperScan 注解配置扫描包路经），然后生成Dao层的Bean才能被注入到Service层中 
  * @Mapper不需要配置扫描地址，通过xml里面的namespace里面的接口地址，生成了Bean后注入到Service层中 

* #### 打包项目的几种方式

  * maven项目
    * 使用编辑器的maven插件进行打包
    * 使用cmd命令行进行打包（mvn clean package）
  * 非maven项目
    * 使用eclipse的export导出为jar或者war
    * 使用idea，File->Project Structure->Artifacts->"+"添加jar或者war包方式打包

* #### 安装第三方jar包到本地仓库

  * 进入jar包所在目录，cmd运行

    ```
    mvn install:install-file -Dfile=jar包路径 -DgroupId=jar包的groupId -DartifactId=jar包的artifactId -Dversion=jar包版本号 -Dpackaging=jar
    ```

* #### 安装本地开发jar包到远程仓库（私服）

  * 部署jar包到远程仓库主要包括两个部分：远程仓库认证，部署jar包到远程仓库 

    * 大部分远程仓库无需认证就可以访问，但有时候出于安全方面的考虑，我们需要提供认证信息才能访问一些远程仓库。例如，组织内部有一个Maven仓库服务器，该服务器为每个项目都提供独立的Maven仓库，为了防止非法的仓库访问，管理员为每个仓库提供了一组用户名及密码。这时，为了能让Maven访问仓库内容，就需要配置认证信息。

      配置认证信息和配置仓库信息不同，仓库信息可以直接配置在POM文件中，但是认证信息必须配置在settings.xml文件中，这是因为POM往往是被提交到代码仓库中供所有成员访问的，而settings.xml一般只放在本机。因此，在settings.xml中配置认证信息更为安全。

      在${MAVEN_HOME}/conf/settings.xml文件中配置认证

      ```xml
      <servers>
      	<server>
      		<id>releases</id>
      		<username>admin</username>
      		<password>admin123</password>
      	</server>
      	<server>
      		<id>snapshots</id>
      		<username>admin</username>
      		<password>admin123</password>
      	</server>
      </servers>
      注：用户名和密码填写自己的实际内容
      ```

    * 私服的一大作用是部署第三方构件，包括组织内部生成的构件以及一些无法从外部仓库直接获取的构件。无论是日常开发中生成的构件，还是正式版本发布的构件，都需要部署到仓库中，供其他团队成员使用。

      Maven除了能对项目进行编译、测试、打包之外，还能将项目生成的构建部署到仓库中。首先，需要编写项目的pom.xml文件。配置distributionManagement元素见下面。

      > 注意：repository里的id需要和第一步里的server id名称保持一致

      ```xml
      <project>
      ...
          <distributionManagement>
              <repository>
                  <!--repository里的id需要和第一步里的server id名称保持一致-->
                  <id>releases</id>
                  <!--仓库名称-->
                  <name>Releases</name>
                  <!--私服仓库地址-->
                  <url>http://xxxx:8081/repository/maven-releases/</url>
              </repository>
              <snapshotRepository>
                  <id>snapshots</id>
                  <name>Snapshot</name>
                  <url>http://xxxx:8081/repository/maven-snapshots/</url>
              </snapshotRepository>
          </distributionManagement>
      ...
      </project>
      ```

      distributionManagement包含repository和snapshotRepository子元素，前者表示发布版本构建的仓库，后者表示快照版本的仓库。这两个元素下都需要配置id、name和url，id为该远程仓库的唯一标识，name是为了方便人阅读，url表示该仓库的地址。

      配置正确后，在命令行运行mvn clean deploy，Maven就会将项目构建输出的构件部署到配置对应的远程仓库，如果项目当前的版本是快照版本，则部署到快照版本仓库地址，否则就部署到发布版本仓库地址。

* #### 安装第三方jar包到远程仓库（私服）

  * 在settings配置文件中添加登录私服第三方登录信息

    ```xml
    <servers>
    	<server>
    		<id>releases</id>
    		<username>admin</username>
    		<password>admin123</password>
    	</server>
    	<server>
    		<id>snapshots</id>
    		<username>admin</username>
    		<password>admin123</password>
    	</server>
    </servers>
    注：用户名和密码填写自己的实际内容
    ```

    进入jar包所在目录运行

    ```
    mvn deploy:deploy-file -Dfile=jar包路径 -DgroupId=jar包的groupId -DartifactId=jar包的artifactId -Dversion=jar包版本号 -Dpackaging=jar -Durl=私服地址 -DrepositoryId=私服Id
    ```

* #### maven配置从私服下载依赖jar包

  * 在maven的settings.xml配置私服下载信息

    ```xml
    <!-- 下载jar包配置 -->
    	<profile> 
    		<!--profile的id -->
    		<id>dev</id>
    		<repositories>
    			<repository> <!--仓库id，repositories可以配置多个仓库，保证id不重复 -->
    				<id>nexus</id> <!--仓库地址，即nexus仓库组的地址 -->
    				<url>http://xxxx:8081/nexus/content/groups/public/</url> 
    				<releases><!--是否下载releases构件 -->
    					<enabled>true</enabled>
    				</releases> 
    				<snapshots><!--是否下载snapshots构件 -->
    					<enabled>true</enabled>
    				</snapshots>
    			</repository>
    		</repositories>
    		<pluginRepositories> <!-- 插件仓库，maven的运行依赖插件，也需要从私服下载插件 -->
    			<pluginRepository> <!-- 插件仓库的id不允许重复，如果重复后边配置会覆盖前边 -->
    				<id>public</id>
    				<name>Public Repositories</name>
    				<url>http://xxxx:8081/nexus/content/groups/public/</url>
    			</pluginRepository>
    		</pluginRepositories>
    	</profile>
    ```

  * 激活下载配置

    ```xml
    <activeProfiles>
    	<activeProfile>dev</activeProfile>
    </activeProfiles>
    ```

* Git相关使用

  * 下载[git](<https://git-scm.com/downloads> )

  * 配置你的用户名和邮箱 （用于提交信息记录）

    ```sh
    $ git config --global user.name "yourName"
    $ git config --global user.email "yourEmail@.com"
    ```

  * GitHub配置ssh公钥（GitHub配置SSH Key的目的是为了帮助我们在通过git提交代码是，不需要繁琐的验证过程，简化操作流程，https的方式需要每次都输入密码 ）

    * 生成SSH key

      ```sh
      ssh-keygen -t rsa -C "yourEmail@.com" 
      ```

    * 获取SSH key

      根据上一步命令提示在本机找到公钥配置文件，复制公钥内容到GitHub的Settings的SSH and KPG keys选项卡中点击new SSH key添加新的公钥key

    * 验证是否成功配置SSH Key 

      ```sh
      ssh -T git@github.com 
      //运行结果出现类似如下 
      Hi yourName! You've successfully authenticated, but GitHub does not provide shell access. 
      ```
