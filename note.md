# 常用知识点总结

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

